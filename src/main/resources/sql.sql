#drop  database channel;
#CREATE  database channel;

--渠道组
drop table if exists channel_group;
create table channel_group
(
   gid             bigint(20) not null auto_increment comment '自增ID',
   gname           varchar(30) not null comment '名称',
   ctime           datetime not null comment '创建时间',
   ltime           datetime not null comment '修改时间',
   type            varchar(30) default NULL comment '类型',
   isactive        tinyint(1) not null comment '是否停用',
   primary key (gid)
);

--子渠道(子渠道默认一定属于一个渠道组)
drop table if exists channel_detail;
create table channel_detail
(
   cid             bigint(20) not null auto_increment comment '自增ID',
   gid             bigint(20) not null comment '所属分组',
   cname           varchar(30) not null comment '名称',
   ctime           datetime not null comment '创建时间',
   ltime           datetime not null comment '修改时间',
   type            varchar(30) default NULL comment '类型',
   isactive        tinyint(1) not null comment '是否停用',
   primary key (cid)
);

--渠道财务相关(关联渠道组)
drop table if exists channel_finance;
create table channel_finance
(
   fid             bigint(20) not null auto_increment comment '自增ID',
   gid             bigint(20) not null comment '对应渠道组',
   ctime           datetime not null comment '创建时间',--月数据,每月产生针对每一个渠道组生成一条数据
   ltime           datetime not null comment '修改时间',
   settlement      tinyint(1) default NULL comment '结算方式',
   budget          DECIMAL default NULL comment '预算',
   provision       DECIMAL default NULL comment '计提',
   payment         DECIMAL default NULL comment '付款',
   deduction       float default NULL comment '渠道扣量',
   primary key (fid)
);

--渠道kpi,关联子渠道,渠道组以及周月周期数据加和计算
drop table if exists channel_analysis;
create table channel_analysis
(
   aid             bigint(20) not null auto_increment comment '自增ID',
   cid             bigint(20) not null comment '对应渠道ID',
   ctime           datetime not null comment '创建时间',
   ltime           datetime not null comment '修改时间',

   new_users       bigint(20) default NULL comment '新增用户',
   dau             bigint(20) default NULL comment '日活',
   visit_length    float default NULL comment '访问时长',
   starts          bigint(20) default NULL comment '启动次数',

   healthy         tinyint(2) default NULL comment '结算方式',
   primary key (aid),
   KEY index_analysis (cid,ctime) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=112779 DEFAULT CHARSET=utf8;

drop table if exists channel_role;
create table channel_role
(
  rid             bigint(20) not null auto_increment comment '自增ID',
  role            varchar(30) not null comment '角色',
  description     varchar(30) comment '描述',
  permissions     varchar(30) not null comment '权限',
  available       tinyint(1) default NULL comment '停用',
  primary key (rid)
);
insert into channel_role(rid,role,description,permissions,available) values(1,'root','root','*:*',1);
insert into channel_role(rid,role,description,permissions,available) values(2,'admin','admin','admin:*',1);
insert into channel_role(rid,role,description,permissions,available) values(3,'test','test','channel:*',1);
insert into channel_role(rid,role,description,permissions,available) values(4,'test2','test2','user:*',1);


drop table if exists channel_user;
create table channel_user
(
  uid             bigint(20) not null auto_increment comment '自增ID',
  email           varchar(30) not null comment '邮箱',
  name            varchar(30) comment '姓名',
  roles           varchar(30) not null comment '角色名称',
  cname           varchar(30) not null comment '创建人',
  ctime           datetime not null comment '创建时间',
  ltime           datetime not null comment '修改时间',
  primary key (uid)
);
insert into channel_user (uid,email,name,roles,cname,ctime,ltime) values (1,'qmgeng1@126.com','qmgeng','root','qmgeng','2016-03-01','2016-03-02');
insert into channel_user (uid,email,name,roles,cname,ctime,ltime) values (2,'qmgeng2@126.com','qmgeng','admin','qmgeng','2016-03-01','2016-03-02');
insert into channel_user (uid,email,name,roles,cname,ctime,ltime) values (3,'qmgeng3@126.com','qmgeng','admin,test2','qmgeng','2016-03-01','2016-03-02');
insert into channel_user (uid,email,name,roles,cname,ctime,ltime) values (4,'qmgeng4@126.com','qmgeng','test2','qmgeng','2016-03-01','2016-03-02');
insert into channel_user (uid,email,name,roles,cname,ctime,ltime) values (5,'qmgeng5@126.com','qmgeng','test','qmgeng','2016-03-01','2016-03-02');
insert into channel_user (uid,email,name,roles,cname,ctime,ltime) values (10,'qmgeng@corp.netease.com','corp.netease.com','admin','qmgeng','2016-03-01','2016-03-02');
update channel_user set roles='root' where email='qmgeng@corp.netease.com';

drop table if exists channel_user_group;
create table channel_user_group
(
  id             bigint(20) not null auto_increment comment '自增ID',
  email          varchar(30) not null comment '用户信息',
  gid            bigint(20) not null comment '渠道组',
  primary key (id)
);





