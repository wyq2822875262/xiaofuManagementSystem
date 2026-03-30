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
import com.xiaofu.system.domain.uniform.UniformMeasureRecord;
import com.xiaofu.system.service.uniform.IUniformMeasureRecordService;

@RestController
@RequestMapping("/uniform/measure")
public class UniformMeasureRecordController extends BaseController
{
    @Autowired
    private IUniformMeasureRecordService measureRecordService;

    @PreAuthorize("@ss.hasPermi('uniform:measure:list')")
    @GetMapping("/list")
    public TableDataInfo list(UniformMeasureRecord measureRecord)
    {
        startPage();
        List<UniformMeasureRecord> list = measureRecordService.selectMeasureRecordList(measureRecord);
        return getDataTable(list);
    }

    @Log(title = "量体测码", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('uniform:measure:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, UniformMeasureRecord measureRecord)
    {
        List<UniformMeasureRecord> list = measureRecordService.selectMeasureRecordList(measureRecord);
        ExcelUtil<UniformMeasureRecord> util = new ExcelUtil<>(UniformMeasureRecord.class);
        util.exportExcel(response, list, "量体测码数据");
    }

    @PreAuthorize("@ss.hasPermi('uniform:measure:query')")
    @GetMapping("/{measureId}")
    public AjaxResult getInfo(@PathVariable Long measureId)
    {
        return success(measureRecordService.selectMeasureRecordById(measureId));
    }

    @PreAuthorize("@ss.hasAnyPermi('uniform:measure:list,uniform:measure:add,uniform:measure:edit')")
    @PostMapping("/recommend")
    public AjaxResult recommend(@RequestBody UniformMeasureRecord measureRecord)
    {
        return success(measureRecordService.recommendMeasureRecord(measureRecord));
    }

    @PreAuthorize("@ss.hasPermi('uniform:measure:add')")
    @Log(title = "量体测码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UniformMeasureRecord measureRecord)
    {
        measureRecord.setCreateBy(getUsername());
        return toAjax(measureRecordService.insertMeasureRecord(measureRecord));
    }

    @PreAuthorize("@ss.hasPermi('uniform:measure:edit')")
    @Log(title = "量体测码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UniformMeasureRecord measureRecord)
    {
        measureRecord.setUpdateBy(getUsername());
        return toAjax(measureRecordService.updateMeasureRecord(measureRecord));
    }

    @PreAuthorize("@ss.hasPermi('uniform:measure:remove')")
    @Log(title = "量体测码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{measureIds}")
    public AjaxResult remove(@PathVariable Long[] measureIds)
    {
        return toAjax(measureRecordService.deleteMeasureRecordByIds(measureIds));
    }
}
