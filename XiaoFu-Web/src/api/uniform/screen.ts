import request from "@/utils/request";
import type { AjaxResult, UniformScreenOverview } from "@/types";

export function getUniformScreenOverview(): Promise<AjaxResult<UniformScreenOverview>> {
  return request({
    url: "/uniform/screen/overview",
    method: "get",
  });
}
