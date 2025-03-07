# testProgJava
Тестовое задание для кандидата на вакансию Java-разработчика.

## Установка и запуск
1. Клонируйте репозиторий:
   git clone https://github.com/maksimkovtun/testProgJava.git

2. Перейдите в директорию проекта:
   cd папка_под_проект

3. Создайте базу данных в PostgreSQL, если еще не создана:
   CREATE DATABASE название_бд;

4. Создайте таблицы, вписав запросы из файла db.txt.

5. Убедитесь, что в файле `application.properties` указаны правильные данные для подключения к базе данных.
   spring.datasource.url=jdbc:postgresql://localhost:5432/название_бд
   spring.datasource.username=ваш_пользователь
   spring.datasource.password=ваш_пароль

6. Запустите приложение:
   ./mvnw spring-boot:run

Или через IntelliJ IDEA (com/program/testProgJava/TestProgJavaApplication.java).
Порт задан: 80 (http://localhost/).
Запросы для создания таблиц прикреплены к проекту в db.txt.
Swagger: /swagger-ui/index.html
