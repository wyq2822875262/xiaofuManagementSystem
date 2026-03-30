package com.xiaofu.system.domain.uniform;

import java.io.Serializable;

/**
 * 供应商资质预警概览
 */
public class UniformSupplierAlertSummary implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer totalCount;

    private Integer normalCount;

    private Integer warningCount;

    private Integer expiredCount;

    public Integer getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount)
    {
        this.totalCount = totalCount;
    }

    public Integer getNormalCount()
    {
        return normalCount;
    }

    public void setNormalCount(Integer normalCount)
    {
        this.normalCount = normalCount;
    }

    public Integer getWarningCount()
    {
        return warningCount;
    }

    public void setWarningCount(Integer warningCount)
    {
        this.warningCount = warningCount;
    }

    public Integer getExpiredCount()
    {
        return expiredCount;
    }

    public void setExpiredCount(Integer expiredCount)
    {
        this.expiredCount = expiredCount;
    }
}
