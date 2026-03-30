package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaofu.common.annotation.Excel;
import com.xiaofu.common.annotation.Excel.ColumnType;
import com.xiaofu.common.xss.Xss;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 工序打卡对象 xf_process_record
 */
public class UniformProcessRecord implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "记录ID", cellType = ColumnType.NUMERIC)
    private Long recordId;

    private Long workOrderId;

    @Excel(name = "工序", readConverterExp = "cutting=裁剪,sewing=缝纫,ironing=熨烫")
    private String processType;

    @Excel(name = "责任人")
    private String operatorName;

    @Excel(name = "质检员")
    private String inspectorName;

    @Excel(name = "设备来源", readConverterExp = "tablet=平板,scanner=扫码枪,manual=手工录入")
    private String deviceType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Excel(name = "合格数量", cellType = ColumnType.NUMERIC)
    private Integer passQuantity;

    @Excel(name = "不良数量", cellType = ColumnType.NUMERIC)
    private Integer defectiveQuantity;

    @Excel(name = "备注")
    private String remark;

    public Long getRecordId()
    {
        return recordId;
    }

    public void setRecordId(Long recordId)
    {
        this.recordId = recordId;
    }

    public Long getWorkOrderId()
    {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId)
    {
        this.workOrderId = workOrderId;
    }

    @NotBlank(message = "工序不能为空")
    public String getProcessType()
    {
        return processType;
    }

    public void setProcessType(String processType)
    {
        this.processType = processType;
    }

    @Xss(message = "责任人不能包含脚本字符")
    @Size(min = 0, max = 50, message = "责任人长度不能超过50个字符")
    public String getOperatorName()
    {
        return operatorName;
    }

    public void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName;
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

    @Size(min = 0, max = 20, message = "设备来源长度不能超过20个字符")
    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Integer getPassQuantity()
    {
        return passQuantity;
    }

    public void setPassQuantity(Integer passQuantity)
    {
        this.passQuantity = passQuantity;
    }

    public Integer getDefectiveQuantity()
    {
        return defectiveQuantity;
    }

    public void setDefectiveQuantity(Integer defectiveQuantity)
    {
        this.defectiveQuantity = defectiveQuantity;
    }

    @Size(min = 0, max = 500, message = "备注长度不能超过500个字符")
    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("workOrderId", getWorkOrderId())
            .append("processType", getProcessType())
            .append("operatorName", getOperatorName())
            .append("inspectorName", getInspectorName())
            .append("deviceType", getDeviceType())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("passQuantity", getPassQuantity())
            .append("defectiveQuantity", getDefectiveQuantity())
            .append("remark", getRemark())
            .toString();
    }
}
