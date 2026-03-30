package com.xiaofu.system.mapper.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformMaterialBatch;

public interface UniformMaterialBatchMapper
{
    public UniformMaterialBatch selectMaterialBatchById(Long batchId);

    public List<UniformMaterialBatch> selectMaterialBatchList(UniformMaterialBatch batch);

    public UniformMaterialBatch checkBatchNoUnique(String batchNo);

    public int insertMaterialBatch(UniformMaterialBatch batch);

    public int updateMaterialBatch(UniformMaterialBatch batch);

    public int deleteMaterialBatchByIds(Long[] batchIds);

    public List<UniformMaterialBatch> selectAvailableBatchOptions();
}
