import request from "@/utils/request";
import type { AjaxResult, TableDataInfo, UniformShipmentOrder, UniformShipmentOrderQueryParams } from "@/types";

export function listShipmentOrder(query: UniformShipmentOrderQueryParams): Promise<TableDataInfo<UniformShipmentOrder>> {
  return request({
    url: "/uniform/shipment/list",
    method: "get",
    params: query,
  });
}

export function getShipmentOrder(shipmentId: number): Promise<AjaxResult<UniformShipmentOrder>> {
  return request({
    url: "/uniform/shipment/" + shipmentId,
    method: "get",
  });
}

export function addShipmentOrder(data: UniformShipmentOrder): Promise<AjaxResult> {
  return request({
    url: "/uniform/shipment",
    method: "post",
    data,
  });
}

export function updateShipmentOrder(data: UniformShipmentOrder): Promise<AjaxResult> {
  return request({
    url: "/uniform/shipment",
    method: "put",
    data,
  });
}

export function delShipmentOrder(shipmentId: number | number[]): Promise<AjaxResult> {
  return request({
    url: "/uniform/shipment/" + shipmentId,
    method: "delete",
  });
}
