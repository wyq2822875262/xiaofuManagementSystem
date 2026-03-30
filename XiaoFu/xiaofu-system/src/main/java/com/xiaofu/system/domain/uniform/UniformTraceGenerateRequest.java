package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;

/**
 * 溯源码生成请求
 */
public class UniformTraceGenerateRequest implements Serializable
{
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单不能为空")
    private Long workOrderId;

    private Integer generateCount;

    public Long getWorkOrderId()
    {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId)
    {
        this.workOrderId = workOrderId;
    }

    public Integer getGenerateCount()
    {
        return generateCount;
    }

    public void setGenerateCount(Integer generateCount)
    {
        this.generateCount = generateCount;
    }
}
