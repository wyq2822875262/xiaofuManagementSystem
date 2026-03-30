import request from "@/utils/request";
import type { AjaxResult, TableDataInfo, UniformMeasureRecord, UniformMeasureRecordQueryParams } from "@/types";

export function listMeasureRecord(query: UniformMeasureRecordQueryParams): Promise<TableDataInfo<UniformMeasureRecord>> {
  return request({
    url: "/uniform/measure/list",
    method: "get",
    params: query,
  });
}

export function getMeasureRecord(measureId: number): Promise<AjaxResult<UniformMeasureRecord>> {
  return request({
    url: "/uniform/measure/" + measureId,
    method: "get",
  });
}

export function recommendMeasureRecord(data: UniformMeasureRecord): Promise<AjaxResult<UniformMeasureRecord>> {
  return request({
    url: "/uniform/measure/recommend",
    method: "post",
    data,
  });
}

export function addMeasureRecord(data: UniformMeasureRecord): Promise<AjaxResult> {
  return request({
    url: "/uniform/measure",
    method: "post",
    data,
  });
}

export function updateMeasureRecord(data: UniformMeasureRecord): Promise<AjaxResult> {
  return request({
    url: "/uniform/measure",
    method: "put",
    data,
  });
}

export function delMeasureRecord(measureId: number | number[]): Promise<AjaxResult> {
  return request({
    url: "/uniform/measure/" + measureId,
    method: "delete",
  });
}
