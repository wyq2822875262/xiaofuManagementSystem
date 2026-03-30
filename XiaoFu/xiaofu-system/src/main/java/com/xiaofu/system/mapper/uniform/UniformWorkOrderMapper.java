package com.xiaofu.system.mapper.uniform;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.xiaofu.system.domain.uniform.UniformWorkOrder;

public interface UniformWorkOrderMapper
{
    public UniformWorkOrder selectWorkOrderById(Long workOrderId);

    public List<UniformWorkOrder> selectWorkOrderList(UniformWorkOrder workOrder);

    public UniformWorkOrder checkWorkOrderNoUnique(String workOrderNo);

    public int insertWorkOrder(UniformWorkOrder workOrder);

    public int updateWorkOrder(UniformWorkOrder workOrder);

    public int deleteWorkOrderByIds(Long[] workOrderIds);

    public List<UniformWorkOrder> selectAvailableWorkOrderOptions();

    public int updateWorkOrderFlowFields(@Param("workOrderId") Long workOrderId,
            @Param("completedQuantity") Integer completedQuantity,
            @Param("orderStatus") String orderStatus,
            @Param("qaStatus") String qaStatus,
            @Param("traceStatus") String traceStatus,
            @Param("traceGeneratedCount") Integer traceGeneratedCount,
            @Param("actualEndTime") Date actualEndTime);
}
