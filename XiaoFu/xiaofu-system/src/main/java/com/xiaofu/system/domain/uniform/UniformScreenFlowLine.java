package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物流飞线
 */
public class UniformScreenFlowLine implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String routeName;

    private String sourceName;

    private BigDecimal sourceLng;

    private BigDecimal sourceLat;

    private String targetName;

    private BigDecimal targetLng;

    private BigDecimal targetLat;

    private Integer shipmentQuantity;

    private Integer packedQuantity;

    private BigDecimal progressRate;

    public String getRouteName()
    {
        return routeName;
    }

    public void setRouteName(String routeName)
    {
        this.routeName = routeName;
    }

    public String getSourceName()
    {
        return sourceName;
    }

    public void setSourceName(String sourceName)
    {
        this.sourceName = sourceName;
    }

    public BigDecimal getSourceLng()
    {
        return sourceLng;
    }

    public void setSourceLng(BigDecimal sourceLng)
    {
        this.sourceLng = sourceLng;
    }

    public BigDecimal getSourceLat()
    {
        return sourceLat;
    }

    public void setSourceLat(BigDecimal sourceLat)
    {
        this.sourceLat = sourceLat;
    }

    public String getTargetName()
    {
        return targetName;
    }

    public void setTargetName(String targetName)
    {
        this.targetName = targetName;
    }

    public BigDecimal getTargetLng()
    {
        return targetLng;
    }

    public void setTargetLng(BigDecimal targetLng)
    {
        this.targetLng = targetLng;
    }

    public BigDecimal getTargetLat()
    {
        return targetLat;
    }

    public void setTargetLat(BigDecimal targetLat)
    {
        this.targetLat = targetLat;
    }

    public Integer getShipmentQuantity()
    {
        return shipmentQuantity;
    }

    public void setShipmentQuantity(Integer shipmentQuantity)
    {
        this.shipmentQuantity = shipmentQuantity;
    }

    public Integer getPackedQuantity()
    {
        return packedQuantity;
    }

    public void setPackedQuantity(Integer packedQuantity)
    {
        this.packedQuantity = packedQuantity;
    }

    public BigDecimal getProgressRate()
    {
        return progressRate;
    }

    public void setProgressRate(BigDecimal progressRate)
    {
        this.progressRate = progressRate;
    }
}
