package com.xiaofu.system.service.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformQualityInspection;

public interface IUniformQualityInspectionService
{
    public UniformQualityInspection selectInspectionById(Long inspectionId);

    public List<UniformQualityInspection> selectInspectionList(UniformQualityInspection inspection);

    public int insertInspection(UniformQualityInspection inspection);

    public int updateInspection(UniformQualityInspection inspection);

    public int deleteInspectionByIds(Long[] inspectionIds);
}
