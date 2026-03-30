package com.xiaofu.system.service.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformMaterialBatch;

public interface IUniformMaterialBatchService
{
    public UniformMaterialBatch selectMaterialBatchById(Long batchId);

    public List<UniformMaterialBatch> selectMaterialBatchList(UniformMaterialBatch batch);

    public boolean checkBatchNoUnique(UniformMaterialBatch batch);

    public int insertMaterialBatch(UniformMaterialBatch batch);

    public int updateMaterialBatch(UniformMaterialBatch batch);

    public int deleteMaterialBatchByIds(Long[] batchIds);

    public List<UniformMaterialBatch> selectAvailableBatchOptions();
}
