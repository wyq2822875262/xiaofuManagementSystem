package com.xiaofu.system.mapper.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformProcessRecord;

public interface UniformProcessRecordMapper
{
    public List<UniformProcessRecord> selectProcessListByWorkOrderId(Long workOrderId);

    public int deleteProcessByWorkOrderId(Long workOrderId);

    public int deleteProcessByWorkOrderIds(Long[] workOrderIds);

    public int batchInsertProcess(List<UniformProcessRecord> processList);
}
