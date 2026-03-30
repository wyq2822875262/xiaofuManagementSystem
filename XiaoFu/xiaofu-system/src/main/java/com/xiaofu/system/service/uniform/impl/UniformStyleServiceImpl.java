package com.xiaofu.system.service.uniform.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xiaofu.common.constant.UserConstants;
import com.xiaofu.common.utils.StringUtils;
import com.xiaofu.system.domain.uniform.UniformStyle;
import com.xiaofu.system.domain.uniform.UniformStyleBom;
import com.xiaofu.system.domain.uniform.UniformStyleSize;
import com.xiaofu.system.mapper.uniform.UniformStyleBomMapper;
import com.xiaofu.system.mapper.uniform.UniformStyleMapper;
import com.xiaofu.system.mapper.uniform.UniformStyleSizeMapper;
import com.xiaofu.system.service.uniform.IUniformStyleService;

/**
 * 校服款式 Service 实现
 */
@Service
public class UniformStyleServiceImpl implements IUniformStyleService
{
    @Autowired
    private UniformStyleMapper styleMapper;

    @Autowired
    private UniformStyleSizeMapper styleSizeMapper;

    @Autowired
    private UniformStyleBomMapper styleBomMapper;

    @Override
    public UniformStyle selectStyleById(Long styleId)
    {
        UniformStyle style = styleMapper.selectStyleById(styleId);
        if (StringUtils.isNotNull(style))
        {
            style.setSizeList(styleSizeMapper.selectStyleSizeListByStyleId(styleId));
            style.setBomList(styleBomMapper.selectStyleBomListByStyleId(styleId));
            fillStyleDerivedInfo(style);
        }
        return style;
    }

    @Override
    public List<UniformStyle> selectStyleList(UniformStyle style)
    {
        List<UniformStyle> list = styleMapper.selectStyleList(style);
        list.forEach(this::fillStyleDerivedInfo);
        return list;
    }

    @Override
    public boolean checkStyleCodeUnique(UniformStyle style)
    {
        Long styleId = StringUtils.isNull(style.getStyleId()) ? -1L : style.getStyleId();
        UniformStyle info = styleMapper.checkStyleCodeUnique(style.getStyleCode());
        if (StringUtils.isNotNull(info) && info.getStyleId().longValue() != styleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStyle(UniformStyle style)
    {
        normalizeStyleDetail(style);
        int rows = styleMapper.insertStyle(style);
        insertStyleChildren(style);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStyle(UniformStyle style)
    {
        normalizeStyleDetail(style);
        styleSizeMapper.deleteStyleSizeByStyleId(style.getStyleId());
        styleBomMapper.deleteStyleBomByStyleId(style.getStyleId());
        int rows = styleMapper.updateStyle(style);
        insertStyleChildren(style);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStyleByIds(Long[] styleIds)
    {
        styleSizeMapper.deleteStyleSizeByStyleIds(styleIds);
        styleBomMapper.deleteStyleBomByStyleIds(styleIds);
        return styleMapper.deleteStyleByIds(styleIds);
    }

    @Override
    public List<UniformStyle> selectAvailableStyles()
    {
        UniformStyle style = new UniformStyle();
        style.setStatus("0");
        return styleMapper.selectStyleList(style);
    }

    private void insertStyleChildren(UniformStyle style)
    {
        if (StringUtils.isNotEmpty(style.getSizeList()))
        {
            style.getSizeList().forEach(item -> item.setStyleId(style.getStyleId()));
            styleSizeMapper.batchInsertStyleSize(style.getSizeList());
        }
        if (StringUtils.isNotEmpty(style.getBomList()))
        {
            style.getBomList().forEach(item -> item.setStyleId(style.getStyleId()));
            styleBomMapper.batchInsertStyleBom(style.getBomList());
        }
    }

    private void normalizeStyleDetail(UniformStyle style)
    {
        style.setSizeList(normalizeSizeList(style.getSizeList()));
        style.setBomList(normalizeBomList(style.getBomList()));
        fillStyleDerivedInfo(style);
    }

    private List<UniformStyleSize> normalizeSizeList(List<UniformStyleSize> sizeList)
    {
        if (StringUtils.isEmpty(sizeList))
        {
            return new ArrayList<>();
        }
        List<UniformStyleSize> normalized = new ArrayList<>();
        int index = 1;
        for (UniformStyleSize item : sizeList)
        {
            if (item == null || (StringUtils.isEmpty(item.getSizeCode()) && StringUtils.isEmpty(item.getSizeName())))
            {
                continue;
            }
            item.setSpecialType(StringUtils.isEmpty(item.getSpecialType()) ? "0" : item.getSpecialType());
            item.setSortOrder(item.getSortOrder() == null ? index : item.getSortOrder());
            normalized.add(item);
            index++;
        }
        return normalized;
    }

    private List<UniformStyleBom> normalizeBomList(List<UniformStyleBom> bomList)
    {
        if (StringUtils.isEmpty(bomList))
        {
            return new ArrayList<>();
        }
        List<UniformStyleBom> normalized = new ArrayList<>();
        int index = 1;
        for (UniformStyleBom item : bomList)
        {
            if (item == null || StringUtils.isEmpty(item.getMaterialName()))
            {
                continue;
            }
            item.setMaterialType(StringUtils.isEmpty(item.getMaterialType()) ? "1" : item.getMaterialType());
            item.setTraceRequired(StringUtils.isEmpty(item.getTraceRequired()) ? "1" : item.getTraceRequired());
            item.setSortOrder(item.getSortOrder() == null ? index : item.getSortOrder());
            normalized.add(item);
            index++;
        }
        return normalized;
    }

    private void fillStyleDerivedInfo(UniformStyle style)
    {
        List<UniformStyleSize> sizeList = style.getSizeList();
        if (sizeList == null)
        {
            sizeList = Collections.emptyList();
        }
        List<UniformStyleBom> bomList = style.getBomList();
        if (bomList == null)
        {
            bomList = Collections.emptyList();
        }
        style.setSizeCount(style.getSizeCount() == null ? sizeList.size() : style.getSizeCount());
        style.setBomCount(style.getBomCount() == null ? bomList.size() : style.getBomCount());
        if (StringUtils.isEmpty(style.getSizeSummary()) && StringUtils.isNotEmpty(sizeList))
        {
            List<String> labels = new ArrayList<>();
            for (UniformStyleSize item : sizeList)
            {
                StringBuilder builder = new StringBuilder();
                builder.append(StringUtils.isNotEmpty(item.getSizeName()) ? item.getSizeName() : item.getSizeCode());
                if ("1".equals(item.getSpecialType()))
                {
                    builder.append("(加肥)");
                }
                else if ("2".equals(item.getSpecialType()))
                {
                    builder.append("(加长)");
                }
                else if ("3".equals(item.getSpecialType()))
                {
                    builder.append("(加肥加长)");
                }
                labels.add(builder.toString());
            }
            style.setSizeSummary(String.join("、", labels));
        }
    }
}
