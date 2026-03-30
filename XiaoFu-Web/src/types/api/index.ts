/**
 * API 类型统一导出
 */
export * from "./common";

// 登录模块
export * from "./login";
export * from "./menu";

// System 模块
export * from "./system/user";
export * from "./system/role";
export * from "./system/menu";
export * from "./system/dept";
export * from "./system/post";
export * from "./system/dict";
export * from "./system/config";
export * from "./system/notice";

// uniform 模块
export * from "./uniform/supplier";
export * from "./uniform/style";
export * from "./uniform/material";
export * from "./uniform/workorder";
export * from "./uniform/inspection";
export * from "./uniform/trace";
export * from "./uniform/measure";
export * from "./uniform/shipment";
export * from "./uniform/rma";
export * from "./uniform/portal";
export * from "./uniform/screen";

// monitor 模块
export * from "./monitor/cache";
export * from "./monitor/job";
export * from "./monitor/jobLog";
export * from "./monitor/logininfor";
export * from "./monitor/operlog";
export * from "./monitor/online";

// 代码生成模块
export * from "./tool/gen";
