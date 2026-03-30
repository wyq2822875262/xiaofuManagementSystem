package com.xiaofu.system.mapper.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformQualityInspection;

public interface UniformQualityInspectionMapper
{
    public UniformQualityInspection selectInspectionById(Long inspectionId);

    public List<UniformQualityInspection> selectInspectionList(UniformQualityInspection inspection);

    public UniformQualityInspection selectLatestInspectionByWorkOrderId(Long workOrderId);

    public int insertInspection(UniformQualityInspection inspection);

    public int updateInspection(UniformQualityInspection inspection);

    public int deleteInspectionByIds(Long[] inspectionIds);

    public int deleteInspectionByWorkOrderIds(Long[] workOrderIds);
}
