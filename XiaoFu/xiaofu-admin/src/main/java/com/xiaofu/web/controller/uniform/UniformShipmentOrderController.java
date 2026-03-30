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
import com.xiaofu.system.domain.uniform.UniformShipmentOrder;
import com.xiaofu.system.service.uniform.IUniformShipmentOrderService;

@RestController
@RequestMapping("/uniform/shipment")
public class UniformShipmentOrderController extends BaseController
{
    @Autowired
    private IUniformShipmentOrderService shipmentOrderService;

    @PreAuthorize("@ss.hasPermi('uniform:shipment:list')")
    @GetMapping("/list")
    public TableDataInfo list(UniformShipmentOrder shipmentOrder)
    {
        startPage();
        List<UniformShipmentOrder> list = shipmentOrderService.selectShipmentOrderList(shipmentOrder);
        return getDataTable(list);
    }

    @Log(title = "班级分拣发运", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('uniform:shipment:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, UniformShipmentOrder shipmentOrder)
    {
        List<UniformShipmentOrder> list = shipmentOrderService.selectShipmentOrderList(shipmentOrder);
        ExcelUtil<UniformShipmentOrder> util = new ExcelUtil<>(UniformShipmentOrder.class);
        util.exportExcel(response, list, "班级分拣发运数据");
    }

    @PreAuthorize("@ss.hasPermi('uniform:shipment:query')")
    @GetMapping("/{shipmentId}")
    public AjaxResult getInfo(@PathVariable Long shipmentId)
    {
        return success(shipmentOrderService.selectShipmentOrderById(shipmentId));
    }

    @PreAuthorize("@ss.hasPermi('uniform:shipment:add')")
    @Log(title = "班级分拣发运", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UniformShipmentOrder shipmentOrder)
    {
        if (shipmentOrder.getShipmentNo() != null && !"".equals(shipmentOrder.getShipmentNo())
            && !shipmentOrderService.checkShipmentNoUnique(shipmentOrder))
        {
            return error("新增发运单失败，发运单号已存在");
        }
        shipmentOrder.setCreateBy(getUsername());
        return toAjax(shipmentOrderService.insertShipmentOrder(shipmentOrder));
    }

    @PreAuthorize("@ss.hasPermi('uniform:shipment:edit')")
    @Log(title = "班级分拣发运", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UniformShipmentOrder shipmentOrder)
    {
        if (shipmentOrder.getShipmentNo() != null && !"".equals(shipmentOrder.getShipmentNo())
            && !shipmentOrderService.checkShipmentNoUnique(shipmentOrder))
        {
            return error("修改发运单失败，发运单号已存在");
        }
        shipmentOrder.setUpdateBy(getUsername());
        return toAjax(shipmentOrderService.updateShipmentOrder(shipmentOrder));
    }

    @PreAuthorize("@ss.hasPermi('uniform:shipment:remove')")
    @Log(title = "班级分拣发运", businessType = BusinessType.DELETE)
    @DeleteMapping("/{shipmentIds}")
    public AjaxResult remove(@PathVariable Long[] shipmentIds)
    {
        return toAjax(shipmentOrderService.deleteShipmentOrderByIds(shipmentIds));
    }
}
