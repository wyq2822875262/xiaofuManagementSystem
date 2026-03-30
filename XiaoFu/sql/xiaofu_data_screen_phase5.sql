-- 云裳校服模块五：全链路数据监管舱大屏
-- 包含：飞线坐标配置、家长反馈画像、监管舱菜单入口

-- ----------------------------
-- 1、物流飞线坐标配置表
-- ----------------------------
create table if not exists xf_screen_route_config (
  route_id            bigint(20)      not null auto_increment comment '路线ID',
  source_name         varchar(100)    not null                comment '生产基地名称',
  source_lng          decimal(10,6)   not null                comment '生产基地经度',
  source_lat          decimal(10,6)   not null                comment '生产基地纬度',
  target_name         varchar(100)    not null                comment '学校名称',
  target_lng          decimal(10,6)   not null                comment '学校经度',
  target_lat          decimal(10,6)   not null                comment '学校纬度',
  route_status        char(1)         default '0'             comment '状态（0启用 1停用）',
  create_by           varchar(64)     default ''              comment '创建者',
  create_time         datetime                                comment '创建时间',
  update_by           varchar(64)     default ''              comment '更新者',
  update_time         datetime                                comment '更新时间',
  remark              varchar(500)    default null            comment '备注',
  primary key (route_id),
  unique key uk_xf_screen_route_target_name (target_name),
  key idx_xf_screen_route_status (route_status)
) engine=innodb comment='校服监管舱飞线坐标配置表';

-- ----------------------------
-- 2、家长反馈画像表
-- ----------------------------
create table if not exists xf_parent_feedback (
  feedback_id         bigint(20)      not null auto_increment comment '反馈ID',
  school_name         varchar(100)    not null                comment '学校名称',
  student_name        varchar(50)     not null                comment '学生姓名',
  keyword_tag         varchar(50)     not null                comment '标签关键词',
  feedback_text       varchar(500)    default ''              comment '反馈内容',
  sentiment           char(1)         default '1'             comment '情感倾向（0中性 1正向 2负向）',
  score               decimal(4,1)    default 5.0             comment '满意度评分',
  create_by           varchar(64)     default ''              comment '创建者',
  create_time         datetime                                comment '创建时间',
  update_by           varchar(64)     default ''              comment '更新者',
  update_time         datetime                                comment '更新时间',
  remark              varchar(500)    default null            comment '备注',
  primary key (feedback_id),
  key idx_xf_parent_feedback_school_name (school_name),
  key idx_xf_parent_feedback_sentiment (sentiment)
) engine=innodb comment='校服家长反馈画像表';

-- ----------------------------
-- 3、演示数据
-- ----------------------------
insert into xf_screen_route_config (
  route_id, source_name, source_lng, source_lat, target_name, target_lng, target_lat,
  route_status, create_by, create_time, update_by, update_time, remark
) values
  (5501, '云裳智造中心', 120.457180, 41.576980, '朝阳实验学校', 120.475620, 41.586410,
   '0', 'admin', sysdate(), 'admin', sysdate(), '监管舱飞线演示：朝阳实验学校'),
  (5502, '云裳智造中心', 120.457180, 41.576980, '北塔中学', 120.446320, 41.596520,
   '0', 'admin', sysdate(), 'admin', sysdate(), '监管舱飞线演示：北塔中学'),
  (5503, '云裳智造中心', 120.457180, 41.576980, '龙城小学', 120.503610, 41.609230,
   '0', 'admin', sysdate(), 'admin', sysdate(), '监管舱飞线演示：龙城小学'),
  (5504, '云裳智造中心', 120.457180, 41.576980, '双塔一中', 120.432880, 41.578140,
   '0', 'admin', sysdate(), 'admin', sysdate(), '监管舱飞线演示：双塔一中')
on duplicate key update
  source_name = values(source_name),
  source_lng = values(source_lng),
  source_lat = values(source_lat),
  target_name = values(target_name),
  target_lng = values(target_lng),
  target_lat = values(target_lat),
  route_status = values(route_status),
  update_by = values(update_by),
  update_time = values(update_time),
  remark = values(remark);

insert into xf_parent_feedback (
  feedback_id, school_name, student_name, keyword_tag, feedback_text, sentiment, score,
  create_by, create_time, update_by, update_time, remark
) values
  (5601, '朝阳实验学校', '王梓涵', '透气轻便', '孩子穿着运动量大也不闷，跑操反馈很好。', '1', 4.9,
   'portal', sysdate(), 'portal', sysdate(), '模块五演示：正向反馈'),
  (5602, '朝阳实验学校', '李沐晨', '尺码合身', '量体后推荐的尺码很准，收到基本不用改。', '1', 4.8,
   'portal', sysdate(), 'portal', sysdate(), '模块五演示：正向反馈'),
  (5603, '北塔中学', '张小福', '安全无异味', '新校服打开没有刺激味道，家长更放心。', '1', 5.0,
   'portal', sysdate(), 'portal', sysdate(), '模块五演示：正向反馈'),
  (5604, '北塔中学', '张小福', '追溯清晰', '扫码就能看到批次和质检信息，透明度很高。', '1', 4.9,
   'portal', sysdate(), 'portal', sysdate(), '模块五演示：正向反馈'),
  (5605, '龙城小学', '陈子谦', '面料柔软', '面料手感很好，孩子说贴身穿也舒服。', '1', 4.8,
   'portal', sysdate(), 'portal', sysdate(), '模块五演示：正向反馈'),
  (5606, '龙城小学', '赵雨桐', '透气轻便', '上体育课活动量大，穿着还是挺透气的。', '1', 4.7,
   'portal', sysdate(), 'portal', sysdate(), '模块五演示：正向反馈'),
  (5607, '双塔一中', '刘昊然', '安全无异味', '洗后也没有异味，孩子穿着比较安心。', '1', 4.9,
   'portal', sysdate(), 'portal', sysdate(), '模块五演示：正向反馈'),
  (5608, '双塔一中', '孙雅宁', '尺码合身', '特体建议很有帮助，最后成衣很合身。', '1', 4.8,
   'portal', sysdate(), 'portal', sysdate(), '模块五演示：正向反馈'),
  (5609, '北塔中学', '韩佳怡', '安全无异味', '收到后直接试穿，没有刺鼻味道。', '1', 5.0,
   'portal', sysdate(), 'portal', sysdate(), '模块五演示：正向反馈'),
  (5610, '朝阳实验学校', '周子轩', '追溯清晰', '每件衣服都能看到完整溯源档案，学校展示很方便。', '1', 4.9,
   'portal', sysdate(), 'portal', sysdate(), '模块五演示：正向反馈')
on duplicate key update
  school_name = values(school_name),
  student_name = values(student_name),
  keyword_tag = values(keyword_tag),
  feedback_text = values(feedback_text),
  sentiment = values(sentiment),
  score = values(score),
  update_by = values(update_by),
  update_time = values(update_time),
  remark = values(remark);

-- ----------------------------
-- 4、菜单与权限
-- ----------------------------
delete from sys_menu where menu_id in (3111);

insert into sys_menu values
  (3111, '监管舱大屏', '3100', '11', 'screen', 'uniform/screen/index', '', '', 1, 0, 'C', '0', '0', 'uniform:screen:view', 'monitor', 'admin', sysdate(), '', null, '模块五监管舱大屏入口')
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
-- 1. 执行本脚本后请重启后端服务。
-- 2. 执行后请刷新前端菜单缓存，即可在“校服业务”下看到“监管舱大屏”。
-- 3. 大屏接口地址为 /uniform/screen/overview，前端页面组件为 uniform/screen/index。
