import type { BaseEntity, PageDomain } from "../common";

export interface UniformMaterialBatchQueryParams extends PageDomain {
  batchNo?: string;
  supplierId?: number;
  materialType?: string;
  materialName?: string;
  status?: string;
  params?: {
    beginTime?: string;
    endTime?: string;
  };
}

export interface UniformMaterialBatch extends BaseEntity {
  batchId?: number;
  batchNo?: string;
  supplierId?: number;
  supplierName?: string;
  styleId?: number;
  styleName?: string;
  materialType?: "1" | "2";
  materialName?: string;
  vatNo?: string;
  gramWeight?: number;
  fabricComposition?: string;
  colorName?: string;
  widthValue?: number;
  inboundQuantity?: number;
  quantityUnit?: string;
  reportNo?: string;
  reportFileUrl?: string;
  inspectDate?: string;
  status?: "0" | "1" | "2" | "3";
}
