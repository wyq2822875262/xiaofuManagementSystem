package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 最新质检快照
 */
public class UniformScreenInspectionSnapshot implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long inspectionId;

    private String inspectionNo;

    private String schoolName;

    private String styleName;

    private String batchNo;

    private String gbStandard;

    private String result;

    private String conclusion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectTime;

    private BigDecimal formaldehyde;

    private BigDecimal phValue;

    private String odorResult;

    private String amineResult;

    private BigDecimal colorFastness;

    private BigDecimal pillingGrade;

    public Long getInspectionId()
    {
        return inspectionId;
    }

    public void setInspectionId(Long inspectionId)
    {
        this.inspectionId = inspectionId;
    }

    public String getInspectionNo()
    {
        return inspectionNo;
    }

    public void setInspectionNo(String inspectionNo)
    {
        this.inspectionNo = inspectionNo;
    }

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

    public String getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

    public String getGbStandard()
    {
        return gbStandard;
    }

    public void setGbStandard(String gbStandard)
    {
        this.gbStandard = gbStandard;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getConclusion()
    {
        return conclusion;
    }

    public void setConclusion(String conclusion)
    {
        this.conclusion = conclusion;
    }

    public Date getInspectTime()
    {
        return inspectTime;
    }

    public void setInspectTime(Date inspectTime)
    {
        this.inspectTime = inspectTime;
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

    public String getOdorResult()
    {
        return odorResult;
    }

    public void setOdorResult(String odorResult)
    {
        this.odorResult = odorResult;
    }

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
}
