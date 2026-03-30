package com.xiaofu.system.service.uniform;

import java.util.List;
import com.xiaofu.system.domain.uniform.UniformSupplier;
import com.xiaofu.system.domain.uniform.UniformSupplierAlertSummary;

/**
 * 供应商主数据 Service
 */
public interface IUniformSupplierService
{
    public UniformSupplier selectSupplierById(Long supplierId);

    public List<UniformSupplier> selectSupplierList(UniformSupplier supplier);

    public boolean checkSupplierCodeUnique(UniformSupplier supplier);

    public int insertSupplier(UniformSupplier supplier);

    public int updateSupplier(UniformSupplier supplier);

    public int deleteSupplierByIds(Long[] supplierIds);

    public UniformSupplierAlertSummary selectSupplierAlertSummary();

    public List<UniformSupplier> selectAvailableSuppliers();
}
