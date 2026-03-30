package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 监管舱大屏总览
 */
public class UniformScreenOverview implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String seasonCode;

    private String seasonLabel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date refreshedAt;

    private UniformScreenInspectionSnapshot latestInspection;

    private List<UniformScreenMetricCard> metricCards;

    private List<UniformScreenIndicator> qualityRadar;

    private List<UniformScreenStageStat> productionFunnel;

    private List<UniformScreenFlowLine> logisticsFlows;

    private List<UniformScreenProgressItem> deliveryProgress;

    private List<UniformScreenWordCloudItem> feedbackCloud;

    private List<UniformScreenPieItem> bodyTypeDistribution;

    public String getSeasonCode()
    {
        return seasonCode;
    }

    public void setSeasonCode(String seasonCode)
    {
        this.seasonCode = seasonCode;
    }

    public String getSeasonLabel()
    {
        return seasonLabel;
    }

    public void setSeasonLabel(String seasonLabel)
    {
        this.seasonLabel = seasonLabel;
    }

    public Date getRefreshedAt()
    {
        return refreshedAt;
    }

    public void setRefreshedAt(Date refreshedAt)
    {
        this.refreshedAt = refreshedAt;
    }

    public UniformScreenInspectionSnapshot getLatestInspection()
    {
        return latestInspection;
    }

    public void setLatestInspection(UniformScreenInspectionSnapshot latestInspection)
    {
        this.latestInspection = latestInspection;
    }

    public List<UniformScreenMetricCard> getMetricCards()
    {
        return metricCards;
    }

    public void setMetricCards(List<UniformScreenMetricCard> metricCards)
    {
        this.metricCards = metricCards;
    }

    public List<UniformScreenIndicator> getQualityRadar()
    {
        return qualityRadar;
    }

    public void setQualityRadar(List<UniformScreenIndicator> qualityRadar)
    {
        this.qualityRadar = qualityRadar;
    }

    public List<UniformScreenStageStat> getProductionFunnel()
    {
        return productionFunnel;
    }

    public void setProductionFunnel(List<UniformScreenStageStat> productionFunnel)
    {
        this.productionFunnel = productionFunnel;
    }

    public List<UniformScreenFlowLine> getLogisticsFlows()
    {
        return logisticsFlows;
    }

    public void setLogisticsFlows(List<UniformScreenFlowLine> logisticsFlows)
    {
        this.logisticsFlows = logisticsFlows;
    }

    public List<UniformScreenProgressItem> getDeliveryProgress()
    {
        return deliveryProgress;
    }

    public void setDeliveryProgress(List<UniformScreenProgressItem> deliveryProgress)
    {
        this.deliveryProgress = deliveryProgress;
    }

    public List<UniformScreenWordCloudItem> getFeedbackCloud()
    {
        return feedbackCloud;
    }

    public void setFeedbackCloud(List<UniformScreenWordCloudItem> feedbackCloud)
    {
        this.feedbackCloud = feedbackCloud;
    }

    public List<UniformScreenPieItem> getBodyTypeDistribution()
    {
        return bodyTypeDistribution;
    }

    public void setBodyTypeDistribution(List<UniformScreenPieItem> bodyTypeDistribution)
    {
        this.bodyTypeDistribution = bodyTypeDistribution;
    }
}
