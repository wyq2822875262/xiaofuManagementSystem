import type { BaseEntity, PageDomain } from "../common";

export interface UniformQualityInspectionQueryParams extends PageDomain {
  inspectionNo?: string;
  workOrderNo?: string;
  result?: string;
  interceptStatus?: string;
  params?: {
    beginTime?: string;
    endTime?: string;
  };
}

export interface UniformQualityInspection extends BaseEntity {
  inspectionId?: number;
  inspectionNo?: string;
  workOrderId?: number;
  workOrderNo?: string;
  styleName?: string;
  batchNo?: string;
  gbStandard?: string;
  sampleQuantity?: number;
  formaldehyde?: number;
  phValue?: number;
  odorResult?: "0" | "1";
  amineResult?: "0" | "1";
  colorFastness?: number;
  pillingGrade?: number;
  result?: "0" | "1";
  interceptStatus?: "0" | "1";
  failReason?: string;
  inspectorName?: string;
  inspectTime?: string;
  reportFileUrl?: string;
  conclusion?: string;
}
