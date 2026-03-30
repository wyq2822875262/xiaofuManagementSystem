package com.xiaofu.web.controller.uniform;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xiaofu.common.annotation.Log;
import com.xiaofu.common.core.controller.BaseController;
import com.xiaofu.common.core.domain.AjaxResult;
import com.xiaofu.common.core.page.TableDataInfo;
import com.xiaofu.common.enums.BusinessType;
import com.xiaofu.common.utils.poi.ExcelUtil;
import com.xiaofu.system.domain.uniform.UniformStyle;
import com.xiaofu.system.service.uniform.IUniformStyleService;

/**
 * 校服款式控制器
 */
@RestController
@RequestMapping("/uniform/style")
public class UniformStyleController extends BaseController
{
    @Autowired
    private IUniformStyleService styleService;

    @PreAuthorize("@ss.hasPermi('uniform:style:list')")
    @GetMapping("/list")
    public TableDataInfo list(UniformStyle style)
    {
        startPage();
        List<UniformStyle> list = styleService.selectStyleList(style);
        return getDataTable(list);
    }

    @Log(title = "校服款式库", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('uniform:style:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, UniformStyle style)
    {
        List<UniformStyle> list = styleService.selectStyleList(style);
        ExcelUtil<UniformStyle> util = new ExcelUtil<>(UniformStyle.class);
        util.exportExcel(response, list, "款式与BOM数据");
    }

    @PreAuthorize("@ss.hasPermi('uniform:style:query')")
    @GetMapping("/{styleId}")
    public AjaxResult getInfo(@PathVariable Long styleId)
    {
        return success(styleService.selectStyleById(styleId));
    }

    @PreAuthorize("@ss.hasAnyPermi('uniform:style:list,uniform:material:add,uniform:material:edit,uniform:workorder:add,uniform:workorder:edit')")
    @GetMapping("/options")
    public AjaxResult options()
    {
        return success(styleService.selectAvailableStyles());
    }

    @PreAuthorize("@ss.hasPermi('uniform:style:add')")
    @Log(title = "校服款式库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UniformStyle style)
    {
        if (!styleService.checkStyleCodeUnique(style))
        {
            return error("新增款式'" + style.getStyleName() + "'失败，款式编码已存在");
        }
        style.setCreateBy(getUsername());
        return toAjax(styleService.insertStyle(style));
    }

    @PreAuthorize("@ss.hasPermi('uniform:style:edit')")
    @Log(title = "校服款式库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UniformStyle style)
    {
        if (!styleService.checkStyleCodeUnique(style))
        {
            return error("修改款式'" + style.getStyleName() + "'失败，款式编码已存在");
        }
        style.setUpdateBy(getUsername());
        return toAjax(styleService.updateStyle(style));
    }

    @PreAuthorize("@ss.hasPermi('uniform:style:remove')")
    @Log(title = "校服款式库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{styleIds}")
    public AjaxResult remove(@PathVariable Long[] styleIds)
    {
        return toAjax(styleService.deleteStyleByIds(styleIds));
    }
}
