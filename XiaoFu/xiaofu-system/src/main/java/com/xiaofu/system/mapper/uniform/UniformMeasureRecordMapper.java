package com.xiaofu.system.mapper.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformMeasureRecord;

public interface UniformMeasureRecordMapper
{
    public UniformMeasureRecord selectMeasureRecordById(Long measureId);

    public List<UniformMeasureRecord> selectMeasureRecordList(UniformMeasureRecord measureRecord);

    public int insertMeasureRecord(UniformMeasureRecord measureRecord);

    public int updateMeasureRecord(UniformMeasureRecord measureRecord);

    public int deleteMeasureRecordByIds(Long[] measureIds);
}
