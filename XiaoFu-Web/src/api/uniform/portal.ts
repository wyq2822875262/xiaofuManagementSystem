import request from "@/utils/request";
import type { AjaxResult, UniformLostFoundBindRequest, UniformLostFoundCard, UniformPortalTraceArchive } from "@/types";

export function getPortalTraceArchive(traceCode: string): Promise<AjaxResult<UniformPortalTraceArchive>> {
  return request({
    url: "/portal/trace/" + encodeURIComponent(traceCode),
    method: "get",
    headers: {
      isToken: false,
    },
  });
}

export function saveLostFoundBinding(traceCode: string, data: UniformLostFoundBindRequest): Promise<AjaxResult<UniformLostFoundCard>> {
  return request({
    url: "/portal/trace/" + encodeURIComponent(traceCode) + "/lost-found",
    method: "post",
    headers: {
      isToken: false,
    },
    data,
  });
}
