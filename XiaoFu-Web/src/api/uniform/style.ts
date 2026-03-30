import request from "@/utils/request";
import type { AjaxResult, TableDataInfo, UniformStyle, UniformStyleQueryParams } from "@/types";

export function listStyle(query: UniformStyleQueryParams): Promise<TableDataInfo<UniformStyle>> {
  return request({
    url: "/uniform/style/list",
    method: "get",
    params: query,
  });
}

export function getStyle(styleId: number): Promise<AjaxResult<UniformStyle>> {
  return request({
    url: "/uniform/style/" + styleId,
    method: "get",
  });
}

export function addStyle(data: UniformStyle): Promise<AjaxResult> {
  return request({
    url: "/uniform/style",
    method: "post",
    data,
  });
}

export function updateStyle(data: UniformStyle): Promise<AjaxResult> {
  return request({
    url: "/uniform/style",
    method: "put",
    data,
  });
}

export function delStyle(styleId: number | number[]): Promise<AjaxResult> {
  return request({
    url: "/uniform/style/" + styleId,
    method: "delete",
  });
}

export function listStyleOptions(): Promise<AjaxResult<UniformStyle[]>> {
  return request({
    url: "/uniform/style/options",
    method: "get",
  });
}
