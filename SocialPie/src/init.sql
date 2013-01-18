-- SocialPie的数据库初始化脚本
create database if not exists SocialPie character set utf8;

USE SocialPie;
set names utf8; -- 控制台使用utf8编码
drop table if exists tb_user;
create table tb_user (
	id integer auto_increment comment 'id',
	name varchar(255) comment '用户名',
	password varchar(255) comment '密码',
	access_token varchar(255) comment '微博accessToken'
	expire_date varchar(255) comment 'token过期时间'
	primary key (id)
)
