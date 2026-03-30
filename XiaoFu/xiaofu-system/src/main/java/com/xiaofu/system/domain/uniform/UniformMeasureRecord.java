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
 * 量体测码对象 xf_measure_record
 */
public class UniformMeasureRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "量体ID", cellType = ColumnType.NUMERIC)
    private Long measureId;

    private Long styleId;

    @Excel(name = "款式名称")
    private String styleName;

    @Excel(name = "学校")
    private String schoolName;

    @Excel(name = "校区")
    private String campusName;

    @Excel(name = "年级")
    private String gradeName;

    @Excel(name = "班级")
    private String className;

    @Excel(name = "学生姓名")
    private String studentName;

    @Excel(name = "学号")
    private String studentNo;

    @Excel(name = "性别", readConverterExp = "male=男,female=女")
    private String gender;

    @Excel(name = "身高(cm)")
    private BigDecimal heightValue;

    @Excel(name = "体重(kg)")
    private BigDecimal weightValue;

    @Excel(name = "录入来源", readConverterExp = "manual=后台录入,wechat=家长H5")
    private String sourceChannel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "量体时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date measureTime;

    @Excel(name = "推荐尺码")
    private String recommendedSizeCode;

    @Excel(name = "推荐尺码名称")
    private String recommendedSizeName;

    @Excel(name = "最终尺码")
    private String finalSizeCode;

    @Excel(name = "最终尺码名称")
    private String finalSizeName;

    @Excel(name = "状态", readConverterExp = "0=待复核,1=已确认,2=已下发")
    private String measureStatus;

    @Excel(name = "体型备注")
    private String bodyRemark;

    public Long getMeasureId()
    {
        return measureId;
    }

    public void setMeasureId(Long measureId)
    {
        this.measureId = measureId;
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

    public String getStyleName()
    {
        return styleName;
    }

    public void setStyleName(String styleName)
    {
        this.styleName = styleName;
    }

    @Xss(message = "学校名称不能包含脚本字符")
    @NotBlank(message = "学校不能为空")
    @Size(min = 0, max = 100, message = "学校名称长度不能超过100个字符")
    public String getSchoolName()
    {
        return schoolName;
    }

    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }

    @Xss(message = "校区名称不能包含脚本字符")
    @Size(min = 0, max = 100, message = "校区名称长度不能超过100个字符")
    public String getCampusName()
    {
        return campusName;
    }

    public void setCampusName(String campusName)
    {
        this.campusName = campusName;
    }

    @Xss(message = "年级名称不能包含脚本字符")
    @Size(min = 0, max = 50, message = "年级名称长度不能超过50个字符")
    public String getGradeName()
    {
        return gradeName;
    }

    public void setGradeName(String gradeName)
    {
        this.gradeName = gradeName;
    }

    @Xss(message = "班级名称不能包含脚本字符")
    @NotBlank(message = "班级不能为空")
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

    @Xss(message = "学号不能包含脚本字符")
    @Size(min = 0, max = 32, message = "学号长度不能超过32个字符")
    public String getStudentNo()
    {
        return studentNo;
    }

    public void setStudentNo(String studentNo)
    {
        this.studentNo = studentNo;
    }

    @NotBlank(message = "性别不能为空")
    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    @NotNull(message = "身高不能为空")
    public BigDecimal getHeightValue()
    {
        return heightValue;
    }

    public void setHeightValue(BigDecimal heightValue)
    {
        this.heightValue = heightValue;
    }

    @NotNull(message = "体重不能为空")
    public BigDecimal getWeightValue()
    {
        return weightValue;
    }

    public void setWeightValue(BigDecimal weightValue)
    {
        this.weightValue = weightValue;
    }

    public String getSourceChannel()
    {
        return sourceChannel;
    }

    public void setSourceChannel(String sourceChannel)
    {
        this.sourceChannel = sourceChannel;
    }

    public Date getMeasureTime()
    {
        return measureTime;
    }

    public void setMeasureTime(Date measureTime)
    {
        this.measureTime = measureTime;
    }

    public String getRecommendedSizeCode()
    {
        return recommendedSizeCode;
    }

    public void setRecommendedSizeCode(String recommendedSizeCode)
    {
        this.recommendedSizeCode = recommendedSizeCode;
    }

    public String getRecommendedSizeName()
    {
        return recommendedSizeName;
    }

    public void setRecommendedSizeName(String recommendedSizeName)
    {
        this.recommendedSizeName = recommendedSizeName;
    }

    public String getFinalSizeCode()
    {
        return finalSizeCode;
    }

    public void setFinalSizeCode(String finalSizeCode)
    {
        this.finalSizeCode = finalSizeCode;
    }

    public String getFinalSizeName()
    {
        return finalSizeName;
    }

    public void setFinalSizeName(String finalSizeName)
    {
        this.finalSizeName = finalSizeName;
    }

    public String getMeasureStatus()
    {
        return measureStatus;
    }

    public void setMeasureStatus(String measureStatus)
    {
        this.measureStatus = measureStatus;
    }

    @Xss(message = "体型备注不能包含脚本字符")
    @Size(min = 0, max = 500, message = "体型备注长度不能超过500个字符")
    public String getBodyRemark()
    {
        return bodyRemark;
    }

    public void setBodyRemark(String bodyRemark)
    {
        this.bodyRemark = bodyRemark;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("measureId", getMeasureId())
            .append("styleId", getStyleId())
            .append("styleName", getStyleName())
            .append("schoolName", getSchoolName())
            .append("campusName", getCampusName())
            .append("gradeName", getGradeName())
            .append("className", getClassName())
            .append("studentName", getStudentName())
            .append("studentNo", getStudentNo())
            .append("gender", getGender())
            .append("heightValue", getHeightValue())
            .append("weightValue", getWeightValue())
            .append("sourceChannel", getSourceChannel())
            .append("measureTime", getMeasureTime())
            .append("recommendedSizeCode", getRecommendedSizeCode())
            .append("recommendedSizeName", getRecommendedSizeName())
            .append("finalSizeCode", getFinalSizeCode())
            .append("finalSizeName", getFinalSizeName())
            .append("measureStatus", getMeasureStatus())
            .append("bodyRemark", getBodyRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
