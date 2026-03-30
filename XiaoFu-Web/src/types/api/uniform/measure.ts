import type { BaseEntity, PageDomain } from "../common";

export interface UniformMeasureRecordQueryParams extends PageDomain {
  styleId?: number;
  schoolName?: string;
  className?: string;
  studentName?: string;
  measureStatus?: string;
  finalSizeCode?: string;
  params?: {
    beginTime?: string;
    endTime?: string;
  };
}

export interface UniformMeasureRecord extends BaseEntity {
  measureId?: number;
  styleId?: number;
  styleName?: string;
  schoolName?: string;
  campusName?: string;
  gradeName?: string;
  className?: string;
  studentName?: string;
  studentNo?: string;
  gender?: "male" | "female";
  heightValue?: number;
  weightValue?: number;
  sourceChannel?: "manual" | "wechat";
  measureTime?: string;
  recommendedSizeCode?: string;
  recommendedSizeName?: string;
  finalSizeCode?: string;
  finalSizeName?: string;
  measureStatus?: "0" | "1" | "2";
  bodyRemark?: string;
}
