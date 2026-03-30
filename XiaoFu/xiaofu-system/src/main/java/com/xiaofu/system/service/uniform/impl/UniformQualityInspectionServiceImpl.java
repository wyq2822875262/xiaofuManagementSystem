package com.xiaofu.system.service.uniform.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xiaofu.common.exception.ServiceException;
import com.xiaofu.common.utils.StringUtils;
import com.xiaofu.common.utils.uuid.Seq;
import com.xiaofu.system.domain.uniform.UniformQualityInspection;
import com.xiaofu.system.domain.uniform.UniformWorkOrder;
import com.xiaofu.system.mapper.uniform.UniformQualityInspectionMapper;
import com.xiaofu.system.mapper.uniform.UniformWorkOrderMapper;
import com.xiaofu.system.service.uniform.IUniformQualityInspectionService;
import com.xiaofu.system.service.uniform.IUniformWorkOrderService;

@Service
public class UniformQualityInspectionServiceImpl implements IUniformQualityInspectionService
{
    @Autowired
    private UniformQualityInspectionMapper inspectionMapper;

    @Autowired
    private UniformWorkOrderMapper workOrderMapper;

    @Autowired
    private IUniformWorkOrderService workOrderService;

    @Override
    public UniformQualityInspection selectInspectionById(Long inspectionId)
    {
        return inspectionMapper.selectInspectionById(inspectionId);
    }

    @Override
    public List<UniformQualityInspection> selectInspectionList(UniformQualityInspection inspection)
    {
        return inspectionMapper.selectInspectionList(inspection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertInspection(UniformQualityInspection inspection)
    {
        validateWorkOrderExists(inspection.getWorkOrderId());
        fillInspectionDefaults(inspection);
        evaluateInspection(inspection);
        int rows = inspectionMapper.insertInspection(inspection);
        workOrderService.refreshWorkOrderState(inspection.getWorkOrderId());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateInspection(UniformQualityInspection inspection)
    {
        validateWorkOrderExists(inspection.getWorkOrderId());
        fillInspectionDefaults(inspection);
        evaluateInspection(inspection);
        int rows = inspectionMapper.updateInspection(inspection);
        workOrderService.refreshWorkOrderState(inspection.getWorkOrderId());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteInspectionByIds(Long[] inspectionIds)
    {
        List<Long> workOrderIds = new ArrayList<>();
        for (Long inspectionId : inspectionIds)
        {
            UniformQualityInspection inspection = inspectionMapper.selectInspectionById(inspectionId);
            if (StringUtils.isNotNull(inspection))
            {
                workOrderIds.add(inspection.getWorkOrderId());
            }
        }
        int rows = inspectionMapper.deleteInspectionByIds(inspectionIds);
        workOrderIds.forEach(workOrderService::refreshWorkOrderState);
        return rows;
    }

    private void validateWorkOrderExists(Long workOrderId)
    {
        UniformWorkOrder workOrder = workOrderMapper.selectWorkOrderById(workOrderId);
        if (StringUtils.isNull(workOrder))
        {
            throw new ServiceException("关联工单不存在，请刷新后重试");
        }
    }

    private void fillInspectionDefaults(UniformQualityInspection inspection)
    {
        if (StringUtils.isEmpty(inspection.getInspectionNo()))
        {
            inspection.setInspectionNo("QA" + Seq.getId());
        }
        if (StringUtils.isEmpty(inspection.getGbStandard()))
        {
            inspection.setGbStandard("GB/T 31888-2015");
        }
        if (inspection.getInspectTime() == null)
        {
            inspection.setInspectTime(new Date());
        }
    }

    private void evaluateInspection(UniformQualityInspection inspection)
    {
        List<String> failReasons = new ArrayList<>();
        if (isGreaterThan(inspection.getFormaldehyde(), new BigDecimal("75")))
        {
            failReasons.add("甲醛含量超标");
        }
        if (inspection.getPhValue() != null && (inspection.getPhValue().compareTo(new BigDecimal("4.0")) < 0
                || inspection.getPhValue().compareTo(new BigDecimal("8.5")) > 0))
        {
            failReasons.add("PH值不在4.0-8.5范围内");
        }
        if ("1".equals(inspection.getOdorResult()))
        {
            failReasons.add("存在异味");
        }
        if ("1".equals(inspection.getAmineResult()))
        {
            failReasons.add("可分解致癌芳香胺染料不合格");
        }
        if (inspection.getColorFastness() != null && inspection.getColorFastness().compareTo(new BigDecimal("3.0")) < 0)
        {
            failReasons.add("色牢度低于3级");
        }
        if (inspection.getPillingGrade() != null && inspection.getPillingGrade().compareTo(new BigDecimal("3.0")) < 0)
        {
            failReasons.add("起球率低于3级");
        }

        if (failReasons.isEmpty())
        {
            inspection.setResult("0");
            inspection.setInterceptStatus("0");
            if (StringUtils.isEmpty(inspection.getConclusion()))
            {
                inspection.setConclusion("符合 GB/T 31888-2015 要求，可放行赋码");
            }
            inspection.setFailReason("");
        }
        else
        {
            inspection.setResult("1");
            inspection.setInterceptStatus("1");
            inspection.setFailReason(String.join("；", failReasons));
            if (StringUtils.isEmpty(inspection.getConclusion()))
            {
                inspection.setConclusion("存在不合格项，系统已自动拦截赋码");
            }
        }
    }

    private boolean isGreaterThan(BigDecimal source, BigDecimal target)
    {
        return source != null && source.compareTo(target) > 0;
    }
}
