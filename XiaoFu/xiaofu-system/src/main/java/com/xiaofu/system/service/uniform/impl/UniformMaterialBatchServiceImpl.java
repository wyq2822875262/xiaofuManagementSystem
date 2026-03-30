package com.xiaofu.system.service.uniform.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaofu.common.constant.UserConstants;
import com.xiaofu.common.utils.StringUtils;
import com.xiaofu.common.utils.uuid.Seq;
import com.xiaofu.system.domain.uniform.UniformMaterialBatch;
import com.xiaofu.system.mapper.uniform.UniformMaterialBatchMapper;
import com.xiaofu.system.service.uniform.IUniformMaterialBatchService;

@Service
public class UniformMaterialBatchServiceImpl implements IUniformMaterialBatchService
{
    @Autowired
    private UniformMaterialBatchMapper materialBatchMapper;

    @Override
    public UniformMaterialBatch selectMaterialBatchById(Long batchId)
    {
        return materialBatchMapper.selectMaterialBatchById(batchId);
    }

    @Override
    public List<UniformMaterialBatch> selectMaterialBatchList(UniformMaterialBatch batch)
    {
        return materialBatchMapper.selectMaterialBatchList(batch);
    }

    @Override
    public boolean checkBatchNoUnique(UniformMaterialBatch batch)
    {
        Long batchId = StringUtils.isNull(batch.getBatchId()) ? -1L : batch.getBatchId();
        UniformMaterialBatch info = materialBatchMapper.checkBatchNoUnique(batch.getBatchNo());
        if (StringUtils.isNotNull(info) && info.getBatchId().longValue() != batchId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int insertMaterialBatch(UniformMaterialBatch batch)
    {
        if (StringUtils.isEmpty(batch.getBatchNo()))
        {
            batch.setBatchNo("MB" + Seq.getId());
        }
        if (StringUtils.isEmpty(batch.getStatus()))
        {
            batch.setStatus("0");
        }
        return materialBatchMapper.insertMaterialBatch(batch);
    }

    @Override
    public int updateMaterialBatch(UniformMaterialBatch batch)
    {
        return materialBatchMapper.updateMaterialBatch(batch);
    }

    @Override
    public int deleteMaterialBatchByIds(Long[] batchIds)
    {
        return materialBatchMapper.deleteMaterialBatchByIds(batchIds);
    }

    @Override
    public List<UniformMaterialBatch> selectAvailableBatchOptions()
    {
        return materialBatchMapper.selectAvailableBatchOptions();
    }
}
