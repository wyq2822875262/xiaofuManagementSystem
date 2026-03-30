package com.xiaofu.system.domain.uniform;

import java.math.BigDecimal;
import java.util.Date;
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
 * 成品质检对象 xf_quality_inspection
 */
public class UniformQualityInspection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "质检ID", cellType = ColumnType.NUMERIC)
    private Long inspectionId;

    @Excel(name = "质检单号")
    private String inspectionNo;

    private Long workOrderId;

    @Excel(name = "工单号")
    private String workOrderNo;

    @Excel(name = "款式名称")
    private String styleName;

    @Excel(name = "原料批次号")
    private String batchNo;

    @Excel(name = "执行标准")
    private String gbStandard;

    @Excel(name = "抽检数量", cellType = ColumnType.NUMERIC)
    private Integer sampleQuantity;

    @Excel(name = "甲醛含量")
    private BigDecimal formaldehyde;

    @Excel(name = "PH值")
    private BigDecimal phValue;

    @Excel(name = "异味", readConverterExp = "0=无异味,1=有异味")
    private String odorResult;

    @Excel(name = "可分解致癌芳香胺", readConverterExp = "0=合格,1=不合格")
    private String amineResult;

    @Excel(name = "色牢度")
    private BigDecimal colorFastness;

    @Excel(name = "起球率")
    private BigDecimal pillingGrade;

    @Excel(name = "质检结果", readConverterExp = "0=合格,1=不合格")
    private String result;

    @Excel(name = "拦截状态", readConverterExp = "0=放行,1=拦截")
    private String interceptStatus;

    @Excel(name = "不合格原因")
    private String failReason;

    @Excel(name = "质检员")
    private String inspectorName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "质检时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date inspectTime;

    private String reportFileUrl;

    @Excel(name = "结论")
    private String conclusion;

    public Long getInspectionId()
    {
        return inspectionId;
    }

    public void setInspectionId(Long inspectionId)
    {
        this.inspectionId = inspectionId;
    }

    @Xss(message = "质检单号不能包含脚本字符")
    @Size(min = 0, max = 64, message = "质检单号长度不能超过64个字符")
    public String getInspectionNo()
    {
        return inspectionNo;
    }

    public void setInspectionNo(String inspectionNo)
    {
        this.inspectionNo = inspectionNo;
    }

    @NotNull(message = "工单不能为空")
    public Long getWorkOrderId()
    {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId)
    {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderNo()
    {
        return workOrderNo;
    }

    public void setWorkOrderNo(String workOrderNo)
    {
        this.workOrderNo = workOrderNo;
    }

    public String getStyleName()
    {
        return styleName;
    }

    public void setStyleName(String styleName)
    {
        this.styleName = styleName;
    }

    public String getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

    @NotBlank(message = "执行标准不能为空")
    public String getGbStandard()
    {
        return gbStandard;
    }

    public void setGbStandard(String gbStandard)
    {
        this.gbStandard = gbStandard;
    }

    @NotNull(message = "抽检数量不能为空")
    public Integer getSampleQuantity()
    {
        return sampleQuantity;
    }

    public void setSampleQuantity(Integer sampleQuantity)
    {
        this.sampleQuantity = sampleQuantity;
    }

    public BigDecimal getFormaldehyde()
    {
        return formaldehyde;
    }

    public void setFormaldehyde(BigDecimal formaldehyde)
    {
        this.formaldehyde = formaldehyde;
    }

    public BigDecimal getPhValue()
    {
        return phValue;
    }

    public void setPhValue(BigDecimal phValue)
    {
        this.phValue = phValue;
    }

    @NotBlank(message = "异味判定不能为空")
    public String getOdorResult()
    {
        return odorResult;
    }

    public void setOdorResult(String odorResult)
    {
        this.odorResult = odorResult;
    }

    @NotBlank(message = "芳香胺判定不能为空")
    public String getAmineResult()
    {
        return amineResult;
    }

    public void setAmineResult(String amineResult)
    {
        this.amineResult = amineResult;
    }

    public BigDecimal getColorFastness()
    {
        return colorFastness;
    }

    public void setColorFastness(BigDecimal colorFastness)
    {
        this.colorFastness = colorFastness;
    }

    public BigDecimal getPillingGrade()
    {
        return pillingGrade;
    }

    public void setPillingGrade(BigDecimal pillingGrade)
    {
        this.pillingGrade = pillingGrade;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getInterceptStatus()
    {
        return interceptStatus;
    }

    public void setInterceptStatus(String interceptStatus)
    {
        this.interceptStatus = interceptStatus;
    }

    @Size(min = 0, max = 500, message = "不合格原因长度不能超过500个字符")
    public String getFailReason()
    {
        return failReason;
    }

    public void setFailReason(String failReason)
    {
        this.failReason = failReason;
    }

    @Xss(message = "质检员不能包含脚本字符")
    @Size(min = 0, max = 50, message = "质检员长度不能超过50个字符")
    public String getInspectorName()
    {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName)
    {
        this.inspectorName = inspectorName;
    }

    public Date getInspectTime()
    {
        return inspectTime;
    }

    public void setInspectTime(Date inspectTime)
    {
        this.inspectTime = inspectTime;
    }

    @Size(min = 0, max = 500, message = "质检报告附件地址长度不能超过500个字符")
    public String getReportFileUrl()
    {
        return reportFileUrl;
    }

    public void setReportFileUrl(String reportFileUrl)
    {
        this.reportFileUrl = reportFileUrl;
    }

    @Size(min = 0, max = 500, message = "结论长度不能超过500个字符")
    public String getConclusion()
    {
        return conclusion;
    }

    public void setConclusion(String conclusion)
    {
        this.conclusion = conclusion;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("inspectionId", getInspectionId())
            .append("inspectionNo", getInspectionNo())
            .append("workOrderId", getWorkOrderId())
            .append("workOrderNo", getWorkOrderNo())
            .append("styleName", getStyleName())
            .append("batchNo", getBatchNo())
            .append("gbStandard", getGbStandard())
            .append("sampleQuantity", getSampleQuantity())
            .append("formaldehyde", getFormaldehyde())
            .append("phValue", getPhValue())
            .append("odorResult", getOdorResult())
            .append("amineResult", getAmineResult())
            .append("colorFastness", getColorFastness())
            .append("pillingGrade", getPillingGrade())
            .append("result", getResult())
            .append("interceptStatus", getInterceptStatus())
            .append("failReason", getFailReason())
            .append("inspectorName", getInspectorName())
            .append("inspectTime", getInspectTime())
            .append("reportFileUrl", getReportFileUrl())
            .append("conclusion", getConclusion())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
