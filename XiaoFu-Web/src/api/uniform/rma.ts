import request from "@/utils/request";
import type { AjaxResult, TableDataInfo, UniformRmaOrder, UniformRmaOrderQueryParams } from "@/types";

export function listRmaOrder(query: UniformRmaOrderQueryParams): Promise<TableDataInfo<UniformRmaOrder>> {
  return request({
    url: "/uniform/rma/list",
    method: "get",
    params: query,
  });
}

export function getRmaOrder(rmaId: number): Promise<AjaxResult<UniformRmaOrder>> {
  return request({
    url: "/uniform/rma/" + rmaId,
    method: "get",
  });
}

export function addRmaOrder(data: UniformRmaOrder): Promise<AjaxResult> {
  return request({
    url: "/uniform/rma",
    method: "post",
    data,
  });
}

export function updateRmaOrder(data: UniformRmaOrder): Promise<AjaxResult> {
  return request({
    url: "/uniform/rma",
    method: "put",
    data,
  });
}

export function delRmaOrder(rmaId: number | number[]): Promise<AjaxResult> {
  return request({
    url: "/uniform/rma/" + rmaId,
    method: "delete",
  });
}
