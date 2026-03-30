import type { BaseEntity, PageDomain } from "../common";

export interface UniformProcessRecord {
  recordId?: number;
  workOrderId?: number;
  processType?: "cutting" | "sewing" | "ironing";
  operatorName?: string;
  inspectorName?: string;
  deviceType?: "tablet" | "scanner" | "manual";
  startTime?: string;
  endTime?: string;
  passQuantity?: number;
  defectiveQuantity?: number;
  remark?: string;
}

export interface UniformWorkOrderQueryParams extends PageDomain {
  workOrderNo?: string;
  sourceOrderNo?: string;
  schoolName?: string;
  orderStatus?: string;
  qaStatus?: string;
  traceStatus?: string;
  params?: {
    beginTime?: string;
    endTime?: string;
  };
}

export interface UniformWorkOrder extends BaseEntity {
  workOrderId?: number;
  workOrderNo?: string;
  sourceOrderNo?: string;
  schoolName?: string;
  styleId?: number;
  styleName?: string;
  materialBatchId?: number;
  materialBatchNo?: string;
  plannedQuantity?: number;
  completedQuantity?: number;
  codeMode?: "1" | "2";
  orderStatus?: "0" | "1" | "2" | "3" | "4" | "5" | "6" | "8";
  qaStatus?: "0" | "1" | "2";
  traceStatus?: "0" | "1";
  traceGeneratedCount?: number;
  plannedStartDate?: string;
  plannedEndDate?: string;
  actualEndTime?: string;
  processList?: UniformProcessRecord[];
}
