-- 云裳校服模块三：智慧仓储与柔性履约
-- 包含：量体测码、班级分拣发运、退换货RMA、菜单权限与演示数据

-- ----------------------------
-- 1、量体测码表
-- ----------------------------
create table if not exists xf_measure_record (
  measure_id               bigint(20)      not null auto_increment comment '量体ID',
  style_id                 bigint(20)      not null                comment '款式ID',
  school_name              varchar(100)    not null                comment '学校名称',
  campus_name              varchar(100)    default ''              comment '校区名称',
  grade_name               varchar(50)     default ''              comment '年级名称',
  class_name               varchar(50)     not null                comment '班级名称',
  student_name             varchar(50)     not null                comment '学生姓名',
  student_no               varchar(32)     default ''              comment '学号',
  gender                   varchar(20)     not null                comment '性别（male男 female女）',
  height_value             decimal(10,2)   not null                comment '身高(cm)',
  weight_value             decimal(10,2)   not null                comment '体重(kg)',
  source_channel           varchar(20)     default 'manual'        comment '录入来源（manual后台录入 wechat家长H5）',
  measure_time             datetime                                comment '量体时间',
  recommended_size_code    varchar(32)     default ''              comment '推荐尺码编码',
  recommended_size_name    varchar(32)     default ''              comment '推荐尺码名称',
  final_size_code          varchar(32)     default ''              comment '最终尺码编码',
  final_size_name          varchar(32)     default ''              comment '最终尺码名称',
  measure_status           char(1)         default '0'             comment '状态（0待复核 1已确认 2已下发）',
  body_remark              varchar(500)    default ''              comment '体型备注',
  create_by                varchar(64)     default ''              comment '创建者',
  create_time              datetime                                comment '创建时间',
  update_by                varchar(64)     default ''              comment '更新者',
  update_time              datetime                                comment '更新时间',
  remark                   varchar(500)    default null            comment '备注',
  primary key (measure_id),
  key idx_xf_measure_record_style_id (style_id),
  key idx_xf_measure_record_school_name (school_name),
  key idx_xf_measure_record_class_name (class_name),
  key idx_xf_measure_record_measure_status (measure_status)
) engine=innodb comment='校服量体测码表';

-- ----------------------------
-- 2、班级分拣发运单表
-- ----------------------------
create table if not exists xf_shipment_order (
  shipment_id          bigint(20)      not null auto_increment comment '发运单ID',
  shipment_no          varchar(64)     not null                comment '发运单号',
  work_order_id        bigint(20)      not null                comment '工单ID',
  school_name          varchar(100)    not null                comment '学校名称',
  campus_name          varchar(100)    default ''              comment '校区名称',
  grade_name           varchar(50)     default ''              comment '年级名称',
  class_name           varchar(50)     not null                comment '班级名称',
  total_quantity       int(11)         default 0               comment '总件数',
  packed_quantity      int(11)         default 0               comment '已装箱件数',
  shipment_status      char(1)         default '0'             comment '履约状态（0待分拣 1分拣中 2待发货 3已发货 4已签收）',
  logistics_company    varchar(100)    default ''              comment '物流公司',
  logistics_no         varchar(100)    default ''              comment '物流单号',
  dispatch_time        datetime                                comment '发货时间',
  sign_time            datetime                                comment '签收时间',
  create_by            varchar(64)     default ''              comment '创建者',
  create_time          datetime                                comment '创建时间',
  update_by            varchar(64)     default ''              comment '更新者',
  update_time          datetime                                comment '更新时间',
  remark               varchar(500)    default null            comment '备注',
  primary key (shipment_id),
  unique key uk_xf_shipment_order_no (shipment_no),
  key idx_xf_shipment_order_work_order_id (work_order_id),
  key idx_xf_shipment_order_school_name (school_name),
  key idx_xf_shipment_order_status (shipment_status)
) engine=innodb comment='校服班级分拣发运单表';

-- ----------------------------
-- 3、装箱明细表
-- ----------------------------
create table if not exists xf_shipment_package (
  package_id          bigint(20)      not null auto_increment comment '装箱ID',
  shipment_id         bigint(20)      not null                comment '发运单ID',
  package_no          varchar(64)     not null                comment '箱号',
  gender              varchar(20)     default 'unisex'        comment '性别（male男 female女 unisex中性）',
  size_code           varchar(32)     default ''              comment '尺码编码',
  quantity            int(11)         default 0               comment '数量',
  scan_status         char(1)         default '0'             comment '装箱状态（0待装箱 1已装箱 2已出库）',
  box_label           varchar(255)    default ''              comment '箱贴',
  trace_code_start    varchar(100)    default ''              comment '起始溯源码',
  trace_code_end      varchar(100)    default ''              comment '结束溯源码',
  remark              varchar(500)    default null            comment '备注',
  create_time         datetime                                comment '创建时间',
  primary key (package_id),
  unique key uk_xf_shipment_package_no (package_no),
  key idx_xf_shipment_package_shipment_id (shipment_id),
  key idx_xf_shipment_package_status (scan_status)
) engine=innodb comment='校服装箱明细表';

-- ----------------------------
-- 4、退换货RMA表
-- ----------------------------
create table if not exists xf_rma_order (
  rma_id                bigint(20)      not null auto_increment comment 'RMA ID',
  rma_no                varchar(64)     not null                comment 'RMA单号',
  trace_id              bigint(20)      not null                comment '溯源码ID',
  work_order_id         bigint(20)      not null                comment '工单ID',
  school_name           varchar(100)    default ''              comment '学校名称',
  class_name            varchar(50)     default ''              comment '班级名称',
  student_name          varchar(50)     not null                comment '学生姓名',
  request_type          char(1)         not null                comment '申请类型（1换货 2退货）',
  reason_type           varchar(50)     not null                comment '原因类型',
  old_size_code         varchar(32)     default ''              comment '原尺码',
  new_size_code         varchar(32)     default ''              comment '新尺码',
  apply_quantity        int(11)         not null default 1      comment '申请数量',
  status                char(1)         default '0'             comment '状态（0待审核 1待回收入库 2待补发 3已完成 8已驳回）',
  apply_time            datetime                                comment '申请时间',
  receive_time          datetime                                comment '旧衣回收时间',
  resend_tracking_no    varchar(100)    default ''              comment '补发单号',
  conclusion            varchar(500)    default ''              comment '处理结论',
  create_by             varchar(64)     default ''              comment '创建者',
  create_time           datetime                                comment '创建时间',
  update_by             varchar(64)     default ''              comment '更新者',
  update_time           datetime                                comment '更新时间',
  remark                varchar(500)    default null            comment '备注',
  primary key (rma_id),
  unique key uk_xf_rma_order_no (rma_no),
  key idx_xf_rma_order_trace_id (trace_id),
  key idx_xf_rma_order_work_order_id (work_order_id),
  key idx_xf_rma_order_status (status)
) engine=innodb comment='校服退换货RMA表';

-- ----------------------------
-- 5、演示数据
-- ----------------------------
insert into xf_measure_record (
  measure_id, style_id, school_name, campus_name, grade_name, class_name, student_name, student_no,
  gender, height_value, weight_value, source_channel, measure_time,
  recommended_size_code, recommended_size_name, final_size_code, final_size_name,
  measure_status, body_remark, create_by, create_time, update_by, update_time, remark
) values
  (5001, 1, '朝阳实验学校', '东校区', '2026级初一', '3班', '王梓涵', '20260301',
   'male', 148.00, 38.50, 'wechat', date_sub(now(), interval 3 day),
   '150', '150', '150', '150',
   '1', '标准体型', 'admin', sysdate(), 'admin', sysdate(), '示例：家长H5量体'),
  (5002, 1, '朝阳实验学校', '东校区', '2026级初一', '3班', '李沐晨', '20260302',
   'female', 159.00, 47.00, 'manual', date_sub(now(), interval 2 day),
   '160', '160加肥', '160', '160加肥',
   '0', '上身偏宽，建议复核', 'admin', sysdate(), 'admin', sysdate(), '示例：后台人工量体')
on duplicate key update
  style_id = values(style_id),
  school_name = values(school_name),
  campus_name = values(campus_name),
  grade_name = values(grade_name),
  class_name = values(class_name),
  student_name = values(student_name),
  student_no = values(student_no),
  gender = values(gender),
  height_value = values(height_value),
  weight_value = values(weight_value),
  source_channel = values(source_channel),
  measure_time = values(measure_time),
  recommended_size_code = values(recommended_size_code),
  recommended_size_name = values(recommended_size_name),
  final_size_code = values(final_size_code),
  final_size_name = values(final_size_name),
  measure_status = values(measure_status),
  body_remark = values(body_remark),
  update_by = values(update_by),
  update_time = values(update_time),
  remark = values(remark);

insert into xf_shipment_order (
  shipment_id, shipment_no, work_order_id, school_name, campus_name, grade_name, class_name,
  total_quantity, packed_quantity, shipment_status, logistics_company, logistics_no,
  dispatch_time, sign_time, create_by, create_time, update_by, update_time, remark
) values
  (5101, 'SH-202603-001', 2102, '北塔中学', '主校区', '2026级初一', '3班',
   29, 29, '3', '顺丰速运', 'SF202603300001',
   date_sub(now(), interval 8 hour), null, 'admin', sysdate(), 'admin', sysdate(), '示例：已发货待签收的班级分拣单')
on duplicate key update
  shipment_no = values(shipment_no),
  work_order_id = values(work_order_id),
  school_name = values(school_name),
  campus_name = values(campus_name),
  grade_name = values(grade_name),
  class_name = values(class_name),
  total_quantity = values(total_quantity),
  packed_quantity = values(packed_quantity),
  shipment_status = values(shipment_status),
  logistics_company = values(logistics_company),
  logistics_no = values(logistics_no),
  dispatch_time = values(dispatch_time),
  sign_time = values(sign_time),
  update_by = values(update_by),
  update_time = values(update_time),
  remark = values(remark);

insert into xf_shipment_package (
  package_id, shipment_id, package_no, gender, size_code, quantity, scan_status,
  box_label, trace_code_start, trace_code_end, remark, create_time
) values
  (5201, 5101, 'PK-202603-001', 'male', '150', 15, '2',
   '2026级初一3班 / 男装 / 150 / 15件', 'BT202603300001', 'BT202603300001', '示例：男装整箱', sysdate()),
  (5202, 5101, 'PK-202603-002', 'female', '160', 14, '2',
   '2026级初一3班 / 女装 / 160 / 14件', 'BT202603300001', 'BT202603300001', '示例：女装整箱', sysdate())
on duplicate key update
  shipment_id = values(shipment_id),
  gender = values(gender),
  size_code = values(size_code),
  quantity = values(quantity),
  scan_status = values(scan_status),
  box_label = values(box_label),
  trace_code_start = values(trace_code_start),
  trace_code_end = values(trace_code_end),
  remark = values(remark),
  create_time = values(create_time);

insert into xf_rma_order (
  rma_id, rma_no, trace_id, work_order_id, school_name, class_name, student_name,
  request_type, reason_type, old_size_code, new_size_code, apply_quantity,
  status, apply_time, receive_time, resend_tracking_no, conclusion,
  create_by, create_time, update_by, update_time, remark
) values
  (5301, 'RMA-202603-001', 2301, 2102, '北塔中学', '3班', '张小福',
   '1', 'size_small', '150', '160', 1,
   '2', date_sub(now(), interval 6 hour), date_sub(now(), interval 2 hour), '', '旧衣已回收入库，待补发新尺码',
   'admin', sysdate(), 'admin', sysdate(), '示例：换货处理中')
on duplicate key update
  trace_id = values(trace_id),
  work_order_id = values(work_order_id),
  school_name = values(school_name),
  class_name = values(class_name),
  student_name = values(student_name),
  request_type = values(request_type),
  reason_type = values(reason_type),
  old_size_code = values(old_size_code),
  new_size_code = values(new_size_code),
  apply_quantity = values(apply_quantity),
  status = values(status),
  apply_time = values(apply_time),
  receive_time = values(receive_time),
  resend_tracking_no = values(resend_tracking_no),
  conclusion = values(conclusion),
  update_by = values(update_by),
  update_time = values(update_time),
  remark = values(remark);

-- ----------------------------
-- 6、菜单与权限
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
  3107, 3108, 3109,
  3340, 3341, 3342, 3343, 3344, 3345,
  3346, 3347, 3348, 3349, 3350, 3351,
  3352, 3353, 3354, 3355, 3356, 3357
);

insert into sys_menu values
  (3107, '量体测码', '3100', '7', 'measure', 'uniform/measure/index', '', '', 1, 0, 'C', '0', '0', 'uniform:measure:list', 'education', 'admin', sysdate(), '', null, '量体测码管理'),
  (3108, '班级分拣', '3100', '8', 'shipment', 'uniform/shipment/index', '', '', 1, 0, 'C', '0', '0', 'uniform:shipment:list', 'list', 'admin', sysdate(), '', null, '班级分拣发运管理'),
  (3109, '退换货RMA', '3100', '9', 'rma', 'uniform/rma/index', '', '', 1, 0, 'C', '0', '0', 'uniform:rma:list', 'guide', 'admin', sysdate(), '', null, '退换货RMA管理'),

  (3340, '量体查询', '3107', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:measure:query', '#', 'admin', sysdate(), '', null, ''),
  (3341, '量体新增', '3107', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:measure:add', '#', 'admin', sysdate(), '', null, ''),
  (3342, '量体修改', '3107', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:measure:edit', '#', 'admin', sysdate(), '', null, ''),
  (3343, '量体删除', '3107', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:measure:remove', '#', 'admin', sysdate(), '', null, ''),
  (3344, '量体导出', '3107', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:measure:export', '#', 'admin', sysdate(), '', null, ''),
  (3345, '量体列表', '3107', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:measure:list', '#', 'admin', sysdate(), '', null, ''),

  (3346, '分拣查询', '3108', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:shipment:query', '#', 'admin', sysdate(), '', null, ''),
  (3347, '分拣新增', '3108', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:shipment:add', '#', 'admin', sysdate(), '', null, ''),
  (3348, '分拣修改', '3108', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:shipment:edit', '#', 'admin', sysdate(), '', null, ''),
  (3349, '分拣删除', '3108', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:shipment:remove', '#', 'admin', sysdate(), '', null, ''),
  (3350, '分拣导出', '3108', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:shipment:export', '#', 'admin', sysdate(), '', null, ''),
  (3351, '分拣列表', '3108', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:shipment:list', '#', 'admin', sysdate(), '', null, ''),

  (3352, 'RMA查询', '3109', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:rma:query', '#', 'admin', sysdate(), '', null, ''),
  (3353, 'RMA新增', '3109', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:rma:add', '#', 'admin', sysdate(), '', null, ''),
  (3354, 'RMA修改', '3109', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:rma:edit', '#', 'admin', sysdate(), '', null, ''),
  (3355, 'RMA删除', '3109', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:rma:remove', '#', 'admin', sysdate(), '', null, ''),
  (3356, 'RMA导出', '3109', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:rma:export', '#', 'admin', sysdate(), '', null, ''),
  (3357, 'RMA列表', '3109', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:rma:list', '#', 'admin', sysdate(), '', null, '');

-- 提示：
-- 1. 请在执行本脚本前确保模块一、模块二脚本已完成。
-- 2. 执行后请重启后端，并刷新前端菜单缓存。
-- 3. 非 admin 账号需在角色管理中手工勾选模块三菜单与按钮权限。
