package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 大屏指标项
 */
public class UniformScreenIndicator implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String name;

    private BigDecimal value;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public BigDecimal getValue()
    {
        return value;
    }

    public void setValue(BigDecimal value)
    {
        this.value = value;
    }
}
