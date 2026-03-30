package com.xiaofu.system.service.uniform;

import com.xiaofu.system.domain.uniform.UniformLostFoundBindRequest;
import com.xiaofu.system.domain.uniform.UniformLostFoundCard;
import com.xiaofu.system.domain.uniform.UniformPortalTraceArchive;

public interface IUniformPortalService
{
    public UniformPortalTraceArchive getTraceArchive(String traceCodeText);

    public UniformLostFoundCard saveLostFoundBinding(String traceCodeText, UniformLostFoundBindRequest request);
}
