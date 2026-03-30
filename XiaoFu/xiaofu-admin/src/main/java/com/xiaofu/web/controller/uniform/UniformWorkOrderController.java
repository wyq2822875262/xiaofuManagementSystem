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
import com.xiaofu.system.domain.uniform.UniformWorkOrder;
import com.xiaofu.system.service.uniform.IUniformWorkOrderService;

@RestController
@RequestMapping("/uniform/workorder")
public class UniformWorkOrderController extends BaseController
{
    @Autowired
    private IUniformWorkOrderService workOrderService;

    @PreAuthorize("@ss.hasPermi('uniform:workorder:list')")
    @GetMapping("/list")
    public TableDataInfo list(UniformWorkOrder workOrder)
    {
        startPage();
        List<UniformWorkOrder> list = workOrderService.selectWorkOrderList(workOrder);
        return getDataTable(list);
    }

    @Log(title = "生产工单", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('uniform:workorder:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, UniformWorkOrder workOrder)
    {
        List<UniformWorkOrder> list = workOrderService.selectWorkOrderList(workOrder);
        ExcelUtil<UniformWorkOrder> util = new ExcelUtil<>(UniformWorkOrder.class);
        util.exportExcel(response, list, "生产工单数据");
    }

    @PreAuthorize("@ss.hasPermi('uniform:workorder:query')")
    @GetMapping("/{workOrderId}")
    public AjaxResult getInfo(@PathVariable Long workOrderId)
    {
        return success(workOrderService.selectWorkOrderById(workOrderId));
    }

    @PreAuthorize("@ss.hasAnyPermi('uniform:workorder:list,uniform:inspection:add,uniform:trace:add')")
    @GetMapping("/options")
    public AjaxResult options()
    {
        return success(workOrderService.selectAvailableWorkOrderOptions());
    }

    @PreAuthorize("@ss.hasPermi('uniform:workorder:add')")
    @Log(title = "生产工单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UniformWorkOrder workOrder)
    {
        if (workOrder.getWorkOrderNo() != null && !"".equals(workOrder.getWorkOrderNo()) && !workOrderService.checkWorkOrderNoUnique(workOrder))
        {
            return error("新增工单失败，工单号已存在");
        }
        workOrder.setCreateBy(getUsername());
        return toAjax(workOrderService.insertWorkOrder(workOrder));
    }

    @PreAuthorize("@ss.hasPermi('uniform:workorder:edit')")
    @Log(title = "生产工单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UniformWorkOrder workOrder)
    {
        if (workOrder.getWorkOrderNo() != null && !"".equals(workOrder.getWorkOrderNo()) && !workOrderService.checkWorkOrderNoUnique(workOrder))
        {
            return error("修改工单失败，工单号已存在");
        }
        workOrder.setUpdateBy(getUsername());
        return toAjax(workOrderService.updateWorkOrder(workOrder));
    }

    @PreAuthorize("@ss.hasPermi('uniform:workorder:remove')")
    @Log(title = "生产工单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{workOrderIds}")
    public AjaxResult remove(@PathVariable Long[] workOrderIds)
    {
        return toAjax(workOrderService.deleteWorkOrderByIds(workOrderIds));
    }
}
