package com.xiaofu.system.task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.xiaofu.common.utils.DateUtils;
import com.xiaofu.system.domain.SysNotice;
import com.xiaofu.system.domain.uniform.UniformSupplier;
import com.xiaofu.system.domain.uniform.UniformSupplierAlertSummary;
import com.xiaofu.system.service.ISysNoticeService;
import com.xiaofu.system.service.uniform.IUniformSupplierService;

/**
 * 供应商资质预警任务
 */
@Component("uniformSupplierTask")
public class UniformSupplierTask
{
    private static final Logger log = LoggerFactory.getLogger(UniformSupplierTask.class);

    @Autowired
    private IUniformSupplierService supplierService;

    @Autowired
    private ISysNoticeService noticeService;

    public void scanCertificateAlerts()
    {
        UniformSupplierAlertSummary summary = supplierService.selectSupplierAlertSummary();
        int warningCount = summary.getWarningCount() == null ? 0 : summary.getWarningCount();
        int expiredCount = summary.getExpiredCount() == null ? 0 : summary.getExpiredCount();
        if (warningCount == 0 && expiredCount == 0)
        {
            log.info("供应商资质预警扫描完成，当前无待处理预警");
            return;
        }

        String title = "供应商资质预警提醒（" + DateUtils.getDate() + "）";
        if (existsNotice(title))
        {
            log.info("供应商资质预警通知已生成，无需重复写入，title={}", title);
            return;
        }

        SysNotice notice = new SysNotice();
        notice.setNoticeTitle(title);
        notice.setNoticeType("1");
        notice.setStatus("0");
        notice.setCreateBy("system");
        notice.setNoticeContent(buildNoticeContent(summary));
        noticeService.insertNotice(notice);
        log.warn("供应商资质预警扫描完成，即将到期{}条，已过期{}条", warningCount, expiredCount);
    }

    private boolean existsNotice(String title)
    {
        SysNotice condition = new SysNotice();
        condition.setNoticeTitle(title);
        List<SysNotice> notices = noticeService.selectNoticeList(condition);
        return notices.stream().anyMatch(item -> title.equals(item.getNoticeTitle()));
    }

    private String buildNoticeContent(UniformSupplierAlertSummary summary)
    {
        UniformSupplier query = new UniformSupplier();
        query.setStatus("0");
        List<UniformSupplier> suppliers = supplierService.selectSupplierList(query);
        List<UniformSupplier> alertSuppliers = suppliers.stream()
            .filter(item -> "1".equals(item.getAlertStatus()) || "2".equals(item.getAlertStatus()))
            .limit(10)
            .collect(Collectors.toList());

        List<String> details = new ArrayList<>();
        for (UniformSupplier item : alertSuppliers)
        {
            String expireDate = item.getNearestExpireDate() == null ? "未维护" : DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getNearestExpireDate());
            details.add("<li>" + item.getSupplierName() + " - " + item.getAlertCertificates() + "，最近到期日：" + expireDate + "</li>");
        }

        StringBuilder content = new StringBuilder();
        content.append("<p>供应商资质预警扫描结果：</p>");
        content.append("<p>30天内到期 <strong>").append(summary.getWarningCount()).append("</strong> 条，已过期 <strong>")
            .append(summary.getExpiredCount()).append("</strong> 条。</p>");
        if (!details.isEmpty())
        {
            content.append("<ul>").append(String.join("", details)).append("</ul>");
        }
        return content.toString();
    }
}
