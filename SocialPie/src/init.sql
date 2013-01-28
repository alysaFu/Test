-- SocialPie的数据库初始化脚本
create database if not exists SocialPie character set utf8;

USE SocialPie;
set names utf8; -- 控制台使用utf8编码
-- 定义系统用户表
drop table if exists tb_user;
create table tb_user (
	id integer auto_increment comment 'id',
	name varchar(255) comment '用户名',
	password varchar(255) comment '密码',
	access_token varchar(255) comment '微博accessToken',
	expire_date varchar(255) comment 'token过期时间',
	primary key (id)
);

-- 定义微博用户表
drop table if exists tb_s_user;
create table tb_s_user (
	id BIGINT NOT NULL comment '微博用户id',
	screen_name VARCHAR(255) comment '微博名',
	gender CHAR(1) comment '性别',
	followers_count INT NOT NULL comment '粉丝数目',
	followees_count INT NOT NULL comment '关注者数目',
	status_count INT NOT NULL 	comment '微博数',
	province TINYINT comment '所在省份',
	city TINYINT comment '所在城市',
	description VARCHAR(1024) comment '用户个人描述',
	created_at DATETIME NOT NULL comment '用户注册时间',
	primary key(id)
);

-- 定义微博内容表
drop table if exists tb_s_weibo;
create table tb_s_weibo (
	id BIGINT NOT NULL comment '微博id',
	user_id BIGINT NOT NULL REFERENCES tb_s_user(id),
	repost_count integer comment '转发计数',
	comment_count integer comment '评论计数',
	created_at DATETIME NOT NULL comment '微博创建时间',
	text text comment '微博内容',
	primary key(id)
);

-- 定义微博关注表
drop table if exists tb_s_relation;
create table tb_s_relation (
	id INT NOT NULL comment '关系id',
	followee_id BIGINT NOT NULL REFERENCES tb_s_user(id),
	follower_id BIGINT NOT NULL REFERENCES tb_s_user(id),
	primary key(id)
);