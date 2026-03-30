package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 学校交付进度
 */
public class UniformScreenProgressItem implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String schoolName;

    private Integer totalQuantity;

    private Integer packedQuantity;

    private Integer signedQuantity;

    private BigDecimal progressRate;

    private BigDecimal signRate;

    private String shipmentStatus;

    private String statusLabel;

    public String getSchoolName()
    {
        return schoolName;
    }

    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }

    public Integer getTotalQuantity()
    {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity)
    {
        this.totalQuantity = totalQuantity;
    }

    public Integer getPackedQuantity()
    {
        return packedQuantity;
    }

    public void setPackedQuantity(Integer packedQuantity)
    {
        this.packedQuantity = packedQuantity;
    }

    public Integer getSignedQuantity()
    {
        return signedQuantity;
    }

    public void setSignedQuantity(Integer signedQuantity)
    {
        this.signedQuantity = signedQuantity;
    }

    public BigDecimal getProgressRate()
    {
        return progressRate;
    }

    public void setProgressRate(BigDecimal progressRate)
    {
        this.progressRate = progressRate;
    }

    public BigDecimal getSignRate()
    {
        return signRate;
    }

    public void setSignRate(BigDecimal signRate)
    {
        this.signRate = signRate;
    }

    public String getShipmentStatus()
    {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus)
    {
        this.shipmentStatus = shipmentStatus;
    }

    public String getStatusLabel()
    {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel)
    {
        this.statusLabel = statusLabel;
    }
}
