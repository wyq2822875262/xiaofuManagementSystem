package com.xiaofu.system.service.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformRmaOrder;

public interface IUniformRmaOrderService
{
    public UniformRmaOrder selectRmaOrderById(Long rmaId);

    public List<UniformRmaOrder> selectRmaOrderList(UniformRmaOrder rmaOrder);

    public int insertRmaOrder(UniformRmaOrder rmaOrder);

    public int updateRmaOrder(UniformRmaOrder rmaOrder);

    public int deleteRmaOrderByIds(Long[] rmaIds);
}
