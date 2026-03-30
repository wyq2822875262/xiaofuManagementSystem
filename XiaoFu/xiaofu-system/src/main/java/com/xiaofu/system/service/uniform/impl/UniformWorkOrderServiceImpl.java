package com.xiaofu.system.service.uniform.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xiaofu.common.constant.UserConstants;
import com.xiaofu.common.exception.ServiceException;
import com.xiaofu.common.utils.StringUtils;
import com.xiaofu.common.utils.uuid.Seq;
import com.xiaofu.system.domain.uniform.UniformMaterialBatch;
import com.xiaofu.system.domain.uniform.UniformProcessRecord;
import com.xiaofu.system.domain.uniform.UniformQualityInspection;
import com.xiaofu.system.domain.uniform.UniformStyle;
import com.xiaofu.system.domain.uniform.UniformWorkOrder;
import com.xiaofu.system.mapper.uniform.UniformMaterialBatchMapper;
import com.xiaofu.system.mapper.uniform.UniformProcessRecordMapper;
import com.xiaofu.system.mapper.uniform.UniformQualityInspectionMapper;
import com.xiaofu.system.mapper.uniform.UniformTraceCodeMapper;
import com.xiaofu.system.mapper.uniform.UniformWorkOrderMapper;
import com.xiaofu.system.service.uniform.IUniformWorkOrderService;
import com.xiaofu.system.mapper.uniform.UniformStyleMapper;

@Service
public class UniformWorkOrderServiceImpl implements IUniformWorkOrderService
{
    @Autowired
    private UniformWorkOrderMapper workOrderMapper;

    @Autowired
    private UniformProcessRecordMapper processRecordMapper;

    @Autowired
    private UniformMaterialBatchMapper materialBatchMapper;

    @Autowired
    private UniformStyleMapper styleMapper;

    @Autowired
    private UniformQualityInspectionMapper inspectionMapper;

    @Autowired
    private UniformTraceCodeMapper traceCodeMapper;

    @Override
    public UniformWorkOrder selectWorkOrderById(Long workOrderId)
    {
        UniformWorkOrder workOrder = workOrderMapper.selectWorkOrderById(workOrderId);
        if (StringUtils.isNotNull(workOrder))
        {
            workOrder.setProcessList(processRecordMapper.selectProcessListByWorkOrderId(workOrderId));
        }
        return workOrder;
    }

    @Override
    public List<UniformWorkOrder> selectWorkOrderList(UniformWorkOrder workOrder)
    {
        return workOrderMapper.selectWorkOrderList(workOrder);
    }

    @Override
    public boolean checkWorkOrderNoUnique(UniformWorkOrder workOrder)
    {
        Long workOrderId = StringUtils.isNull(workOrder.getWorkOrderId()) ? -1L : workOrder.getWorkOrderId();
        UniformWorkOrder info = workOrderMapper.checkWorkOrderNoUnique(workOrder.getWorkOrderNo());
        if (StringUtils.isNotNull(info) && info.getWorkOrderId().longValue() != workOrderId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertWorkOrder(UniformWorkOrder workOrder)
    {
        fillDefaultValues(workOrder);
        validateWorkOrder(workOrder);
        int rows = workOrderMapper.insertWorkOrder(workOrder);
        saveProcessRecords(workOrder);
        markMaterialBatchInUse(workOrder.getMaterialBatchId());
        refreshWorkOrderState(workOrder.getWorkOrderId());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateWorkOrder(UniformWorkOrder workOrder)
    {
        validateWorkOrder(workOrder);
        workOrderMapper.updateWorkOrder(workOrder);
        processRecordMapper.deleteProcessByWorkOrderId(workOrder.getWorkOrderId());
        saveProcessRecords(workOrder);
        markMaterialBatchInUse(workOrder.getMaterialBatchId());
        refreshWorkOrderState(workOrder.getWorkOrderId());
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteWorkOrderByIds(Long[] workOrderIds)
    {
        processRecordMapper.deleteProcessByWorkOrderIds(workOrderIds);
        inspectionMapper.deleteInspectionByWorkOrderIds(workOrderIds);
        traceCodeMapper.deleteTraceCodeByWorkOrderIds(workOrderIds);
        return workOrderMapper.deleteWorkOrderByIds(workOrderIds);
    }

    @Override
    public List<UniformWorkOrder> selectAvailableWorkOrderOptions()
    {
        return workOrderMapper.selectAvailableWorkOrderOptions();
    }

    @Override
    public void refreshWorkOrderState(Long workOrderId)
    {
        UniformWorkOrder workOrder = workOrderMapper.selectWorkOrderById(workOrderId);
        if (StringUtils.isNull(workOrder))
        {
            return;
        }

        List<UniformProcessRecord> processList = processRecordMapper.selectProcessListByWorkOrderId(workOrderId);
        UniformQualityInspection latestInspection = inspectionMapper.selectLatestInspectionByWorkOrderId(workOrderId);
        int traceCount = traceCodeMapper.countTraceCodeByWorkOrderId(workOrderId);

        Integer completedQuantity = 0;
        String orderStatus = "0";
        String qaStatus = "0";
        String traceStatus = "0";
        Date actualEndTime = null;

        int maxStage = 0;
        UniformProcessRecord latestStageRecord = null;
        for (UniformProcessRecord record : processList)
        {
            int stage = getProcessStage(record.getProcessType());
            if (stage >= maxStage)
            {
                maxStage = stage;
                latestStageRecord = record;
                if (record.getPassQuantity() != null)
                {
                    completedQuantity = record.getPassQuantity();
                }
            }
        }

        if (maxStage == 1)
        {
            orderStatus = "1";
        }
        else if (maxStage == 2)
        {
            orderStatus = "2";
        }
        else if (maxStage == 3)
        {
            if (latestStageRecord != null && latestStageRecord.getEndTime() == null)
            {
                orderStatus = "3";
            }
            else
            {
                orderStatus = "4";
            }
        }

        if (latestInspection != null)
        {
            if ("0".equals(latestInspection.getResult()))
            {
                qaStatus = "1";
                orderStatus = "5";
            }
            else
            {
                qaStatus = "2";
                orderStatus = "8";
                actualEndTime = latestInspection.getInspectTime();
            }
        }

        if (traceCount > 0)
        {
            traceStatus = "1";
            orderStatus = "6";
            actualEndTime = new Date();
        }

        workOrderMapper.updateWorkOrderFlowFields(workOrderId, completedQuantity, orderStatus, qaStatus, traceStatus, traceCount, actualEndTime);
    }

    private void fillDefaultValues(UniformWorkOrder workOrder)
    {
        if (StringUtils.isEmpty(workOrder.getWorkOrderNo()))
        {
            workOrder.setWorkOrderNo("WO" + Seq.getId());
        }
        if (StringUtils.isEmpty(workOrder.getQaStatus()))
        {
            workOrder.setQaStatus("0");
        }
        if (StringUtils.isEmpty(workOrder.getTraceStatus()))
        {
            workOrder.setTraceStatus("0");
        }
        if (StringUtils.isEmpty(workOrder.getOrderStatus()))
        {
            workOrder.setOrderStatus("0");
        }
        if (workOrder.getTraceGeneratedCount() == null)
        {
            workOrder.setTraceGeneratedCount(0);
        }
    }

    private void validateWorkOrder(UniformWorkOrder workOrder)
    {
        UniformStyle style = styleMapper.selectStyleById(workOrder.getStyleId());
        if (StringUtils.isNull(style))
        {
            throw new ServiceException("关联款式不存在，请刷新后重试");
        }
        UniformMaterialBatch batch = materialBatchMapper.selectMaterialBatchById(workOrder.getMaterialBatchId());
        if (StringUtils.isNull(batch))
        {
            throw new ServiceException("关联原料批次不存在，请刷新后重试");
        }
        if ("3".equals(batch.getStatus()))
        {
            throw new ServiceException("该原料批次已锁定，禁止绑定到生产工单");
        }
        if (StringUtils.isEmpty(workOrder.getCodeMode()))
        {
            workOrder.setCodeMode(style.getCodeMode());
        }
    }

    private void saveProcessRecords(UniformWorkOrder workOrder)
    {
        List<UniformProcessRecord> records = normalizeProcessRecords(workOrder.getProcessList());
        if (StringUtils.isEmpty(records))
        {
            return;
        }
        records.forEach(item -> item.setWorkOrderId(workOrder.getWorkOrderId()));
        processRecordMapper.batchInsertProcess(records);
    }

    private List<UniformProcessRecord> normalizeProcessRecords(List<UniformProcessRecord> records)
    {
        if (StringUtils.isEmpty(records))
        {
            return new ArrayList<>();
        }
        List<UniformProcessRecord> normalized = new ArrayList<>();
        for (UniformProcessRecord record : records)
        {
            if (record == null || StringUtils.isEmpty(record.getProcessType()))
            {
                continue;
            }
            if (StringUtils.isEmpty(record.getDeviceType()))
            {
                record.setDeviceType("manual");
            }
            normalized.add(record);
        }
        return normalized;
    }

    private void markMaterialBatchInUse(Long batchId)
    {
        UniformMaterialBatch batch = materialBatchMapper.selectMaterialBatchById(batchId);
        if (StringUtils.isNotNull(batch) && !"1".equals(batch.getStatus()))
        {
            batch.setStatus("1");
            materialBatchMapper.updateMaterialBatch(batch);
        }
    }

    private int getProcessStage(String processType)
    {
        if ("cutting".equals(processType))
        {
            return 1;
        }
        if ("sewing".equals(processType))
        {
            return 2;
        }
        if ("ironing".equals(processType))
        {
            return 3;
        }
        return 0;
    }
}
