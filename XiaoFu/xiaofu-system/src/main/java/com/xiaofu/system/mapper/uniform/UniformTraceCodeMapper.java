package com.xiaofu.system.mapper.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformTraceCode;

public interface UniformTraceCodeMapper
{
    public UniformTraceCode selectTraceCodeById(Long traceId);

    public UniformTraceCode selectTraceCodeByTraceCode(String traceCode);

    public List<UniformTraceCode> selectTraceCodeList(UniformTraceCode traceCode);

    public List<UniformTraceCode> selectAvailableTraceOptions();

    public int countTraceCodeByWorkOrderId(Long workOrderId);

    public int batchInsertTraceCode(List<UniformTraceCode> traceCodes);

    public int deleteTraceCodeByIds(Long[] traceIds);

    public int deleteTraceCodeByWorkOrderIds(Long[] workOrderIds);

    public List<Long> selectWorkOrderIdsByTraceIds(Long[] traceIds);
}
