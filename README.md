# Tour Management System

## Configuration

```json
{
    "database_url": "jdbc:mysql://localhost:3307/tour",
    "database_username": "root",
    "database_password": "learn_mysql_password"
}
```

## Table Size

- 总公司: 1
- 旅游分公司: 10
- 身份信息: 1050
- 经理: 10
- 导游员工: 40
- 顾客: 1000
- 旅游线路: 10
- 地点: 20
- 景点: 20
- 旅游时间段: 30
- 旅游信息: 1000

## Create Order

Table -> View -> Trigger -> Data

## Backup

docker exec -i docker-compose-learn-learn-mysql-1 /usr/bin/mysqldump -u root --password='learn_mysql_password' -h docker-compose-learn-learn-mysql-1 tour | Set-Content C:\Users\windsnow1024\Downloads\backup.sql
