package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 数字寻物贴公开展示卡片
 */
public class UniformLostFoundCard implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Boolean bound;

    private String traceCode;

    private String schoolName;

    private String className;

    private String studentName;

    private String contactName;

    private String maskedContactPhone;

    private String ownerRemark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bindTime;

    public Boolean getBound()
    {
        return bound;
    }

    public void setBound(Boolean bound)
    {
        this.bound = bound;
    }

    public String getTraceCode()
    {
        return traceCode;
    }

    public void setTraceCode(String traceCode)
    {
        this.traceCode = traceCode;
    }

    public String getSchoolName()
    {
        return schoolName;
    }

    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getMaskedContactPhone()
    {
        return maskedContactPhone;
    }

    public void setMaskedContactPhone(String maskedContactPhone)
    {
        this.maskedContactPhone = maskedContactPhone;
    }

    public String getOwnerRemark()
    {
        return ownerRemark;
    }

    public void setOwnerRemark(String ownerRemark)
    {
        this.ownerRemark = ownerRemark;
    }

    public Date getBindTime()
    {
        return bindTime;
    }

    public void setBindTime(Date bindTime)
    {
        this.bindTime = bindTime;
    }
}
