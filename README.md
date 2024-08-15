# Restaurante Servidor


### Setup

> CREATE DATABASE suatigc CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
>
> composer install
>
> npm install
>
> php artisan migrate
>
> php artisan db:seed

### Refresh Permissions

> php artisan cache:clear
>
> php artisan db:seed --class=PermissionSeeder
>
> php artisan db:seed --class=RoleSeeder

Criar usuário no mysql

> CREATE USER 'user'@'localhost' IDENTIFIED BY 'password';

Concede acesso ao usuário

> GRANT ALL PRIVILEGES ON db.* TO 'user'@'localhost';

Adicionar em /etc/phpmyadmin/config.inc.php para ocultar bancos do sistema

$cfg['Servers'][$i]['hide_db'] = 'information_schema|performance_schema';