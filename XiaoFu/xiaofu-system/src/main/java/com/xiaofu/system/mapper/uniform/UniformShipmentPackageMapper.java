package com.xiaofu.system.mapper.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformShipmentPackage;

public interface UniformShipmentPackageMapper
{
    public List<UniformShipmentPackage> selectShipmentPackageListByShipmentId(Long shipmentId);

    public int deleteShipmentPackageByShipmentId(Long shipmentId);

    public int deleteShipmentPackageByShipmentIds(Long[] shipmentIds);

    public int batchInsertShipmentPackage(List<UniformShipmentPackage> packageList);
}
