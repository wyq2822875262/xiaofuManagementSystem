package com.xiaofu.system.service.uniform.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaofu.common.utils.StringUtils;
import com.xiaofu.system.domain.uniform.UniformScreenFlowLine;
import com.xiaofu.system.domain.uniform.UniformScreenIndicator;
import com.xiaofu.system.domain.uniform.UniformScreenInspectionSnapshot;
import com.xiaofu.system.domain.uniform.UniformScreenMetricAggregate;
import com.xiaofu.system.domain.uniform.UniformScreenMetricCard;
import com.xiaofu.system.domain.uniform.UniformScreenOverview;
import com.xiaofu.system.domain.uniform.UniformScreenPieItem;
import com.xiaofu.system.domain.uniform.UniformScreenProgressItem;
import com.xiaofu.system.domain.uniform.UniformScreenStageStat;
import com.xiaofu.system.domain.uniform.UniformScreenWordCloudItem;
import com.xiaofu.system.mapper.uniform.UniformScreenMapper;
import com.xiaofu.system.service.uniform.IUniformScreenService;

/**
 * 监管舱大屏服务
 */
@Service
public class UniformScreenServiceImpl implements IUniformScreenService
{
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final List<String[]> STAGE_DEFINITIONS = Arrays.asList(
        new String[] {"0", "待开工"},
        new String[] {"1", "裁剪中"},
        new String[] {"2", "缝纫中"},
        new String[] {"3", "熨烫中"},
        new String[] {"4", "待质检"},
        new String[] {"5", "质检合格"},
        new String[] {"6", "已赋码"},
        new String[] {"8", "质检拦截"});
    private static final List<String> BODY_TYPE_ORDER = Arrays.asList("偏瘦", "标准", "微胖", "壮实", "待完善");

    @Autowired
    private UniformScreenMapper screenMapper;

    @Override
    public UniformScreenOverview getOverview()
    {
        String seasonCode = resolveSeasonCode();
        UniformScreenMetricAggregate aggregate = normalizeMetricAggregate(screenMapper.selectMetricAggregate(seasonCode));
        UniformScreenInspectionSnapshot latestInspection = screenMapper.selectLatestInspectionSnapshot(seasonCode);

        UniformScreenOverview overview = new UniformScreenOverview();
        overview.setSeasonCode(seasonCode);
        overview.setSeasonLabel(resolveSeasonLabel(seasonCode));
        overview.setRefreshedAt(new Date());
        overview.setLatestInspection(latestInspection);
        overview.setMetricCards(buildMetricCards(aggregate));
        overview.setQualityRadar(buildQualityRadar(latestInspection));
        overview.setProductionFunnel(buildProductionFunnel(screenMapper.selectProductionStageStats(seasonCode)));
        overview.setLogisticsFlows(normalizeFlowLines(screenMapper.selectLogisticsFlows(seasonCode)));
        overview.setDeliveryProgress(normalizeProgress(screenMapper.selectDeliveryProgress(seasonCode)));
        overview.setFeedbackCloud(normalizeFeedbackCloud(screenMapper.selectFeedbackCloud()));
        overview.setBodyTypeDistribution(buildBodyTypeDistribution(screenMapper.selectBodyTypeDistribution(seasonCode)));
        return overview;
    }

    private String resolveSeasonCode()
    {
        int month = new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).getMonthValue();
        String preferredSeason = month >= 6 && month <= 8 ? "summer"
            : (month >= 11 || month <= 2) ? "winter" : "spring_autumn";
        Integer seasonCount = screenMapper.countWorkOrdersBySeason(preferredSeason);
        if (seasonCount != null && seasonCount > 0)
        {
            return preferredSeason;
        }
        String dominantSeason = screenMapper.selectDominantSeasonCode();
        return StringUtils.isNotEmpty(dominantSeason) ? dominantSeason : "summer";
    }

    private UniformScreenMetricAggregate normalizeMetricAggregate(UniformScreenMetricAggregate aggregate)
    {
        UniformScreenMetricAggregate normalized = aggregate == null ? new UniformScreenMetricAggregate() : aggregate;
        normalized.setProtectedStudents(defaultInt(normalized.getProtectedStudents()));
        normalized.setSeasonalDeliveryQuantity(defaultInt(normalized.getSeasonalDeliveryQuantity()));
        normalized.setActiveSchools(defaultInt(normalized.getActiveSchools()));
        normalized.setQualityPassCount(defaultInt(normalized.getQualityPassCount()));
        normalized.setQualityInspectCount(defaultInt(normalized.getQualityInspectCount()));
        return normalized;
    }

    private List<UniformScreenMetricCard> buildMetricCards(UniformScreenMetricAggregate aggregate)
    {
        List<UniformScreenMetricCard> cards = new ArrayList<>();
        cards.add(createMetricCard("protectedStudents", "守护学生数", String.valueOf(aggregate.getProtectedStudents()), "人",
            "来自量体档案的累计去重学生数"));
        cards.add(createMetricCard("seasonalDelivery", "本季交付总量", String.valueOf(aggregate.getSeasonalDeliveryQuantity()), "件",
            "已进入发运链路的本季交付件数"));
        cards.add(createMetricCard("qualityPassRate", "质检合格率", formatDecimal(calculateRate(aggregate.getQualityPassCount(),
            aggregate.getQualityInspectCount())), "%", "合格" + aggregate.getQualityPassCount() + "批 / 总抽检"
            + aggregate.getQualityInspectCount() + "批"));
        cards.add(createMetricCard("activeSchools", "活跃学校数", String.valueOf(aggregate.getActiveSchools()), "所",
            "当前生产季已接入订单的学校数量"));
        return cards;
    }

    private UniformScreenMetricCard createMetricCard(String code, String label, String value, String unit, String description)
    {
        UniformScreenMetricCard card = new UniformScreenMetricCard();
        card.setCode(code);
        card.setLabel(label);
        card.setValue(value);
        card.setUnit(unit);
        card.setDescription(description);
        return card;
    }

    private List<UniformScreenIndicator> buildQualityRadar(UniformScreenInspectionSnapshot snapshot)
    {
        List<UniformScreenIndicator> indicators = new ArrayList<>();
        indicators.add(createIndicator("甲醛安全", scoreFormaldehyde(snapshot == null ? null : snapshot.getFormaldehyde())));
        indicators.add(createIndicator("PH平衡", scorePh(snapshot == null ? null : snapshot.getPhValue())));
        indicators.add(createIndicator("异味控制", scoreBinary(snapshot == null ? null : snapshot.getOdorResult())));
        indicators.add(createIndicator("芳香胺", scoreBinary(snapshot == null ? null : snapshot.getAmineResult())));
        indicators.add(createIndicator("色牢度", scoreAscending(snapshot == null ? null : snapshot.getColorFastness(),
            new BigDecimal("4.0"))));
        indicators.add(createIndicator("抗起球", scoreAscending(snapshot == null ? null : snapshot.getPillingGrade(),
            new BigDecimal("4.0"))));
        return indicators;
    }

    private UniformScreenIndicator createIndicator(String name, BigDecimal value)
    {
        UniformScreenIndicator indicator = new UniformScreenIndicator();
        indicator.setName(name);
        indicator.setValue(value);
        return indicator;
    }

    private BigDecimal scoreFormaldehyde(BigDecimal value)
    {
        if (value == null)
        {
            return BigDecimal.ZERO;
        }

        BigDecimal limit = new BigDecimal("75");
        if (value.compareTo(limit) <= 0)
        {
            BigDecimal score = HUNDRED.subtract(value.multiply(new BigDecimal("40")).divide(limit, 2, RoundingMode.HALF_UP));
            return clampScore(score);
        }

        BigDecimal exceeded = value.subtract(limit);
        BigDecimal score = new BigDecimal("60").subtract(exceeded.multiply(new BigDecimal("60")).divide(limit, 2, RoundingMode.HALF_UP));
        return clampScore(score);
    }

    private BigDecimal scorePh(BigDecimal value)
    {
        if (value == null)
        {
            return BigDecimal.ZERO;
        }

        BigDecimal center = new BigDecimal("6.25");
        BigDecimal validDistance = new BigDecimal("2.25");
        BigDecimal distance = value.subtract(center).abs();
        if (distance.compareTo(validDistance) <= 0)
        {
            BigDecimal score = HUNDRED.subtract(distance.multiply(new BigDecimal("30")).divide(validDistance, 2, RoundingMode.HALF_UP));
            return clampScore(score);
        }

        BigDecimal exceeded = distance.subtract(validDistance);
        BigDecimal score = new BigDecimal("70").subtract(exceeded.multiply(new BigDecimal("28")));
        return clampScore(score);
    }

    private BigDecimal scoreBinary(String result)
    {
        if (StringUtils.isEmpty(result))
        {
            return BigDecimal.ZERO;
        }
        return "0".equals(result) ? HUNDRED : BigDecimal.ZERO;
    }

    private BigDecimal scoreAscending(BigDecimal value, BigDecimal target)
    {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0 || target.compareTo(BigDecimal.ZERO) <= 0)
        {
            return BigDecimal.ZERO;
        }
        BigDecimal score = value.multiply(HUNDRED).divide(target, 2, RoundingMode.HALF_UP);
        return clampScore(score);
    }

    private BigDecimal clampScore(BigDecimal score)
    {
        if (score.compareTo(BigDecimal.ZERO) < 0)
        {
            return BigDecimal.ZERO;
        }
        if (score.compareTo(HUNDRED) > 0)
        {
            return HUNDRED;
        }
        return score.setScale(1, RoundingMode.HALF_UP);
    }

    private List<UniformScreenStageStat> buildProductionFunnel(List<UniformScreenStageStat> stageStats)
    {
        Map<String, Integer> valueMap = new LinkedHashMap<>();
        if (stageStats != null)
        {
            for (UniformScreenStageStat item : stageStats)
            {
                valueMap.put(item.getStageCode(), defaultInt(item.getValue()));
            }
        }

        List<UniformScreenStageStat> result = new ArrayList<>();
        for (String[] definition : STAGE_DEFINITIONS)
        {
            UniformScreenStageStat stat = new UniformScreenStageStat();
            stat.setStageCode(definition[0]);
            stat.setStageLabel(definition[1]);
            stat.setValue(valueMap.getOrDefault(definition[0], 0));
            result.add(stat);
        }
        return result;
    }

    private List<UniformScreenFlowLine> normalizeFlowLines(List<UniformScreenFlowLine> flowLines)
    {
        List<UniformScreenFlowLine> result = new ArrayList<>();
        if (flowLines == null)
        {
            return result;
        }

        for (UniformScreenFlowLine item : flowLines)
        {
            if (item == null)
            {
                continue;
            }
            item.setShipmentQuantity(defaultInt(item.getShipmentQuantity()));
            item.setPackedQuantity(defaultInt(item.getPackedQuantity()));
            item.setProgressRate(defaultDecimal(item.getProgressRate()));
            if (StringUtils.isEmpty(item.getRouteName()))
            {
                item.setRouteName(StringUtils.trimToEmpty(item.getSourceName()) + " -> " + StringUtils.trimToEmpty(item.getTargetName()));
            }
            result.add(item);
        }
        return result;
    }

    private List<UniformScreenProgressItem> normalizeProgress(List<UniformScreenProgressItem> progressItems)
    {
        List<UniformScreenProgressItem> result = new ArrayList<>();
        if (progressItems == null)
        {
            return result;
        }

        for (UniformScreenProgressItem item : progressItems)
        {
            if (item == null)
            {
                continue;
            }
            item.setTotalQuantity(defaultInt(item.getTotalQuantity()));
            item.setPackedQuantity(defaultInt(item.getPackedQuantity()));
            item.setSignedQuantity(defaultInt(item.getSignedQuantity()));
            item.setProgressRate(defaultDecimal(item.getProgressRate()));
            item.setSignRate(defaultDecimal(item.getSignRate()));
            item.setStatusLabel(resolveShipmentStatusLabel(item.getShipmentStatus()));
            result.add(item);
        }
        return result;
    }

    private List<UniformScreenWordCloudItem> normalizeFeedbackCloud(List<UniformScreenWordCloudItem> feedbackCloud)
    {
        if (feedbackCloud != null && !feedbackCloud.isEmpty())
        {
            return feedbackCloud;
        }

        List<UniformScreenWordCloudItem> defaults = new ArrayList<>();
        defaults.add(createWordCloudItem("安全无异味", 8));
        defaults.add(createWordCloudItem("面料柔软", 7));
        defaults.add(createWordCloudItem("尺码合身", 6));
        defaults.add(createWordCloudItem("透气轻便", 5));
        defaults.add(createWordCloudItem("追溯清晰", 4));
        return defaults;
    }

    private UniformScreenWordCloudItem createWordCloudItem(String name, Integer value)
    {
        UniformScreenWordCloudItem item = new UniformScreenWordCloudItem();
        item.setName(name);
        item.setValue(value);
        return item;
    }

    private List<UniformScreenPieItem> buildBodyTypeDistribution(List<UniformScreenPieItem> bodyTypeDistribution)
    {
        Map<String, Integer> valueMap = new LinkedHashMap<>();
        if (bodyTypeDistribution != null)
        {
            for (UniformScreenPieItem item : bodyTypeDistribution)
            {
                valueMap.put(item.getName(), defaultInt(item.getValue()));
            }
        }

        List<UniformScreenPieItem> result = new ArrayList<>();
        for (String name : BODY_TYPE_ORDER)
        {
            UniformScreenPieItem item = new UniformScreenPieItem();
            item.setName(name);
            item.setValue(valueMap.getOrDefault(name, 0));
            result.add(item);
        }
        return result;
    }

    private BigDecimal calculateRate(Integer numerator, Integer denominator)
    {
        if (denominator == null || denominator <= 0)
        {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(defaultInt(numerator)).multiply(HUNDRED).divide(new BigDecimal(denominator), 1, RoundingMode.HALF_UP);
    }

    private String formatDecimal(BigDecimal value)
    {
        return defaultDecimal(value).stripTrailingZeros().toPlainString();
    }

    private Integer defaultInt(Integer value)
    {
        return value == null ? 0 : value;
    }

    private BigDecimal defaultDecimal(BigDecimal value)
    {
        return value == null ? BigDecimal.ZERO : value.setScale(1, RoundingMode.HALF_UP);
    }

    private String resolveSeasonLabel(String seasonCode)
    {
        if ("summer".equals(seasonCode))
        {
            return "夏季校服季";
        }
        if ("winter".equals(seasonCode))
        {
            return "冬季校服季";
        }
        if ("spring_autumn".equals(seasonCode))
        {
            return "春秋校服季";
        }
        return "校服监管季";
    }

    private String resolveShipmentStatusLabel(String shipmentStatus)
    {
        if ("4".equals(shipmentStatus))
        {
            return "已签收";
        }
        if ("3".equals(shipmentStatus))
        {
            return "已发货";
        }
        if ("2".equals(shipmentStatus))
        {
            return "待发货";
        }
        if ("1".equals(shipmentStatus))
        {
            return "分拣中";
        }
        return "待分拣";
    }
}
