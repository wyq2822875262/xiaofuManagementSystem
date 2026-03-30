package com.xiaofu.system.domain.uniform;

import java.util.List;
import com.xiaofu.common.annotation.Excel;
import com.xiaofu.common.annotation.Excel.ColumnType;
import com.xiaofu.common.core.domain.BaseEntity;
import com.xiaofu.common.xss.Xss;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 校服款式对象 xf_uniform_style
 */
public class UniformStyle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 款式ID */
    @Excel(name = "款式ID", cellType = ColumnType.NUMERIC)
    private Long styleId;

    /** 款式编码 */
    @Excel(name = "款式编码")
    private String styleCode;

    /** 款式名称 */
    @Excel(name = "款式名称")
    private String styleName;

    /** 季节 */
    @Excel(name = "季节", readConverterExp = "spring_autumn=春秋,summer=夏装,winter=冬装")
    private String season;

    /** 类型 */
    @Excel(name = "类型", readConverterExp = "sportswear=运动装,uniform=制服,ceremonial=礼服,outerwear=外套,other=其他")
    private String styleType;

    /** 适用性别 */
    @Excel(name = "适用性别", readConverterExp = "unisex=中性,male=男,female=女")
    private String gender;

    /** 主面料名称 */
    @Excel(name = "主面料")
    private String fabricName;

    /** 面料成分 */
    @Excel(name = "面料成分")
    private String fabricComposition;

    /** 赋码模式 */
    @Excel(name = "赋码模式", readConverterExp = "1=一物一码,2=一批一码")
    private String codeMode;

    /** 是否支持特体 */
    @Excel(name = "支持特体", readConverterExp = "0=否,1=是")
    private String specialBodySupport;

    /** 洗护说明 */
    @Excel(name = "洗护说明")
    private String careInstructions;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 尺码数量 */
    @Excel(name = "尺码数", cellType = ColumnType.NUMERIC)
    private Integer sizeCount;

    /** BOM 数量 */
    @Excel(name = "BOM数", cellType = ColumnType.NUMERIC)
    private Integer bomCount;

    /** 尺码摘要 */
    @Excel(name = "尺码矩阵")
    private String sizeSummary;

    /** 尺码矩阵 */
    private List<UniformStyleSize> sizeList;

    /** BOM 明细 */
    private List<UniformStyleBom> bomList;

    public Long getStyleId()
    {
        return styleId;
    }

    public void setStyleId(Long styleId)
    {
        this.styleId = styleId;
    }

    @Xss(message = "款式编码不能包含脚本字符")
    @NotBlank(message = "款式编码不能为空")
    @Size(min = 0, max = 64, message = "款式编码长度不能超过64个字符")
    public String getStyleCode()
    {
        return styleCode;
    }

    public void setStyleCode(String styleCode)
    {
        this.styleCode = styleCode;
    }

    @Xss(message = "款式名称不能包含脚本字符")
    @NotBlank(message = "款式名称不能为空")
    @Size(min = 0, max = 100, message = "款式名称长度不能超过100个字符")
    public String getStyleName()
    {
        return styleName;
    }

    public void setStyleName(String styleName)
    {
        this.styleName = styleName;
    }

    @NotBlank(message = "季节不能为空")
    public String getSeason()
    {
        return season;
    }

    public void setSeason(String season)
    {
        this.season = season;
    }

    @NotBlank(message = "类型不能为空")
    public String getStyleType()
    {
        return styleType;
    }

    public void setStyleType(String styleType)
    {
        this.styleType = styleType;
    }

    @NotBlank(message = "适用性别不能为空")
    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    @Size(min = 0, max = 100, message = "主面料名称长度不能超过100个字符")
    public String getFabricName()
    {
        return fabricName;
    }

    public void setFabricName(String fabricName)
    {
        this.fabricName = fabricName;
    }

    @Size(min = 0, max = 255, message = "面料成分长度不能超过255个字符")
    public String getFabricComposition()
    {
        return fabricComposition;
    }

    public void setFabricComposition(String fabricComposition)
    {
        this.fabricComposition = fabricComposition;
    }

    @NotBlank(message = "赋码模式不能为空")
    public String getCodeMode()
    {
        return codeMode;
    }

    public void setCodeMode(String codeMode)
    {
        this.codeMode = codeMode;
    }

    @NotBlank(message = "是否支持特体不能为空")
    public String getSpecialBodySupport()
    {
        return specialBodySupport;
    }

    public void setSpecialBodySupport(String specialBodySupport)
    {
        this.specialBodySupport = specialBodySupport;
    }

    @Size(min = 0, max = 500, message = "洗护说明长度不能超过500个字符")
    public String getCareInstructions()
    {
        return careInstructions;
    }

    public void setCareInstructions(String careInstructions)
    {
        this.careInstructions = careInstructions;
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

    public Integer getSizeCount()
    {
        return sizeCount;
    }

    public void setSizeCount(Integer sizeCount)
    {
        this.sizeCount = sizeCount;
    }

    public Integer getBomCount()
    {
        return bomCount;
    }

    public void setBomCount(Integer bomCount)
    {
        this.bomCount = bomCount;
    }

    public String getSizeSummary()
    {
        return sizeSummary;
    }

    public void setSizeSummary(String sizeSummary)
    {
        this.sizeSummary = sizeSummary;
    }

    public List<UniformStyleSize> getSizeList()
    {
        return sizeList;
    }

    public void setSizeList(List<UniformStyleSize> sizeList)
    {
        this.sizeList = sizeList;
    }

    public List<UniformStyleBom> getBomList()
    {
        return bomList;
    }

    public void setBomList(List<UniformStyleBom> bomList)
    {
        this.bomList = bomList;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("styleId", getStyleId())
            .append("styleCode", getStyleCode())
            .append("styleName", getStyleName())
            .append("season", getSeason())
            .append("styleType", getStyleType())
            .append("gender", getGender())
            .append("fabricName", getFabricName())
            .append("fabricComposition", getFabricComposition())
            .append("codeMode", getCodeMode())
            .append("specialBodySupport", getSpecialBodySupport())
            .append("careInstructions", getCareInstructions())
            .append("status", getStatus())
            .append("sizeCount", getSizeCount())
            .append("bomCount", getBomCount())
            .append("sizeSummary", getSizeSummary())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
