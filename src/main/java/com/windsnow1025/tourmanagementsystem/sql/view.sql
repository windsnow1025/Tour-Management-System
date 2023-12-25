CREATE VIEW 旅游线路视图 AS
SELECT 旅游线路.id                      AS 旅游线路_id,
       GROUP_CONCAT(DISTINCT 地点.地点) AS 地点,
       GROUP_CONCAT(DISTINCT 景点.景点) AS 景点
FROM 旅游线路
         JOIN 地点 ON 旅游线路.id = 地点.旅游线路_id
         JOIN 景点 ON 旅游线路.id = 景点.旅游线路_id
GROUP BY 旅游线路.id;

CREATE VIEW 旅游团月度销售额视图 AS
SELECT 旅游团.id AS 旅游团_id,
       旅游团.分公司_id AS 分公司_id,
       旅游时间段.时间段 AS 月份,
       SUM(旅游信息.旅游费用) AS 旅游团月度收入
FROM 旅游信息
         JOIN 旅游线路_旅游时间段_旅游信息 ON 旅游信息.id = 旅游线路_旅游时间段_旅游信息.旅游信息_id
         JOIN 旅游时间段 ON 旅游时间段.id = 旅游线路_旅游时间段_旅游信息.旅游时间段_id
         JOIN 旅游团 ON 旅游团.id = 旅游信息.旅游团_id
GROUP BY 旅游时间段.时间段,
         旅游团.分公司_id,
         旅游团.id;

CREATE VIEW 分公司月度销售额视图 AS
SELECT 旅游分公司.分公司名,
       旅游分公司.总公司_id AS 总公司_id,
       旅游团月度销售额视图.月份,
       SUM(旅游团月度销售额视图.旅游团月度收入) AS 分公司月度收入
FROM 旅游团月度销售额视图
        JOIN 旅游分公司 ON 旅游团月度销售额视图.分公司_id = 旅游分公司.id
GROUP BY 旅游团月度销售额视图.分公司_id,
         旅游分公司.分公司名,
         旅游团月度销售额视图.月份;

CREATE VIEW 总公司月度销售额视图 AS
SELECT 总公司.总公司名,
       分公司月度销售额视图.月份,
       SUM(分公司月度销售额视图.分公司月度收入) AS 总公司月度收入
FROM 分公司月度销售额视图
        JOIN 总公司 ON 分公司月度销售额视图.总公司_id = 总公司.id
GROUP BY 分公司月度销售额视图.总公司_id,
         总公司.总公司名,
         分公司月度销售额视图.月份;


CREATE VIEW 旅游信息视图 AS
SELECT 旅游信息.id AS 旅游信息_id,
       旅游信息.旅游时间,
       旅游信息.旅游费用,
       旅游信息.保险,
       旅游信息.旅游团_id,
       旅游信息.顾客_身份证号,
       旅游线路_旅游时间段_旅游信息.旅游线路_id,
       旅游线路视图.地点,
       旅游线路视图.景点,
       旅游时间段.时间段,
       旅游时间段.价格,
       旅游时间段.交通方式,
       旅游时间段.服务等级
FROM 旅游信息
         JOIN 旅游线路_旅游时间段_旅游信息 ON 旅游信息.id = 旅游线路_旅游时间段_旅游信息.旅游信息_id
         JOIN 旅游线路视图 ON 旅游线路_旅游时间段_旅游信息.旅游线路_id = 旅游线路视图.旅游线路_id
         JOIN 旅游时间段 ON 旅游线路_旅游时间段_旅游信息.旅游时间段_id = 旅游时间段.id;


CREATE VIEW 旅游线路收入视图 AS
SELECT 旅游线路.id            AS 旅游线路_id,
       旅游线路视图.地点,
       旅游线路视图.景点,
       SUM(旅游信息.旅游费用) AS 总收入
FROM 旅游线路
         JOIN 旅游线路视图 ON 旅游线路.id = 旅游线路视图.旅游线路_id
         JOIN 旅游线路_旅游时间段_旅游信息 ON 旅游线路.id = 旅游线路_旅游时间段_旅游信息.旅游线路_id
         JOIN 旅游信息 ON 旅游线路_旅游时间段_旅游信息.旅游信息_id = 旅游信息.id
GROUP BY 旅游线路.id;


CREATE VIEW 顾客消费视图 AS
SELECT 顾客.身份证号,
       SUM(旅游信息.旅游费用) AS 总消费
FROM 顾客
         JOIN 旅游信息 ON 顾客.身份证号 = 旅游信息.顾客_身份证号
GROUP BY 顾客.身份证号;


CREATE VIEW 导游业绩视图 AS
SELECT 导游员工.身份证号,
       旅游团.id              AS 旅游团_id,
       SUM(旅游信息.旅游费用) AS 总业绩
FROM 导游员工
         JOIN 旅游团 ON 导游员工.旅游团_id = 旅游团.id
         JOIN 旅游信息 ON 旅游团.id = 旅游信息.旅游团_id
GROUP BY 导游员工.身份证号, 旅游团.id;


CREATE VIEW 分公司业绩视图 AS
SELECT 旅游分公司.id,
       旅游分公司.分公司名,
       SUM(导游业绩视图.总业绩) AS 总业绩
FROM 旅游分公司
         JOIN 旅游团 ON 旅游分公司.id = 旅游团.分公司_id
         JOIN 导游业绩视图 ON 旅游团.id = 导游业绩视图.旅游团_id
GROUP BY 旅游分公司.id, 旅游分公司.分公司名;


CREATE VIEW 总公司业绩试图 AS
SELECT 总公司.id,
       总公司.总公司名,
       SUM(分公司业绩视图.总业绩) AS 总业绩
FROM 总公司
         JOIN 旅游分公司 ON 总公司.id = 旅游分公司.总公司_id
         JOIN 分公司业绩视图 ON 旅游分公司.id = 分公司业绩视图.id
GROUP BY 总公司.id, 总公司.总公司名;