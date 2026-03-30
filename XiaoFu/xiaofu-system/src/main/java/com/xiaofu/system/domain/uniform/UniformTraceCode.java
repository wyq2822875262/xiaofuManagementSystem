package com.xiaofu.system.domain.uniform;

import com.xiaofu.common.annotation.Excel;
import com.xiaofu.common.annotation.Excel.ColumnType;
import com.xiaofu.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 溯源码对象 xf_trace_code
 */
public class UniformTraceCode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "溯源码ID", cellType = ColumnType.NUMERIC)
    private Long traceId;

    private Long workOrderId;

    @Excel(name = "工单号")
    private String workOrderNo;

    private Long inspectionId;

    @Excel(name = "款式名称")
    private String styleName;

    @Excel(name = "批次号")
    private String batchNo;

    @Excel(name = "赋码模式", readConverterExp = "1=一物一码,2=一批一码")
    private String codeMode;

    @Excel(name = "序号", cellType = ColumnType.NUMERIC)
    private Integer serialNo;

    @Excel(name = "溯源码")
    private String traceCode;

    @Excel(name = "二维码内容")
    private String qrContent;

    @Excel(name = "打印状态", readConverterExp = "0=未打印,1=已打印")
    private String printStatus;

    @Excel(name = "状态", readConverterExp = "0=有效,1=作废")
    private String traceStatus;

    public Long getTraceId()
    {
        return traceId;
    }

    public void setTraceId(Long traceId)
    {
        this.traceId = traceId;
    }

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

    public Long getInspectionId()
    {
        return inspectionId;
    }

    public void setInspectionId(Long inspectionId)
    {
        this.inspectionId = inspectionId;
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

    public String getCodeMode()
    {
        return codeMode;
    }

    public void setCodeMode(String codeMode)
    {
        this.codeMode = codeMode;
    }

    public Integer getSerialNo()
    {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo)
    {
        this.serialNo = serialNo;
    }

    public String getTraceCode()
    {
        return traceCode;
    }

    public void setTraceCode(String traceCode)
    {
        this.traceCode = traceCode;
    }

    public String getQrContent()
    {
        return qrContent;
    }

    public void setQrContent(String qrContent)
    {
        this.qrContent = qrContent;
    }

    public String getPrintStatus()
    {
        return printStatus;
    }

    public void setPrintStatus(String printStatus)
    {
        this.printStatus = printStatus;
    }

    public String getTraceStatus()
    {
        return traceStatus;
    }

    public void setTraceStatus(String traceStatus)
    {
        this.traceStatus = traceStatus;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("traceId", getTraceId())
            .append("workOrderId", getWorkOrderId())
            .append("workOrderNo", getWorkOrderNo())
            .append("inspectionId", getInspectionId())
            .append("styleName", getStyleName())
            .append("batchNo", getBatchNo())
            .append("codeMode", getCodeMode())
            .append("serialNo", getSerialNo())
            .append("traceCode", getTraceCode())
            .append("qrContent", getQrContent())
            .append("printStatus", getPrintStatus())
            .append("traceStatus", getTraceStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
