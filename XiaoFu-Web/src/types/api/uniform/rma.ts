import type { BaseEntity, PageDomain } from "../common";

export interface UniformRmaOrderQueryParams extends PageDomain {
  rmaNo?: string;
  traceCode?: string;
  studentName?: string;
  requestType?: string;
  status?: string;
  params?: {
    beginTime?: string;
    endTime?: string;
  };
}

export interface UniformRmaOrder extends BaseEntity {
  rmaId?: number;
  rmaNo?: string;
  traceId?: number;
  traceCode?: string;
  workOrderId?: number;
  workOrderNo?: string;
  styleName?: string;
  schoolName?: string;
  className?: string;
  studentName?: string;
  requestType?: "1" | "2";
  reasonType?: "size_small" | "size_large" | "quality_issue" | "other";
  oldSizeCode?: string;
  newSizeCode?: string;
  applyQuantity?: number;
  status?: "0" | "1" | "2" | "3" | "8";
  applyTime?: string;
  receiveTime?: string;
  resendTrackingNo?: string;
  conclusion?: string;
}
