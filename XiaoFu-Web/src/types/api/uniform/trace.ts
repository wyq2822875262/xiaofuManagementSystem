import type { BaseEntity, PageDomain } from "../common";

export interface UniformTraceCodeQueryParams extends PageDomain {
  workOrderNo?: string;
  traceCode?: string;
  codeMode?: string;
  printStatus?: string;
  traceStatus?: string;
  params?: {
    beginTime?: string;
    endTime?: string;
  };
}

export interface UniformTraceCode extends BaseEntity {
  traceId?: number;
  workOrderId?: number;
  workOrderNo?: string;
  inspectionId?: number;
  styleName?: string;
  batchNo?: string;
  codeMode?: "1" | "2";
  serialNo?: number;
  traceCode?: string;
  qrContent?: string;
  printStatus?: "0" | "1";
  traceStatus?: "0" | "1";
}

export interface UniformTraceGenerateRequest {
  workOrderId?: number;
  generateCount?: number;
}
