package org.openwes.wes.outbound.domain.service.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.openwes.common.utils.exception.WmsException;
import org.openwes.domain.event.DomainEventPublisher;
import org.openwes.wes.api.config.ISystemConfigApi;
import org.openwes.wes.api.config.dto.SystemConfigDTO;
import org.openwes.wes.api.main.data.dto.SkuMainDataDTO;
import org.openwes.wes.api.outbound.dto.OutboundPlanOrderCancelDTO;
import org.openwes.wes.api.outbound.dto.OutboundPlanOrderDTO;
import org.openwes.wes.api.outbound.event.NewOutboundPlanOrderEvent;
import org.openwes.wes.common.validator.IValidator;
import org.openwes.wes.common.validator.ValidateResult;
import org.openwes.wes.common.validator.ValidatorFactory;
import org.openwes.wes.common.validator.impl.OwnerValidator;
import org.openwes.wes.common.validator.impl.SkuValidator;
import org.openwes.wes.common.validator.impl.WarehouseValidator;
import org.openwes.wes.outbound.domain.context.OutboundPlanOrderCancelContext;
import org.openwes.wes.outbound.domain.entity.*;
import org.openwes.wes.outbound.domain.repository.OutboundPlanOrderRepository;
import org.openwes.wes.outbound.domain.repository.OutboundPreAllocatedRecordRepository;
import org.openwes.wes.outbound.domain.repository.OutboundWaveRepository;
import org.openwes.wes.outbound.domain.repository.PickingOrderRepository;
import org.openwes.wes.outbound.domain.service.OutboundPlanOrderService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.openwes.common.utils.exception.code_enum.OutboundErrorDescEnum.OUTBOUND_CUSTOMER_ORDER_NO_REPEATED;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboundPlanOrderServiceImpl implements OutboundPlanOrderService {

    private final OutboundPlanOrderRepository outboundPlanOrderRepository;
    private final OutboundWaveRepository outboundWaveRepository;
    private final OutboundPreAllocatedRecordRepository preAllocatedRecordRepository;
    private final PickingOrderRepository pickingOrderRepository;
    private final ISystemConfigApi systemConfigApi;

    @Override
    public void beforeDoCreation(OutboundPlanOrderDTO outboundPlanOrderDTO) {
    }

    @Override
    public ValidateResult<Set<SkuMainDataDTO>> validate(OutboundPlanOrder outboundPlanOrder) {

        WarehouseValidator warehouseValidator = (WarehouseValidator) ValidatorFactory.getValidator(IValidator.ValidatorName.WAREHOUSE);
        warehouseValidator.validate(Lists.newArrayList(outboundPlanOrder.getWarehouseCode()));

        Set<String> ownerCodes = outboundPlanOrder.getDetails().stream().map(OutboundPlanOrderDetail::getOwnerCode).collect(Collectors.toSet());
        OwnerValidator ownerValidator = (OwnerValidator) ValidatorFactory.getValidator(IValidator.ValidatorName.OWNER);
        ownerValidator.validate(new OwnerValidator.OwnerValidatorObject(ownerCodes, outboundPlanOrder.getWarehouseCode()));

        SkuValidator skuValidator = (SkuValidator) ValidatorFactory.getValidator(IValidator.ValidatorName.SKU);
        Set<SkuMainDataDTO> skuMainDataDTOS = outboundPlanOrder.getDetails().stream()
                .collect(Collectors.groupingBy(OutboundPlanOrderDetail::getOwnerCode, Collectors.mapping(OutboundPlanOrderDetail::getSkuCode, Collectors.toSet())))
                .entrySet()
                .stream()
                .map(k -> skuValidator.validate(new SkuValidator.SkuValidatorObject(k.getValue(), k.getKey())))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        ValidateResult<Set<SkuMainDataDTO>> result = new ValidateResult<>();
        result.setResult(IValidator.ValidatorName.SKU, skuMainDataDTOS);
        return result;
    }

    @Override
    public void afterDoCreation(OutboundPlanOrder outboundPlanOrder) {
        DomainEventPublisher.sendAsyncDomainEvent(new NewOutboundPlanOrderEvent().setOrderNo(outboundPlanOrder.getOrderNo()));
    }

    @Override
    public void syncValidate(List<OutboundPlanOrder> outboundPlanOrders) {
        SystemConfigDTO.OutboundConfigDTO outboundConfig = systemConfigApi.getOutboundConfig();
        if (!outboundConfig.isCheckRepeatedCustomerOrderNo()) {
            return;
        }

        Set<String> customerOrderNos = outboundPlanOrders.stream().map(OutboundPlanOrder::getCustomerOrderNo).collect(Collectors.toSet());

        if (customerOrderNos.size() != outboundPlanOrders.size()) {
            throw WmsException.throwWmsException(OUTBOUND_CUSTOMER_ORDER_NO_REPEATED);
        }

        outboundPlanOrders.stream().collect(Collectors.groupingBy(OutboundPlanOrder::getWarehouseCode))
                .forEach((warehouseCode, subOrders) -> {

                    List<String> subCustomerOrderNos = subOrders.stream().map(OutboundPlanOrder::getCustomerOrderNo).toList();
                    long count = outboundPlanOrderRepository.countByCustomerOrderNos(warehouseCode, subCustomerOrderNos);
                    if (count > 0) {
                        throw WmsException.throwWmsException(OUTBOUND_CUSTOMER_ORDER_NO_REPEATED);
                    }

                });

    }

    @Override
    public OutboundPlanOrderCancelContext prepareCancelContext(OutboundPlanOrderCancelDTO outboundPlanOrderCancelDTO) {

        List<OutboundPlanOrder> outboundPlanOrders = outboundPlanOrderRepository
                .findByCustomerOrderNos(outboundPlanOrderCancelDTO.getWarehouseCode(), outboundPlanOrderCancelDTO.getCustomerOrderNos());
        Set<String> waveNos = outboundPlanOrders.stream().map(OutboundPlanOrder::getWaveNo)
                .filter(ObjectUtils::isNotEmpty).collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(waveNos) && outboundPlanOrderCancelDTO.isSyncCancelOrdersInSameWave()) {
            outboundPlanOrders = outboundPlanOrderRepository.findByWaveNos(waveNos);
        }

        List<Long> outboundPlanOrderIds = outboundPlanOrders.stream().map(OutboundPlanOrder::getId).toList();
        List<OutboundPreAllocatedRecord> planPreAllocatedRecords = preAllocatedRecordRepository.findByOutboundPlanOrderIds(outboundPlanOrderIds);

        List<OutboundWave> outboundWaves = outboundWaveRepository.findByWaveNos(waveNos);

        List<PickingOrder> pickingOrders = pickingOrderRepository.findOrderAndDetailsByWaveNos(waveNos);

        return new OutboundPlanOrderCancelContext().setOutboundPlanOrders(outboundPlanOrders).setPickingOrders(pickingOrders)
                .setOutboundWaves(outboundWaves).setOutboundPreAllocatedRecords(planPreAllocatedRecords);

    }

}
