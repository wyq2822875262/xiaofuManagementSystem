package com.xiaofu.system.domain.uniform;

import java.io.Serializable;
import com.xiaofu.common.annotation.Excel;
import com.xiaofu.common.annotation.Excel.ColumnType;
import com.xiaofu.common.xss.Xss;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 装箱明细对象 xf_shipment_package
 */
public class UniformShipmentPackage implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "装箱ID", cellType = ColumnType.NUMERIC)
    private Long packageId;

    private Long shipmentId;

    @Excel(name = "箱号")
    private String packageNo;

    @Excel(name = "性别", readConverterExp = "male=男,female=女,unisex=中性")
    private String gender;

    @Excel(name = "尺码")
    private String sizeCode;

    @Excel(name = "数量", cellType = ColumnType.NUMERIC)
    private Integer quantity;

    @Excel(name = "装箱状态", readConverterExp = "0=待装箱,1=已装箱,2=已出库")
    private String scanStatus;

    @Excel(name = "箱贴")
    private String boxLabel;

    @Excel(name = "起始溯源码")
    private String traceCodeStart;

    @Excel(name = "结束溯源码")
    private String traceCodeEnd;

    @Excel(name = "备注")
    private String remark;

    public Long getPackageId()
    {
        return packageId;
    }

    public void setPackageId(Long packageId)
    {
        this.packageId = packageId;
    }

    public Long getShipmentId()
    {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId)
    {
        this.shipmentId = shipmentId;
    }

    @Size(min = 0, max = 64, message = "箱号长度不能超过64个字符")
    public String getPackageNo()
    {
        return packageNo;
    }

    public void setPackageNo(String packageNo)
    {
        this.packageNo = packageNo;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    @Size(min = 0, max = 32, message = "尺码长度不能超过32个字符")
    public String getSizeCode()
    {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode)
    {
        this.sizeCode = sizeCode;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public String getScanStatus()
    {
        return scanStatus;
    }

    public void setScanStatus(String scanStatus)
    {
        this.scanStatus = scanStatus;
    }

    @Xss(message = "箱贴不能包含脚本字符")
    @Size(min = 0, max = 255, message = "箱贴长度不能超过255个字符")
    public String getBoxLabel()
    {
        return boxLabel;
    }

    public void setBoxLabel(String boxLabel)
    {
        this.boxLabel = boxLabel;
    }

    @Size(min = 0, max = 100, message = "起始溯源码长度不能超过100个字符")
    public String getTraceCodeStart()
    {
        return traceCodeStart;
    }

    public void setTraceCodeStart(String traceCodeStart)
    {
        this.traceCodeStart = traceCodeStart;
    }

    @Size(min = 0, max = 100, message = "结束溯源码长度不能超过100个字符")
    public String getTraceCodeEnd()
    {
        return traceCodeEnd;
    }

    public void setTraceCodeEnd(String traceCodeEnd)
    {
        this.traceCodeEnd = traceCodeEnd;
    }

    @Xss(message = "备注不能包含脚本字符")
    @Size(min = 0, max = 500, message = "备注长度不能超过500个字符")
    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("packageId", getPackageId())
            .append("shipmentId", getShipmentId())
            .append("packageNo", getPackageNo())
            .append("gender", getGender())
            .append("sizeCode", getSizeCode())
            .append("quantity", getQuantity())
            .append("scanStatus", getScanStatus())
            .append("boxLabel", getBoxLabel())
            .append("traceCodeStart", getTraceCodeStart())
            .append("traceCodeEnd", getTraceCodeEnd())
            .append("remark", getRemark())
            .toString();
    }
}
