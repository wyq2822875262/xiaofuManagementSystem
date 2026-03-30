package com.xiaofu.web.controller.uniform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xiaofu.common.annotation.Anonymous;
import com.xiaofu.common.core.controller.BaseController;
import com.xiaofu.common.core.domain.AjaxResult;
import com.xiaofu.system.domain.uniform.UniformLostFoundBindRequest;
import com.xiaofu.system.service.uniform.IUniformPortalService;

/**
 * C端门户匿名接口
 */
@Anonymous
@RestController
@RequestMapping("/portal/trace")
public class UniformPortalController extends BaseController
{
    @Autowired
    private IUniformPortalService portalService;

    @GetMapping("/{traceCode}")
    public AjaxResult archive(@PathVariable String traceCode)
    {
        return success(portalService.getTraceArchive(traceCode));
    }

    @PostMapping("/{traceCode}/lost-found")
    public AjaxResult bindLostFound(@PathVariable String traceCode, @Validated @RequestBody UniformLostFoundBindRequest request)
    {
        request.setTraceCode(traceCode);
        return success(portalService.saveLostFoundBinding(traceCode, request));
    }
}
