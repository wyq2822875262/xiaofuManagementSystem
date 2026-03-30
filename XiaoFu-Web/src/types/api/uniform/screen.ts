export interface UniformScreenMetricCard {
  code?: string;
  label?: string;
  value?: string;
  unit?: string;
  description?: string;
}

export interface UniformScreenIndicator {
  name?: string;
  value?: number;
}

export interface UniformScreenInspectionSnapshot {
  inspectionId?: number;
  inspectionNo?: string;
  schoolName?: string;
  styleName?: string;
  batchNo?: string;
  gbStandard?: string;
  result?: "0" | "1";
  conclusion?: string;
  inspectTime?: string;
  formaldehyde?: number;
  phValue?: number;
  odorResult?: "0" | "1";
  amineResult?: "0" | "1";
  colorFastness?: number;
  pillingGrade?: number;
}

export interface UniformScreenStageStat {
  stageCode?: string;
  stageLabel?: string;
  value?: number;
}

export interface UniformScreenFlowLine {
  routeName?: string;
  sourceName?: string;
  sourceLng?: number;
  sourceLat?: number;
  targetName?: string;
  targetLng?: number;
  targetLat?: number;
  shipmentQuantity?: number;
  packedQuantity?: number;
  progressRate?: number;
}

export interface UniformScreenProgressItem {
  schoolName?: string;
  totalQuantity?: number;
  packedQuantity?: number;
  signedQuantity?: number;
  progressRate?: number;
  signRate?: number;
  shipmentStatus?: string;
  statusLabel?: string;
}

export interface UniformScreenWordCloudItem {
  name?: string;
  value?: number;
}

export interface UniformScreenPieItem {
  name?: string;
  value?: number;
}

export interface UniformScreenOverview {
  seasonCode?: string;
  seasonLabel?: string;
  refreshedAt?: string;
  latestInspection?: UniformScreenInspectionSnapshot;
  metricCards?: UniformScreenMetricCard[];
  qualityRadar?: UniformScreenIndicator[];
  productionFunnel?: UniformScreenStageStat[];
  logisticsFlows?: UniformScreenFlowLine[];
  deliveryProgress?: UniformScreenProgressItem[];
  feedbackCloud?: UniformScreenWordCloudItem[];
  bodyTypeDistribution?: UniformScreenPieItem[];
}
