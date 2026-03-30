package com.xiaofu.system.service.uniform.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xiaofu.common.exception.ServiceException;
import com.xiaofu.common.utils.StringUtils;
import com.xiaofu.system.domain.uniform.UniformLostFoundBindRequest;
import com.xiaofu.system.domain.uniform.UniformLostFoundBinding;
import com.xiaofu.system.domain.uniform.UniformLostFoundCard;
import com.xiaofu.system.domain.uniform.UniformMaterialBatch;
import com.xiaofu.system.domain.uniform.UniformPortalProcessNode;
import com.xiaofu.system.domain.uniform.UniformPortalTraceArchive;
import com.xiaofu.system.domain.uniform.UniformProcessRecord;
import com.xiaofu.system.domain.uniform.UniformQualityInspection;
import com.xiaofu.system.domain.uniform.UniformStyle;
import com.xiaofu.system.domain.uniform.UniformTraceCode;
import com.xiaofu.system.domain.uniform.UniformWorkOrder;
import com.xiaofu.system.mapper.uniform.UniformLostFoundBindingMapper;
import com.xiaofu.system.mapper.uniform.UniformMaterialBatchMapper;
import com.xiaofu.system.mapper.uniform.UniformProcessRecordMapper;
import com.xiaofu.system.mapper.uniform.UniformQualityInspectionMapper;
import com.xiaofu.system.mapper.uniform.UniformStyleMapper;
import com.xiaofu.system.mapper.uniform.UniformTraceCodeMapper;
import com.xiaofu.system.mapper.uniform.UniformWorkOrderMapper;
import com.xiaofu.system.service.uniform.IUniformPortalService;

@Service
public class UniformPortalServiceImpl implements IUniformPortalService
{
    @Autowired
    private UniformTraceCodeMapper traceCodeMapper;

    @Autowired
    private UniformWorkOrderMapper workOrderMapper;

    @Autowired
    private UniformStyleMapper styleMapper;

    @Autowired
    private UniformMaterialBatchMapper materialBatchMapper;

    @Autowired
    private UniformQualityInspectionMapper inspectionMapper;

    @Autowired
    private UniformProcessRecordMapper processRecordMapper;

    @Autowired
    private UniformLostFoundBindingMapper lostFoundBindingMapper;

    @Override
    public UniformPortalTraceArchive getTraceArchive(String traceCodeText)
    {
        UniformTraceCode traceCode = getValidTraceCode(traceCodeText);
        UniformWorkOrder workOrder = getRequiredWorkOrder(traceCode.getWorkOrderId());
        UniformStyle style = workOrder.getStyleId() == null ? null : styleMapper.selectStyleById(workOrder.getStyleId());
        UniformMaterialBatch materialBatch = workOrder.getMaterialBatchId() == null ? null : materialBatchMapper.selectMaterialBatchById(workOrder.getMaterialBatchId());
        UniformQualityInspection inspection = getInspection(traceCode, workOrder.getWorkOrderId());
        List<UniformProcessRecord> processRecords = processRecordMapper.selectProcessListByWorkOrderId(workOrder.getWorkOrderId());
        UniformLostFoundBinding binding = lostFoundBindingMapper.selectActiveBindingByTraceId(traceCode.getTraceId());

        UniformPortalTraceArchive archive = new UniformPortalTraceArchive();
        archive.setTraceId(traceCode.getTraceId());
        archive.setTraceCode(traceCode.getTraceCode());
        archive.setTraceStatus(traceCode.getTraceStatus());
        archive.setCodeMode(traceCode.getCodeMode());
        archive.setStyleName(resolveStyleName(traceCode, workOrder, style));
        archive.setSeason(style == null ? null : style.getSeason());
        archive.setStyleType(style == null ? null : style.getStyleType());
        archive.setSchoolName(workOrder.getSchoolName());
        archive.setSourceOrderNo(workOrder.getSourceOrderNo());
        archive.setWorkOrderNo(workOrder.getWorkOrderNo());
        archive.setBatchNo(resolveBatchNo(traceCode, workOrder, materialBatch));
        archive.setSupplierName(materialBatch == null ? null : materialBatch.getSupplierName());
        archive.setMaterialName(materialBatch == null ? null : materialBatch.getMaterialName());
        archive.setFabricName(style == null ? null : style.getFabricName());
        archive.setFabricComposition(resolveFabricComposition(style, materialBatch));
        archive.setColorName(materialBatch == null ? null : materialBatch.getColorName());
        archive.setGramWeight(materialBatch == null ? null : materialBatch.getGramWeight());
        archive.setVatNo(materialBatch == null ? null : materialBatch.getVatNo());
        archive.setFactoryDate(resolveFactoryDate(workOrder, processRecords));
        archive.setMaterialInspectDate(materialBatch == null ? null : materialBatch.getInspectDate());
        archive.setMaterialReportNo(materialBatch == null ? null : materialBatch.getReportNo());
        archive.setMaterialReportFileUrl(materialBatch == null ? null : materialBatch.getReportFileUrl());
        archive.setInspectionNo(inspection == null ? null : inspection.getInspectionNo());
        archive.setGbStandard(inspection == null ? null : inspection.getGbStandard());
        archive.setInspectionTime(inspection == null ? null : inspection.getInspectTime());
        archive.setInspectionReportFileUrl(inspection == null ? null : inspection.getReportFileUrl());
        archive.setFormaldehyde(inspection == null ? null : inspection.getFormaldehyde());
        archive.setPhValue(inspection == null ? null : inspection.getPhValue());
        archive.setOdorResult(inspection == null ? null : inspection.getOdorResult());
        archive.setAmineResult(inspection == null ? null : inspection.getAmineResult());
        archive.setColorFastness(inspection == null ? null : inspection.getColorFastness());
        archive.setPillingGrade(inspection == null ? null : inspection.getPillingGrade());
        archive.setConclusion(inspection == null ? null : inspection.getConclusion());
        archive.setCareInstructions(style == null ? null : style.getCareInstructions());
        archive.setCareTips(buildCareTips(style, materialBatch));
        archive.setProcessList(buildProcessNodes(processRecords));
        archive.setLostFoundCard(buildLostFoundCard(binding, traceCode.getTraceCode()));
        return archive;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UniformLostFoundCard saveLostFoundBinding(String traceCodeText, UniformLostFoundBindRequest request)
    {
        UniformTraceCode traceCode = getValidTraceCode(traceCodeText);
        UniformLostFoundBinding existing = lostFoundBindingMapper.selectActiveBindingByTraceId(traceCode.getTraceId());
        Date now = new Date();
        if (StringUtils.isNull(existing))
        {
            UniformLostFoundBinding binding = new UniformLostFoundBinding();
            binding.setTraceId(traceCode.getTraceId());
            binding.setTraceCode(traceCode.getTraceCode());
            binding.setSchoolName(request.getSchoolName());
            binding.setClassName(request.getClassName());
            binding.setStudentName(request.getStudentName());
            binding.setContactName(request.getContactName());
            binding.setContactPhone(request.getContactPhone());
            binding.setOwnerRemark(request.getOwnerRemark());
            binding.setBindStatus("0");
            binding.setBindTime(now);
            binding.setCreateBy("portal");
            lostFoundBindingMapper.insertLostFoundBinding(binding);
            return buildLostFoundCard(binding, traceCode.getTraceCode());
        }

        existing.setSchoolName(request.getSchoolName());
        existing.setClassName(request.getClassName());
        existing.setStudentName(request.getStudentName());
        existing.setContactName(request.getContactName());
        existing.setContactPhone(request.getContactPhone());
        existing.setOwnerRemark(request.getOwnerRemark());
        existing.setBindStatus("0");
        existing.setBindTime(now);
        existing.setUpdateBy("portal");
        lostFoundBindingMapper.updateLostFoundBinding(existing);
        return buildLostFoundCard(existing, traceCode.getTraceCode());
    }

    private UniformTraceCode getValidTraceCode(String traceCodeText)
    {
        String traceCodeValue = normalizeTraceCode(traceCodeText);
        if (StringUtils.isEmpty(traceCodeValue))
        {
            throw new ServiceException("请先输入有效的溯源码");
        }
        UniformTraceCode traceCode = traceCodeMapper.selectTraceCodeByTraceCode(traceCodeValue);
        if (StringUtils.isNull(traceCode) || !"0".equals(traceCode.getTraceStatus()))
        {
            throw new ServiceException("未查询到有效的溯源码档案");
        }
        return traceCode;
    }

    private UniformWorkOrder getRequiredWorkOrder(Long workOrderId)
    {
        UniformWorkOrder workOrder = workOrderMapper.selectWorkOrderById(workOrderId);
        if (StringUtils.isNull(workOrder))
        {
            throw new ServiceException("当前溯源码关联工单不存在");
        }
        return workOrder;
    }

    private UniformQualityInspection getInspection(UniformTraceCode traceCode, Long workOrderId)
    {
        if (traceCode.getInspectionId() != null)
        {
            UniformQualityInspection inspection = inspectionMapper.selectInspectionById(traceCode.getInspectionId());
            if (StringUtils.isNotNull(inspection))
            {
                return inspection;
            }
        }
        return inspectionMapper.selectLatestInspectionByWorkOrderId(workOrderId);
    }

    private String normalizeTraceCode(String traceCodeText)
    {
        String value = StringUtils.trimToEmpty(traceCodeText);
        if (StringUtils.isEmpty(value))
        {
            return value;
        }
        if (value.startsWith("XFTRACE|"))
        {
            String[] segments = value.split("\\|");
            if (segments.length > 1)
            {
                return StringUtils.trimToEmpty(segments[1]);
            }
        }
        return value;
    }

    private String resolveStyleName(UniformTraceCode traceCode, UniformWorkOrder workOrder, UniformStyle style)
    {
        if (style != null && StringUtils.isNotEmpty(style.getStyleName()))
        {
            return style.getStyleName();
        }
        if (StringUtils.isNotEmpty(workOrder.getStyleName()))
        {
            return workOrder.getStyleName();
        }
        return traceCode.getStyleName();
    }

    private String resolveBatchNo(UniformTraceCode traceCode, UniformWorkOrder workOrder, UniformMaterialBatch materialBatch)
    {
        if (materialBatch != null && StringUtils.isNotEmpty(materialBatch.getBatchNo()))
        {
            return materialBatch.getBatchNo();
        }
        if (StringUtils.isNotEmpty(workOrder.getMaterialBatchNo()))
        {
            return workOrder.getMaterialBatchNo();
        }
        return traceCode.getBatchNo();
    }

    private String resolveFabricComposition(UniformStyle style, UniformMaterialBatch materialBatch)
    {
        if (style != null && StringUtils.isNotEmpty(style.getFabricComposition()))
        {
            return style.getFabricComposition();
        }
        return materialBatch == null ? null : materialBatch.getFabricComposition();
    }

    private Date resolveFactoryDate(UniformWorkOrder workOrder, List<UniformProcessRecord> processRecords)
    {
        if (workOrder.getActualEndTime() != null)
        {
            return workOrder.getActualEndTime();
        }
        if (StringUtils.isEmpty(processRecords))
        {
            return workOrder.getPlannedEndDate();
        }
        Date latestEndTime = null;
        for (UniformProcessRecord processRecord : processRecords)
        {
            if (processRecord.getEndTime() != null)
            {
                if (latestEndTime == null || processRecord.getEndTime().after(latestEndTime))
                {
                    latestEndTime = processRecord.getEndTime();
                }
            }
        }
        if (latestEndTime != null)
        {
            return latestEndTime;
        }
        return workOrder.getPlannedEndDate();
    }

    private List<String> buildCareTips(UniformStyle style, UniformMaterialBatch materialBatch)
    {
        List<String> tips = new ArrayList<>();
        String careInstructions = style == null ? null : style.getCareInstructions();
        String fabricComposition = resolveFabricComposition(style, materialBatch);
        String materialName = materialBatch == null ? null : materialBatch.getMaterialName();
        String fabricName = style == null ? null : style.getFabricName();
        String styleName = style == null ? null : style.getStyleName();

        if (StringUtils.isNotEmpty(careInstructions))
        {
            String[] segments = careInstructions.split("[\\r\\n；;]+");
            for (String segment : segments)
            {
                addTip(tips, segment);
            }
        }
        if (containsKeyword(fabricComposition, "聚酯"))
        {
            addTip(tips, "建议 30°C 以下轻柔洗涤，减少高温导致的纤维老化。");
        }
        if (containsKeyword(fabricComposition, "氨纶"))
        {
            addTip(tips, "含氨纶材质请避免高温烘干，保持面料弹性与版型。");
        }
        if (containsKeyword(materialName, "反光") || containsKeyword(fabricName, "反光") || containsKeyword(careInstructions, "反光"))
        {
            addTip(tips, "带反光条部位请勿高温熨烫，建议垫布低温整烫。");
        }
        if ("outerwear".equals(style == null ? null : style.getStyleType()) || containsKeyword(styleName, "冲锋") || containsKeyword(careInstructions, "冲锋"))
        {
            addTip(tips, "外套类校服清洗后建议自然阴干，避免暴晒影响防护层。");
        }
        if (tips.isEmpty())
        {
            addTip(tips, "建议与深色衣物分开洗涤，常温阴干即可。");
            addTip(tips, "如需熨烫，请优先选择低温蒸汽模式。");
        }
        return tips;
    }

    private void addTip(List<String> tips, String tip)
    {
        String value = StringUtils.trimToEmpty(tip);
        if (StringUtils.isNotEmpty(value) && !tips.contains(value))
        {
            tips.add(value);
        }
    }

    private boolean containsKeyword(String text, String keyword)
    {
        return StringUtils.isNotEmpty(text) && StringUtils.isNotEmpty(keyword) && text.contains(keyword);
    }

    private List<UniformPortalProcessNode> buildProcessNodes(List<UniformProcessRecord> processRecords)
    {
        List<UniformPortalProcessNode> nodes = new ArrayList<>();
        if (StringUtils.isEmpty(processRecords))
        {
            return nodes;
        }
        for (UniformProcessRecord processRecord : processRecords)
        {
            UniformPortalProcessNode node = new UniformPortalProcessNode();
            node.setProcessType(processRecord.getProcessType());
            node.setProcessName(resolveProcessName(processRecord.getProcessType()));
            node.setOperatorName(processRecord.getOperatorName());
            node.setInspectorName(processRecord.getInspectorName());
            node.setDeviceType(resolveDeviceType(processRecord.getDeviceType()));
            node.setStartTime(processRecord.getStartTime());
            node.setEndTime(processRecord.getEndTime());
            node.setPassQuantity(processRecord.getPassQuantity());
            node.setDefectiveQuantity(processRecord.getDefectiveQuantity());
            node.setRemark(processRecord.getRemark());
            nodes.add(node);
        }
        return nodes;
    }

    private String resolveProcessName(String processType)
    {
        if ("cutting".equals(processType))
        {
            return "裁剪组";
        }
        if ("sewing".equals(processType))
        {
            return "缝纫组";
        }
        if ("ironing".equals(processType))
        {
            return "熨烫组";
        }
        return "工序节点";
    }

    private String resolveDeviceType(String deviceType)
    {
        if ("tablet".equals(deviceType))
        {
            return "平板";
        }
        if ("scanner".equals(deviceType))
        {
            return "扫码枪";
        }
        if ("manual".equals(deviceType))
        {
            return "人工录入";
        }
        return "未知来源";
    }

    private UniformLostFoundCard buildLostFoundCard(UniformLostFoundBinding binding, String traceCode)
    {
        UniformLostFoundCard card = new UniformLostFoundCard();
        card.setTraceCode(traceCode);
        if (StringUtils.isNull(binding))
        {
            card.setBound(false);
            return card;
        }
        card.setBound(true);
        card.setSchoolName(binding.getSchoolName());
        card.setClassName(binding.getClassName());
        card.setStudentName(binding.getStudentName());
        card.setContactName(binding.getContactName());
        card.setMaskedContactPhone(maskPhone(binding.getContactPhone()));
        card.setOwnerRemark(binding.getOwnerRemark());
        card.setBindTime(binding.getBindTime());
        return card;
    }

    private String maskPhone(String phone)
    {
        String value = StringUtils.trimToEmpty(phone);
        if (value.length() < 7)
        {
            return value;
        }
        return value.substring(0, 3) + "****" + value.substring(value.length() - 4);
    }
}
