import request from "@/utils/request";
import type { AjaxResult, TableDataInfo, UniformMaterialBatch, UniformMaterialBatchQueryParams } from "@/types";

export function listMaterialBatch(query: UniformMaterialBatchQueryParams): Promise<TableDataInfo<UniformMaterialBatch>> {
  return request({
    url: "/uniform/material/list",
    method: "get",
    params: query,
  });
}

export function getMaterialBatch(batchId: number): Promise<AjaxResult<UniformMaterialBatch>> {
  return request({
    url: "/uniform/material/" + batchId,
    method: "get",
  });
}

export function addMaterialBatch(data: UniformMaterialBatch): Promise<AjaxResult> {
  return request({
    url: "/uniform/material",
    method: "post",
    data,
  });
}

export function updateMaterialBatch(data: UniformMaterialBatch): Promise<AjaxResult> {
  return request({
    url: "/uniform/material",
    method: "put",
    data,
  });
}

export function delMaterialBatch(batchId: number | number[]): Promise<AjaxResult> {
  return request({
    url: "/uniform/material/" + batchId,
    method: "delete",
  });
}

export function listMaterialBatchOptions(): Promise<AjaxResult<UniformMaterialBatch[]>> {
  return request({
    url: "/uniform/material/options",
    method: "get",
  });
}
