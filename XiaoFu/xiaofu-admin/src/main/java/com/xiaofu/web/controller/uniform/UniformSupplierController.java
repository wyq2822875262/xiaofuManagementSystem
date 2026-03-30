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
import com.xiaofu.system.domain.uniform.UniformSupplier;
import com.xiaofu.system.service.uniform.IUniformSupplierService;

/**
 * 供应商主数据控制器
 */
@RestController
@RequestMapping("/uniform/supplier")
public class UniformSupplierController extends BaseController
{
    @Autowired
    private IUniformSupplierService supplierService;

    @PreAuthorize("@ss.hasPermi('uniform:supplier:list')")
    @GetMapping("/list")
    public TableDataInfo list(UniformSupplier supplier)
    {
        startPage();
        List<UniformSupplier> list = supplierService.selectSupplierList(supplier);
        return getDataTable(list);
    }

    @Log(title = "供应商主数据", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('uniform:supplier:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, UniformSupplier supplier)
    {
        List<UniformSupplier> list = supplierService.selectSupplierList(supplier);
        ExcelUtil<UniformSupplier> util = new ExcelUtil<>(UniformSupplier.class);
        util.exportExcel(response, list, "供应商主数据");
    }

    @PreAuthorize("@ss.hasPermi('uniform:supplier:query')")
    @GetMapping("/{supplierId}")
    public AjaxResult getInfo(@PathVariable Long supplierId)
    {
        return success(supplierService.selectSupplierById(supplierId));
    }

    @PreAuthorize("@ss.hasPermi('uniform:supplier:list')")
    @GetMapping("/alertSummary")
    public AjaxResult alertSummary()
    {
        return success(supplierService.selectSupplierAlertSummary());
    }

    @PreAuthorize("@ss.hasAnyPermi('uniform:supplier:list,uniform:style:add,uniform:style:edit')")
    @GetMapping("/options")
    public AjaxResult options()
    {
        return success(supplierService.selectAvailableSuppliers());
    }

    @PreAuthorize("@ss.hasPermi('uniform:supplier:add')")
    @Log(title = "供应商主数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UniformSupplier supplier)
    {
        if (!supplierService.checkSupplierCodeUnique(supplier))
        {
            return error("新增供应商'" + supplier.getSupplierName() + "'失败，供应商编码已存在");
        }
        supplier.setCreateBy(getUsername());
        return toAjax(supplierService.insertSupplier(supplier));
    }

    @PreAuthorize("@ss.hasPermi('uniform:supplier:edit')")
    @Log(title = "供应商主数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UniformSupplier supplier)
    {
        if (!supplierService.checkSupplierCodeUnique(supplier))
        {
            return error("修改供应商'" + supplier.getSupplierName() + "'失败，供应商编码已存在");
        }
        supplier.setUpdateBy(getUsername());
        return toAjax(supplierService.updateSupplier(supplier));
    }

    @PreAuthorize("@ss.hasPermi('uniform:supplier:remove')")
    @Log(title = "供应商主数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{supplierIds}")
    public AjaxResult remove(@PathVariable Long[] supplierIds)
    {
        return toAjax(supplierService.deleteSupplierByIds(supplierIds));
    }
}
