package com.xiaofu.system.service.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformStyle;

/**
 * 校服款式 Service
 */
public interface IUniformStyleService
{
    public UniformStyle selectStyleById(Long styleId);

    public List<UniformStyle> selectStyleList(UniformStyle style);

    public boolean checkStyleCodeUnique(UniformStyle style);

    public int insertStyle(UniformStyle style);

    public int updateStyle(UniformStyle style);

    public int deleteStyleByIds(Long[] styleIds);

    public List<UniformStyle> selectAvailableStyles();
}
