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
import com.xiaofu.system.domain.uniform.UniformRmaOrder;
import com.xiaofu.system.service.uniform.IUniformRmaOrderService;

@RestController
@RequestMapping("/uniform/rma")
public class UniformRmaOrderController extends BaseController
{
    @Autowired
    private IUniformRmaOrderService rmaOrderService;

    @PreAuthorize("@ss.hasPermi('uniform:rma:list')")
    @GetMapping("/list")
    public TableDataInfo list(UniformRmaOrder rmaOrder)
    {
        startPage();
        List<UniformRmaOrder> list = rmaOrderService.selectRmaOrderList(rmaOrder);
        return getDataTable(list);
    }

    @Log(title = "退换货RMA", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('uniform:rma:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, UniformRmaOrder rmaOrder)
    {
        List<UniformRmaOrder> list = rmaOrderService.selectRmaOrderList(rmaOrder);
        ExcelUtil<UniformRmaOrder> util = new ExcelUtil<>(UniformRmaOrder.class);
        util.exportExcel(response, list, "退换货RMA数据");
    }

    @PreAuthorize("@ss.hasPermi('uniform:rma:query')")
    @GetMapping("/{rmaId}")
    public AjaxResult getInfo(@PathVariable Long rmaId)
    {
        return success(rmaOrderService.selectRmaOrderById(rmaId));
    }

    @PreAuthorize("@ss.hasPermi('uniform:rma:add')")
    @Log(title = "退换货RMA", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UniformRmaOrder rmaOrder)
    {
        rmaOrder.setCreateBy(getUsername());
        return toAjax(rmaOrderService.insertRmaOrder(rmaOrder));
    }

    @PreAuthorize("@ss.hasPermi('uniform:rma:edit')")
    @Log(title = "退换货RMA", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UniformRmaOrder rmaOrder)
    {
        rmaOrder.setUpdateBy(getUsername());
        return toAjax(rmaOrderService.updateRmaOrder(rmaOrder));
    }

    @PreAuthorize("@ss.hasPermi('uniform:rma:remove')")
    @Log(title = "退换货RMA", businessType = BusinessType.DELETE)
    @DeleteMapping("/{rmaIds}")
    public AjaxResult remove(@PathVariable Long[] rmaIds)
    {
        return toAjax(rmaOrderService.deleteRmaOrderByIds(rmaIds));
    }
}
