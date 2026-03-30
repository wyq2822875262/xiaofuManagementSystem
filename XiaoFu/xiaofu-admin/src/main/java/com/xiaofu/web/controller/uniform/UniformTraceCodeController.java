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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xiaofu.common.annotation.Log;
import com.xiaofu.common.core.controller.BaseController;
import com.xiaofu.common.core.domain.AjaxResult;
import com.xiaofu.common.core.page.TableDataInfo;
import com.xiaofu.common.enums.BusinessType;
import com.xiaofu.common.utils.poi.ExcelUtil;
import com.xiaofu.system.domain.uniform.UniformTraceCode;
import com.xiaofu.system.domain.uniform.UniformTraceGenerateRequest;
import com.xiaofu.system.service.uniform.IUniformTraceCodeService;

@RestController
@RequestMapping("/uniform/trace")
public class UniformTraceCodeController extends BaseController
{
    @Autowired
    private IUniformTraceCodeService traceCodeService;

    @PreAuthorize("@ss.hasPermi('uniform:trace:list')")
    @GetMapping("/list")
    public TableDataInfo list(UniformTraceCode traceCode)
    {
        startPage();
        List<UniformTraceCode> list = traceCodeService.selectTraceCodeList(traceCode);
        return getDataTable(list);
    }

    @Log(title = "溯源码记录", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('uniform:trace:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, UniformTraceCode traceCode)
    {
        List<UniformTraceCode> list = traceCodeService.selectTraceCodeList(traceCode);
        ExcelUtil<UniformTraceCode> util = new ExcelUtil<>(UniformTraceCode.class);
        util.exportExcel(response, list, "溯源码数据");
    }

    @PreAuthorize("@ss.hasAnyPermi('uniform:trace:list,uniform:rma:add,uniform:rma:edit')")
    @GetMapping("/options")
    public AjaxResult options()
    {
        return success(traceCodeService.selectAvailableTraceOptions());
    }

    @PreAuthorize("@ss.hasPermi('uniform:trace:add')")
    @Log(title = "溯源码记录", businessType = BusinessType.INSERT)
    @PostMapping("/generate")
    public AjaxResult generate(@Validated @RequestBody UniformTraceGenerateRequest request)
    {
        return toAjax(traceCodeService.generateTraceCodes(request, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('uniform:trace:remove')")
    @Log(title = "溯源码记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{traceIds}")
    public AjaxResult remove(@PathVariable Long[] traceIds)
    {
        return toAjax(traceCodeService.deleteTraceCodesByIds(traceIds));
    }
}
