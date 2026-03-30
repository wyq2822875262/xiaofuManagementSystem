package com.xiaofu.system.service.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformShipmentOrder;

public interface IUniformShipmentOrderService
{
    public UniformShipmentOrder selectShipmentOrderById(Long shipmentId);

    public List<UniformShipmentOrder> selectShipmentOrderList(UniformShipmentOrder shipmentOrder);

    public boolean checkShipmentNoUnique(UniformShipmentOrder shipmentOrder);

    public int insertShipmentOrder(UniformShipmentOrder shipmentOrder);

    public int updateShipmentOrder(UniformShipmentOrder shipmentOrder);

    public int deleteShipmentOrderByIds(Long[] shipmentIds);
}
