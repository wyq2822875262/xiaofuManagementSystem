import request from "@/utils/request";
import type { AjaxResult, TableDataInfo, UniformQualityInspection, UniformQualityInspectionQueryParams } from "@/types";

export function listInspection(query: UniformQualityInspectionQueryParams): Promise<TableDataInfo<UniformQualityInspection>> {
  return request({
    url: "/uniform/inspection/list",
    method: "get",
    params: query,
  });
}

export function getInspection(inspectionId: number): Promise<AjaxResult<UniformQualityInspection>> {
  return request({
    url: "/uniform/inspection/" + inspectionId,
    method: "get",
  });
}

export function addInspection(data: UniformQualityInspection): Promise<AjaxResult> {
  return request({
    url: "/uniform/inspection",
    method: "post",
    data,
  });
}

export function updateInspection(data: UniformQualityInspection): Promise<AjaxResult> {
  return request({
    url: "/uniform/inspection",
    method: "put",
    data,
  });
}

export function delInspection(inspectionId: number | number[]): Promise<AjaxResult> {
  return request({
    url: "/uniform/inspection/" + inspectionId,
    method: "delete",
  });
}
