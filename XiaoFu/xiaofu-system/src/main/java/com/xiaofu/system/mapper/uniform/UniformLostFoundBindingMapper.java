package com.xiaofu.system.mapper.uniform;

import com.xiaofu.system.domain.uniform.UniformLostFoundBinding;

public interface UniformLostFoundBindingMapper
{
    public UniformLostFoundBinding selectActiveBindingByTraceId(Long traceId);

    public UniformLostFoundBinding selectActiveBindingByTraceCode(String traceCode);

    public int insertLostFoundBinding(UniformLostFoundBinding binding);

    public int updateLostFoundBinding(UniformLostFoundBinding binding);
}
