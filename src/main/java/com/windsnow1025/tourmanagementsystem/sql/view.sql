CREATE VIEW 旅游线路详情视图 AS
SELECT 旅游线路.id AS 线路ID,
       地点.地点,
       景点.景点,
       旅游时间段.旅游时间段,
       旅游时间段.价格,
       旅游时间段.交通方式,
       旅游时间段.服务等级
FROM 旅游线路
         JOIN 地点 ON 旅游线路.id = 地点.旅游线路_id
         JOIN 景点 ON 旅游线路.id = 景点.旅游线路_id
         JOIN 旅游时间段 ON 旅游线路.id = 旅游时间段.旅游线路_id;


