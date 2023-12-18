# Tour Management System

## Configuration

```json
{
    "database_url": "jdbc:mysql://localhost:3307/tour",
    "database_username": "root",
    "database_password": "learn_mysql_password"
}
```

## Backup

docker exec -i docker-compose-learn-learn-mysql-1 /usr/bin/mysqldump -u root --password='learn_mysql_password' -h docker-compose-learn-learn-mysql-1 tour | Set-Content C:\Users\windsnow1024\Downloads\data.sql
