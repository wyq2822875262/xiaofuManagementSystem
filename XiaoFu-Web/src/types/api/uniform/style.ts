import type { BaseEntity, PageDomain } from "../common";

export interface UniformStyleQueryParams extends PageDomain {
  styleCode?: string;
  styleName?: string;
  season?: string;
  styleType?: string;
  gender?: string;
  status?: string;
  params?: {
    beginTime?: string;
    endTime?: string;
  };
}

export interface UniformStyleSize {
  sizeId?: number;
  styleId?: number;
  sizeCode?: string;
  sizeName?: string;
  specialType?: "0" | "1" | "2" | "3";
  heightMin?: number;
  heightMax?: number;
  weightMin?: number;
  weightMax?: number;
  sortOrder?: number;
}

export interface UniformStyleBom {
  bomId?: number;
  styleId?: number;
  materialType?: "1" | "2" | "3";
  materialName?: string;
  materialSpec?: string;
  materialComposition?: string;
  dosage?: number;
  dosageUnit?: string;
  supplierId?: number;
  supplierName?: string;
  traceRequired?: "0" | "1";
  sortOrder?: number;
  remark?: string;
}

export interface UniformStyle extends BaseEntity {
  styleId?: number;
  styleCode?: string;
  styleName?: string;
  season?: "spring_autumn" | "summer" | "winter";
  styleType?: "sportswear" | "uniform" | "ceremonial" | "outerwear" | "other";
  gender?: "unisex" | "male" | "female";
  fabricName?: string;
  fabricComposition?: string;
  codeMode?: "1" | "2";
  specialBodySupport?: "0" | "1";
  careInstructions?: string;
  status?: "0" | "1";
  sizeCount?: number;
  bomCount?: number;
  sizeSummary?: string;
  sizeList?: UniformStyleSize[];
  bomList?: UniformStyleBom[];
}
