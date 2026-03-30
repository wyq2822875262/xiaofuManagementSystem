export interface UniformPortalProcessNode {
  processType?: string;
  processName?: string;
  operatorName?: string;
  inspectorName?: string;
  deviceType?: string;
  startTime?: string;
  endTime?: string;
  passQuantity?: number;
  defectiveQuantity?: number;
  remark?: string;
}

export interface UniformLostFoundCard {
  bound?: boolean;
  traceCode?: string;
  schoolName?: string;
  className?: string;
  studentName?: string;
  contactName?: string;
  maskedContactPhone?: string;
  ownerRemark?: string;
  bindTime?: string;
}

export interface UniformLostFoundBindRequest {
  schoolName?: string;
  className?: string;
  studentName?: string;
  contactName?: string;
  contactPhone?: string;
  ownerRemark?: string;
}

export interface UniformPortalTraceArchive {
  traceId?: number;
  traceCode?: string;
  traceStatus?: string;
  codeMode?: "1" | "2";
  styleName?: string;
  season?: string;
  styleType?: string;
  schoolName?: string;
  sourceOrderNo?: string;
  workOrderNo?: string;
  batchNo?: string;
  supplierName?: string;
  materialName?: string;
  fabricName?: string;
  fabricComposition?: string;
  colorName?: string;
  gramWeight?: number;
  vatNo?: string;
  factoryDate?: string;
  materialInspectDate?: string;
  materialReportNo?: string;
  materialReportFileUrl?: string;
  inspectionNo?: string;
  gbStandard?: string;
  inspectionTime?: string;
  inspectionReportFileUrl?: string;
  formaldehyde?: number;
  phValue?: number;
  odorResult?: string;
  amineResult?: string;
  colorFastness?: number;
  pillingGrade?: number;
  conclusion?: string;
  careInstructions?: string;
  careTips?: string[];
  processList?: UniformPortalProcessNode[];
  lostFoundCard?: UniformLostFoundCard;
}
