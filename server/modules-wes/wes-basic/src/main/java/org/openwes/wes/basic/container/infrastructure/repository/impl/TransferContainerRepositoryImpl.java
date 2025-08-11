package org.openwes.wes.basic.container.infrastructure.repository.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.openwes.domain.event.AggregatorRoot;
import org.openwes.wes.api.task.constants.TransferContainerStatusEnum;
import org.openwes.wes.basic.container.domain.entity.TransferContainer;
import org.openwes.wes.basic.container.domain.repository.TransferContainerRepository;
import org.openwes.wes.basic.container.infrastructure.persistence.mapper.TransferContainerPORepository;
import org.openwes.wes.basic.container.infrastructure.persistence.po.TransferContainerPO;
import org.openwes.wes.basic.container.infrastructure.persistence.transfer.TransferContainerPOTransfer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferContainerRepositoryImpl implements TransferContainerRepository {

    private final TransferContainerPORepository transferContainerPORepository;
    private final TransferContainerPOTransfer transferContainerPOTransfer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(TransferContainer transferContainer) {
        transferContainerPORepository.save(transferContainerPOTransfer.toPO(transferContainer));
        transferContainer.sendAndClearEvents();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(List<TransferContainer> transferContainers) {
        transferContainerPORepository.saveAll(transferContainerPOTransfer.toPOs(transferContainers));
        transferContainers.forEach(AggregatorRoot::sendAndClearEvents);
    }

    @Override
    public TransferContainer findByContainerCodeAndWarehouseCode(String containerCode, String warehouseCode) {
        return transferContainerPOTransfer.toDO(transferContainerPORepository.findByTransferContainerCodeAndWarehouseCode(containerCode, warehouseCode));
    }

    @Override
    public List<TransferContainer> findAllByWarehouseCodeAndContainerCodeIn(String warehouseCode, Collection<String> containerCodes) {
        return transferContainerPOTransfer.toDOs(transferContainerPORepository.findByWarehouseCodeAndTransferContainerCodeIn(warehouseCode, containerCodes));
    }

    @Override
    public List<TransferContainer> findAllLockedContainers(int limitDays) {
        Date date = DateUtils.addDays(new Date(), -limitDays);
        List<TransferContainerPO> transferContainerPOS = transferContainerPORepository
                .findAllByTransferContainerStatusAndUpdateTimeAfter(TransferContainerStatusEnum.LOCKED, date.getTime());
        return transferContainerPOTransfer.toDOs(transferContainerPOS);
    }

    @Override
    public TransferContainer findById(Long id) {
        TransferContainerPO transferContainerPO = transferContainerPORepository.findById(id).orElseThrow();
        return transferContainerPOTransfer.toDO(transferContainerPO);
    }

    @Override
    public boolean existByContainerSpecCode(String containerSpecCode, String warehouseCode) {
        return transferContainerPORepository.existsByContainerSpecCodeAndWarehouseCode(containerSpecCode, warehouseCode);
    }

}
