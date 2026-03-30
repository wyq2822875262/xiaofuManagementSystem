import type { BaseEntity, PageDomain } from "../common";

export interface UniformSupplierQueryParams extends PageDomain {
  supplierCode?: string;
  supplierName?: string;
  supplierType?: string;
  alertStatus?: string;
  status?: string;
  params?: {
    beginTime?: string;
    endTime?: string;
  };
}

export interface UniformSupplier extends BaseEntity {
  supplierId?: number;
  supplierCode?: string;
  supplierName?: string;
  supplierType?: string;
  contactPerson?: string;
  contactPhone?: string;
  address?: string;
  isoCertNo?: string;
  isoExpireDate?: string;
  isoCertFileUrl?: string;
  envCertNo?: string;
  envExpireDate?: string;
  envCertFileUrl?: string;
  qualityReportNo?: string;
  qualityReportExpireDate?: string;
  qualityReportFileUrl?: string;
  warningDays?: number;
  status?: "0" | "1";
  nearestExpireDate?: string;
  alertStatus?: "0" | "1" | "2";
  alertCertificates?: string;
}

export interface UniformSupplierAlertSummary {
  totalCount?: number;
  normalCount?: number;
  warningCount?: number;
  expiredCount?: number;
}
