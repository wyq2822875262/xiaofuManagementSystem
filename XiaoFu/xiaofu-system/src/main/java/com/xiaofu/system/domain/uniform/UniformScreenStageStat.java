package com.xiaofu.system.domain.uniform;

import java.io.Serializable;

/**
 * 工单阶段统计
 */
public class UniformScreenStageStat implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String stageCode;

    private String stageLabel;

    private Integer value;

    public String getStageCode()
    {
        return stageCode;
    }

    public void setStageCode(String stageCode)
    {
        this.stageCode = stageCode;
    }

    public String getStageLabel()
    {
        return stageLabel;
    }

    public void setStageLabel(String stageLabel)
    {
        this.stageLabel = stageLabel;
    }

    public Integer getValue()
    {
        return value;
    }

    public void setValue(Integer value)
    {
        this.value = value;
    }
}
