package com.xiaofu.system.mapper.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformStyleBom;

/**
 * 校服 BOM Mapper
 */
public interface UniformStyleBomMapper
{
    public List<UniformStyleBom> selectStyleBomListByStyleId(Long styleId);

    public int deleteStyleBomByStyleIds(Long[] styleIds);

    public int deleteStyleBomByStyleId(Long styleId);

    public int batchInsertStyleBom(List<UniformStyleBom> bomList);
}
