package com.xiaofu.system.service.uniform.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaofu.common.exception.ServiceException;
import com.xiaofu.common.utils.StringUtils;
import com.xiaofu.common.utils.uuid.Seq;
import com.xiaofu.system.domain.uniform.UniformRmaOrder;
import com.xiaofu.system.domain.uniform.UniformTraceCode;
import com.xiaofu.system.mapper.uniform.UniformRmaOrderMapper;
import com.xiaofu.system.mapper.uniform.UniformTraceCodeMapper;
import com.xiaofu.system.service.uniform.IUniformRmaOrderService;

@Service
public class UniformRmaOrderServiceImpl implements IUniformRmaOrderService
{
    @Autowired
    private UniformRmaOrderMapper rmaOrderMapper;

    @Autowired
    private UniformTraceCodeMapper traceCodeMapper;

    @Override
    public UniformRmaOrder selectRmaOrderById(Long rmaId)
    {
        return rmaOrderMapper.selectRmaOrderById(rmaId);
    }

    @Override
    public List<UniformRmaOrder> selectRmaOrderList(UniformRmaOrder rmaOrder)
    {
        return rmaOrderMapper.selectRmaOrderList(rmaOrder);
    }

    @Override
    public int insertRmaOrder(UniformRmaOrder rmaOrder)
    {
        fillDefaults(rmaOrder);
        validateAndFillTraceInfo(rmaOrder);
        return rmaOrderMapper.insertRmaOrder(rmaOrder);
    }

    @Override
    public int updateRmaOrder(UniformRmaOrder rmaOrder)
    {
        fillDefaults(rmaOrder);
        validateAndFillTraceInfo(rmaOrder);
        return rmaOrderMapper.updateRmaOrder(rmaOrder);
    }

    @Override
    public int deleteRmaOrderByIds(Long[] rmaIds)
    {
        return rmaOrderMapper.deleteRmaOrderByIds(rmaIds);
    }

    private void fillDefaults(UniformRmaOrder rmaOrder)
    {
        if (StringUtils.isEmpty(rmaOrder.getRmaNo()))
        {
            rmaOrder.setRmaNo("RMA" + Seq.getId());
        }
        if (StringUtils.isEmpty(rmaOrder.getStatus()))
        {
            rmaOrder.setStatus("0");
        }
        if (rmaOrder.getApplyTime() == null)
        {
            rmaOrder.setApplyTime(new Date());
        }
    }

    private void validateAndFillTraceInfo(UniformRmaOrder rmaOrder)
    {
        UniformTraceCode traceCode = traceCodeMapper.selectTraceCodeById(rmaOrder.getTraceId());
        if (StringUtils.isNull(traceCode))
        {
            throw new ServiceException("关联溯源码不存在，请刷新后重试");
        }
        rmaOrder.setTraceCode(traceCode.getTraceCode());
        rmaOrder.setWorkOrderId(traceCode.getWorkOrderId());
        rmaOrder.setWorkOrderNo(traceCode.getWorkOrderNo());
        rmaOrder.setStyleName(traceCode.getStyleName());
        if ("2".equals(rmaOrder.getRequestType()))
        {
            rmaOrder.setNewSizeCode("");
        }
    }
}
