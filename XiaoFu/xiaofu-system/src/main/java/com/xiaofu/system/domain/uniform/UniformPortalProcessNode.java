package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * C端门户工序节点
 */
public class UniformPortalProcessNode implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String processType;

    private String processName;

    private String operatorName;

    private String inspectorName;

    private String deviceType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Integer passQuantity;

    private Integer defectiveQuantity;

    private String remark;

    public String getProcessType()
    {
        return processType;
    }

    public void setProcessType(String processType)
    {
        this.processType = processType;
    }

    public String getProcessName()
    {
        return processName;
    }

    public void setProcessName(String processName)
    {
        this.processName = processName;
    }

    public String getOperatorName()
    {
        return operatorName;
    }

    public void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName;
    }

    public String getInspectorName()
    {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName)
    {
        this.inspectorName = inspectorName;
    }

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

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}
