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
 * 原料批次对象 xf_material_batch
 */
public class UniformMaterialBatch extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "批次ID", cellType = ColumnType.NUMERIC)
    private Long batchId;

    @Excel(name = "内部批次号")
    private String batchNo;

    @Excel(name = "供应商ID", cellType = ColumnType.NUMERIC)
    private Long supplierId;

    @Excel(name = "供应商名称")
    private String supplierName;

    @Excel(name = "关联款式")
    private String styleName;

    private Long styleId;

    @Excel(name = "物料类型", readConverterExp = "1=主面料,2=辅料")
    private String materialType;

    @Excel(name = "物料名称")
    private String materialName;

    @Excel(name = "缸号")
    private String vatNo;

    @Excel(name = "克重(g/m2)")
    private BigDecimal gramWeight;

    @Excel(name = "面料成分")
    private String fabricComposition;

    @Excel(name = "颜色")
    private String colorName;

    @Excel(name = "门幅(cm)")
    private BigDecimal widthValue;

    @Excel(name = "入库数量")
    private BigDecimal inboundQuantity;

    @Excel(name = "单位")
    private String quantityUnit;

    @Excel(name = "检测报告编号")
    private String reportNo;

    private String reportFileUrl;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测日期", width = 18, dateFormat = "yyyy-MM-dd")
    private Date inspectDate;

    @Excel(name = "状态", readConverterExp = "0=待投产,1=生产中,2=已用完,3=锁定")
    private String status;

    public Long getBatchId()
    {
        return batchId;
    }

    public void setBatchId(Long batchId)
    {
        this.batchId = batchId;
    }

    @Xss(message = "内部批次号不能包含脚本字符")
    @Size(min = 0, max = 64, message = "内部批次号长度不能超过64个字符")
    public String getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

    @NotNull(message = "供应商不能为空")
    public Long getSupplierId()
    {
        return supplierId;
    }

    public void setSupplierId(Long supplierId)
    {
        this.supplierId = supplierId;
    }

    public String getSupplierName()
    {
        return supplierName;
    }

    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }

    public String getStyleName()
    {
        return styleName;
    }

    public void setStyleName(String styleName)
    {
        this.styleName = styleName;
    }

    public Long getStyleId()
    {
        return styleId;
    }

    public void setStyleId(Long styleId)
    {
        this.styleId = styleId;
    }

    @NotBlank(message = "物料类型不能为空")
    public String getMaterialType()
    {
        return materialType;
    }

    public void setMaterialType(String materialType)
    {
        this.materialType = materialType;
    }

    @Xss(message = "物料名称不能包含脚本字符")
    @NotBlank(message = "物料名称不能为空")
    @Size(min = 0, max = 100, message = "物料名称长度不能超过100个字符")
    public String getMaterialName()
    {
        return materialName;
    }

    public void setMaterialName(String materialName)
    {
        this.materialName = materialName;
    }

    @Xss(message = "缸号不能包含脚本字符")
    @Size(min = 0, max = 64, message = "缸号长度不能超过64个字符")
    public String getVatNo()
    {
        return vatNo;
    }

    public void setVatNo(String vatNo)
    {
        this.vatNo = vatNo;
    }

    public BigDecimal getGramWeight()
    {
        return gramWeight;
    }

    public void setGramWeight(BigDecimal gramWeight)
    {
        this.gramWeight = gramWeight;
    }

    @Xss(message = "面料成分不能包含脚本字符")
    @Size(min = 0, max = 255, message = "面料成分长度不能超过255个字符")
    public String getFabricComposition()
    {
        return fabricComposition;
    }

    public void setFabricComposition(String fabricComposition)
    {
        this.fabricComposition = fabricComposition;
    }

    @Xss(message = "颜色不能包含脚本字符")
    @Size(min = 0, max = 50, message = "颜色长度不能超过50个字符")
    public String getColorName()
    {
        return colorName;
    }

    public void setColorName(String colorName)
    {
        this.colorName = colorName;
    }

    public BigDecimal getWidthValue()
    {
        return widthValue;
    }

    public void setWidthValue(BigDecimal widthValue)
    {
        this.widthValue = widthValue;
    }

    public BigDecimal getInboundQuantity()
    {
        return inboundQuantity;
    }

    public void setInboundQuantity(BigDecimal inboundQuantity)
    {
        this.inboundQuantity = inboundQuantity;
    }

    @Size(min = 0, max = 20, message = "单位长度不能超过20个字符")
    public String getQuantityUnit()
    {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit)
    {
        this.quantityUnit = quantityUnit;
    }

    @Size(min = 0, max = 100, message = "检测报告编号长度不能超过100个字符")
    public String getReportNo()
    {
        return reportNo;
    }

    public void setReportNo(String reportNo)
    {
        this.reportNo = reportNo;
    }

    @Size(min = 0, max = 500, message = "检测报告附件地址长度不能超过500个字符")
    public String getReportFileUrl()
    {
        return reportFileUrl;
    }

    public void setReportFileUrl(String reportFileUrl)
    {
        this.reportFileUrl = reportFileUrl;
    }

    public Date getInspectDate()
    {
        return inspectDate;
    }

    public void setInspectDate(Date inspectDate)
    {
        this.inspectDate = inspectDate;
    }

    @NotBlank(message = "状态不能为空")
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("batchId", getBatchId())
            .append("batchNo", getBatchNo())
            .append("supplierId", getSupplierId())
            .append("supplierName", getSupplierName())
            .append("styleId", getStyleId())
            .append("styleName", getStyleName())
            .append("materialType", getMaterialType())
            .append("materialName", getMaterialName())
            .append("vatNo", getVatNo())
            .append("gramWeight", getGramWeight())
            .append("fabricComposition", getFabricComposition())
            .append("colorName", getColorName())
            .append("widthValue", getWidthValue())
            .append("inboundQuantity", getInboundQuantity())
            .append("quantityUnit", getQuantityUnit())
            .append("reportNo", getReportNo())
            .append("reportFileUrl", getReportFileUrl())
            .append("inspectDate", getInspectDate())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
