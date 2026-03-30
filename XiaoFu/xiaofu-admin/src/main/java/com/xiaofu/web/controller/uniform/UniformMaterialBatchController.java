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
import com.xiaofu.system.domain.uniform.UniformMaterialBatch;
import com.xiaofu.system.service.uniform.IUniformMaterialBatchService;

@RestController
@RequestMapping("/uniform/material")
public class UniformMaterialBatchController extends BaseController
{
    @Autowired
    private IUniformMaterialBatchService materialBatchService;

    @PreAuthorize("@ss.hasPermi('uniform:material:list')")
    @GetMapping("/list")
    public TableDataInfo list(UniformMaterialBatch batch)
    {
        startPage();
        List<UniformMaterialBatch> list = materialBatchService.selectMaterialBatchList(batch);
        return getDataTable(list);
    }

    @Log(title = "原料批次", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('uniform:material:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, UniformMaterialBatch batch)
    {
        List<UniformMaterialBatch> list = materialBatchService.selectMaterialBatchList(batch);
        ExcelUtil<UniformMaterialBatch> util = new ExcelUtil<>(UniformMaterialBatch.class);
        util.exportExcel(response, list, "原料批次数据");
    }

    @PreAuthorize("@ss.hasPermi('uniform:material:query')")
    @GetMapping("/{batchId}")
    public AjaxResult getInfo(@PathVariable Long batchId)
    {
        return success(materialBatchService.selectMaterialBatchById(batchId));
    }

    @PreAuthorize("@ss.hasAnyPermi('uniform:material:list,uniform:workorder:add,uniform:workorder:edit')")
    @GetMapping("/options")
    public AjaxResult options()
    {
        return success(materialBatchService.selectAvailableBatchOptions());
    }

    @PreAuthorize("@ss.hasPermi('uniform:material:add')")
    @Log(title = "原料批次", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UniformMaterialBatch batch)
    {
        if (batch.getBatchNo() != null && !"".equals(batch.getBatchNo()) && !materialBatchService.checkBatchNoUnique(batch))
        {
            return error("新增原料批次失败，内部批次号已存在");
        }
        batch.setCreateBy(getUsername());
        return toAjax(materialBatchService.insertMaterialBatch(batch));
    }

    @PreAuthorize("@ss.hasPermi('uniform:material:edit')")
    @Log(title = "原料批次", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UniformMaterialBatch batch)
    {
        if (batch.getBatchNo() != null && !"".equals(batch.getBatchNo()) && !materialBatchService.checkBatchNoUnique(batch))
        {
            return error("修改原料批次失败，内部批次号已存在");
        }
        batch.setUpdateBy(getUsername());
        return toAjax(materialBatchService.updateMaterialBatch(batch));
    }

    @PreAuthorize("@ss.hasPermi('uniform:material:remove')")
    @Log(title = "原料批次", businessType = BusinessType.DELETE)
    @DeleteMapping("/{batchIds}")
    public AjaxResult remove(@PathVariable Long[] batchIds)
    {
        return toAjax(materialBatchService.deleteMaterialBatchByIds(batchIds));
    }
}
