package com.xiaofu.system.domain.uniform;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaofu.common.annotation.Excel;
import com.xiaofu.common.annotation.Excel.ColumnType;
import com.xiaofu.common.core.domain.BaseEntity;
import com.xiaofu.common.xss.Xss;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 供应商主数据对象 xf_supplier
 */
public class UniformSupplier extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 供应商ID */
    @Excel(name = "供应商ID", cellType = ColumnType.NUMERIC)
    private Long supplierId;

    /** 供应商编码 */
    @Excel(name = "供应商编码")
    private String supplierCode;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 供应商类型 */
    @Excel(name = "供应商类型", readConverterExp = "fabric=面料供应商,accessory=辅料供应商,processing=加工厂,logistics=物流商,other=其他")
    private String supplierType;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactPerson;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactPhone;

    /** 地址 */
    private String address;

    /** ISO 证书编号 */
    @Excel(name = "ISO9001证书编号")
    private String isoCertNo;

    /** ISO 证书到期日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "ISO9001到期日", width = 18, dateFormat = "yyyy-MM-dd")
    private Date isoExpireDate;

    /** ISO 证书附件 */
    private String isoCertFileUrl;

    /** 环保证书编号 */
    @Excel(name = "环保证书编号")
    private String envCertNo;

    /** 环保证书到期日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "环保认证到期日", width = 18, dateFormat = "yyyy-MM-dd")
    private Date envExpireDate;

    /** 环保证书附件 */
    private String envCertFileUrl;

    /** 质检报告编号 */
    @Excel(name = "质检报告编号")
    private String qualityReportNo;

    /** 质检报告到期日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "质检报告到期日", width = 18, dateFormat = "yyyy-MM-dd")
    private Date qualityReportExpireDate;

    /** 质检报告附件 */
    private String qualityReportFileUrl;

    /** 预警提前天数 */
    @Excel(name = "预警天数", cellType = ColumnType.NUMERIC)
    private Integer warningDays;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 最近到期日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最近到期日", width = 18, dateFormat = "yyyy-MM-dd")
    private Date nearestExpireDate;

    /** 预警状态 */
    @Excel(name = "预警状态", readConverterExp = "0=正常,1=30天内到期,2=已过期")
    private String alertStatus;

    /** 触发预警的证照 */
    @Excel(name = "预警证照")
    private String alertCertificates;

    public Long getSupplierId()
    {
        return supplierId;
    }

    public void setSupplierId(Long supplierId)
    {
        this.supplierId = supplierId;
    }

    @Xss(message = "供应商编码不能包含脚本字符")
    @NotBlank(message = "供应商编码不能为空")
    @Size(min = 0, max = 64, message = "供应商编码长度不能超过64个字符")
    public String getSupplierCode()
    {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode;
    }

    @Xss(message = "供应商名称不能包含脚本字符")
    @NotBlank(message = "供应商名称不能为空")
    @Size(min = 0, max = 100, message = "供应商名称长度不能超过100个字符")
    public String getSupplierName()
    {
        return supplierName;
    }

    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }

    @NotBlank(message = "供应商类型不能为空")
    @Size(min = 0, max = 32, message = "供应商类型长度不能超过32个字符")
    public String getSupplierType()
    {
        return supplierType;
    }

    public void setSupplierType(String supplierType)
    {
        this.supplierType = supplierType;
    }

    @Xss(message = "联系人不能包含脚本字符")
    @Size(min = 0, max = 30, message = "联系人长度不能超过30个字符")
    public String getContactPerson()
    {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson)
    {
        this.contactPerson = contactPerson;
    }

    @Size(min = 0, max = 20, message = "联系电话长度不能超过20个字符")
    public String getContactPhone()
    {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }

    @Xss(message = "地址不能包含脚本字符")
    @Size(min = 0, max = 255, message = "地址长度不能超过255个字符")
    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    @Size(min = 0, max = 100, message = "ISO证书编号长度不能超过100个字符")
    public String getIsoCertNo()
    {
        return isoCertNo;
    }

    public void setIsoCertNo(String isoCertNo)
    {
        this.isoCertNo = isoCertNo;
    }

    public Date getIsoExpireDate()
    {
        return isoExpireDate;
    }

    public void setIsoExpireDate(Date isoExpireDate)
    {
        this.isoExpireDate = isoExpireDate;
    }

    @Size(min = 0, max = 500, message = "ISO证书附件地址长度不能超过500个字符")
    public String getIsoCertFileUrl()
    {
        return isoCertFileUrl;
    }

    public void setIsoCertFileUrl(String isoCertFileUrl)
    {
        this.isoCertFileUrl = isoCertFileUrl;
    }

    @Size(min = 0, max = 100, message = "环保认证编号长度不能超过100个字符")
    public String getEnvCertNo()
    {
        return envCertNo;
    }

    public void setEnvCertNo(String envCertNo)
    {
        this.envCertNo = envCertNo;
    }

    public Date getEnvExpireDate()
    {
        return envExpireDate;
    }

    public void setEnvExpireDate(Date envExpireDate)
    {
        this.envExpireDate = envExpireDate;
    }

    @Size(min = 0, max = 500, message = "环保认证附件地址长度不能超过500个字符")
    public String getEnvCertFileUrl()
    {
        return envCertFileUrl;
    }

    public void setEnvCertFileUrl(String envCertFileUrl)
    {
        this.envCertFileUrl = envCertFileUrl;
    }

    @Size(min = 0, max = 100, message = "质检报告编号长度不能超过100个字符")
    public String getQualityReportNo()
    {
        return qualityReportNo;
    }

    public void setQualityReportNo(String qualityReportNo)
    {
        this.qualityReportNo = qualityReportNo;
    }

    public Date getQualityReportExpireDate()
    {
        return qualityReportExpireDate;
    }

    public void setQualityReportExpireDate(Date qualityReportExpireDate)
    {
        this.qualityReportExpireDate = qualityReportExpireDate;
    }

    @Size(min = 0, max = 500, message = "质检报告附件地址长度不能超过500个字符")
    public String getQualityReportFileUrl()
    {
        return qualityReportFileUrl;
    }

    public void setQualityReportFileUrl(String qualityReportFileUrl)
    {
        this.qualityReportFileUrl = qualityReportFileUrl;
    }

    @NotNull(message = "预警天数不能为空")
    public Integer getWarningDays()
    {
        return warningDays;
    }

    public void setWarningDays(Integer warningDays)
    {
        this.warningDays = warningDays;
    }

    @NotBlank(message = "状态不能为空")
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getNearestExpireDate()
    {
        return nearestExpireDate;
    }

    public void setNearestExpireDate(Date nearestExpireDate)
    {
        this.nearestExpireDate = nearestExpireDate;
    }

    public String getAlertStatus()
    {
        return alertStatus;
    }

    public void setAlertStatus(String alertStatus)
    {
        this.alertStatus = alertStatus;
    }

    public String getAlertCertificates()
    {
        return alertCertificates;
    }

    public void setAlertCertificates(String alertCertificates)
    {
        this.alertCertificates = alertCertificates;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("supplierId", getSupplierId())
            .append("supplierCode", getSupplierCode())
            .append("supplierName", getSupplierName())
            .append("supplierType", getSupplierType())
            .append("contactPerson", getContactPerson())
            .append("contactPhone", getContactPhone())
            .append("address", getAddress())
            .append("isoCertNo", getIsoCertNo())
            .append("isoExpireDate", getIsoExpireDate())
            .append("envCertNo", getEnvCertNo())
            .append("envExpireDate", getEnvExpireDate())
            .append("qualityReportNo", getQualityReportNo())
            .append("qualityReportExpireDate", getQualityReportExpireDate())
            .append("warningDays", getWarningDays())
            .append("status", getStatus())
            .append("nearestExpireDate", getNearestExpireDate())
            .append("alertStatus", getAlertStatus())
            .append("alertCertificates", getAlertCertificates())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
