package com.xiaofu.system.domain.uniform;

import java.io.Serializable;

/**
 * 大屏指标卡片
 */
public class UniformScreenMetricCard implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String code;

    private String label;

    private String value;

    private String unit;

    private String description;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
