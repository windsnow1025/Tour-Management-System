# 旅游费用_after_insert_旅游信息
DELIMITER $$

CREATE TRIGGER 旅游费用_after_insert_旅游信息
    AFTER INSERT
    ON 旅游信息
    FOR EACH ROW
BEGIN
    DECLARE 价格 FLOAT;

    SELECT 旅游时间段.价格
    INTO 价格
    FROM 旅游时间段
             JOIN 旅游线路_旅游时间段_旅游信息 ON 旅游时间段.id = 旅游线路_旅游时间段_旅游信息.旅游时间段_id
    WHERE 旅游线路_旅游时间段_旅游信息.旅游信息_id = NEW.id;

    UPDATE 旅游信息
    SET 旅游费用 = 价格
    WHERE id = NEW.id;
END$$

DELIMITER ;

# 旅游合同_after_insert_旅游信息
DELIMITER $$

CREATE TRIGGER 旅游合同_after_insert_旅游信息
    AFTER INSERT
    ON 旅游信息
    FOR EACH ROW
BEGIN
    DECLARE 旅游合同信息 JSON;

    SELECT JSON_OBJECT(
                   '信息ID', 信息ID,
                   '旅游时间', 旅游时间,
                   '旅游费用', 旅游费用,
                   '保险', 保险,
                   '旅游团_id', 旅游团_id,
                   '顾客_身份证号', 顾客_身份证号,
                   '旅游线路_id', 旅游线路_id,
                   '地点', 地点,
                   '景点', 景点,
                   '时间段', 时间段,
                   '价格', 价格,
                   '交通方式', 交通方式,
                   '服务等级', 服务等级
           )
    INTO 旅游合同信息
    FROM 旅游信息视图
    WHERE 信息ID = NEW.id;

    UPDATE 旅游信息
    SET 旅游合同 = 旅游合同信息
    WHERE id = NEW.id;
END$$

DELIMITER ;