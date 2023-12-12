CREATE TABLE 总公司
(
    id       INT          NOT NULL IDENTITY,
    总公司名 VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
)

CREATE TABLE 旅游分公司
(
    id        INT          NOT NULL IDENTITY,
    分公司名  VARCHAR(255) NOT NULL,
    总公司_id INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (总公司_id) REFERENCES 总公司 (id)
)

CREATE TABLE 身份信息
(
    身份证号 VARCHAR(255) NOT NULL,
    姓名     VARCHAR(255) NOT NULL,
    工作单位 VARCHAR(255),
    职业     VARCHAR(255),
    PRIMARY KEY (身份证号)
)

CREATE TABLE 经理
(
    经理号    INT          NOT NULL IDENTITY,
    身份证号  VARCHAR(255) NOT NULL,
    分公司_id INT          NOT NULL,
    PRIMARY KEY (经理号),
    FOREIGN KEY (身份证号) REFERENCES 身份信息 (身份证号),
    FOREIGN KEY (分公司_id) REFERENCES 旅游分公司 (id)
)

CREATE TABLE 导游员工
(
    导游号       INT          NOT NULL IDENTITY,
    身份证号     VARCHAR(255) NOT NULL,
    导游资格等级 VARCHAR(255) NOT NULL,
    业绩金额     FLOAT        NOT NULL,
    分公司_id    INT          NOT NULL,
    旅游团_id    INT          NOT NULL,
    PRIMARY KEY (导游号),
    FOREIGN KEY (身份证号) REFERENCES 身份信息 (身份证号),
    FOREIGN KEY (分公司_id) REFERENCES 旅游分公司 (id)
)

CREATE TABLE 旅游团
(
    id INT NOT NULL IDENTITY,
    PRIMARY KEY (id)
)

CREATE TABLE 顾客
(
    身份证号  VARCHAR(255) NOT NULL,
    消费金额  FLOAT        NOT NULL,
    旅游团_id INT          NOT NULL,
    PRIMARY KEY (身份证号),
    FOREIGN KEY (身份证号) REFERENCES 身份信息 (身份证号),
    FOREIGN KEY (旅游团_id) REFERENCES 旅游团 (id)
)

CREATE TABLE 旅游线路
(
    id        INT NOT NULL IDENTITY,
    总公司_id INT NOT NULL,
    PRIMARY KEY (id)
)

CREATE TABLE 旅游信息
(
    id            INT            NOT NULL IDENTITY,
    旅游时间      VARCHAR(255)   NOT NULL,
    旅游线路      VARCHAR(255)   NOT NULL,
    旅游费用      FLOAT          NOT NULL,
    保险          VARCHAR(255)   NOT NULL,
    服务等级      VARCHAR(255)   NOT NULL,
    旅游合同      VARBINARY(MAX) NOT NULL,
    旅游团_id     INT            NOT NULL,
    顾客_身份证号 VARCHAR(255)   NOT NULL,
    旅游线路_id   INT            NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (旅游团_id) REFERENCES 旅游团 (id),
    FOREIGN KEY (顾客_身份证号) REFERENCES 顾客 (身份证号),
    FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id)
)

CREATE TABLE 地点
(
    地点        VARCHAR(255) NOT NULL,
    旅游线路_id INT          NOT NULL,
    PRIMARY KEY (地点, 旅游线路_id),
    FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id)
)

CREATE TABLE 景点
(
    景点        VARCHAR(255) NOT NULL,
    旅游线路_id INT          NOT NULL,
    PRIMARY KEY (景点, 旅游线路_id),
    FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id)
)

CREATE TABLE 旅游时间段
(
    旅游时间段  VARCHAR(255) NOT NULL,
    价格        FLOAT        NOT NULL,
    交通方式    VARCHAR(255) NOT NULL,
    服务等级    VARCHAR(255) NOT NULL,
    收入信息    FLOAT        NOT NULL,
    旅游线路_id INT          NOT NULL,
    PRIMARY KEY (旅游时间段, 旅游线路_id),
    FOREIGN KEY (旅游线路_id) REFERENCES 旅游线路 (id)
)