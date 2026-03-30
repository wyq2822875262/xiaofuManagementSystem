package com.xiaofu.system.service.uniform.impl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaofu.common.exception.ServiceException;
import com.xiaofu.common.utils.StringUtils;
import com.xiaofu.system.domain.uniform.UniformMeasureRecord;
import com.xiaofu.system.domain.uniform.UniformStyle;
import com.xiaofu.system.domain.uniform.UniformStyleSize;
import com.xiaofu.system.mapper.uniform.UniformMeasureRecordMapper;
import com.xiaofu.system.mapper.uniform.UniformStyleMapper;
import com.xiaofu.system.mapper.uniform.UniformStyleSizeMapper;
import com.xiaofu.system.service.uniform.IUniformMeasureRecordService;

@Service
public class UniformMeasureRecordServiceImpl implements IUniformMeasureRecordService
{
    private static final BigDecimal STANDARD_SIZE_PENALTY = new BigDecimal("5");

    @Autowired
    private UniformMeasureRecordMapper measureRecordMapper;

    @Autowired
    private UniformStyleMapper styleMapper;

    @Autowired
    private UniformStyleSizeMapper styleSizeMapper;

    @Override
    public UniformMeasureRecord selectMeasureRecordById(Long measureId)
    {
        return measureRecordMapper.selectMeasureRecordById(measureId);
    }

    @Override
    public List<UniformMeasureRecord> selectMeasureRecordList(UniformMeasureRecord measureRecord)
    {
        return measureRecordMapper.selectMeasureRecordList(measureRecord);
    }

    @Override
    public UniformMeasureRecord recommendMeasureRecord(UniformMeasureRecord measureRecord)
    {
        if (measureRecord.getStyleId() == null)
        {
            throw new ServiceException("请选择款式后再进行推荐");
        }
        if (measureRecord.getHeightValue() == null || measureRecord.getWeightValue() == null)
        {
            throw new ServiceException("请先录入身高和体重后再进行推荐");
        }
        UniformStyle style = styleMapper.selectStyleById(measureRecord.getStyleId());
        if (StringUtils.isNull(style))
        {
            throw new ServiceException("关联款式不存在，请刷新后重试");
        }
        List<UniformStyleSize> sizeList = styleSizeMapper.selectStyleSizeListByStyleId(measureRecord.getStyleId());
        if (StringUtils.isEmpty(sizeList))
        {
            throw new ServiceException("当前款式未配置尺码矩阵，无法推荐尺码");
        }

        UniformStyleSize recommendedSize = chooseBestSize(sizeList, measureRecord.getHeightValue(), measureRecord.getWeightValue());
        measureRecord.setStyleName(style.getStyleName());
        measureRecord.setRecommendedSizeCode(recommendedSize.getSizeCode());
        measureRecord.setRecommendedSizeName(recommendedSize.getSizeName());
        fillFinalSize(measureRecord, sizeList);
        return measureRecord;
    }

    @Override
    public int insertMeasureRecord(UniformMeasureRecord measureRecord)
    {
        fillDefaults(measureRecord);
        recommendMeasureRecord(measureRecord);
        return measureRecordMapper.insertMeasureRecord(measureRecord);
    }

    @Override
    public int updateMeasureRecord(UniformMeasureRecord measureRecord)
    {
        fillDefaults(measureRecord);
        recommendMeasureRecord(measureRecord);
        return measureRecordMapper.updateMeasureRecord(measureRecord);
    }

    @Override
    public int deleteMeasureRecordByIds(Long[] measureIds)
    {
        return measureRecordMapper.deleteMeasureRecordByIds(measureIds);
    }

    private void fillDefaults(UniformMeasureRecord measureRecord)
    {
        if (StringUtils.isEmpty(measureRecord.getSourceChannel()))
        {
            measureRecord.setSourceChannel("manual");
        }
        if (StringUtils.isEmpty(measureRecord.getMeasureStatus()))
        {
            measureRecord.setMeasureStatus("0");
        }
        if (measureRecord.getMeasureTime() == null)
        {
            measureRecord.setMeasureTime(new Date());
        }
    }

    private void fillFinalSize(UniformMeasureRecord measureRecord, List<UniformStyleSize> sizeList)
    {
        if (StringUtils.isEmpty(measureRecord.getFinalSizeCode()))
        {
            measureRecord.setFinalSizeCode(measureRecord.getRecommendedSizeCode());
            measureRecord.setFinalSizeName(measureRecord.getRecommendedSizeName());
            return;
        }

        UniformStyleSize finalSize = sizeList.stream()
            .filter(item -> Objects.equals(item.getSizeCode(), measureRecord.getFinalSizeCode())
                || Objects.equals(item.getSizeName(), measureRecord.getFinalSizeCode()))
            .findFirst()
            .orElseThrow(() -> new ServiceException("最终确认尺码不在当前款式尺码矩阵内"));
        measureRecord.setFinalSizeCode(finalSize.getSizeCode());
        measureRecord.setFinalSizeName(finalSize.getSizeName());
    }

    private UniformStyleSize chooseBestSize(List<UniformStyleSize> sizeList, BigDecimal heightValue, BigDecimal weightValue)
    {
        return sizeList.stream()
            .min(Comparator
                .comparing((UniformStyleSize item) -> calculateScore(item, heightValue, weightValue))
                .thenComparing(item -> item.getSortOrder() == null ? Integer.MAX_VALUE : item.getSortOrder())
                .thenComparing(item -> item.getSizeId() == null ? Long.MAX_VALUE : item.getSizeId()))
            .orElseThrow(() -> new ServiceException("未找到可用尺码，请检查款式尺码矩阵"));
    }

    private BigDecimal calculateScore(UniformStyleSize size, BigDecimal heightValue, BigDecimal weightValue)
    {
        BigDecimal heightDistance = distanceToRange(heightValue, size.getHeightMin(), size.getHeightMax()).multiply(new BigDecimal("2"));
        BigDecimal weightDistance = distanceToRange(weightValue, size.getWeightMin(), size.getWeightMax());
        BigDecimal specialPenalty = "0".equals(StringUtils.isEmpty(size.getSpecialType()) ? "0" : size.getSpecialType())
            ? BigDecimal.ZERO : STANDARD_SIZE_PENALTY;
        return heightDistance.add(weightDistance).add(specialPenalty);
    }

    private BigDecimal distanceToRange(BigDecimal value, BigDecimal min, BigDecimal max)
    {
        if (value == null)
        {
            return new BigDecimal("9999");
        }
        if (min != null && value.compareTo(min) < 0)
        {
            return min.subtract(value);
        }
        if (max != null && value.compareTo(max) > 0)
        {
            return value.subtract(max);
        }
        return BigDecimal.ZERO;
    }
}
