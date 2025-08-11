package org.openwes.wes.basic.main.data.infrastructure.repository.impl;

import lombok.RequiredArgsConstructor;
import org.openwes.common.utils.constants.RedisConstants;
import org.openwes.wes.basic.main.data.domain.entity.WarehouseMainData;
import org.openwes.wes.basic.main.data.domain.repository.WarehouseMainDataRepository;
import org.openwes.wes.basic.main.data.infrastructure.persistence.mapper.WarehouseMainDataPORepository;
import org.openwes.wes.basic.main.data.infrastructure.persistence.transfer.WarehouseMainDataPOTransfer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class WarehouseMainDataRepositoryImpl implements WarehouseMainDataRepository {

    private final WarehouseMainDataPORepository warehouseMainDataPORepository;
    private final WarehouseMainDataPOTransfer warehouseMainDataPOTransfer;

    @Override
    @CacheEvict(cacheNames = RedisConstants.WAREHOUSE_MAIN_DATA_CACHE, key = "#warehouse.warehouseCode")
    public void save(WarehouseMainData warehouse) {
        warehouseMainDataPORepository.save(warehouseMainDataPOTransfer.toPO(warehouse));
    }

    @Override
    public WarehouseMainData findById(Long id) {
        return warehouseMainDataPOTransfer.toDO(warehouseMainDataPORepository.findById(id).orElseThrow());
    }

    @Override
    @Cacheable(cacheNames = RedisConstants.WAREHOUSE_MAIN_DATA_CACHE, key = "#warehouseCode")
    public WarehouseMainData findByWarehouseCode(String warehouseCode) {
        return warehouseMainDataPOTransfer.toDO(warehouseMainDataPORepository.findByWarehouseCode(warehouseCode));
    }

    @Override
    public Collection<WarehouseMainData> findAllByWarehouseCodes(Collection<String> warehouseCodes) {
        return warehouseMainDataPOTransfer.toDOs(warehouseMainDataPORepository.findByWarehouseCodeIn(warehouseCodes));
    }

    @Override
    public Collection<WarehouseMainData> findAll() {
        return warehouseMainDataPOTransfer.toDOs(warehouseMainDataPORepository.findAll());
    }
}
