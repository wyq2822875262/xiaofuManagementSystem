package com.xiaofu.system.domain.uniform;

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
 * 退换货对象 xf_rma_order
 */
public class UniformRmaOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "RMA ID", cellType = ColumnType.NUMERIC)
    private Long rmaId;

    @Excel(name = "RMA单号")
    private String rmaNo;

    private Long traceId;

    @Excel(name = "溯源码")
    private String traceCode;

    private Long workOrderId;

    @Excel(name = "工单号")
    private String workOrderNo;

    @Excel(name = "款式名称")
    private String styleName;

    @Excel(name = "学校")
    private String schoolName;

    @Excel(name = "班级")
    private String className;

    @Excel(name = "学生姓名")
    private String studentName;

    @Excel(name = "申请类型", readConverterExp = "1=换货,2=退货")
    private String requestType;

    @Excel(name = "原因类型", readConverterExp = "size_small=尺码偏小,size_large=尺码偏大,quality_issue=质量问题,other=其他")
    private String reasonType;

    @Excel(name = "原尺码")
    private String oldSizeCode;

    @Excel(name = "新尺码")
    private String newSizeCode;

    @Excel(name = "申请数量", cellType = ColumnType.NUMERIC)
    private Integer applyQuantity;

    @Excel(name = "状态", readConverterExp = "0=待审核,1=待回收入库,2=待补发,3=已完成,8=已驳回")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "申请时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "旧衣回收时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    @Excel(name = "补发单号")
    private String resendTrackingNo;

    @Excel(name = "处理结论")
    private String conclusion;

    public Long getRmaId()
    {
        return rmaId;
    }

    public void setRmaId(Long rmaId)
    {
        this.rmaId = rmaId;
    }

    @Size(min = 0, max = 64, message = "RMA单号长度不能超过64个字符")
    public String getRmaNo()
    {
        return rmaNo;
    }

    public void setRmaNo(String rmaNo)
    {
        this.rmaNo = rmaNo;
    }

    @NotNull(message = "溯源码不能为空")
    public Long getTraceId()
    {
        return traceId;
    }

    public void setTraceId(Long traceId)
    {
        this.traceId = traceId;
    }

    public String getTraceCode()
    {
        return traceCode;
    }

    public void setTraceCode(String traceCode)
    {
        this.traceCode = traceCode;
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

    public String getStyleName()
    {
        return styleName;
    }

    public void setStyleName(String styleName)
    {
        this.styleName = styleName;
    }

    @Xss(message = "学校名称不能包含脚本字符")
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

    @NotBlank(message = "申请类型不能为空")
    public String getRequestType()
    {
        return requestType;
    }

    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }

    @NotBlank(message = "原因类型不能为空")
    public String getReasonType()
    {
        return reasonType;
    }

    public void setReasonType(String reasonType)
    {
        this.reasonType = reasonType;
    }

    @Size(min = 0, max = 32, message = "原尺码长度不能超过32个字符")
    public String getOldSizeCode()
    {
        return oldSizeCode;
    }

    public void setOldSizeCode(String oldSizeCode)
    {
        this.oldSizeCode = oldSizeCode;
    }

    @Size(min = 0, max = 32, message = "新尺码长度不能超过32个字符")
    public String getNewSizeCode()
    {
        return newSizeCode;
    }

    public void setNewSizeCode(String newSizeCode)
    {
        this.newSizeCode = newSizeCode;
    }

    @NotNull(message = "申请数量不能为空")
    public Integer getApplyQuantity()
    {
        return applyQuantity;
    }

    public void setApplyQuantity(Integer applyQuantity)
    {
        this.applyQuantity = applyQuantity;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getApplyTime()
    {
        return applyTime;
    }

    public void setApplyTime(Date applyTime)
    {
        this.applyTime = applyTime;
    }

    public Date getReceiveTime()
    {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime)
    {
        this.receiveTime = receiveTime;
    }

    @Size(min = 0, max = 100, message = "补发单号长度不能超过100个字符")
    public String getResendTrackingNo()
    {
        return resendTrackingNo;
    }

    public void setResendTrackingNo(String resendTrackingNo)
    {
        this.resendTrackingNo = resendTrackingNo;
    }

    @Xss(message = "处理结论不能包含脚本字符")
    @Size(min = 0, max = 500, message = "处理结论长度不能超过500个字符")
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
            .append("rmaId", getRmaId())
            .append("rmaNo", getRmaNo())
            .append("traceId", getTraceId())
            .append("traceCode", getTraceCode())
            .append("workOrderId", getWorkOrderId())
            .append("workOrderNo", getWorkOrderNo())
            .append("styleName", getStyleName())
            .append("schoolName", getSchoolName())
            .append("className", getClassName())
            .append("studentName", getStudentName())
            .append("requestType", getRequestType())
            .append("reasonType", getReasonType())
            .append("oldSizeCode", getOldSizeCode())
            .append("newSizeCode", getNewSizeCode())
            .append("applyQuantity", getApplyQuantity())
            .append("status", getStatus())
            .append("applyTime", getApplyTime())
            .append("receiveTime", getReceiveTime())
            .append("resendTrackingNo", getResendTrackingNo())
            .append("conclusion", getConclusion())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
