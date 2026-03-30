package com.xiaofu.system.service.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformMeasureRecord;

public interface IUniformMeasureRecordService
{
    public UniformMeasureRecord selectMeasureRecordById(Long measureId);

    public List<UniformMeasureRecord> selectMeasureRecordList(UniformMeasureRecord measureRecord);

    public UniformMeasureRecord recommendMeasureRecord(UniformMeasureRecord measureRecord);

    public int insertMeasureRecord(UniformMeasureRecord measureRecord);

    public int updateMeasureRecord(UniformMeasureRecord measureRecord);

    public int deleteMeasureRecordByIds(Long[] measureIds);
}
