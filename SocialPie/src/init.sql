-- SocialPie的数据库初始化脚本
create database if not exists SocialPie character set utf8;

USE SocialPie;
set names utf8; -- 控制台使用utf8编码
-- 定义系统用户表
-- drop table if exists tb_user;
create table if NOT EXISTS tb_user (
	id integer auto_increment comment 'id',
	name varchar(255) comment '用户名',
	password varchar(255) comment '密码',
	access_token varchar(255) DEFAULT NULL comment '微博accessToken',
	expire_date varchar(255) DEFAULT NULL comment 'token过期时间',
	uid long comment '用户微博ID',
	since_id long comment '前数据库已索引到的最新微博id',
	primary key (id)
);

-- 定义微博用户表
-- drop table if exists tb_s_user;
create table if NOT EXISTS tb_s_user (
	id BIGINT NOT NULL comment '微博用户id',
	screen_name VARCHAR(255) comment '微博名',
	gender CHAR(1) comment '性别',
	followers_count INT NOT NULL comment '粉丝数目',
	followees_count INT NOT NULL comment '关注者数目',
	status_count INT NOT NULL 	comment '微博数',
	province INT comment '所在省份',
	city INT comment '所在城市',
	description VARCHAR(1024) comment '用户个人描述',
	created_at DATETIME NOT NULL comment '用户注册时间',
	primary key(id)
);
-- 定义微博内容表
-- drop table if exists tb_s_weibo;
create table if NOT EXISTS tb_s_weibo (
	id BIGINT NOT NULL comment '微博id',
	user_id BIGINT NOT NULL REFERENCES tb_s_user(id),
	repost_count integer comment '转发计数',
	comment_count integer comment '评论计数',
	created_at DATETIME NOT NULL comment '微博创建时间',
	text text comment '微博内容',
	primary key(id)
);

-- 定义微博关注表
-- drop table if exists tb_s_relation;
create table if NOT EXISTS tb_s_relation (
	id INT NOT NULL comment '关系id',
	followee_id BIGINT NOT NULL REFERENCES tb_s_user(id),
	follower_id BIGINT NOT NULL REFERENCES tb_s_user(id),
	primary key(id)
);

-- 定义word表（word即分词后的词）
-- drop table if exists tb_s_wordlist;
create table if NOT EXISTS tb_s_wordlist (
	id INT NOT NULL comment '分词id',
	word VARCHAR(32) comment '分词结果',
	primary key(id)
);

-- 定义TFMF模块数据库表
CREATE TABLE if NOT EXISTS tb_s_TFMF_word (
	wordid INT NOT NULL REFERENCES tb_s_wordlist(id),
	userid BIGINT NOT NULL REFERENCES tb_s_user(id),
	tf INT DEFAULT 0 comment '分词词频',
	mf INT DEFAULT 0 comment '出现该分词的微博频率',
	primary key(wordid, userid)
);

-- 定义TextRank模块中无向图结点表
CREATE TABLE if NOT EXISTS tb_s_TR_vertice (
	wordid INT NOT NULL REFERENCES tb_s_wordlist(id),
	userid BIGINT NOT NULL REFERENCES tb_s_user(id),
	textrank DOUBLE DEFAULT 0.0 comment '每个用户所属的分词的textrank值',
	primary key(wordid, userid)
);

-- 定义TextRank模块中无向图边的表
CREATE TABLE IF NOT EXISTS tb_s_TR_edge (
	wordid_1 INT NOT NULL REFERENCES tb_s_wordlist(id),
	wordid_2 INT NOT NULL REFERENCES tb_s_wordlist(id),
	userid BIGINT NOT NULL REFERENCES tb_s_user(id),
	weight INT DEFAULT 0 comment '边的权重（定义为词对的共现次数）',
	primary key(wordid_1, wordid_2, userid)
);
-- 定义触发器，保证edge表中wordid_1 < wordid_2 ***该定义语句在sql脚本中总有错误，在命令行里直接执行可以。
delimiter |
CREATE TRIGGER tg_insert_edge BEFORE INSERT ON tb_s_TR_edge
FOR EACH ROW BEGIN
	DECLARE temp INT DEFAULT 0;
	IF NEW.wordid_1>NEW.wordid_2 THEN
		SET temp = NEW.wordid_1;
		SET NEW.wordid_1 = NEW.wordid_2;
		SET NEW.wordid_2 = temp;
	END IF;
END;
|
delimiter ;