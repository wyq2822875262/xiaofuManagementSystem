package com.xiaofu.system.domain.uniform;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaofu.common.core.domain.BaseEntity;
import com.xiaofu.common.xss.Xss;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数字寻物贴绑定对象 xf_lost_found_binding
 */
public class UniformLostFoundBinding extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long bindingId;

    private Long traceId;

    private String traceCode;

    private String schoolName;

    private String className;

    private String studentName;

    private String contactName;

    private String contactPhone;

    private String ownerRemark;

    private String bindStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bindTime;

    public Long getBindingId()
    {
        return bindingId;
    }

    public void setBindingId(Long bindingId)
    {
        this.bindingId = bindingId;
    }

    public Long getTraceId()
    {
        return traceId;
    }

    public void setTraceId(Long traceId)
    {
        this.traceId = traceId;
    }

    @Size(min = 0, max = 100, message = "溯源码长度不能超过100个字符")
    public String getTraceCode()
    {
        return traceCode;
    }

    public void setTraceCode(String traceCode)
    {
        this.traceCode = traceCode;
    }

    @Xss(message = "学校名称不能包含脚本字符")
    @NotBlank(message = "学校名称不能为空")
    @Size(min = 0, max = 100, message = "学校名称长度不能超过100个字符")
    public String getSchoolName()
    {
        return schoolName;
    }

    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }

    @Xss(message = "班级名称不能包含脚本字符")
    @NotBlank(message = "班级名称不能为空")
    @Size(min = 0, max = 50, message = "班级名称长度不能超过50个字符")
    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    @Xss(message = "学生姓名不能包含脚本字符")
    @NotBlank(message = "学生姓名不能为空")
    @Size(min = 0, max = 50, message = "学生姓名长度不能超过50个字符")
    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    @Xss(message = "联系人不能包含脚本字符")
    @Size(min = 0, max = 50, message = "联系人长度不能超过50个字符")
    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    @Size(min = 0, max = 20, message = "联系电话长度不能超过20个字符")
    public String getContactPhone()
    {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }

    @Xss(message = "归还提示不能包含脚本字符")
    @Size(min = 0, max = 500, message = "归还提示长度不能超过500个字符")
    public String getOwnerRemark()
    {
        return ownerRemark;
    }

    public void setOwnerRemark(String ownerRemark)
    {
        this.ownerRemark = ownerRemark;
    }

    public String getBindStatus()
    {
        return bindStatus;
    }

    public void setBindStatus(String bindStatus)
    {
        this.bindStatus = bindStatus;
    }

    public Date getBindTime()
    {
        return bindTime;
    }

    public void setBindTime(Date bindTime)
    {
        this.bindTime = bindTime;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("bindingId", getBindingId())
            .append("traceId", getTraceId())
            .append("traceCode", getTraceCode())
            .append("schoolName", getSchoolName())
            .append("className", getClassName())
            .append("studentName", getStudentName())
            .append("contactName", getContactName())
            .append("contactPhone", getContactPhone())
            .append("ownerRemark", getOwnerRemark())
            .append("bindStatus", getBindStatus())
            .append("bindTime", getBindTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
