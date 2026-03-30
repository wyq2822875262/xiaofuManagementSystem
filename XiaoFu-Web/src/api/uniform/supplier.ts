import request from "@/utils/request";
import type { AjaxResult, TableDataInfo, UniformSupplier, UniformSupplierAlertSummary, UniformSupplierQueryParams } from "@/types";

export function listSupplier(query: UniformSupplierQueryParams): Promise<TableDataInfo<UniformSupplier>> {
  return request({
    url: "/uniform/supplier/list",
    method: "get",
    params: query,
  });
}

export function getSupplier(supplierId: number): Promise<AjaxResult<UniformSupplier>> {
  return request({
    url: "/uniform/supplier/" + supplierId,
    method: "get",
  });
}

export function addSupplier(data: UniformSupplier): Promise<AjaxResult> {
  return request({
    url: "/uniform/supplier",
    method: "post",
    data,
  });
}

export function updateSupplier(data: UniformSupplier): Promise<AjaxResult> {
  return request({
    url: "/uniform/supplier",
    method: "put",
    data,
  });
}

export function delSupplier(supplierId: number | number[]): Promise<AjaxResult> {
  return request({
    url: "/uniform/supplier/" + supplierId,
    method: "delete",
  });
}

export function getSupplierAlertSummary(): Promise<AjaxResult<UniformSupplierAlertSummary>> {
  return request({
    url: "/uniform/supplier/alertSummary",
    method: "get",
  });
}

export function listSupplierOptions(): Promise<AjaxResult<UniformSupplier[]>> {
  return request({
    url: "/uniform/supplier/options",
    method: "get",
  });
}
