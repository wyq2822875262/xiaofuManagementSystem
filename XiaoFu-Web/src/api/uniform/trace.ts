import request from "@/utils/request";
import type { AjaxResult, TableDataInfo, UniformTraceCode, UniformTraceCodeQueryParams, UniformTraceGenerateRequest } from "@/types";

export function listTraceCode(query: UniformTraceCodeQueryParams): Promise<TableDataInfo<UniformTraceCode>> {
  return request({
    url: "/uniform/trace/list",
    method: "get",
    params: query,
  });
}

export function generateTraceCode(data: UniformTraceGenerateRequest): Promise<AjaxResult> {
  return request({
    url: "/uniform/trace/generate",
    method: "post",
    data,
  });
}

export function listTraceCodeOptions(): Promise<AjaxResult<UniformTraceCode[]>> {
  return request({
    url: "/uniform/trace/options",
    method: "get",
  });
}

export function delTraceCode(traceId: number | number[]): Promise<AjaxResult> {
  return request({
    url: "/uniform/trace/" + traceId,
    method: "delete",
  });
}
