CREATE TRIGGER 旅游费用_after_insert_旅游信息
AFTER INSERT
ON 旅游信息 FOR EACH ROW
BEGIN
    UPDATE 旅游信息
    SET 旅游费用 = (
        SELECT 价格
        FROM 旅游时间段
        WHERE 旅游线路_id = NEW.旅游线路_id
    )
    WHERE id = NEW.id;
END;


CREATE TRIGGER 旅游合同_after_insert_旅游信息
    AFTER INSERT
    ON 旅游信息 FOR EACH ROW
BEGIN
    DECLARE 旅游合同信息 JSON;
    DECLARE 导游员工信息 JSON;
    DECLARE 顾客信息 JSON;
    DECLARE 旅游线路信息 JSON;

    SELECT JSON_OBJECT('导游号', 导游号, '身份证号', 身份证号, '导游资格等级', 导游资格等级)
    INTO 导游员工信息
    FROM 导游员工
    WHERE 旅游团_id = NEW.旅游团_id;

    SELECT JSON_OBJECT('身份证号', 身份证号, '姓名', 姓名, '工作单位', 工作单位, '职业', 职业)
    INTO 顾客信息
    FROM 身份信息
    WHERE 身份证号 = NEW.顾客_身份证号;

    SELECT JSON_OBJECT('旅游线路 ID', 旅游线路_id, '总公司 ID', 总公司_id)
    INTO 旅游线路信息
    FROM 旅游线路
    WHERE id = NEW.旅游线路_id;

    SET 旅游合同信息 = JSON_OBJECT(
            '旅游时间', NEW.旅游时间,
            '旅游费用', NEW.旅游费用,
            '保险', NEW.保险,
            '服务等级', NEW.服务等级,
            '旅游团_id', NEW.旅游团_id,
            '导游员工信息', 导游员工信息,
            '顾客信息', 顾客信息,
            '旅游线路信息', 旅游线路信息
    );

    UPDATE 旅游信息
    SET 旅游合同 = 旅游合同信息
    WHERE id = NEW.id;
END;