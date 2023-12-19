CREATE TABLE 总公司
(
    id       INT          NOT NULL AUTO_INCREMENT,
    总公司名 VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE 旅游分公司
(
    id        INT          NOT NULL AUTO_INCREMENT,
    分公司名  VARCHAR(255) NOT NULL,
    总公司_id INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (总公司_id) REFERENCES 总公司 (id) ON DELETE CASCADE
);

CREATE TABLE 身份信息
(
    身份证号 VARCHAR(255) NOT NULL,
    姓名     VARCHAR(255) NOT NULL,
    工作单位 VARCHAR(255),
    职业     VARCHAR(255),
    PRIMARY KEY (身份证号)
);

CREATE TABLE 经理
(
    经理号    INT          NOT NULL AUTO_INCREMENT,
    身份证号  VARCHAR(255) NOT NULL,
    分公司_id INT          NOT NULL,
    PRIMARY KEY (经理号),
    FOREIGN KEY (身份证号) REFERENCES 身份信息 (身份证号) ON UPDATE CASCADE,
    FOREIGN KEY (分公司_id) REFERENCES 旅游分公司 (id) ON DELETE CASCADE
);

CREATE TABLE 旅游团
(
    id        INT NOT NULL AUTO_INCREMENT,
    分公司_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (分公司_id) REFERENCES 旅游分公司 (id) ON DELETE CASCADE
);

CREATE TABLE 导游员工
(
    导游号       INT          NOT NULL AUTO_INCREMENT,
    身份证号     VARCHAR(255) NOT NULL,
    导游资格等级 VARCHAR(255) NOT NULL,
    旅游团_id    INT          NOT NULL,
    PRIMARY KEY (导游号),
    FOREIGN KEY (身份证号) REFERENCES 身份信息 (身份证号) ON UPDATE CASCADE,
    FOREIGN KEY (旅游团_id) REFERENCES 旅游团 (id) ON DELETE CASCADE
);

CREATE TABLE 顾客
(
    身份证号  VARCHAR(255) NOT NULL,
    旅游团_id INT          NOT NULL,
    PRIMARY KEY (身份证号),
    FOREIGN KEY (身份证号) REFERENCES 身份信息 (身份证号) ON UPDATE CASCADE,
    FOREIGN KEY (旅游团_id) REFERENCES 旅游团 (id) ON DELETE CASCADE
);

CREATE TABLE 旅游线路
(
    id        INT NOT NULL AUTO_INCREMENT,
    总公司_id INT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE 地点
(
    地点        VARCHAR(255) NOT NULL,
    旅游线路_id INT          ,
    PRIMARY KEY (地点, 旅游线路_id),
    FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id) ON DELETE SET NULL
);

CREATE TABLE 景点
(
    景点        VARCHAR(255) NOT NULL,
    旅游线路_id INT          ,
    PRIMARY KEY (景点, 旅游线路_id),
    FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id) ON DELETE SET NULL
);

CREATE TABLE 旅游时间段
(
    id       INT          NOT NULL AUTO_INCREMENT,
    时间段   VARCHAR(255) NOT NULL,
    价格     FLOAT        NOT NULL,
    交通方式 VARCHAR(255) NOT NULL,
    服务等级 VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE 旅游信息
(
    id            INT          NOT NULL AUTO_INCREMENT,
    旅游时间      VARCHAR(255) NOT NULL,
    旅游费用      FLOAT        NOT NULL,
    保险          VARCHAR(255) NOT NULL,
    旅游合同      JSON,
    旅游团_id     INT          NOT NULL,
    顾客_身份证号 VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (旅游团_id) REFERENCES 旅游团 (id) ON DELETE CASCADE ,
    FOREIGN KEY (顾客_身份证号) REFERENCES 顾客 (身份证号) ON UPDATE CASCADE
);

CREATE TABLE 旅游线路_旅游时间段_旅游信息
(
    旅游线路_id   INT NOT NULL,
    旅游时间段_id INT NOT NULL,
    旅游信息_id   INT NOT NULL,
    PRIMARY KEY (旅游线路_id, 旅游时间段_id, 旅游信息_id),
    FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id) ON DELETE CASCADE,
    FOREIGN KEY (旅游时间段_id) REFERENCES 旅游时间段 (id) ON DELETE CASCADE,
    FOREIGN KEY (旅游信息_id) REFERENCES 旅游信息 (id) ON DELETE CASCADE
);