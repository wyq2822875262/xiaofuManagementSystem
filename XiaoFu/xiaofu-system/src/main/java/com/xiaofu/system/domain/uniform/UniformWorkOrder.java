package com.xiaofu.system.domain.uniform;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaofu.common.annotation.Excel;
import com.xiaofu.common.annotation.Excel.ColumnType;
import com.xiaofu.common.core.domain.BaseEntity;
import com.xiaofu.common.xss.Xss;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 生产工单对象 xf_work_order
 */
public class UniformWorkOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "工单ID", cellType = ColumnType.NUMERIC)
    private Long workOrderId;

    @Excel(name = "工单号")
    private String workOrderNo;

    @Excel(name = "客户订单号")
    private String sourceOrderNo;

    @Excel(name = "学校/客户")
    private String schoolName;

    @Excel(name = "款式名称")
    private String styleName;

    private Long styleId;

    @Excel(name = "原料批次号")
    private String materialBatchNo;

    private Long materialBatchId;

    @Excel(name = "计划数量", cellType = ColumnType.NUMERIC)
    private Integer plannedQuantity;

    @Excel(name = "完工数量", cellType = ColumnType.NUMERIC)
    private Integer completedQuantity;

    @Excel(name = "赋码模式", readConverterExp = "1=一物一码,2=一批一码")
    private String codeMode;

    @Excel(name = "工单状态", readConverterExp = "0=待开工,1=裁剪中,2=缝纫中,3=熨烫中,4=待质检,5=质检合格,6=已赋码,8=质检拦截")
    private String orderStatus;

    @Excel(name = "质检状态", readConverterExp = "0=待检,1=合格,2=不合格")
    private String qaStatus;

    @Excel(name = "赋码状态", readConverterExp = "0=未生成,1=已生成")
    private String traceStatus;

    @Excel(name = "溯源码数量", cellType = ColumnType.NUMERIC)
    private Integer traceGeneratedCount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开工日", width = 18, dateFormat = "yyyy-MM-dd")
    private Date plannedStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划完工日", width = 18, dateFormat = "yyyy-MM-dd")
    private Date plannedEndDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际完工时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date actualEndTime;

    private List<UniformProcessRecord> processList;

    public Long getWorkOrderId()
    {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId)
    {
        this.workOrderId = workOrderId;
    }

    @Xss(message = "工单号不能包含脚本字符")
    @Size(min = 0, max = 64, message = "工单号长度不能超过64个字符")
    public String getWorkOrderNo()
    {
        return workOrderNo;
    }

    public void setWorkOrderNo(String workOrderNo)
    {
        this.workOrderNo = workOrderNo;
    }

    @Xss(message = "客户订单号不能包含脚本字符")
    @Size(min = 0, max = 64, message = "客户订单号长度不能超过64个字符")
    public String getSourceOrderNo()
    {
        return sourceOrderNo;
    }

    public void setSourceOrderNo(String sourceOrderNo)
    {
        this.sourceOrderNo = sourceOrderNo;
    }

    @Xss(message = "学校/客户名称不能包含脚本字符")
    @Size(min = 0, max = 100, message = "学校/客户名称长度不能超过100个字符")
    public String getSchoolName()
    {
        return schoolName;
    }

    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }

    public String getStyleName()
    {
        return styleName;
    }

    public void setStyleName(String styleName)
    {
        this.styleName = styleName;
    }

    @NotNull(message = "款式不能为空")
    public Long getStyleId()
    {
        return styleId;
    }

    public void setStyleId(Long styleId)
    {
        this.styleId = styleId;
    }

    public String getMaterialBatchNo()
    {
        return materialBatchNo;
    }

    public void setMaterialBatchNo(String materialBatchNo)
    {
        this.materialBatchNo = materialBatchNo;
    }

    @NotNull(message = "原料批次不能为空")
    public Long getMaterialBatchId()
    {
        return materialBatchId;
    }

    public void setMaterialBatchId(Long materialBatchId)
    {
        this.materialBatchId = materialBatchId;
    }

    @NotNull(message = "计划数量不能为空")
    public Integer getPlannedQuantity()
    {
        return plannedQuantity;
    }

    public void setPlannedQuantity(Integer plannedQuantity)
    {
        this.plannedQuantity = plannedQuantity;
    }

    public Integer getCompletedQuantity()
    {
        return completedQuantity;
    }

    public void setCompletedQuantity(Integer completedQuantity)
    {
        this.completedQuantity = completedQuantity;
    }

    @NotBlank(message = "赋码模式不能为空")
    public String getCodeMode()
    {
        return codeMode;
    }

    public void setCodeMode(String codeMode)
    {
        this.codeMode = codeMode;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public String getQaStatus()
    {
        return qaStatus;
    }

    public void setQaStatus(String qaStatus)
    {
        this.qaStatus = qaStatus;
    }

    public String getTraceStatus()
    {
        return traceStatus;
    }

    public void setTraceStatus(String traceStatus)
    {
        this.traceStatus = traceStatus;
    }

    public Integer getTraceGeneratedCount()
    {
        return traceGeneratedCount;
    }

    public void setTraceGeneratedCount(Integer traceGeneratedCount)
    {
        this.traceGeneratedCount = traceGeneratedCount;
    }

    public Date getPlannedStartDate()
    {
        return plannedStartDate;
    }

    public void setPlannedStartDate(Date plannedStartDate)
    {
        this.plannedStartDate = plannedStartDate;
    }

    public Date getPlannedEndDate()
    {
        return plannedEndDate;
    }

    public void setPlannedEndDate(Date plannedEndDate)
    {
        this.plannedEndDate = plannedEndDate;
    }

    public Date getActualEndTime()
    {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime)
    {
        this.actualEndTime = actualEndTime;
    }

    public List<UniformProcessRecord> getProcessList()
    {
        return processList;
    }

    public void setProcessList(List<UniformProcessRecord> processList)
    {
        this.processList = processList;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("workOrderId", getWorkOrderId())
            .append("workOrderNo", getWorkOrderNo())
            .append("sourceOrderNo", getSourceOrderNo())
            .append("schoolName", getSchoolName())
            .append("styleId", getStyleId())
            .append("styleName", getStyleName())
            .append("materialBatchId", getMaterialBatchId())
            .append("materialBatchNo", getMaterialBatchNo())
            .append("plannedQuantity", getPlannedQuantity())
            .append("completedQuantity", getCompletedQuantity())
            .append("codeMode", getCodeMode())
            .append("orderStatus", getOrderStatus())
            .append("qaStatus", getQaStatus())
            .append("traceStatus", getTraceStatus())
            .append("traceGeneratedCount", getTraceGeneratedCount())
            .append("plannedStartDate", getPlannedStartDate())
            .append("plannedEndDate", getPlannedEndDate())
            .append("actualEndTime", getActualEndTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
