package com.xiaofu.system.service.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformWorkOrder;

public interface IUniformWorkOrderService
{
    public UniformWorkOrder selectWorkOrderById(Long workOrderId);

    public List<UniformWorkOrder> selectWorkOrderList(UniformWorkOrder workOrder);

    public boolean checkWorkOrderNoUnique(UniformWorkOrder workOrder);

    public int insertWorkOrder(UniformWorkOrder workOrder);

    public int updateWorkOrder(UniformWorkOrder workOrder);

    public int deleteWorkOrderByIds(Long[] workOrderIds);

    public List<UniformWorkOrder> selectAvailableWorkOrderOptions();

    public void refreshWorkOrderState(Long workOrderId);
}
