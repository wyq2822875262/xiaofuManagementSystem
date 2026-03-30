package com.xiaofu.system.domain.uniform;

import java.io.Serializable;

/**
 * 大屏聚合指标
 */
public class UniformScreenMetricAggregate implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer protectedStudents;

    private Integer seasonalDeliveryQuantity;

    private Integer activeSchools;

    private Integer qualityPassCount;

    private Integer qualityInspectCount;

    public Integer getProtectedStudents()
    {
        return protectedStudents;
    }

    public void setProtectedStudents(Integer protectedStudents)
    {
        this.protectedStudents = protectedStudents;
    }

    public Integer getSeasonalDeliveryQuantity()
    {
        return seasonalDeliveryQuantity;
    }

    public void setSeasonalDeliveryQuantity(Integer seasonalDeliveryQuantity)
    {
        this.seasonalDeliveryQuantity = seasonalDeliveryQuantity;
    }

    public Integer getActiveSchools()
    {
        return activeSchools;
    }

    public void setActiveSchools(Integer activeSchools)
    {
        this.activeSchools = activeSchools;
    }

    public Integer getQualityPassCount()
    {
        return qualityPassCount;
    }

    public void setQualityPassCount(Integer qualityPassCount)
    {
        this.qualityPassCount = qualityPassCount;
    }

    public Integer getQualityInspectCount()
    {
        return qualityInspectCount;
    }

    public void setQualityInspectCount(Integer qualityInspectCount)
    {
        this.qualityInspectCount = qualityInspectCount;
    }
}
