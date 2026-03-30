package com.xiaofu.system.mapper.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformStyle;

/**
 * 校服款式 Mapper
 */
public interface UniformStyleMapper
{
    public UniformStyle selectStyleById(Long styleId);

    public List<UniformStyle> selectStyleList(UniformStyle style);

    public UniformStyle checkStyleCodeUnique(String styleCode);

    public int insertStyle(UniformStyle style);

    public int updateStyle(UniformStyle style);

    public int deleteStyleByIds(Long[] styleIds);
}
