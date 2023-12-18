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
             JOIN 旅游线路_旅游时间段_旅游信息 ON 旅游时间段.时间段 = 旅游线路_旅游时间段_旅游信息.旅游时间段_时间段
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
    DECLARE 导游员工信息 JSON;
    DECLARE 顾客信息 JSON;

    SELECT JSON_OBJECT('导游号', 导游号, '身份证号', 身份证号, '导游资格等级', 导游资格等级)
    INTO 导游员工信息
    FROM 导游员工
    WHERE 旅游团_id = NEW.旅游团_id;

    SELECT JSON_OBJECT('身份证号', 身份证号, '姓名', 姓名, '工作单位', 工作单位, '职业', 职业)
    INTO 顾客信息
    FROM 身份信息
    WHERE 身份证号 = NEW.顾客_身份证号;

    SET 旅游合同信息 = JSON_OBJECT(
            '旅游时间', NEW.旅游时间,
            '旅游费用', NEW.旅游费用,
            '保险', NEW.保险,
            '服务等级', NEW.服务等级,
            '旅游团_id', NEW.旅游团_id,
            '导游员工信息', 导游员工信息,
            '顾客信息', 顾客信息
                       );

    UPDATE 旅游信息
    SET 旅游合同 = 旅游合同信息
    WHERE id = NEW.id;
END$$

DELIMITER ;