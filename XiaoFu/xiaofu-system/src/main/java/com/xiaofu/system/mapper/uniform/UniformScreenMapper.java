package com.xiaofu.system.mapper.uniform;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.xiaofu.system.domain.uniform.UniformScreenFlowLine;
import com.xiaofu.system.domain.uniform.UniformScreenInspectionSnapshot;
import com.xiaofu.system.domain.uniform.UniformScreenMetricAggregate;
import com.xiaofu.system.domain.uniform.UniformScreenPieItem;
import com.xiaofu.system.domain.uniform.UniformScreenProgressItem;
import com.xiaofu.system.domain.uniform.UniformScreenStageStat;
import com.xiaofu.system.domain.uniform.UniformScreenWordCloudItem;

public interface UniformScreenMapper
{
    public Integer countWorkOrdersBySeason(@Param("seasonCode") String seasonCode);

    public String selectDominantSeasonCode();

    public UniformScreenMetricAggregate selectMetricAggregate(@Param("seasonCode") String seasonCode);

    public UniformScreenInspectionSnapshot selectLatestInspectionSnapshot(@Param("seasonCode") String seasonCode);

    public List<UniformScreenStageStat> selectProductionStageStats(@Param("seasonCode") String seasonCode);

    public List<UniformScreenFlowLine> selectLogisticsFlows(@Param("seasonCode") String seasonCode);

    public List<UniformScreenProgressItem> selectDeliveryProgress(@Param("seasonCode") String seasonCode);

    public List<UniformScreenWordCloudItem> selectFeedbackCloud();

    public List<UniformScreenPieItem> selectBodyTypeDistribution(@Param("seasonCode") String seasonCode);
}
