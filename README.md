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
