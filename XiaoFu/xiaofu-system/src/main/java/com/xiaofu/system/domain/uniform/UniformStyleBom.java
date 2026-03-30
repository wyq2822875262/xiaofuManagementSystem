package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import java.math.BigDecimal;
import com.xiaofu.common.annotation.Excel;
import com.xiaofu.common.annotation.Excel.ColumnType;
import com.xiaofu.common.xss.Xss;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 校服 BOM 明细对象 xf_uniform_style_bom
 */
public class UniformStyleBom implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** BOM ID */
    @Excel(name = "BOM ID", cellType = ColumnType.NUMERIC)
    private Long bomId;

    /** 款式ID */
    private Long styleId;

    /** 物料类型 */
    @Excel(name = "物料类型", readConverterExp = "1=主面料,2=辅料,3=包装")
    private String materialType;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 规格 */
    @Excel(name = "物料规格")
    private String materialSpec;

    /** 成分说明 */
    @Excel(name = "成分说明")
    private String materialComposition;

    /** 用量 */
    @Excel(name = "用量")
    private BigDecimal dosage;

    /** 用量单位 */
    @Excel(name = "单位")
    private String dosageUnit;

    /** 供应商ID */
    private Long supplierId;

    /** 供应商名称 */
    @Excel(name = "供应商")
    private String supplierName;

    /** 是否强制追溯 */
    @Excel(name = "强制追溯", readConverterExp = "0=否,1=是")
    private String traceRequired;

    /** 排序 */
    @Excel(name = "排序", cellType = ColumnType.NUMERIC)
    private Integer sortOrder;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public Long getBomId()
    {
        return bomId;
    }

    public void setBomId(Long bomId)
    {
        this.bomId = bomId;
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

    @Xss(message = "物料规格不能包含脚本字符")
    @Size(min = 0, max = 255, message = "物料规格长度不能超过255个字符")
    public String getMaterialSpec()
    {
        return materialSpec;
    }

    public void setMaterialSpec(String materialSpec)
    {
        this.materialSpec = materialSpec;
    }

    @Xss(message = "成分说明不能包含脚本字符")
    @Size(min = 0, max = 255, message = "成分说明长度不能超过255个字符")
    public String getMaterialComposition()
    {
        return materialComposition;
    }

    public void setMaterialComposition(String materialComposition)
    {
        this.materialComposition = materialComposition;
    }

    public BigDecimal getDosage()
    {
        return dosage;
    }

    public void setDosage(BigDecimal dosage)
    {
        this.dosage = dosage;
    }

    @Size(min = 0, max = 20, message = "单位长度不能超过20个字符")
    public String getDosageUnit()
    {
        return dosageUnit;
    }

    public void setDosageUnit(String dosageUnit)
    {
        this.dosageUnit = dosageUnit;
    }

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

    public String getTraceRequired()
    {
        return traceRequired;
    }

    public void setTraceRequired(String traceRequired)
    {
        this.traceRequired = traceRequired;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
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
            .append("bomId", getBomId())
            .append("styleId", getStyleId())
            .append("materialType", getMaterialType())
            .append("materialName", getMaterialName())
            .append("materialSpec", getMaterialSpec())
            .append("materialComposition", getMaterialComposition())
            .append("dosage", getDosage())
            .append("dosageUnit", getDosageUnit())
            .append("supplierId", getSupplierId())
            .append("supplierName", getSupplierName())
            .append("traceRequired", getTraceRequired())
            .append("sortOrder", getSortOrder())
            .append("remark", getRemark())
            .toString();
    }
}
