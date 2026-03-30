package com.xiaofu.system.mapper.uniform;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.xiaofu.system.domain.uniform.UniformShipmentOrder;

public interface UniformShipmentOrderMapper
{
    public UniformShipmentOrder selectShipmentOrderById(Long shipmentId);

    public List<UniformShipmentOrder> selectShipmentOrderList(UniformShipmentOrder shipmentOrder);

    public UniformShipmentOrder checkShipmentNoUnique(String shipmentNo);

    public int insertShipmentOrder(UniformShipmentOrder shipmentOrder);

    public int updateShipmentOrder(UniformShipmentOrder shipmentOrder);

    public int deleteShipmentOrderByIds(Long[] shipmentIds);

    public int updateShipmentFlowFields(@Param("shipmentId") Long shipmentId,
            @Param("totalQuantity") Integer totalQuantity,
            @Param("packedQuantity") Integer packedQuantity,
            @Param("shipmentStatus") String shipmentStatus);
}
