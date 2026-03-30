package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * C端门户溯源档案
 */
public class UniformPortalTraceArchive implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long traceId;

    private String traceCode;

    private String traceStatus;

    private String codeMode;

    private String styleName;

    private String season;

    private String styleType;

    private String schoolName;

    private String sourceOrderNo;

    private String workOrderNo;

    private String batchNo;

    private String supplierName;

    private String materialName;

    private String fabricName;

    private String fabricComposition;

    private String colorName;

    private BigDecimal gramWeight;

    private String vatNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date factoryDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date materialInspectDate;

    private String materialReportNo;

    private String materialReportFileUrl;

    private String inspectionNo;

    private String gbStandard;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionTime;

    private String inspectionReportFileUrl;

    private BigDecimal formaldehyde;

    private BigDecimal phValue;

    private String odorResult;

    private String amineResult;

    private BigDecimal colorFastness;

    private BigDecimal pillingGrade;

    private String conclusion;

    private String careInstructions;

    private List<String> careTips;

    private List<UniformPortalProcessNode> processList;

    private UniformLostFoundCard lostFoundCard;

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

    public String getTraceStatus()
    {
        return traceStatus;
    }

    public void setTraceStatus(String traceStatus)
    {
        this.traceStatus = traceStatus;
    }

    public String getCodeMode()
    {
        return codeMode;
    }

    public void setCodeMode(String codeMode)
    {
        this.codeMode = codeMode;
    }

    public String getStyleName()
    {
        return styleName;
    }

    public void setStyleName(String styleName)
    {
        this.styleName = styleName;
    }

    public String getSeason()
    {
        return season;
    }

    public void setSeason(String season)
    {
        this.season = season;
    }

    public String getStyleType()
    {
        return styleType;
    }

    public void setStyleType(String styleType)
    {
        this.styleType = styleType;
    }

    public String getSchoolName()
    {
        return schoolName;
    }

    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }

    public String getSourceOrderNo()
    {
        return sourceOrderNo;
    }

    public void setSourceOrderNo(String sourceOrderNo)
    {
        this.sourceOrderNo = sourceOrderNo;
    }

    public String getWorkOrderNo()
    {
        return workOrderNo;
    }

    public void setWorkOrderNo(String workOrderNo)
    {
        this.workOrderNo = workOrderNo;
    }

    public String getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

    public String getSupplierName()
    {
        return supplierName;
    }

    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }

    public String getMaterialName()
    {
        return materialName;
    }

    public void setMaterialName(String materialName)
    {
        this.materialName = materialName;
    }

    public String getFabricName()
    {
        return fabricName;
    }

    public void setFabricName(String fabricName)
    {
        this.fabricName = fabricName;
    }

    public String getFabricComposition()
    {
        return fabricComposition;
    }

    public void setFabricComposition(String fabricComposition)
    {
        this.fabricComposition = fabricComposition;
    }

    public String getColorName()
    {
        return colorName;
    }

    public void setColorName(String colorName)
    {
        this.colorName = colorName;
    }

    public BigDecimal getGramWeight()
    {
        return gramWeight;
    }

    public void setGramWeight(BigDecimal gramWeight)
    {
        this.gramWeight = gramWeight;
    }

    public String getVatNo()
    {
        return vatNo;
    }

    public void setVatNo(String vatNo)
    {
        this.vatNo = vatNo;
    }

    public Date getFactoryDate()
    {
        return factoryDate;
    }

    public void setFactoryDate(Date factoryDate)
    {
        this.factoryDate = factoryDate;
    }

    public Date getMaterialInspectDate()
    {
        return materialInspectDate;
    }

    public void setMaterialInspectDate(Date materialInspectDate)
    {
        this.materialInspectDate = materialInspectDate;
    }

    public String getMaterialReportNo()
    {
        return materialReportNo;
    }

    public void setMaterialReportNo(String materialReportNo)
    {
        this.materialReportNo = materialReportNo;
    }

    public String getMaterialReportFileUrl()
    {
        return materialReportFileUrl;
    }

    public void setMaterialReportFileUrl(String materialReportFileUrl)
    {
        this.materialReportFileUrl = materialReportFileUrl;
    }

    public String getInspectionNo()
    {
        return inspectionNo;
    }

    public void setInspectionNo(String inspectionNo)
    {
        this.inspectionNo = inspectionNo;
    }

    public String getGbStandard()
    {
        return gbStandard;
    }

    public void setGbStandard(String gbStandard)
    {
        this.gbStandard = gbStandard;
    }

    public Date getInspectionTime()
    {
        return inspectionTime;
    }

    public void setInspectionTime(Date inspectionTime)
    {
        this.inspectionTime = inspectionTime;
    }

    public String getInspectionReportFileUrl()
    {
        return inspectionReportFileUrl;
    }

    public void setInspectionReportFileUrl(String inspectionReportFileUrl)
    {
        this.inspectionReportFileUrl = inspectionReportFileUrl;
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

    public String getConclusion()
    {
        return conclusion;
    }

    public void setConclusion(String conclusion)
    {
        this.conclusion = conclusion;
    }

    public String getCareInstructions()
    {
        return careInstructions;
    }

    public void setCareInstructions(String careInstructions)
    {
        this.careInstructions = careInstructions;
    }

    public List<String> getCareTips()
    {
        return careTips;
    }

    public void setCareTips(List<String> careTips)
    {
        this.careTips = careTips;
    }

    public List<UniformPortalProcessNode> getProcessList()
    {
        return processList;
    }

    public void setProcessList(List<UniformPortalProcessNode> processList)
    {
        this.processList = processList;
    }

    public UniformLostFoundCard getLostFoundCard()
    {
        return lostFoundCard;
    }

    public void setLostFoundCard(UniformLostFoundCard lostFoundCard)
    {
        this.lostFoundCard = lostFoundCard;
    }
}
