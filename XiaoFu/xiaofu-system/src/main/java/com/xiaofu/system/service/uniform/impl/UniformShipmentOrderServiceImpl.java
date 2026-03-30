package com.xiaofu.system.service.uniform.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xiaofu.common.constant.UserConstants;
import com.xiaofu.common.exception.ServiceException;
import com.xiaofu.common.utils.StringUtils;
import com.xiaofu.common.utils.uuid.Seq;
import com.xiaofu.system.domain.uniform.UniformShipmentOrder;
import com.xiaofu.system.domain.uniform.UniformShipmentPackage;
import com.xiaofu.system.domain.uniform.UniformWorkOrder;
import com.xiaofu.system.mapper.uniform.UniformShipmentOrderMapper;
import com.xiaofu.system.mapper.uniform.UniformShipmentPackageMapper;
import com.xiaofu.system.mapper.uniform.UniformWorkOrderMapper;
import com.xiaofu.system.service.uniform.IUniformShipmentOrderService;

@Service
public class UniformShipmentOrderServiceImpl implements IUniformShipmentOrderService
{
    @Autowired
    private UniformShipmentOrderMapper shipmentOrderMapper;

    @Autowired
    private UniformShipmentPackageMapper shipmentPackageMapper;

    @Autowired
    private UniformWorkOrderMapper workOrderMapper;

    @Override
    public UniformShipmentOrder selectShipmentOrderById(Long shipmentId)
    {
        UniformShipmentOrder shipmentOrder = shipmentOrderMapper.selectShipmentOrderById(shipmentId);
        if (StringUtils.isNotNull(shipmentOrder))
        {
            shipmentOrder.setPackageList(shipmentPackageMapper.selectShipmentPackageListByShipmentId(shipmentId));
        }
        return shipmentOrder;
    }

    @Override
    public List<UniformShipmentOrder> selectShipmentOrderList(UniformShipmentOrder shipmentOrder)
    {
        return shipmentOrderMapper.selectShipmentOrderList(shipmentOrder);
    }

    @Override
    public boolean checkShipmentNoUnique(UniformShipmentOrder shipmentOrder)
    {
        Long shipmentId = StringUtils.isNull(shipmentOrder.getShipmentId()) ? -1L : shipmentOrder.getShipmentId();
        UniformShipmentOrder info = shipmentOrderMapper.checkShipmentNoUnique(shipmentOrder.getShipmentNo());
        if (StringUtils.isNotNull(info) && info.getShipmentId().longValue() != shipmentId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertShipmentOrder(UniformShipmentOrder shipmentOrder)
    {
        fillDefaults(shipmentOrder);
        validateShipment(shipmentOrder);
        int rows = shipmentOrderMapper.insertShipmentOrder(shipmentOrder);
        saveShipmentPackages(shipmentOrder);
        refreshShipmentState(shipmentOrder.getShipmentId());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateShipmentOrder(UniformShipmentOrder shipmentOrder)
    {
        fillDefaults(shipmentOrder);
        validateShipment(shipmentOrder);
        int rows = shipmentOrderMapper.updateShipmentOrder(shipmentOrder);
        shipmentPackageMapper.deleteShipmentPackageByShipmentId(shipmentOrder.getShipmentId());
        saveShipmentPackages(shipmentOrder);
        refreshShipmentState(shipmentOrder.getShipmentId());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteShipmentOrderByIds(Long[] shipmentIds)
    {
        shipmentPackageMapper.deleteShipmentPackageByShipmentIds(shipmentIds);
        return shipmentOrderMapper.deleteShipmentOrderByIds(shipmentIds);
    }

    private void fillDefaults(UniformShipmentOrder shipmentOrder)
    {
        if (StringUtils.isEmpty(shipmentOrder.getShipmentNo()))
        {
            shipmentOrder.setShipmentNo("SH" + Seq.getId());
        }
        if (StringUtils.isEmpty(shipmentOrder.getShipmentStatus()))
        {
            shipmentOrder.setShipmentStatus("0");
        }
        if (shipmentOrder.getTotalQuantity() == null)
        {
            shipmentOrder.setTotalQuantity(0);
        }
        if (shipmentOrder.getPackedQuantity() == null)
        {
            shipmentOrder.setPackedQuantity(0);
        }
    }

    private void validateShipment(UniformShipmentOrder shipmentOrder)
    {
        UniformWorkOrder workOrder = workOrderMapper.selectWorkOrderById(shipmentOrder.getWorkOrderId());
        if (StringUtils.isNull(workOrder))
        {
            throw new ServiceException("关联工单不存在，请刷新后重试");
        }
        if (!"1".equals(workOrder.getTraceStatus()))
        {
            throw new ServiceException("该工单尚未完成赋码，不能进入分拣发运");
        }
    }

    private void saveShipmentPackages(UniformShipmentOrder shipmentOrder)
    {
        List<UniformShipmentPackage> packageList = normalizePackageList(shipmentOrder.getPackageList(), shipmentOrder);
        if (StringUtils.isEmpty(packageList))
        {
            return;
        }
        packageList.forEach(item -> item.setShipmentId(shipmentOrder.getShipmentId()));
        shipmentPackageMapper.batchInsertShipmentPackage(packageList);
    }

    private List<UniformShipmentPackage> normalizePackageList(List<UniformShipmentPackage> packageList, UniformShipmentOrder shipmentOrder)
    {
        if (StringUtils.isEmpty(packageList))
        {
            return new ArrayList<>();
        }

        List<UniformShipmentPackage> normalized = new ArrayList<>();
        for (UniformShipmentPackage item : packageList)
        {
            if (item == null || item.getQuantity() == null || item.getQuantity() <= 0)
            {
                continue;
            }
            if (StringUtils.isEmpty(item.getPackageNo()))
            {
                item.setPackageNo("PK" + Seq.getId());
            }
            if (StringUtils.isEmpty(item.getScanStatus()))
            {
                item.setScanStatus("0");
            }
            if (StringUtils.isEmpty(item.getBoxLabel()))
            {
                item.setBoxLabel(buildBoxLabel(shipmentOrder, item));
            }
            normalized.add(item);
        }
        return normalized;
    }

    private String buildBoxLabel(UniformShipmentOrder shipmentOrder, UniformShipmentPackage shipmentPackage)
    {
        String genderLabel = "male".equals(shipmentPackage.getGender()) ? "男装"
            : "female".equals(shipmentPackage.getGender()) ? "女装" : "中性";
        String sizeLabel = StringUtils.isEmpty(shipmentPackage.getSizeCode()) ? "混码" : shipmentPackage.getSizeCode();
        String gradeName = StringUtils.isEmpty(shipmentOrder.getGradeName()) ? "" : shipmentOrder.getGradeName();
        String className = StringUtils.isEmpty(shipmentOrder.getClassName()) ? "" : shipmentOrder.getClassName();
        return gradeName + className + " / " + genderLabel + " / " + sizeLabel + " / " + shipmentPackage.getQuantity() + "件";
    }

    private void refreshShipmentState(Long shipmentId)
    {
        UniformShipmentOrder shipmentOrder = shipmentOrderMapper.selectShipmentOrderById(shipmentId);
        if (StringUtils.isNull(shipmentOrder))
        {
            return;
        }

        List<UniformShipmentPackage> packageList = shipmentPackageMapper.selectShipmentPackageListByShipmentId(shipmentId);
        int totalQuantity = 0;
        int packedQuantity = 0;
        for (UniformShipmentPackage item : packageList)
        {
            int quantity = item.getQuantity() == null ? 0 : item.getQuantity();
            totalQuantity += quantity;
            if ("1".equals(item.getScanStatus()) || "2".equals(item.getScanStatus()))
            {
                packedQuantity += quantity;
            }
        }

        String shipmentStatus = "0";
        if (shipmentOrder.getSignTime() != null)
        {
            shipmentStatus = "4";
        }
        else if (shipmentOrder.getDispatchTime() != null)
        {
            shipmentStatus = "3";
        }
        else if (totalQuantity > 0 && packedQuantity >= totalQuantity)
        {
            shipmentStatus = "2";
        }
        else if (packedQuantity > 0)
        {
            shipmentStatus = "1";
        }

        shipmentOrderMapper.updateShipmentFlowFields(shipmentId, totalQuantity, packedQuantity, shipmentStatus);
    }
}
