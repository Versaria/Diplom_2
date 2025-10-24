# Diplom_2

# API тестирование Stellar Burgers 🍔

![Java](https://img.shields.io/badge/Java-11%2B-blue.svg)
![Maven](https://img.shields.io/badge/Maven-3.9%2B-orange.svg)
![JUnit](https://img.shields.io/badge/JUnit-4.13.2-green.svg)
![REST-Assured](https://img.shields.io/badge/REST--Assured-5.4.0-purple.svg)
![Allure](https://img.shields.io/badge/Allure-2.25.0-yellow.svg)
![Tests](https://img.shields.io/badge/Tests-16%20passed-brightgreen.svg)

Проект автоматизированного тестирования API для космической бургерной Stellar Burgers. Включает полное покрытие тестов всех основных эндпоинтов с использованием REST Assured и генерацией Allure отчетов

## 🛠 Технологии

| Технология | Версия | Назначение |
|------------|--------|------------|
| Java | 11 | Основной язык программирования |
| Maven | 3.9+ | Сборка проекта и управление зависимостями |
| JUnit | 4.13.2 | Фреймворк для модульного тестирования |
| REST Assured | 5.4.0 | Тестирование REST API |
| Allure | 2.25.0 | Построение отчетов о тестировании |
| Lombok | 1.18.30 | Сокращение шаблонного кода |
| JavaFaker | 1.0.2 | Генерация тестовых данных |

## 🚀 Быстрый старт

### Предварительные требования

- Установленная **Java JDK 11** или выше
- Установленный **Maven 3.6** или выше
- Доступ к интернету для загрузки зависимостей

### Установка и настройка

```bash
# Клонирование репозитория
git clone https://github.com/Versaria/Diplom_2.git
cd Diplom_2
# Проверка установки
java -version
mvn -version
```

## 📂 Структура проекта

```
Diplom_2/
├── src/test/java/praktikum/api/
│   ├── clients/                      # REST-клиенты для работы с API
│   │   ├── BaseClient.java           # Базовый клиент с настройками REST Assured
│   │   ├── UserClient.java           # Клиент для работы с пользователями
│   │   └── OrderClient.java          # Клиент для работы с заказами
│   ├── constants/
│   │   ├── Endpoints.java            # Константы эндпоинтов API
│   │   └── ErrorMessages.java        # Сообщения об ошибках API
│   ├── models/                       # Модели данных (DTO)
│   │   ├── User.java                 # Модель пользователя
│   │   ├── UserCredentials.java      # Модель учетных данных
│   │   └── Order.java                # Модель заказа
│   ├── tests/                        # Тестовые классы
│   │   ├── UserRegistrationTest.java # Тесты регистрации пользователя (5 тестов)
│   │   ├── UserLoginTest.java        # Тесты авторизации пользователя (5 тестов)
│   │   └── OrderCreationTest.java    # Тесты создания заказов (6 тестов)
│   └── utils/
│       └── DataGenerator.java        # Генератор тестовых данных с Faker
├── target/allure-results/            # Результаты тестов для Allure
├── pom.xml                           # Конфигурация Maven с зависимостями
└── README.md
```

## 🧪 Покрытие тестирования

### Создание пользователя (5 тестов)

| Тест | Описание | Ожидаемый результат |
|------|----------|---------------------|
| Успешная регистрация | Создание уникального пользователя | 200 OK, токены доступа |
| Дубликат пользователя | Попытка создать существующего пользователя | 403 Forbidden |
| Без email | Регистрация без обязательного поля | 403 Forbidden |
| Без пароля | Регистрация без пароля | 403 Forbidden |
| Без имени | Регистрация без имени | 403 Forbidden |

### Авторизация пользователя (5 тестов)

| Тест | Описание | Ожидаемый результат |
|------|----------|---------------------|
| Успешный вход | Авторизация с корректными данными | 200 OK, токены |
| Неверный пароль | Вход с неправильным паролем | 401 Unauthorized |
| Несуществующий пользователь | Ввод неверного email | 401 Unauthorized |
| Без email | Попытка входа без email | 401 Unauthorized |
| Без пароля | Попытка входа без пароля | 401 Unauthorized |

### Создание заказов (6 тестов)

| Тест | Описание | Ожидаемый результат |
|------|----------|---------------------|
| С авторизацией | Создание заказа авторизованным пользователем | 200 OK |
| Без авторизации | Создание заказа без токена | 200 OK |
| С ингредиентами | Заказ с валидными ингредиентами | 200 OK |
| Без ингредиентов | Заказ без указания ингредиентов | 400 Bad Request |
| Пустой список | Заказ с пустым массивом ингредиентов | 400 Bad Request |
| Невалидные ингредиенты | Заказ с некорректными хешами | 500 Internal Server Error |

## ▶️ Запуск тестов

### Основные команды

```bash
# Запуск всех тестов
mvn clean test
# Запуск конкретного тестового класса
mvn test -Dtest=UserRegistrationTest
# Запуск тестов с детальным логированием
mvn test -Dtest=OrderCreationTest -X
```

### Параметризованный запуск

```bash
# Запуск только тестов регистрации
mvn test -Dtest="*RegistrationTest"
# Запуск тестов авторизации и заказов
mvn test -Dtest="*LoginTest,*CreationTest"
# Пропуск тестов и только компиляция
mvn clean compile
```

## 📊 Allure отчеты

### Генерация отчетов

```bash
# Запуск тестов и сбор данных для Allure
mvn clean test
# Генерация HTML отчета
mvn allure:report
# Запуск локального сервера с отчетом
mvn allure:serve
```

### Структура отчета

- **Обзор** - общая статистика тестов
- **Графики** - диаграммы успешных/проваленных тестов
- **Сьюиты** - группировка тестов по классам
- **Шаги** - детальная информация о каждом шаге теста
- **Вложения** - запросы и ответы API

## 🔧 Эндпоинты API

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `POST` | `/api/auth/register` | Регистрация пользователя |
| `POST` | `/api/auth/login` | Авторизация пользователя |
| `POST` | `/api/orders` | Создание заказа |
| `GET` | `/api/ingredients` | Получение списка ингредиентов |
| `DELETE` | `/api/auth/user` | Удаление пользователя |

## 📝 Примеры использования

### Создание пользователя

```java
User user = DataGenerator.generateRandomUser();
Response response = userClient.create(user);
String accessToken = response.path("accessToken");
```

### Авторизация

```java
UserCredentials credentials = new UserCredentials(user.getEmail(), user.getPassword());
Response loginResponse = userClient.login(credentials);
```

### Создание заказа

```java
List<String> ingredients = Arrays.asList("60d3b41abdacab0026a733c6", "609646e4dc916e00276b2870");
Order order = new Order(ingredients);
Response orderResponse = orderClient.create(order, accessToken);
```

---

<div align="center">

**Проект создан в учебных целях** • [Отчет о тестировании](target/allure-results)

</div>