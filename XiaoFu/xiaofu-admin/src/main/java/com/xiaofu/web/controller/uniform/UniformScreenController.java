package com.xiaofu.web.controller.uniform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xiaofu.common.core.controller.BaseController;
import com.xiaofu.common.core.domain.AjaxResult;
import com.xiaofu.system.service.uniform.IUniformScreenService;

/**
 * 校服监管舱大屏
 */
@RestController
@RequestMapping("/uniform/screen")
public class UniformScreenController extends BaseController
{
    @Autowired
    private IUniformScreenService screenService;

    @PreAuthorize("@ss.hasPermi('uniform:screen:view')")
    @GetMapping("/overview")
    public AjaxResult overview()
    {
        return success(screenService.getOverview());
    }
}
