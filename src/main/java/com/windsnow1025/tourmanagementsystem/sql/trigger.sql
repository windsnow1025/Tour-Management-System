CREATE TRIGGER 旅游费用_before_insert_旅游线路_旅游时间段_旅游信息
    BEFORE INSERT ON 旅游线路_旅游时间段_旅游信息
    FOR EACH ROW
BEGIN
    UPDATE 旅游信息
    SET 旅游费用 = (SELECT 价格 FROM 旅游时间段 WHERE id = NEW.旅游时间段_id)
    WHERE id = NEW.旅游信息_id;
END;


CREATE TRIGGER 旅游合同_after_insert_旅游线路_旅游时间段_旅游信息
    AFTER INSERT
    ON 旅游线路_旅游时间段_旅游信息
    FOR EACH ROW
BEGIN
    DECLARE 旅游合同信息 JSON;

    SELECT JSON_OBJECT(
                   '信息ID', 旅游信息_id,
                   '旅游时间', 旅游时间,
                   '旅游费用', 旅游费用,
                   '保险', 保险,
                   '旅游团_id', 旅游团_id,
                   '顾客_身份证号', 顾客_身份证号,
                   '地点', 地点,
                   '景点', 景点,
                   '时间段', 时间段,
                   '价格', 价格,
                   '交通方式', 交通方式,
                   '服务等级', 服务等级
           )
    INTO 旅游合同信息
    FROM 旅游信息视图
    WHERE 旅游信息_id = NEW.旅游信息_id;

    UPDATE 旅游信息
    SET 旅游合同 = 旅游合同信息
    WHERE id = NEW.旅游信息_id;
END;