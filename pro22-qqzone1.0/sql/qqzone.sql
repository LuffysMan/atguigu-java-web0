DROP DATABASE qqzone;
CREATE DATABASE qqzone CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
use qqzone;

-- 创建用户表
create TABLE t_user_basic (
    id INT  AUTO_INCREMENT PRIMARY KEY,
    loginId VARCHAR(255) NOT NULL,
    nickName VARCHAR(255) NOT NULL,
    pwd VARCHAR(255) NOT NULL,
    headImg VARCHAR(255)
);

-- 创建用户详情表
CREATE TABLE t_user_detail (
    id INT  AUTO_INCREMENT PRIMARY KEY,
    realName VARCHAR(255) NOT NULL,
    tel VARCHAR(50),
    email VARCHAR(255) NOT NULL,
    birth DATE,
    star VARCHAR(255)
);

-- 日志表
create TABLE t_topic (
    id INT  AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    topicDate DATETIME NOT NULL,
    author INT  NOT NULL
--    FOREIGN KEY (author) REFERENCES t_user_basic(id) ON DELETE CASCADE
);

-- 回复表
create TABLE t_reply (
    id INT  AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    replyDate DATETIME NOT NULL,
    author INT  NOT NULL,
    topic INT  NOT NULL
);
--    FOREIGN KEY (author) REFERENCES t_user_basic(id) ON DELETE CASCADE,
--    FOREIGN KEY (topic) REFERENCES t_topic(id) ON DELETE CASCADE

-- 主人回复表
create TABLE t_host_reply (
    id INT  AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    hostReplyDate DATETIME NOT NULL,
    author INT  NOT NULL,
    reply INT  NOT NULL
    FOREIGN KEY (reply) REFERENCES t_reply(id) ON DELETE CASCADE
);
--    FOREIGN KEY (author) REFERENCES t_user_basic(id) ON DELETE CASCADE,

ALTER TABLE t_host_reply ADD CONSTRAINT fk_host_reply FOREIGN KEY (reply) REFERENCES t_reply(id) ON DELETE CASCADE;

-- 好友表
create TABLE t_friend (
    id INT  AUTO_INCREMENT PRIMARY KEY,
    uid INT  NOT NULL,
    fid INT  NOT NULL,
    UNIQUE KEY unique_friendship (uid, fid)
--    FOREIGN KEY (uid) REFERENCES t_user_basic(id) ON DELETE CASCADE,
--    FOREIGN KEY (fid) REFERENCES t_user_basic(id) ON DELETE CASCADE
);

-- 初始化用戶表
insert into t_user_basic (loginId, nickName, pwd, headImg)
    values
        ("u001", "萧峰", "ok", "qf.png"),
        ("u002", "tom", "ok", "dy.png"),
        ("u003", "kate", "ok", "mrf.png"),
        ("u004", "lucy", "ok", "wyy.png"),
        ("u005", "张三丰", "ok", "zl.png");

-- 初始化日志表
INSERT INTO t_topic (title, content, topicDate, author)
       VALUES
            ("我的空间开过来", "大家好, 我是铁锤妹妹", "2021-06-18 11:26:30", 1),
            ("格尔曼今晚加入狩猎", "疯狂的冒险家", "2021-06-18 11:26:30", 1),
            ("愚者的地上天国", "盥洗室", "2021-06-18 11:26:30", 1);

-- 初始化回复表
INSERT INTO t_reply (content, replyDate, author, topic)
       VALUES
            ("回复1", "2021-06-18 11:26:31", 2,  3),
            ("回复2", "2021-06-28 11:26:31", 2,  3),
            ("我张三丰也要加入", "2021-06-19 11:26:31", 5,  2),
            ("我tom也要", "2021-06-20 11:26:31", 2,  2);


-- 初始化主人回复表
INSERT INTO t_host_reply (content, hostReplyDate, author, reply)
       VALUES
            ("这里是主人回复", "2021-06-18 12:26:31", 1,  2);

-- 初始化friend表
INSERT INTO t_friend (uid, fid)
       VALUES
            (1, 2),
            (2, 1),
            (1, 3),
            (3, 1);