CREATE TRIGGER after_insert_旅游信息
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
end;