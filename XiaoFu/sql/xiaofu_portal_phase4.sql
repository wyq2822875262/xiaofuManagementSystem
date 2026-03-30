-- 云裳校服模块四：C 端用户体验微站
-- 包含：全息溯源档案、数字洗护小百科、数字寻物贴绑定演示数据

-- ----------------------------
-- 1、数字寻物贴绑定表
-- ----------------------------
create table if not exists xf_lost_found_binding (
  binding_id          bigint(20)      not null auto_increment comment '绑定ID',
  trace_id            bigint(20)      not null                comment '溯源码ID',
  trace_code          varchar(100)    not null                comment '溯源码',
  school_name         varchar(100)    not null                comment '学校名称',
  class_name          varchar(50)     not null                comment '班级名称',
  student_name        varchar(50)     not null                comment '学生姓名',
  contact_name        varchar(50)     default ''              comment '联系人',
  contact_phone       varchar(20)     default ''              comment '联系电话',
  owner_remark        varchar(500)    default ''              comment '归还提示',
  bind_status         char(1)         default '0'             comment '状态（0已绑定 1已解绑）',
  bind_time           datetime                                comment '绑定时间',
  create_by           varchar(64)     default ''              comment '创建者',
  create_time         datetime                                comment '创建时间',
  update_by           varchar(64)     default ''              comment '更新者',
  update_time         datetime                                comment '更新时间',
  remark              varchar(500)    default null            comment '备注',
  primary key (binding_id),
  unique key uk_xf_lost_found_trace_id (trace_id),
  unique key uk_xf_lost_found_trace_code (trace_code),
  key idx_xf_lost_found_status (bind_status)
) engine=innodb comment='校服数字寻物贴绑定表';

-- ----------------------------
-- 2、演示数据
-- ----------------------------
insert into xf_lost_found_binding (
  binding_id, trace_id, trace_code, school_name, class_name, student_name,
  contact_name, contact_phone, owner_remark, bind_status, bind_time,
  create_by, create_time, update_by, update_time, remark
) values
  (5401, 2301, 'BT202603300001', '北塔中学', '2026级初一3班', '张小福',
   '张妈妈', '13812345678', '拾到后可交至初一3班班主任处，也可联系家长协助核实。', '0', date_sub(now(), interval 1 day),
   'portal', sysdate(), 'portal', sysdate(), '示例：已绑定的数字寻物贴')
on duplicate key update
  trace_id = values(trace_id),
  trace_code = values(trace_code),
  school_name = values(school_name),
  class_name = values(class_name),
  student_name = values(student_name),
  contact_name = values(contact_name),
  contact_phone = values(contact_phone),
  owner_remark = values(owner_remark),
  bind_status = values(bind_status),
  bind_time = values(bind_time),
  update_by = values(update_by),
  update_time = values(update_time),
  remark = values(remark);

-- ----------------------------
-- 3、菜单与权限
-- ----------------------------
delete from sys_menu where menu_id in (3110);

insert into sys_menu values
  (3110, 'C端微站入口', '3100', '10', 'portal', 'uniform/portal/index', '', '', 1, 0, 'C', '0', '0', 'uniform:portal:list', 'link', 'admin', sysdate(), '', null, '模块四微站打开入口')
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

-- 提示：
-- 1. 请在执行本脚本后重启后端服务。
-- 2. 前端扫码微站入口为 /portal/trace/{traceCode}，也支持通过 ?code=XFTRACE|... 访问。
-- 3. 执行后请刷新前端菜单缓存，即可在“校服业务”下看到“C端微站入口”菜单。
