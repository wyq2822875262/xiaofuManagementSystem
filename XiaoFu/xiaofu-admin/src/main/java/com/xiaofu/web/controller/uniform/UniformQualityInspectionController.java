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
import com.xiaofu.system.domain.uniform.UniformQualityInspection;
import com.xiaofu.system.service.uniform.IUniformQualityInspectionService;

@RestController
@RequestMapping("/uniform/inspection")
public class UniformQualityInspectionController extends BaseController
{
    @Autowired
    private IUniformQualityInspectionService inspectionService;

    @PreAuthorize("@ss.hasPermi('uniform:inspection:list')")
    @GetMapping("/list")
    public TableDataInfo list(UniformQualityInspection inspection)
    {
        startPage();
        List<UniformQualityInspection> list = inspectionService.selectInspectionList(inspection);
        return getDataTable(list);
    }

    @Log(title = "成品质检", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('uniform:inspection:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, UniformQualityInspection inspection)
    {
        List<UniformQualityInspection> list = inspectionService.selectInspectionList(inspection);
        ExcelUtil<UniformQualityInspection> util = new ExcelUtil<>(UniformQualityInspection.class);
        util.exportExcel(response, list, "成品质检数据");
    }

    @PreAuthorize("@ss.hasPermi('uniform:inspection:query')")
    @GetMapping("/{inspectionId}")
    public AjaxResult getInfo(@PathVariable Long inspectionId)
    {
        return success(inspectionService.selectInspectionById(inspectionId));
    }

    @PreAuthorize("@ss.hasPermi('uniform:inspection:add')")
    @Log(title = "成品质检", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UniformQualityInspection inspection)
    {
        inspection.setCreateBy(getUsername());
        return toAjax(inspectionService.insertInspection(inspection));
    }

    @PreAuthorize("@ss.hasPermi('uniform:inspection:edit')")
    @Log(title = "成品质检", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UniformQualityInspection inspection)
    {
        inspection.setUpdateBy(getUsername());
        return toAjax(inspectionService.updateInspection(inspection));
    }

    @PreAuthorize("@ss.hasPermi('uniform:inspection:remove')")
    @Log(title = "成品质检", businessType = BusinessType.DELETE)
    @DeleteMapping("/{inspectionIds}")
    public AjaxResult remove(@PathVariable Long[] inspectionIds)
    {
        return toAjax(inspectionService.deleteInspectionByIds(inspectionIds));
    }
}
