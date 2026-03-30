package com.xiaofu.system.service.uniform.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaofu.common.constant.UserConstants;
import com.xiaofu.common.utils.StringUtils;
import com.xiaofu.system.domain.uniform.UniformSupplier;
import com.xiaofu.system.domain.uniform.UniformSupplierAlertSummary;
import com.xiaofu.system.mapper.uniform.UniformSupplierMapper;
import com.xiaofu.system.service.uniform.IUniformSupplierService;

/**
 * 供应商主数据 Service 实现
 */
@Service
public class UniformSupplierServiceImpl implements IUniformSupplierService
{
    private static final String ALERT_NORMAL = "0";

    private static final String ALERT_WARNING = "1";

    private static final String ALERT_EXPIRED = "2";

    @Autowired
    private UniformSupplierMapper supplierMapper;

    @Override
    public UniformSupplier selectSupplierById(Long supplierId)
    {
        UniformSupplier supplier = supplierMapper.selectSupplierById(supplierId);
        if (StringUtils.isNotNull(supplier))
        {
            fillAlertInfo(supplier);
        }
        return supplier;
    }

    @Override
    public List<UniformSupplier> selectSupplierList(UniformSupplier supplier)
    {
        List<UniformSupplier> list = supplierMapper.selectSupplierList(supplier);
        list.forEach(this::fillAlertInfo);
        return list;
    }

    @Override
    public boolean checkSupplierCodeUnique(UniformSupplier supplier)
    {
        Long supplierId = StringUtils.isNull(supplier.getSupplierId()) ? -1L : supplier.getSupplierId();
        UniformSupplier info = supplierMapper.checkSupplierCodeUnique(supplier.getSupplierCode());
        if (StringUtils.isNotNull(info) && info.getSupplierId().longValue() != supplierId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int insertSupplier(UniformSupplier supplier)
    {
        return supplierMapper.insertSupplier(supplier);
    }

    @Override
    public int updateSupplier(UniformSupplier supplier)
    {
        return supplierMapper.updateSupplier(supplier);
    }

    @Override
    public int deleteSupplierByIds(Long[] supplierIds)
    {
        return supplierMapper.deleteSupplierByIds(supplierIds);
    }

    @Override
    public UniformSupplierAlertSummary selectSupplierAlertSummary()
    {
        UniformSupplierAlertSummary summary = supplierMapper.selectSupplierAlertSummary();
        if (summary == null)
        {
            summary = new UniformSupplierAlertSummary();
        }
        summary.setTotalCount(defaultNumber(summary.getTotalCount()));
        summary.setNormalCount(defaultNumber(summary.getNormalCount()));
        summary.setWarningCount(defaultNumber(summary.getWarningCount()));
        summary.setExpiredCount(defaultNumber(summary.getExpiredCount()));
        return summary;
    }

    @Override
    public List<UniformSupplier> selectAvailableSuppliers()
    {
        return supplierMapper.selectAvailableSuppliers();
    }

    private void fillAlertInfo(UniformSupplier supplier)
    {
        int warningDays = supplier.getWarningDays() == null || supplier.getWarningDays() <= 0 ? 30 : supplier.getWarningDays();
        LocalDate today = LocalDate.now();
        Date nearestExpireDate = null;
        String alertStatus = ALERT_NORMAL;
        List<String> alertCertificates = new ArrayList<>();

        nearestExpireDate = earlierDate(nearestExpireDate, supplier.getIsoExpireDate());
        nearestExpireDate = earlierDate(nearestExpireDate, supplier.getEnvExpireDate());
        nearestExpireDate = earlierDate(nearestExpireDate, supplier.getQualityReportExpireDate());

        alertStatus = resolveAlertStatus("ISO9001", supplier.getIsoExpireDate(), today, warningDays, alertStatus, alertCertificates);
        alertStatus = resolveAlertStatus("环保认证", supplier.getEnvExpireDate(), today, warningDays, alertStatus, alertCertificates);
        alertStatus = resolveAlertStatus("质检报告", supplier.getQualityReportExpireDate(), today, warningDays, alertStatus, alertCertificates);

        supplier.setNearestExpireDate(nearestExpireDate);
        supplier.setAlertStatus(alertStatus);
        supplier.setAlertCertificates(String.join("、", alertCertificates));
    }

    private String resolveAlertStatus(String certificateName, Date expireDate, LocalDate today, int warningDays, String currentStatus,
            List<String> alertCertificates)
    {
        if (expireDate == null)
        {
            return currentStatus;
        }
        long days = ChronoUnit.DAYS.between(today, toLocalDate(expireDate));
        if (days < 0)
        {
            alertCertificates.add(certificateName);
            return ALERT_EXPIRED;
        }
        if (days <= warningDays)
        {
            alertCertificates.add(certificateName);
            if (!ALERT_EXPIRED.equals(currentStatus))
            {
                return ALERT_WARNING;
            }
        }
        return currentStatus;
    }

    private Date earlierDate(Date current, Date candidate)
    {
        if (candidate == null)
        {
            return current;
        }
        if (current == null || candidate.before(current))
        {
            return candidate;
        }
        return current;
    }

    private LocalDate toLocalDate(Date date)
    {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Integer defaultNumber(Integer value)
    {
        return value == null ? 0 : value;
    }
}
