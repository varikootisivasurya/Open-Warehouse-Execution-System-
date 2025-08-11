package org.openwes.wes.basic.container.infrastructure.repository.impl;

import lombok.RequiredArgsConstructor;
import org.openwes.wes.api.task.constants.TransferContainerRecordStatusEnum;
import org.openwes.wes.basic.container.domain.entity.TransferContainerRecord;
import org.openwes.wes.basic.container.domain.repository.TransferContainerRecordRepository;
import org.openwes.wes.basic.container.infrastructure.persistence.mapper.TransferContainerRecordPORepository;
import org.openwes.wes.basic.container.infrastructure.persistence.po.TransferContainerRecordPO;
import org.openwes.wes.basic.container.infrastructure.persistence.transfer.TransferContainerRecordPOTransfer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferContainerRecordRepositoryImpl implements TransferContainerRecordRepository {

    private final TransferContainerRecordPORepository transferContainerPORepository;
    private final TransferContainerRecordPOTransfer transferContainerPOTransfer;

    @Transactional
    @Override
    public TransferContainerRecord save(TransferContainerRecord transferContainerRecord) {
        TransferContainerRecordPO transferContainerPO = transferContainerPORepository.save(transferContainerPOTransfer.toPO(transferContainerRecord));
        return transferContainerPOTransfer.toDO(transferContainerPO);
    }

    @Override
    public void delete(Long id) {
        transferContainerPORepository.deleteById(id);
    }

    @Override
    public TransferContainerRecord findById(Long transferContainerRecordId) {
        return transferContainerPOTransfer.toDO(transferContainerPORepository.findById(transferContainerRecordId).orElseThrow());
    }

    @Override
    public List<TransferContainerRecord> findAllById(List<Long> ids) {
        List<TransferContainerRecordPO> transferContainerRecordPOs = transferContainerPORepository.findAllById(ids);
        return transferContainerPOTransfer.toDOs(transferContainerRecordPOs);
    }

    @Override
    public TransferContainerRecord findCurrentPickOrderTransferContainerRecord(Long pickingOrderId, String containerCode) {
        List<TransferContainerRecordPO> transferContainerRecordPOS = transferContainerPORepository.findByTransferContainerCodeAndPickingOrderIdAndTransferContainerStatus(containerCode, pickingOrderId, TransferContainerRecordStatusEnum.BOUNDED);

        if (!transferContainerRecordPOS.isEmpty()) {
            return transferContainerPOTransfer.toDO(transferContainerRecordPOS.get(0));
        }
        return null;
    }

    @Override
    public List<TransferContainerRecord> findAllByPickingOrderId(Long pickingOrderId) {
        List<TransferContainerRecordPO> transferContainerRecordPOs = transferContainerPORepository.findAllByPickingOrderId(pickingOrderId);
        return transferContainerPOTransfer.toDOs(transferContainerRecordPOs);
    }

    @Override
    public List<TransferContainerRecord> findAllBoundedRecordsByPickingOrderId(Long pickingOrderId) {
        List<TransferContainerRecordPO> transferContainerRecordPOs = transferContainerPORepository.findAllByPickingOrderId(pickingOrderId)
                .stream().filter(v -> v.getTransferContainerStatus() == TransferContainerRecordStatusEnum.BOUNDED)
                .toList();
        return transferContainerPOTransfer.toDOs(transferContainerRecordPOs);
    }

}
