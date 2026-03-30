import type { BaseEntity, PageDomain } from "../common";

export interface UniformShipmentPackage {
  packageId?: number;
  shipmentId?: number;
  packageNo?: string;
  gender?: "male" | "female" | "unisex";
  sizeCode?: string;
  quantity?: number;
  scanStatus?: "0" | "1" | "2";
  boxLabel?: string;
  traceCodeStart?: string;
  traceCodeEnd?: string;
  remark?: string;
}

export interface UniformShipmentOrderQueryParams extends PageDomain {
  shipmentNo?: string;
  workOrderNo?: string;
  schoolName?: string;
  className?: string;
  shipmentStatus?: string;
  params?: {
    beginTime?: string;
    endTime?: string;
  };
}

export interface UniformShipmentOrder extends BaseEntity {
  shipmentId?: number;
  shipmentNo?: string;
  workOrderId?: number;
  workOrderNo?: string;
  styleName?: string;
  schoolName?: string;
  campusName?: string;
  gradeName?: string;
  className?: string;
  totalQuantity?: number;
  packedQuantity?: number;
  shipmentStatus?: "0" | "1" | "2" | "3" | "4";
  logisticsCompany?: string;
  logisticsNo?: string;
  dispatchTime?: string;
  signTime?: string;
  packageList?: UniformShipmentPackage[];
}
