-- 云裳校服主数据中心一期
-- 包含：供应商资质管理、款式库、尺码矩阵、BOM 明细、菜单权限与定时预警任务

-- ----------------------------
-- 1、供应商主数据表
-- ----------------------------
create table if not exists xf_supplier (
  supplier_id                 bigint(20)      not null auto_increment comment '供应商ID',
  supplier_code               varchar(64)     not null                comment '供应商编码',
  supplier_name               varchar(100)    not null                comment '供应商名称',
  supplier_type               varchar(32)     not null                comment '供应商类型',
  contact_person              varchar(30)     default ''              comment '联系人',
  contact_phone               varchar(20)     default ''              comment '联系电话',
  address                     varchar(255)    default ''              comment '联系地址',
  iso_cert_no                 varchar(100)    default ''              comment 'ISO9001证书编号',
  iso_expire_date             date            default null            comment 'ISO9001到期日',
  iso_cert_file_url           varchar(500)    default ''              comment 'ISO9001附件',
  env_cert_no                 varchar(100)    default ''              comment '环保认证编号',
  env_expire_date             date            default null            comment '环保认证到期日',
  env_cert_file_url           varchar(500)    default ''              comment '环保认证附件',
  quality_report_no           varchar(100)    default ''              comment '质检报告编号',
  quality_report_expire_date  date            default null            comment '质检报告到期日',
  quality_report_file_url     varchar(500)    default ''              comment '质检报告附件',
  warning_days                int(4)          default 30              comment '预警提前天数',
  status                      char(1)         default '0'             comment '状态（0正常 1停用）',
  create_by                   varchar(64)     default ''              comment '创建者',
  create_time                 datetime                                comment '创建时间',
  update_by                   varchar(64)     default ''              comment '更新者',
  update_time                 datetime                                comment '更新时间',
  remark                      varchar(500)    default null            comment '备注',
  primary key (supplier_id),
  unique key uk_xf_supplier_code (supplier_code),
  key idx_xf_supplier_status (status),
  key idx_xf_supplier_type (supplier_type)
) engine=innodb comment='校服供应商主数据表';

-- ----------------------------
-- 2、款式主表
-- ----------------------------
create table if not exists xf_uniform_style (
  style_id               bigint(20)      not null auto_increment comment '款式ID',
  style_code             varchar(64)     not null                comment '款式编码',
  style_name             varchar(100)    not null                comment '款式名称',
  season                 varchar(20)     not null                comment '季节',
  style_type             varchar(20)     not null                comment '款式类型',
  gender                 varchar(20)     not null                comment '适用性别',
  fabric_name            varchar(100)    default ''              comment '主面料名称',
  fabric_composition     varchar(255)    default ''              comment '面料成分',
  code_mode              char(1)         default '1'             comment '赋码模式（1一物一码 2一批一码）',
  special_body_support   char(1)         default '0'             comment '是否支持特体（0否 1是）',
  care_instructions      varchar(500)    default ''              comment '洗护说明',
  status                 char(1)         default '0'             comment '状态（0正常 1停用）',
  create_by              varchar(64)     default ''              comment '创建者',
  create_time            datetime                                comment '创建时间',
  update_by              varchar(64)     default ''              comment '更新者',
  update_time            datetime                                comment '更新时间',
  remark                 varchar(500)    default null            comment '备注',
  primary key (style_id),
  unique key uk_xf_uniform_style_code (style_code),
  key idx_xf_uniform_style_status (status)
) engine=innodb comment='校服款式主表';

-- ----------------------------
-- 3、尺码矩阵表
-- ----------------------------
create table if not exists xf_uniform_style_size (
  size_id         bigint(20)      not null auto_increment comment '尺码ID',
  style_id        bigint(20)      not null                comment '款式ID',
  size_code       varchar(32)     not null                comment '尺码编码',
  size_name       varchar(32)     not null                comment '尺码名称',
  special_type    char(1)         default '0'             comment '特体类型（0标准 1加肥 2加长 3加肥加长）',
  height_min      decimal(6,2)    default null            comment '建议身高下限',
  height_max      decimal(6,2)    default null            comment '建议身高上限',
  weight_min      decimal(6,2)    default null            comment '建议体重下限',
  weight_max      decimal(6,2)    default null            comment '建议体重上限',
  sort_order      int(4)          default 1               comment '排序',
  create_time     datetime                                comment '创建时间',
  update_time     datetime                                comment '更新时间',
  primary key (size_id),
  key idx_xf_uniform_style_size_style_id (style_id)
) engine=innodb comment='校服尺码矩阵表';

-- ----------------------------
-- 4、BOM 明细表
-- ----------------------------
create table if not exists xf_uniform_style_bom (
  bom_id                 bigint(20)      not null auto_increment comment 'BOM ID',
  style_id               bigint(20)      not null                comment '款式ID',
  material_type          char(1)         default '1'             comment '物料类型（1主面料 2辅料 3包装）',
  material_name          varchar(100)    not null                comment '物料名称',
  material_spec          varchar(255)    default ''              comment '物料规格',
  material_composition   varchar(255)    default ''              comment '成分说明',
  dosage                 decimal(10,2)   default null            comment '用量',
  dosage_unit            varchar(20)     default ''              comment '用量单位',
  supplier_id            bigint(20)      default null            comment '关联供应商ID',
  trace_required         char(1)         default '1'             comment '是否强制追溯（0否 1是）',
  sort_order             int(4)          default 1               comment '排序',
  remark                 varchar(500)    default null            comment '备注',
  create_time            datetime                                comment '创建时间',
  update_time            datetime                                comment '更新时间',
  primary key (bom_id),
  key idx_xf_uniform_style_bom_style_id (style_id),
  key idx_xf_uniform_style_bom_supplier_id (supplier_id)
) engine=innodb comment='校服BOM明细表';

-- ----------------------------
-- 5、示例数据
-- ----------------------------
insert into xf_supplier (
  supplier_id, supplier_code, supplier_name, supplier_type, contact_person, contact_phone, address,
  iso_cert_no, iso_expire_date, env_cert_no, env_expire_date, quality_report_no, quality_report_expire_date,
  warning_days, status, create_by, create_time, remark
) values
  (1, 'FAB-001', '辽宁北纬面料科技', 'fabric', '张工', '13800000001', '辽宁省朝阳市双塔区',
   'ISO-LN-2026-01', date_add(curdate(), interval 15 day), 'ENV-LN-2026-01', date_add(curdate(), interval 45 day),
   'QA-LN-2026-01', date_add(curdate(), interval 20 day), 30, '0', 'admin', sysdate(), '示例：面料供应商'),
  (2, 'ACC-001', '云裳辅料联合工厂', 'accessory', '李厂长', '13800000002', '辽宁省朝阳市龙城区',
   'ISO-ACC-2026-01', date_sub(curdate(), interval 10 day), 'ENV-ACC-2026-01', date_add(curdate(), interval 60 day),
   'QA-ACC-2026-01', date_add(curdate(), interval 90 day), 30, '0', 'admin', sysdate(), '示例：辅料供应商')
on duplicate key update
  supplier_name = values(supplier_name),
  supplier_type = values(supplier_type),
  contact_person = values(contact_person),
  contact_phone = values(contact_phone),
  address = values(address),
  iso_cert_no = values(iso_cert_no),
  iso_expire_date = values(iso_expire_date),
  env_cert_no = values(env_cert_no),
  env_expire_date = values(env_expire_date),
  quality_report_no = values(quality_report_no),
  quality_report_expire_date = values(quality_report_expire_date),
  warning_days = values(warning_days),
  status = values(status),
  remark = values(remark);

insert into xf_uniform_style (
  style_id, style_code, style_name, season, style_type, gender, fabric_name, fabric_composition,
  code_mode, special_body_support, care_instructions, status, create_by, create_time, remark
) values
  (1, 'SUMMER-SPORT-001', '夏季运动服套装', 'summer', 'sportswear', 'unisex', '吸湿速干针织布', '聚酯纤维95%，氨纶5%',
   '1', '1', '建议 40℃ 以下水洗，请勿高温熨烫反光条。', '0', 'admin', sysdate(), '示例款式')
on duplicate key update
  style_name = values(style_name),
  season = values(season),
  style_type = values(style_type),
  gender = values(gender),
  fabric_name = values(fabric_name),
  fabric_composition = values(fabric_composition),
  code_mode = values(code_mode),
  special_body_support = values(special_body_support),
  care_instructions = values(care_instructions),
  status = values(status),
  remark = values(remark);

delete from xf_uniform_style_size where style_id = 1;
insert into xf_uniform_style_size(style_id, size_code, size_name, special_type, height_min, height_max, weight_min, weight_max, sort_order, create_time, update_time) values
  (1, '140', '140', '0', 135, 145, 28, 35, 1, sysdate(), sysdate()),
  (1, '150', '150', '0', 145, 155, 35, 43, 2, sysdate(), sysdate()),
  (1, '160', '160加肥', '1', 155, 165, 43, 55, 3, sysdate(), sysdate());

delete from xf_uniform_style_bom where style_id = 1;
insert into xf_uniform_style_bom(style_id, material_type, material_name, material_spec, material_composition, dosage, dosage_unit, supplier_id, trace_required, sort_order, remark, create_time, update_time) values
  (1, '1', '运动服主面料', '260g/平方米', '聚酯纤维95%，氨纶5%', 2.30, '米', 1, '1', 1, '上衣+裤子主布', sysdate(), sysdate()),
  (1, '2', '反光条', '2cm宽', '涤纶反光材料', 1.20, '米', 2, '1', 2, '夜间安全标识', sysdate(), sysdate()),
  (1, '2', '弹力腰带', '标准腰围版', '橡筋+聚酯纤维', 1.00, '条', 2, '1', 3, '裤腰辅料', sysdate(), sysdate());

-- ----------------------------
-- 6、菜单与权限
-- ----------------------------
delete from sys_menu where menu_id in (
  3100, 3101, 3102,
  3200, 3201, 3202, 3203, 3204, 3205,
  3210, 3211, 3212, 3213, 3214, 3215
);

insert into sys_menu values
  (3100, '校服业务', '0', '5', 'uniform', null, '', '', 1, 0, 'M', '0', '0', '', 'shopping', 'admin', sysdate(), '', null, '校服业务目录'),
  (3101, '供应商资质', '3100', '1', 'supplier', 'uniform/supplier/index', '', '', 1, 0, 'C', '0', '0', 'uniform:supplier:list', 'list', 'admin', sysdate(), '', null, '供应商资质管理'),
  (3102, '款式与BOM', '3100', '2', 'style', 'uniform/style/index', '', '', 1, 0, 'C', '0', '0', 'uniform:style:list', 'table', 'admin', sysdate(), '', null, '款式与BOM管理'),

  (3200, '供应商查询', '3101', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:supplier:query', '#', 'admin', sysdate(), '', null, ''),
  (3201, '供应商新增', '3101', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:supplier:add', '#', 'admin', sysdate(), '', null, ''),
  (3202, '供应商修改', '3101', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:supplier:edit', '#', 'admin', sysdate(), '', null, ''),
  (3203, '供应商删除', '3101', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:supplier:remove', '#', 'admin', sysdate(), '', null, ''),
  (3204, '供应商导出', '3101', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:supplier:export', '#', 'admin', sysdate(), '', null, ''),
  (3205, '供应商列表', '3101', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:supplier:list', '#', 'admin', sysdate(), '', null, ''),

  (3210, '款式查询', '3102', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:style:query', '#', 'admin', sysdate(), '', null, ''),
  (3211, '款式新增', '3102', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:style:add', '#', 'admin', sysdate(), '', null, ''),
  (3212, '款式修改', '3102', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:style:edit', '#', 'admin', sysdate(), '', null, ''),
  (3213, '款式删除', '3102', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:style:remove', '#', 'admin', sysdate(), '', null, ''),
  (3214, '款式导出', '3102', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:style:export', '#', 'admin', sysdate(), '', null, ''),
  (3215, '款式列表', '3102', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'uniform:style:list', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 7、供应商资质预警定时任务
-- ----------------------------
delete from sys_job where invoke_target = 'uniformSupplierTask.scanCertificateAlerts';

insert into sys_job values
  (10, '供应商资质预警扫描', 'DEFAULT', 'uniformSupplierTask.scanCertificateAlerts', '0 0 8 * * ?', '3', '1', '0', 'admin', sysdate(), '', null, '每日8点扫描供应商证照有效期');
