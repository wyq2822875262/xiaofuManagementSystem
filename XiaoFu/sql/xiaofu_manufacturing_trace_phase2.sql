-- 云裳校服模块二：云裳智造与核心溯源链路
-- 包含：原料批次、生产工单、工序打卡、成品质检、溯源码、菜单权限与演示数据

-- ----------------------------
-- 1、原料批次表
-- ----------------------------
create table if not exists xf_material_batch (
  batch_id            bigint(20)      not null auto_increment comment '批次ID',
  batch_no            varchar(64)     not null                comment '内部批次号',
  supplier_id         bigint(20)      not null                comment '供应商ID',
  style_id            bigint(20)      default null            comment '关联款式ID',
  material_type       char(1)         not null default '1'    comment '物料类型（1主面料 2辅料）',
  material_name       varchar(100)    not null                comment '物料名称',
  vat_no              varchar(64)     default ''              comment '缸号',
  gram_weight         decimal(10,2)   default null            comment '克重(g/m2)',
  fabric_composition  varchar(255)    default ''              comment '面料成分',
  color_name          varchar(50)     default ''              comment '颜色',
  width_value         decimal(10,2)   default null            comment '门幅(cm)',
  inbound_quantity    decimal(10,2)   default null            comment '入库数量',
  quantity_unit       varchar(20)     default '米'            comment '数量单位',
  report_no           varchar(100)    default ''              comment '检测报告编号',
  report_file_url     varchar(500)    default ''              comment '检测报告PDF',
  inspect_date        date            default null            comment '检测日期',
  status              char(1)         default '0'             comment '状态（0待投产 1生产中 2已用完 3锁定）',
  create_by           varchar(64)     default ''              comment '创建者',
  create_time         datetime                                comment '创建时间',
  update_by           varchar(64)     default ''              comment '更新者',
  update_time         datetime                                comment '更新时间',
  remark              varchar(500)    default null            comment '备注',
  primary key (batch_id),
  unique key uk_xf_material_batch_no (batch_no),
  key idx_xf_material_batch_supplier_id (supplier_id),
  key idx_xf_material_batch_style_id (style_id),
  key idx_xf_material_batch_status (status),
  key idx_xf_material_batch_type (material_type)
) engine=innodb comment='校服原料批次表';

-- ----------------------------
-- 2、生产工单表
-- ----------------------------
create table if not exists xf_work_order (
  work_order_id           bigint(20)      not null auto_increment comment '工单ID',
  work_order_no           varchar(64)     not null                comment '工单号',
  source_order_no         varchar(64)     default ''              comment '客户订单号',
  school_name             varchar(100)    default ''              comment '学校/客户名称',
  style_id                bigint(20)      not null                comment '款式ID',
  material_batch_id       bigint(20)      not null                comment '原料批次ID',
  planned_quantity        int(11)         not null default 0      comment '计划数量',
  completed_quantity      int(11)         default 0               comment '完工数量',
  code_mode               char(1)         default '1'             comment '赋码模式（1一物一码 2一批一码）',
  order_status            char(1)         default '0'             comment '工单状态（0待开工 1裁剪中 2缝纫中 3熨烫中 4待质检 5质检合格 6已赋码 8质检拦截）',
  qa_status               char(1)         default '0'             comment '质检状态（0待检 1合格 2不合格）',
  trace_status            char(1)         default '0'             comment '赋码状态（0未生成 1已生成）',
  trace_generated_count   int(11)         default 0               comment '已生成溯源码数量',
  planned_start_date      date            default null            comment '计划开工日',
  planned_end_date        date            default null            comment '计划完工日',
  actual_end_time         datetime                                comment '实际完工时间',
  create_by               varchar(64)     default ''              comment '创建者',
  create_time             datetime                                comment '创建时间',
  update_by               varchar(64)     default ''              comment '更新者',
  update_time             datetime                                comment '更新时间',
  remark                  varchar(500)    default null            comment '备注',
  primary key (work_order_id),
  unique key uk_xf_work_order_no (work_order_no),
  key idx_xf_work_order_style_id (style_id),
  key idx_xf_work_order_material_batch_id (material_batch_id),
  key idx_xf_work_order_status (order_status),
  key idx_xf_work_order_qa_status (qa_status),
  key idx_xf_work_order_trace_status (trace_status)
) engine=innodb comment='校服生产工单表';

-- ----------------------------
-- 3、工序打卡表
-- ----------------------------
create table if not exists xf_process_record (
  record_id            bigint(20)      not null auto_increment comment '记录ID',
  work_order_id        bigint(20)      not null                comment '工单ID',
  process_type         varchar(20)     not null                comment '工序类型（cutting裁剪 sewing缝纫 ironing熨烫）',
  operator_name        varchar(50)     default ''              comment '责任人',
  inspector_name       varchar(50)     default ''              comment '质检员',
  device_type          varchar(20)     default 'manual'        comment '设备来源（tablet平板 scanner扫码枪 manual手工录入）',
  start_time           datetime                                comment '开始时间',
  end_time             datetime                                comment '结束时间',
  pass_quantity        int(11)         default 0               comment '合格数量',
  defective_quantity   int(11)         default 0               comment '不良数量',
  remark               varchar(500)    default null            comment '备注',
  create_time          datetime                                comment '创建时间',
  primary key (record_id),
  key idx_xf_process_record_work_order_id (work_order_id),
  key idx_xf_process_record_process_type (process_type),
  key idx_xf_process_record_start_time (start_time)
) engine=innodb comment='校服工序打卡表';

-- ----------------------------
-- 4、成品质检表
-- ----------------------------
create table if not exists xf_quality_inspection (
  inspection_id       bigint(20)      not null auto_increment comment '质检ID',
  inspection_no       varchar(64)     not null                comment '质检单号',
  work_order_id       bigint(20)      not null                comment '工单ID',
  gb_standard         varchar(50)     default 'GB/T 31888-2015' comment '执行标准',
  sample_quantity     int(11)         not null default 1      comment '抽检数量',
  formaldehyde        decimal(10,2)   default null            comment '甲醛含量',
  ph_value            decimal(10,2)   default null            comment 'PH值',
  odor_result         char(1)         default '0'             comment '异味判定（0无异味 1有异味）',
  amine_result        char(1)         default '0'             comment '芳香胺判定（0合格 1不合格）',
  color_fastness      decimal(10,1)   default null            comment '色牢度',
  pilling_grade       decimal(10,1)   default null            comment '起球率',
  result              char(1)         default '0'             comment '质检结果（0合格 1不合格）',
  intercept_status    char(1)         default '0'             comment '拦截状态（0放行 1拦截）',
  fail_reason         varchar(500)    default ''              comment '不合格原因',
  inspector_name      varchar(50)     default ''              comment '质检员',
  inspect_time        datetime                                comment '质检时间',
  report_file_url     varchar(500)    default ''              comment '质检报告PDF',
  conclusion          varchar(500)    default ''              comment '结论说明',
  create_by           varchar(64)     default ''              comment '创建者',
  create_time         datetime                                comment '创建时间',
  update_by           varchar(64)     default ''              comment '更新者',
  update_time         datetime                                comment '更新时间',
  remark              varchar(500)    default null            comment '备注',
  primary key (inspection_id),
  unique key uk_xf_quality_inspection_no (inspection_no),
  key idx_xf_quality_inspection_work_order_id (work_order_id),
  key idx_xf_quality_inspection_result (result),
  key idx_xf_quality_inspection_intercept_status (intercept_status),
  key idx_xf_quality_inspection_inspect_time (inspect_time)
) engine=innodb comment='校服成品质检表';

-- ----------------------------
-- 5、溯源码表
-- ----------------------------
create table if not exists xf_trace_code (
  trace_id           bigint(20)      not null auto_increment comment '溯源码ID',
  work_order_id      bigint(20)      not null                comment '工单ID',
  inspection_id      bigint(20)      not null                comment '质检ID',
  code_mode          char(1)         default '1'             comment '赋码模式（1一物一码 2一批一码）',
  serial_no          int(11)         default 1               comment '序号',
  trace_code         varchar(100)    not null                comment '溯源码',
  qr_content         varchar(500)    default ''              comment '二维码内容',
  print_status       char(1)         default '0'             comment '打印状态（0未打印 1已打印）',
  trace_status       char(1)         default '0'             comment '状态（0有效 1作废）',
  create_by          varchar(64)     default ''              comment '创建者',
  create_time        datetime                                comment '创建时间',
  update_by          varchar(64)     default ''              comment '更新者',
  update_time        datetime                                comment '更新时间',
  remark             varchar(500)    default null            comment '备注',
  primary key (trace_id),
  unique key uk_xf_trace_code_code (trace_code),
  key idx_xf_trace_code_work_order_id (work_order_id),
  key idx_xf_trace_code_inspection_id (inspection_id),
  key idx_xf_trace_code_code_mode (code_mode),
  key idx_xf_trace_code_status (trace_status)
) engine=innodb comment='校服溯源码表';

-- ----------------------------
-- 6、演示数据
-- ----------------------------
insert into xf_material_batch (
  batch_id, batch_no, supplier_id, style_id, material_type, material_name, vat_no, gram_weight,
  fabric_composition, color_name, width_value, inbound_quantity, quantity_unit, report_no, report_file_url,
  inspect_date, status, create_by, create_time, update_by, update_time, remark
) values
  (2001, 'MB-FAB-202603-001', 1, 1, '1', '运动服主面料-藏青', 'VAT-202603-01', 260.00,
   '聚酯纤维95%，氨纶5%', '藏青色', 150.00, 860.00, '米', 'FAB-QA-202603-001', '',
   date_sub(curdate(), interval 8 day), '1', 'admin', sysdate(), 'admin', sysdate(), '示例：正在投产的主面料批次'),
  (2002, 'MB-FAB-202603-002', 1, 1, '1', '运动服主面料-天蓝', 'VAT-202603-02', 255.00,
   '聚酯纤维95%，氨纶5%', '天蓝色', 148.00, 520.00, '米', 'FAB-QA-202603-002', '',
   date_sub(curdate(), interval 18 day), '2', 'admin', sysdate(), 'admin', sysdate(), '示例：已完成工单并赋码的批次'),
  (2003, 'MB-ACC-202603-003', 2, 1, '2', '反光条辅料批次', 'ACC-LOT-03', null,
   '涤纶反光材料', '银灰色', null, 1200.00, '条', 'ACC-QA-202603-003', '',
   date_sub(curdate(), interval 5 day), '0', 'admin', sysdate(), 'admin', sysdate(), '示例：待投产的辅料批次'),
  (2004, 'MB-FAB-202603-004', 1, 1, '1', '运动服主面料-红白撞色', 'VAT-202603-04', 268.00,
   '聚酯纤维92%，氨纶8%', '红白拼色', 152.00, 430.00, '米', 'FAB-QA-202603-004', '',
   date_sub(curdate(), interval 3 day), '3', 'admin', sysdate(), 'admin', sysdate(), '示例：锁定批次，不可继续投产')
on duplicate key update
  batch_no = values(batch_no),
  supplier_id = values(supplier_id),
  style_id = values(style_id),
  material_type = values(material_type),
  material_name = values(material_name),
  vat_no = values(vat_no),
  gram_weight = values(gram_weight),
  fabric_composition = values(fabric_composition),
  color_name = values(color_name),
  width_value = values(width_value),
  inbound_quantity = values(inbound_quantity),
  quantity_unit = values(quantity_unit),
  report_no = values(report_no),
  report_file_url = values(report_file_url),
  inspect_date = values(inspect_date),
  status = values(status),
  update_by = values(update_by),
  update_time = values(update_time),
  remark = values(remark);

insert into xf_work_order (
  work_order_id, work_order_no, source_order_no, school_name, style_id, material_batch_id,
  planned_quantity, completed_quantity, code_mode, order_status, qa_status, trace_status,
  trace_generated_count, planned_start_date, planned_end_date, actual_end_time,
  create_by, create_time, update_by, update_time, remark
) values
  (2101, 'WO-202603-001', 'PO-SCHOOL-001', '朝阳实验学校', 1, 2001,
   600, 600, '1', '5', '1', '0',
   0, date_sub(curdate(), interval 4 day), date_add(curdate(), interval 1 day), null,
   'admin', sysdate(), 'admin', sysdate(), '示例：已完工待赋码工单'),
  (2102, 'WO-202603-002', 'PO-SCHOOL-002', '北塔中学', 1, 2002,
   480, 480, '2', '6', '1', '1',
   1, date_sub(curdate(), interval 16 day), date_sub(curdate(), interval 9 day), date_sub(now(), interval 7 day),
   'admin', sysdate(), 'admin', sysdate(), '示例：已完成质检并生成一批一码'),
  (2103, 'WO-202603-003', 'PO-SCHOOL-003', '龙城小学', 1, 2001,
   720, 420, '1', '2', '0', '0',
   0, curdate(), date_add(curdate(), interval 5 day), null,
   'admin', sysdate(), 'admin', sysdate(), '示例：缝纫进行中的工单'),
  (2104, 'WO-202603-004', 'PO-SCHOOL-004', '双塔一中', 1, 2001,
   360, 360, '1', '8', '2', '0',
   0, date_sub(curdate(), interval 6 day), date_sub(curdate(), interval 1 day), date_sub(now(), interval 2 day),
   'admin', sysdate(), 'admin', sysdate(), '示例：质检拦截工单')
on duplicate key update
  work_order_no = values(work_order_no),
  source_order_no = values(source_order_no),
  school_name = values(school_name),
  style_id = values(style_id),
  material_batch_id = values(material_batch_id),
  planned_quantity = values(planned_quantity),
  completed_quantity = values(completed_quantity),
  code_mode = values(code_mode),
  order_status = values(order_status),
  qa_status = values(qa_status),
  trace_status = values(trace_status),
  trace_generated_count = values(trace_generated_count),
  planned_start_date = values(planned_start_date),
  planned_end_date = values(planned_end_date),
  actual_end_time = values(actual_end_time),
  update_by = values(update_by),
  update_time = values(update_time),
  remark = values(remark);

insert into xf_process_record (
  record_id, work_order_id, process_type, operator_name, inspector_name, device_type,
  start_time, end_time, pass_quantity, defective_quantity, remark, create_time
) values
  (2401, 2101, 'cutting', '王师傅', '周检', 'tablet', date_sub(now(), interval 80 hour), date_sub(now(), interval 74 hour), 600, 8, '裁片尺寸抽检通过', sysdate()),
  (2402, 2101, 'sewing', '李班长', '周检', 'scanner', date_sub(now(), interval 72 hour), date_sub(now(), interval 36 hour), 600, 5, '缝纫针距复检通过', sysdate()),
  (2403, 2101, 'ironing', '孙师傅', '周检', 'tablet', date_sub(now(), interval 35 hour), date_sub(now(), interval 12 hour), 600, 2, '整烫完毕待质检', sysdate()),
  (2404, 2102, 'cutting', '赵师傅', '刘检', 'tablet', date_sub(now(), interval 280 hour), date_sub(now(), interval 272 hour), 480, 3, '裁剪完成', sysdate()),
  (2405, 2102, 'sewing', '钱班长', '刘检', 'scanner', date_sub(now(), interval 270 hour), date_sub(now(), interval 228 hour), 480, 2, '缝纫完成', sysdate()),
  (2406, 2102, 'ironing', '吴师傅', '刘检', 'manual', date_sub(now(), interval 226 hour), date_sub(now(), interval 196 hour), 480, 1, '整烫完成并入质检', sysdate()),
  (2407, 2103, 'cutting', '郑师傅', '何检', 'scanner', date_sub(now(), interval 20 hour), date_sub(now(), interval 12 hour), 420, 6, '裁剪完成', sysdate()),
  (2408, 2103, 'sewing', '冯班长', '何检', 'tablet', date_sub(now(), interval 10 hour), null, 420, 4, '缝纫进行中', sysdate()),
  (2409, 2104, 'cutting', '陈师傅', '杨检', 'tablet', date_sub(now(), interval 120 hour), date_sub(now(), interval 112 hour), 360, 4, '裁剪完成', sysdate()),
  (2410, 2104, 'sewing', '褚班长', '杨检', 'scanner', date_sub(now(), interval 110 hour), date_sub(now(), interval 82 hour), 360, 6, '缝纫完成', sysdate()),
  (2411, 2104, 'ironing', '卫师傅', '杨检', 'manual', date_sub(now(), interval 80 hour), date_sub(now(), interval 58 hour), 360, 3, '整烫完成待检', sysdate())
on duplicate key update
  work_order_id = values(work_order_id),
  process_type = values(process_type),
  operator_name = values(operator_name),
  inspector_name = values(inspector_name),
  device_type = values(device_type),
  start_time = values(start_time),
  end_time = values(end_time),
  pass_quantity = values(pass_quantity),
  defective_quantity = values(defective_quantity),
  remark = values(remark),
  create_time = values(create_time);

insert into xf_quality_inspection (
  inspection_id, inspection_no, work_order_id, gb_standard, sample_quantity, formaldehyde, ph_value,
  odor_result, amine_result, color_fastness, pilling_grade, result, intercept_status, fail_reason,
  inspector_name, inspect_time, report_file_url, conclusion, create_by, create_time, update_by, update_time, remark
) values
  (2201, 'QA-202603-001', 2101, 'GB/T 31888-2015', 20, 28.00, 6.60,
   '0', '0', 4.0, 4.5, '0', '0', '',
   '周检', date_sub(now(), interval 10 hour), '', '符合 GB/T 31888-2015 要求，可放行赋码', 'admin', sysdate(), 'admin', sysdate(), '示例：质检合格'),
  (2202, 'QA-202603-002', 2102, 'GB/T 31888-2015', 16, 24.00, 6.90,
   '0', '0', 4.0, 4.0, '0', '0', '',
   '刘检', date_sub(now(), interval 180 hour), '', '符合 GB/T 31888-2015 要求，可放行赋码', 'admin', sysdate(), 'admin', sysdate(), '示例：已赋码工单对应质检'),
  (2203, 'QA-202603-003', 2104, 'GB/T 31888-2015', 12, 92.00, 8.90,
   '1', '1', 2.5, 2.5, '1', '1', '甲醛含量超标；PH值不在4.0-8.5范围内；存在异味；可分解致癌芳香胺染料不合格；色牢度低于3级；起球率低于3级',
   '杨检', date_sub(now(), interval 48 hour), '', '存在不合格项，系统已自动拦截赋码', 'admin', sysdate(), 'admin', sysdate(), '示例：质检拦截')
on duplicate key update
  inspection_no = values(inspection_no),
  work_order_id = values(work_order_id),
  gb_standard = values(gb_standard),
  sample_quantity = values(sample_quantity),
  formaldehyde = values(formaldehyde),
  ph_value = values(ph_value),
  odor_result = values(odor_result),
  amine_result = values(amine_result),
  color_fastness = values(color_fastness),
  pilling_grade = values(pilling_grade),
  result = values(result),
  intercept_status = values(intercept_status),
  fail_reason = values(fail_reason),
  inspector_name = values(inspector_name),
  inspect_time = values(inspect_time),
  report_file_url = values(report_file_url),
  conclusion = values(conclusion),
  update_by = values(update_by),
  update_time = values(update_time),
  remark = values(remark);

insert into xf_trace_code (
  trace_id, work_order_id, inspection_id, code_mode, serial_no, trace_code, qr_content,
  print_status, trace_status, create_by, create_time, update_by, update_time, remark
) values
  (2301, 2102, 2202, '2', 1, 'BT202603300001',
   'XFTRACE|BT202603300001|WO-202603-002|MB-FAB-202603-002|QA-202603-002',
   '0', '0', 'admin', sysdate(), 'admin', sysdate(), '示例：一批一码溯源码')
on duplicate key update
  work_order_id = values(work_order_id),
  inspection_id = values(inspection_id),
  code_mode = values(code_mode),
  serial_no = values(serial_no),
  trace_code = values(trace_code),
  qr_content = values(qr_content),
  print_status = values(print_status),
  trace_status = values(trace_status),
  update_by = values(update_by),
  update_time = values(update_time),
  remark = values(remark);

-- ----------------------------
-- 7、菜单与权限
-- ----------------------------
insert into sys_menu values
  (3100, '校服业务', '0', '5', 'uniform', null, '', '', 1, 0, 'M', '0', '0', '', 'shopping', 'admin', sysdate(), '', null, '校服业务目录')
on duplicate key update
  menu_name = values(menu_name),
  parent_id = values(parent_id),
  order_num = values(order_num),
  path = values(path),
  component = values(component),
  is_frame = values(is_frame),
  is_cache = values(is_cache),
  menu_type = values(menu_type),
  visible = values(visible),
  status = values(status),
  perms = values(perms),
  icon = values(icon),
  update_by = 'admin',
  update_time = sysdate(),
  remark = values(remark);

delete from sys_menu where menu_id in (
  3103, 3104, 3105, 3106,
  3300, 3301, 3302, 3303, 3304, 3305,
  3310, 3311, 3312, 3313, 3314, 3315,
  3320, 3321, 3322, 3323, 3324, 3325,
  3330, 3331, 3332, 3333
);

insert into sys_menu values
  (3103, '原料批次', '3100', '3', 'material', 'uniform/material/index', '', '', 1, 0, 'C', '0', '0', 'uniform:material:list', 'table', 'admin', sysdate(), '', null, '原料批次管理'),
  (3104, '生产工单', '3100', '4', 'workorder', 'uniform/workorder/index', '', '', 1, 0, 'C', '0', '0', 'uniform:workorder:list', 'job', 'admin', sysdate(), '', null, '生产工单管理'),
  (3105, '成品质检', '3100', '5', 'inspection', 'uniform/inspection/index', '', '', 1, 0, 'C', '0', '0', 'uniform:inspection:list', 'clipboard', 'admin', sysdate(), '', null, '成品质检管理'),
  (3106, '溯源码', '3100', '6', 'trace', 'uniform/trace/index', '', '', 1, 0, 'C', '0', '0', 'uniform:trace:list', 'guide', 'admin', sysdate(), '', null, '溯源码管理'),

  (3300, '原料批次查询', '3103', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:material:query', '#', 'admin', sysdate(), '', null, ''),
  (3301, '原料批次新增', '3103', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:material:add', '#', 'admin', sysdate(), '', null, ''),
  (3302, '原料批次修改', '3103', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:material:edit', '#', 'admin', sysdate(), '', null, ''),
  (3303, '原料批次删除', '3103', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:material:remove', '#', 'admin', sysdate(), '', null, ''),
  (3304, '原料批次导出', '3103', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:material:export', '#', 'admin', sysdate(), '', null, ''),
  (3305, '原料批次列表', '3103', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:material:list', '#', 'admin', sysdate(), '', null, ''),

  (3310, '工单查询', '3104', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:workorder:query', '#', 'admin', sysdate(), '', null, ''),
  (3311, '工单新增', '3104', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:workorder:add', '#', 'admin', sysdate(), '', null, ''),
  (3312, '工单修改', '3104', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:workorder:edit', '#', 'admin', sysdate(), '', null, ''),
  (3313, '工单删除', '3104', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:workorder:remove', '#', 'admin', sysdate(), '', null, ''),
  (3314, '工单导出', '3104', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:workorder:export', '#', 'admin', sysdate(), '', null, ''),
  (3315, '工单列表', '3104', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:workorder:list', '#', 'admin', sysdate(), '', null, ''),

  (3320, '质检查询', '3105', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:inspection:query', '#', 'admin', sysdate(), '', null, ''),
  (3321, '质检新增', '3105', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:inspection:add', '#', 'admin', sysdate(), '', null, ''),
  (3322, '质检修改', '3105', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:inspection:edit', '#', 'admin', sysdate(), '', null, ''),
  (3323, '质检删除', '3105', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:inspection:remove', '#', 'admin', sysdate(), '', null, ''),
  (3324, '质检导出', '3105', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:inspection:export', '#', 'admin', sysdate(), '', null, ''),
  (3325, '质检列表', '3105', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:inspection:list', '#', 'admin', sysdate(), '', null, ''),

  (3330, '溯源码生成', '3106', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:trace:add', '#', 'admin', sysdate(), '', null, ''),
  (3331, '溯源码删除', '3106', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:trace:remove', '#', 'admin', sysdate(), '', null, ''),
  (3332, '溯源码导出', '3106', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:trace:export', '#', 'admin', sysdate(), '', null, ''),
  (3333, '溯源码列表', '3106', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:trace:list', '#', 'admin', sysdate(), '', null, '');

-- 提示：
-- 1. 请在执行本脚本后重启后端，再刷新前端菜单缓存。
-- 2. 如果使用非 admin 账号，请在“角色管理”中为对应角色勾选上述模块二菜单与按钮权限。
