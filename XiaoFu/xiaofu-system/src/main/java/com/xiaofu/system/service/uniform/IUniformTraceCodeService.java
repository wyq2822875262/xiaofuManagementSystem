package com.xiaofu.system.service.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformTraceCode;
import com.xiaofu.system.domain.uniform.UniformTraceGenerateRequest;

public interface IUniformTraceCodeService
{
    public List<UniformTraceCode> selectTraceCodeList(UniformTraceCode traceCode);

    public List<UniformTraceCode> selectAvailableTraceOptions();

    public int generateTraceCodes(UniformTraceGenerateRequest request, String operatorName);

    public int deleteTraceCodesByIds(Long[] traceIds);
}
