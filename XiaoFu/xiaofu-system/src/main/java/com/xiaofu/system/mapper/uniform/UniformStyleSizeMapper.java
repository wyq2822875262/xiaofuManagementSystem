package com.xiaofu.system.mapper.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformStyleSize;

/**
 * 校服尺码矩阵 Mapper
 */
public interface UniformStyleSizeMapper
{
    public List<UniformStyleSize> selectStyleSizeListByStyleId(Long styleId);

    public int deleteStyleSizeByStyleIds(Long[] styleIds);

    public int deleteStyleSizeByStyleId(Long styleId);

    public int batchInsertStyleSize(List<UniformStyleSize> sizeList);
}
