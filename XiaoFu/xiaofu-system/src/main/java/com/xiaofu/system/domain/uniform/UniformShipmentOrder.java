package com.xiaofu.system.domain.uniform;

import java.util.Date;
import java.util.List;
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
 * 班级分拣发运单对象 xf_shipment_order
 */
public class UniformShipmentOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "发运单ID", cellType = ColumnType.NUMERIC)
    private Long shipmentId;

    @Excel(name = "发运单号")
    private String shipmentNo;

    private Long workOrderId;

    @Excel(name = "工单号")
    private String workOrderNo;

    @Excel(name = "款式名称")
    private String styleName;

    @Excel(name = "学校")
    private String schoolName;

    @Excel(name = "校区")
    private String campusName;

    @Excel(name = "年级")
    private String gradeName;

    @Excel(name = "班级")
    private String className;

    @Excel(name = "总件数", cellType = ColumnType.NUMERIC)
    private Integer totalQuantity;

    @Excel(name = "已装箱件数", cellType = ColumnType.NUMERIC)
    private Integer packedQuantity;

    @Excel(name = "履约状态", readConverterExp = "0=待分拣,1=分拣中,2=待发货,3=已发货,4=已签收")
    private String shipmentStatus;

    @Excel(name = "物流公司")
    private String logisticsCompany;

    @Excel(name = "物流单号")
    private String logisticsNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发货时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date dispatchTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "签收时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date signTime;

    private List<UniformShipmentPackage> packageList;

    public Long getShipmentId()
    {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId)
    {
        this.shipmentId = shipmentId;
    }

    @Size(min = 0, max = 64, message = "发运单号长度不能超过64个字符")
    public String getShipmentNo()
    {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo)
    {
        this.shipmentNo = shipmentNo;
    }

    @NotNull(message = "工单不能为空")
    public Long getWorkOrderId()
    {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId)
    {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderNo()
    {
        return workOrderNo;
    }

    public void setWorkOrderNo(String workOrderNo)
    {
        this.workOrderNo = workOrderNo;
    }

    public String getStyleName()
    {
        return styleName;
    }

    public void setStyleName(String styleName)
    {
        this.styleName = styleName;
    }

    @Xss(message = "学校名称不能包含脚本字符")
    @NotBlank(message = "学校不能为空")
    @Size(min = 0, max = 100, message = "学校名称长度不能超过100个字符")
    public String getSchoolName()
    {
        return schoolName;
    }

    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }

    @Xss(message = "校区名称不能包含脚本字符")
    @Size(min = 0, max = 100, message = "校区名称长度不能超过100个字符")
    public String getCampusName()
    {
        return campusName;
    }

    public void setCampusName(String campusName)
    {
        this.campusName = campusName;
    }

    @Xss(message = "年级名称不能包含脚本字符")
    @Size(min = 0, max = 50, message = "年级名称长度不能超过50个字符")
    public String getGradeName()
    {
        return gradeName;
    }

    public void setGradeName(String gradeName)
    {
        this.gradeName = gradeName;
    }

    @Xss(message = "班级名称不能包含脚本字符")
    @NotBlank(message = "班级不能为空")
    @Size(min = 0, max = 50, message = "班级名称长度不能超过50个字符")
    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public Integer getTotalQuantity()
    {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity)
    {
        this.totalQuantity = totalQuantity;
    }

    public Integer getPackedQuantity()
    {
        return packedQuantity;
    }

    public void setPackedQuantity(Integer packedQuantity)
    {
        this.packedQuantity = packedQuantity;
    }

    public String getShipmentStatus()
    {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus)
    {
        this.shipmentStatus = shipmentStatus;
    }

    @Size(min = 0, max = 100, message = "物流公司长度不能超过100个字符")
    public String getLogisticsCompany()
    {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany)
    {
        this.logisticsCompany = logisticsCompany;
    }

    @Size(min = 0, max = 100, message = "物流单号长度不能超过100个字符")
    public String getLogisticsNo()
    {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo)
    {
        this.logisticsNo = logisticsNo;
    }

    public Date getDispatchTime()
    {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime)
    {
        this.dispatchTime = dispatchTime;
    }

    public Date getSignTime()
    {
        return signTime;
    }

    public void setSignTime(Date signTime)
    {
        this.signTime = signTime;
    }

    public List<UniformShipmentPackage> getPackageList()
    {
        return packageList;
    }

    public void setPackageList(List<UniformShipmentPackage> packageList)
    {
        this.packageList = packageList;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("shipmentId", getShipmentId())
            .append("shipmentNo", getShipmentNo())
            .append("workOrderId", getWorkOrderId())
            .append("workOrderNo", getWorkOrderNo())
            .append("styleName", getStyleName())
            .append("schoolName", getSchoolName())
            .append("campusName", getCampusName())
            .append("gradeName", getGradeName())
            .append("className", getClassName())
            .append("totalQuantity", getTotalQuantity())
            .append("packedQuantity", getPackedQuantity())
            .append("shipmentStatus", getShipmentStatus())
            .append("logisticsCompany", getLogisticsCompany())
            .append("logisticsNo", getLogisticsNo())
            .append("dispatchTime", getDispatchTime())
            .append("signTime", getSignTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
