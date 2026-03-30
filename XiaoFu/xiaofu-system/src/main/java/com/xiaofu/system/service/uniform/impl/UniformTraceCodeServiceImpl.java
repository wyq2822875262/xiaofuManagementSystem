package com.xiaofu.system.service.uniform.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xiaofu.common.exception.ServiceException;
import com.xiaofu.common.utils.StringUtils;
import com.xiaofu.common.utils.uuid.Seq;
import com.xiaofu.system.domain.uniform.UniformQualityInspection;
import com.xiaofu.system.domain.uniform.UniformTraceCode;
import com.xiaofu.system.domain.uniform.UniformTraceGenerateRequest;
import com.xiaofu.system.domain.uniform.UniformWorkOrder;
import com.xiaofu.system.mapper.uniform.UniformQualityInspectionMapper;
import com.xiaofu.system.mapper.uniform.UniformTraceCodeMapper;
import com.xiaofu.system.mapper.uniform.UniformWorkOrderMapper;
import com.xiaofu.system.service.uniform.IUniformTraceCodeService;
import com.xiaofu.system.service.uniform.IUniformWorkOrderService;

@Service
public class UniformTraceCodeServiceImpl implements IUniformTraceCodeService
{
    @Autowired
    private UniformTraceCodeMapper traceCodeMapper;

    @Autowired
    private UniformWorkOrderMapper workOrderMapper;

    @Autowired
    private UniformQualityInspectionMapper inspectionMapper;

    @Autowired
    private IUniformWorkOrderService workOrderService;

    @Override
    public List<UniformTraceCode> selectTraceCodeList(UniformTraceCode traceCode)
    {
        return traceCodeMapper.selectTraceCodeList(traceCode);
    }

    @Override
    public List<UniformTraceCode> selectAvailableTraceOptions()
    {
        return traceCodeMapper.selectAvailableTraceOptions();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateTraceCodes(UniformTraceGenerateRequest request, String operatorName)
    {
        UniformWorkOrder workOrder = workOrderMapper.selectWorkOrderById(request.getWorkOrderId());
        if (StringUtils.isNull(workOrder))
        {
            throw new ServiceException("工单不存在，请刷新后重试");
        }
        UniformQualityInspection latestInspection = inspectionMapper.selectLatestInspectionByWorkOrderId(request.getWorkOrderId());
        if (StringUtils.isNull(latestInspection))
        {
            throw new ServiceException("该工单尚未完成质检，不能生成溯源码");
        }
        if (!"0".equals(latestInspection.getResult()))
        {
            throw new ServiceException("该工单最新质检不合格，系统已拦截溯源码生成");
        }
        if (traceCodeMapper.countTraceCodeByWorkOrderId(request.getWorkOrderId()) > 0)
        {
            throw new ServiceException("该工单已生成过溯源码，请勿重复生成");
        }

        String codeMode = StringUtils.isEmpty(workOrder.getCodeMode()) ? "1" : workOrder.getCodeMode();
        int generateCount = resolveGenerateCount(request.getGenerateCount(), workOrder.getPlannedQuantity(), codeMode);
        List<UniformTraceCode> traceCodes = new ArrayList<>();
        for (int i = 1; i <= generateCount; i++)
        {
            UniformTraceCode traceCode = new UniformTraceCode();
            traceCode.setWorkOrderId(workOrder.getWorkOrderId());
            traceCode.setWorkOrderNo(workOrder.getWorkOrderNo());
            traceCode.setInspectionId(latestInspection.getInspectionId());
            traceCode.setStyleName(workOrder.getStyleName());
            traceCode.setBatchNo(workOrder.getMaterialBatchNo());
            traceCode.setCodeMode(codeMode);
            traceCode.setSerialNo(i);
            traceCode.setTraceCode(generateTraceCode(codeMode, workOrder.getWorkOrderNo(), i));
            traceCode.setQrContent(buildQrContent(traceCode.getTraceCode(), workOrder, latestInspection));
            traceCode.setPrintStatus("0");
            traceCode.setTraceStatus("0");
            traceCode.setCreateBy(operatorName);
            traceCodes.add(traceCode);
        }

        int rows = traceCodeMapper.batchInsertTraceCode(traceCodes);
        workOrderService.refreshWorkOrderState(request.getWorkOrderId());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTraceCodesByIds(Long[] traceIds)
    {
        List<Long> workOrderIds = traceCodeMapper.selectWorkOrderIdsByTraceIds(traceIds);
        int rows = traceCodeMapper.deleteTraceCodeByIds(traceIds);
        workOrderIds.forEach(workOrderService::refreshWorkOrderState);
        return rows;
    }

    private int resolveGenerateCount(Integer requestCount, Integer plannedQuantity, String codeMode)
    {
        if ("2".equals(codeMode))
        {
            return 1;
        }
        int count = requestCount == null || requestCount <= 0 ? (plannedQuantity == null || plannedQuantity <= 0 ? 1 : plannedQuantity) : requestCount;
        if (count > 5000)
        {
            throw new ServiceException("单次最多生成5000个一物一码，请分批操作");
        }
        return count;
    }

    private String generateTraceCode(String codeMode, String workOrderNo, int serialNo)
    {
        return ("2".equals(codeMode) ? "BT" : "IT") + Seq.getId() + StringUtils.padl(serialNo, 4);
    }

    private String buildQrContent(String traceCode, UniformWorkOrder workOrder, UniformQualityInspection latestInspection)
    {
        return "XFTRACE|" + traceCode + "|" + workOrder.getWorkOrderNo() + "|" + workOrder.getMaterialBatchNo() + "|" + latestInspection.getInspectionNo();
    }
}
