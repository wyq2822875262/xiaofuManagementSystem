package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import com.xiaofu.common.xss.Xss;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 数字寻物贴绑定请求对象
 */
public class UniformLostFoundBindRequest implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String traceCode;

    private String schoolName;

    private String className;

    private String studentName;

    private String contactName;

    private String contactPhone;

    private String ownerRemark;

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
}
