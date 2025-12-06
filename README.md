# Курсовой проект: Программа для расчёта калорийности и стоимости блюд ресторанного меню

Дисциплина: **«Объектно-ориентированное программирование на Java» (ООП на Java)**  
Автор: Ажикенов Арман ТИИ-34
Вариант темы: **Создание программы для расчёта калорийности и стоимости блюд ресторанного меню**

---

## 1. Описание проекта

Цель проекта — разработать **web-приложение**, которое позволяет:

- хранить структуру ресторанного меню (блюда и их ингредиенты);
- рассчитывать **калорийность** и **стоимость** каждого блюда на основе состава;
- просматривать список блюд и подробный состав через **web-интерфейс**;
- продемонстрировать применение принципов **ООП на Java**, а также работу с файлами и REST API.

Приложение реализовано в виде:

- **backend** на Java / Spring Boot (REST API);
- **frontend** на HTML + CSS + JavaScript, который обращается к backend через `fetch`.

---

## 2. Функциональные возможности

- Загрузка данных о **ингредиентах** и **блюдах** из CSV-файлов:
  - `ingredients.csv` — список ингредиентов с калорийностью и ценой за кг;
  - `dishes.csv` — список блюд с типом и перечнем ингредиентов.
- Расчёт для каждого блюда:
  - общей калорийности (ккал);
  - общей стоимости (тенге).
- REST API:
  - получение списка всех блюд;
  - получение детальной информации по одному блюду.
- Web-интерфейс:
  - таблица блюд;
  - поиск по названию;
  - просмотр состава блюда (ингредиенты, масса, калории, стоимость).
- Логирование основных событий и ошибок.
- Базовая обработка ошибок и выдача понятного JSON-ответа при сбоях.

---

## 3. Используемые технологии

**Backend:**

- Java 17+
- Spring Boot
- Spring Web (REST-контроллеры)
- SLF4J / Logback (логирование)

**Frontend:**

- HTML5
- CSS3 (адаптивный, современный UI)
- Vanilla JavaScript (`fetch` к REST API)

**Хранение данных:**

- CSV-файлы в `src/main/resources/data/`:
  - `ingredients.csv`
  - `dishes.csv`

---

## 4. Архитектура проекта

Приложение построено по многослойной архитектуре:

- **Модель (model):**
  - `MenuItem` — абстрактный элемент меню;
  - `Dish` — блюдо (наследуется от `MenuItem`);
  - `DishIngredient` — связь «блюдо–ингредиент» с массой;
  - `Ingredient` — ингредиент с калорийностью и ценой;
  - `DishType` — enum с типами блюд (STARTER, MAIN_COURSE, DESSERT, BEVERAGE).

- **Доступ к данным (repository):**
  - `MenuRepository` — интерфейс репозитория;
  - `FileMenuRepository` — реализация, читающая CSV-файлы и собирающая объекты модели.

- **Сервисный слой (service):**
  - `MenuService` — бизнес-логика работы с меню (поиск блюд и т.д.).

- **Web-слой / REST API (controller):**
  - `DishController` — REST-контроллер:
    - `GET /api/dishes`
    - `GET /api/dishes/{id}`

- **Обработка ошибок (exception):**
  - `MenuDataException` — собственное runtime-исключение для ошибок данных;
  - `ErrorResponse` — DTO для JSON-ответа об ошибке;
  - `GlobalExceptionHandler` — глобальный обработчик ошибок (`@RestControllerAdvice`).

- **Frontend (`/frontend`):**
  - `index.html` — основная страница web-интерфейса;
  - `styles.css` — оформление (современный dashboard-стиль);
  - `app.js` — логика работы с REST API и отрисовка таблицы и деталей.

---

## 5. Структура проекта

```text
Cursovaya-java/
├─ pom.xml
├─ src/
│  └─ main/
│     ├─ java/
│     │  └─ kz/enu/restaurant/
│     │     ├─ RestaurantApplication.java
│     │     ├─ controller/
│     │     │  ├─ DishController.java
│     │     │  ├─ DishDto.java
│     │     │  └─ DishDetailsDto.java
│     │     ├─ model/
│     │     │  ├─ MenuItem.java
│     │     │  ├─ Dish.java
│     │     │  ├─ DishIngredient.java
│     │     │  ├─ Ingredient.java
│     │     │  └─ DishType.java
│     │     ├─ repository/
│     │     │  ├─ MenuRepository.java
│     │     │  └─ FileMenuRepository.java
│     │     ├─ service/
│     │     │  └─ MenuService.java
│     │     └─ exception/
│     │        ├─ MenuDataException.java
│     │        ├─ ErrorResponse.java
│     │        └─ GlobalExceptionHandler.java
│     └─ resources/
│        ├─ application.properties
│        └─ data/
│           ├─ ingredients.csv
│           └─ dishes.csv
└─ frontend/
   ├─ index.html
   ├─ styles.css
   └─ app.js

## 6. Запуск backend (Spring Boot)
Через IntelliJ IDEA
Открыть проект как Maven-проект.
Найти класс RestaurantApplication (kz.enu.restaurant.RestaurantApplication).
Нажать Run (зелёный треугольник).
Убедиться, что в логах есть строка вроде:
Tomcat started on port(s): 8080
Started RestaurantApplication

Проверить в браузере:
http://localhost:8080/api/dishes
http://localhost:8080/api/dishes/1
Через Maven (из терминала)
mvn spring-boot:run
По умолчанию backend запускается на порту 8080.

## 7. Запуск frontend
Frontend — это статический HTML/JS, обращающийся к REST API по адресу http://localhost:8080/api/dishes.
Убедиться, что backend запущен.
Открыть папку frontend/ в проводнике.
Дважды щёлкнуть по index.html, чтобы открыть его в браузере.
<img width="1495" height="950" alt="image" src="https://github.com/user-attachments/assets/e91dc991-e32a-470c-b9b0-d6080f227d84" />

Интерфейс:
слева — таблица блюд (ID, название, тип, калории, стоимость);
сверху — строка поиска по названию;
при клике на строку слева справа отображаются:
тип блюда;
общая калорийность;
общая стоимость;
таблица ингредиентов (название, масса, калории, стоимость).

## 8. REST API
GET /api/dishes
Возвращает список всех блюд в кратком виде.

Пример ответа:

[
  {
    "id": 1,
    "name": "Куриное филе с рисом",
    "type": "MAIN_COURSE",
    "totalCalories": 520.5,
    "totalCost": 850.00
  },
  {
    "id": 2,
    "name": "Блинчики с сахаром",
    "type": "DESSERT",
    "totalCalories": 430.0,
    "totalCost": 320.00
  }
]

GET /api/dishes/{id}
Возвращает подробную информацию по одному блюду.

Пример ответа:

{
  "id": 1,
  "name": "Куриное филе с рисом",
  "type": "MAIN_COURSE",
  "totalCalories": 520.5,
  "totalCost": 850.0,
  "ingredients": [
    {
      "ingredientName": "Куриное филе",
      "grams": 200.0,
      "calories": 330.0,
      "cost": 440.0
    },
    {
      "ingredientName": "Рис",
      "grams": 150.0,
      "calories": 190.5,
      "cost": 90.0
    }
  ]
}

При ошибках чтения CSV или внутренних сбоях REST-слой возвращает структурированный JSON с полями message, details, timestamp.

## 9. Логирование и обработка ошибок
Для регистрации событий и ошибок используется SLF4J + Logback.
В FileMenuRepository логируются:
успешная загрузка ингредиентов и блюд;
пропуск некорректных строк CSV;
ошибки чтения и парсинга файлов.
Класс MenuDataException служит для ошибок уровня данных.
GlobalExceptionHandler (@RestControllerAdvice) перехватывает исключения и возвращает пользователю понятный JSON-ответ с описанием ошибки.

## 10. Для курсовой работы
Данный проект демонстрирует:
применение принципов ООП (инкапсуляция, наследование, полиморфизм, абстракция);
многослойную архитектуру (model–repository–service–controller–frontend);
работу с файлами (CSV) для хранения данных;
использование web-технологий (REST API + HTML/CSS/JS);
обработку исключений и логирование;
документирование кода с помощью Javadoc.

Ссылка на данный репозиторий может быть указана в Приложении А курсовой работы как исходный код реализации.
