import request from "@/utils/request";
import type { AjaxResult, TableDataInfo, UniformWorkOrder, UniformWorkOrderQueryParams } from "@/types";

export function listWorkOrder(query: UniformWorkOrderQueryParams): Promise<TableDataInfo<UniformWorkOrder>> {
  return request({
    url: "/uniform/workorder/list",
    method: "get",
    params: query,
  });
}

export function getWorkOrder(workOrderId: number): Promise<AjaxResult<UniformWorkOrder>> {
  return request({
    url: "/uniform/workorder/" + workOrderId,
    method: "get",
  });
}

export function addWorkOrder(data: UniformWorkOrder): Promise<AjaxResult> {
  return request({
    url: "/uniform/workorder",
    method: "post",
    data,
  });
}

export function updateWorkOrder(data: UniformWorkOrder): Promise<AjaxResult> {
  return request({
    url: "/uniform/workorder",
    method: "put",
    data,
  });
}

export function delWorkOrder(workOrderId: number | number[]): Promise<AjaxResult> {
  return request({
    url: "/uniform/workorder/" + workOrderId,
    method: "delete",
  });
}

export function listWorkOrderOptions(): Promise<AjaxResult<UniformWorkOrder[]>> {
  return request({
    url: "/uniform/workorder/options",
    method: "get",
  });
}
