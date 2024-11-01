▎Тестовое задание: Серверная часть по работе с вкладами в банке

▎Описание

Данное тестовое задание представляет собой серверную часть приложения для работы с вкладами в банке. Оно позволяет управлять данными о клиентах, банках и вкладах через REST API.

▎Использованные технологии

• Java 17

• Spring Boot

• JPA

• PostgreSQL

• JUnit, Mockito

• Maven

• REST

▎Установка и запуск

1. Создание базы данных:

   • Создайте базу данных в PostgreSQL, как указано в application.properties, или создайте свою и вставьте свои значения переменных.

2. Инициализация базы данных:

   • Примените SQL скрипт sql/db_init.sql для создания таблиц и добавления начальных данных.

3. Запуск приложения:

   • Запустите файл BankApiApplication, чтобы начать работу с API.

▎API Endpoints

▎Общие эндпоинты

• /banks

• /clients

• /deposits

▎Примеры запросов к /banks

• Получить список всех банков:  
  GET /banks

  
• Сортировка и фильтрация:  
  GET /banks?sort_by_name=true&filter_by_name=ВТБ

  
• Получить информацию о конкретном банке:  
  GET /banks/{id}

  
• Удалить банк:  
  DELETE /banks/{id}

  
• Обновить информацию о банке:  
  PUT /banks/{id}

  
• Создать новый банк:  
  POST /banks  
  
  {
      "name": "Новый банк",
      "bankIdCode": "999999999"
  }
  

▎Реализованные сущности

1. Клиенты

   • Наименование

   • Краткое наименование

   • Адрес

   • Организационно-правовая форма (выбор из списка)

2. Банки

   • Наименование

   • БИК

3. Вклад

   • Клиент (ссылка)

   • Банк (ссылка)

   • Дата открытия

   • Процент

   • Срок в месяцах

▎Примерный сценарий работы пользователя

Пользователь может:

• Заходить в реестр.

• Сортировать и/или фильтровать выводимые сущности.

• Редактировать существующие записи.

• Создавать новые записи.

• Удалять существующие записи.


