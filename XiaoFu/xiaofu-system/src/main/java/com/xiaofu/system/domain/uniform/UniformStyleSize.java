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
 * 校服尺码矩阵对象 xf_uniform_style_size
 */
public class UniformStyleSize implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 尺码ID */
    @Excel(name = "尺码ID", cellType = ColumnType.NUMERIC)
    private Long sizeId;

    /** 款式ID */
    private Long styleId;

    /** 尺码编码 */
    @Excel(name = "尺码编码")
    private String sizeCode;

    /** 尺码名称 */
    @Excel(name = "尺码名称")
    private String sizeName;

    /** 特体类型 */
    @Excel(name = "特体类型", readConverterExp = "0=标准,1=加肥,2=加长,3=加肥加长")
    private String specialType;

    /** 建议身高下限 */
    @Excel(name = "身高下限")
    private BigDecimal heightMin;

    /** 建议身高上限 */
    @Excel(name = "身高上限")
    private BigDecimal heightMax;

    /** 建议体重下限 */
    @Excel(name = "体重下限")
    private BigDecimal weightMin;

    /** 建议体重上限 */
    @Excel(name = "体重上限")
    private BigDecimal weightMax;

    /** 排序 */
    @Excel(name = "排序", cellType = ColumnType.NUMERIC)
    private Integer sortOrder;

    public Long getSizeId()
    {
        return sizeId;
    }

    public void setSizeId(Long sizeId)
    {
        this.sizeId = sizeId;
    }

    public Long getStyleId()
    {
        return styleId;
    }

    public void setStyleId(Long styleId)
    {
        this.styleId = styleId;
    }

    @Xss(message = "尺码编码不能包含脚本字符")
    @NotBlank(message = "尺码编码不能为空")
    @Size(min = 0, max = 32, message = "尺码编码长度不能超过32个字符")
    public String getSizeCode()
    {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode)
    {
        this.sizeCode = sizeCode;
    }

    @Xss(message = "尺码名称不能包含脚本字符")
    @NotBlank(message = "尺码名称不能为空")
    @Size(min = 0, max = 32, message = "尺码名称长度不能超过32个字符")
    public String getSizeName()
    {
        return sizeName;
    }

    public void setSizeName(String sizeName)
    {
        this.sizeName = sizeName;
    }

    public String getSpecialType()
    {
        return specialType;
    }

    public void setSpecialType(String specialType)
    {
        this.specialType = specialType;
    }

    public BigDecimal getHeightMin()
    {
        return heightMin;
    }

    public void setHeightMin(BigDecimal heightMin)
    {
        this.heightMin = heightMin;
    }

    public BigDecimal getHeightMax()
    {
        return heightMax;
    }

    public void setHeightMax(BigDecimal heightMax)
    {
        this.heightMax = heightMax;
    }

    public BigDecimal getWeightMin()
    {
        return weightMin;
    }

    public void setWeightMin(BigDecimal weightMin)
    {
        this.weightMin = weightMin;
    }

    public BigDecimal getWeightMax()
    {
        return weightMax;
    }

    public void setWeightMax(BigDecimal weightMax)
    {
        this.weightMax = weightMax;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("sizeId", getSizeId())
            .append("styleId", getStyleId())
            .append("sizeCode", getSizeCode())
            .append("sizeName", getSizeName())
            .append("specialType", getSpecialType())
            .append("heightMin", getHeightMin())
            .append("heightMax", getHeightMax())
            .append("weightMin", getWeightMin())
            .append("weightMax", getWeightMax())
            .append("sortOrder", getSortOrder())
            .toString();
    }
}
