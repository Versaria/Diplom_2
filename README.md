# Diplom_2

# API тестирование Stellar Burgers 🍔

![Java](https://img.shields.io/badge/Java-11%2B-blue.svg)
![Maven](https://img.shields.io/badge/Maven-3.9%2B-orange.svg)
![JUnit](https://img.shields.io/badge/JUnit-4-green.svg)
![REST-Assured](https://img.shields.io/badge/REST--Assured-5.3-purple.svg)
![Allure](https://img.shields.io/badge/Allure-2.21-yellow.svg)
![Tests](https://img.shields.io/badge/Tests-16%20passed-brightgreen)

Проект автоматизированного тестирования API для космической бургерной Stellar Burgers. Включает полное покрытие тестами всех основных эндпоинтов с использованием REST Assured и генерацией Allure отчетов.

## 🚀 Быстрый старт

### Требования
- **Java JDK** 11+ (рекомендуется Zulu 11.0.27+)
- **Maven** 3.6+
- **JUnit** 4.13.2
- **REST Assured** 5.4.0
- **Allure** 2.25.0

### Установка и запуск
```bash
# Клонирование репозитория
git clone https://github.com/Versaria/Diplom_2.git
cd Diplom_2
# Запуск тестов
mvn clean test
# Генерация и просмотр Allure отчета
mvn allure:serve
```

## 📂 Структура проекта
```
Diplom_2/
├── src/test/java/praktikum/api/
│   ├── clients/                      # REST-клиенты для работы с API
│   │   ├── BaseClient.java           # Базовый клиент с настройками
│   │   ├── UserClient.java           # Клиент для работы с пользователями
│   │   └── OrderClient.java          # Клиент для работы с заказами
│   ├── constants/
│   │   ├── Endpoints.java            # Константы эндпоинтов API
│   │   └── ErrorMessages.java        # Сообщения об ошибках API
│   ├── models/                       # Модели данных
│   │   ├── User.java                 # Модель пользователя
│   │   ├── UserCredentials.java      # Модель учетных данных
│   │   └── Order.java                # Модель заказа
│   ├── tests/                        # Тестовые классы
│   │   ├── UserRegistrationTest.java # Тесты регистрации пользователя
│   │   ├── UserLoginTest.java        # Тесты авторизации пользователя
│   │   └── OrderCreationTest.java    # Тесты создания заказов
│   └── utils/
│       └── DataGenerator.java        # Генератор тестовых данных
├── pom.xml                           # Конфигурация Maven
└── README.md
```

## 📋 Покрытие тестирования

### Создание пользователя 👤
- ✅ **Успешная регистрация** - создание уникального пользователя
- ✅ **Дубликат пользователя** - обработка существующего пользователя
- ✅ **Обязательные поля** - валидация отсутствия email, пароля, имени

### Авторизация пользователя 🔐
- ✅ **Успешный вход** - авторизация с корректными данными
- ✅ **Неверные данные** - вход с неправильным паролем
- ✅ **Несуществующий пользователь** - обработка неизвестного email
- ✅ **Отсутствие данных** - попытка входа без email или пароля

### Создание заказов 🛒
- ✅ **С авторизацией** - создание заказа авторизованным пользователем
- ✅ **Без авторизации** - создание заказа неавторизованным пользователем
- ✅ **С ингредиентами** - заказ с валидными ингредиентами
- ✅ **Без ингредиентов** - заказ без указания ингредиентов
- ✅ **Пустой список** - заказ с пустым массивом ингредиентов
- ✅ **Невалидные ингредиенты** - заказ с некорректными хешами

## 📊 Результаты тестирования

**Статистика тестов:**
- ✅ **Всего тестов:** 16
- ✅ **Успешных:** 16 (100%)
- ✅ **Проваленных:** 0
- ✅ **Ошибок:** 0

**Тестируемые эндпоинты:**
- `POST /api/auth/register` - регистрация пользователя
- `POST /api/auth/login` - авторизация пользователя
- `POST /api/orders` - создание заказа
- `GET /api/ingredients` - получение списка ингредиентов
- `DELETE /api/auth/user` - удаление пользователя

## 💻 Пример работы
```bash
// Создание пользователя
User user = DataGenerator.generateRandomUser();
Response response = userClient.create(user);

// Авторизация
UserCredentials credentials = new UserCredentials(user.getEmail(), user.getPassword());
Response loginResponse = userClient.login(credentials);
String accessToken = loginResponse.path("accessToken");

// Создание заказа
List<String> ingredients = Arrays.asList("60d3b41abdacab0026a733c6", "609646e4dc916e00276b2870");
Order order = new Order(ingredients);
Response orderResponse = orderClient.create(order, accessToken);
```
### Особенности реализации

- **Чистая архитектура** - разделение на клиенты, модели и тесты
- **Динамические данные** - генерация уникальных тестовых данных
- **Обработка токенов** - корректная работа с accessToken и refreshToken
- **Allure интеграция** - детальные шаги и описания в отчетах
- **Очистка ресурсов** - автоматическое удаление созданных пользователей

## 📜 Лицензия

**MIT License**. Полный текст доступен в файле [LICENSE](https://github.com/Versaria/qa-mesto-selenium-test/blob/main/LICENSE).

## 🤝 Как внести вклад

1. Форкните репозиторий
2. Создайте ветку для функции (`git checkout -b feature/new-feature`)
3. Закоммитьте изменения (`git commit -m 'Add new feature'`)
4. Запушьте в ветку (`git push origin feature/new-feature`)
5. Откройте Pull Request

---

<details>
<summary>🔧 Дополнительная информация</summary>

### Технологический стек
- **REST Assured 5.4** - фреймворк для тестирования REST API
- **Allure 2.25** - генерация детальных отчетов о тестировании
- **JUnit 4** - фреймворк для организации тестов
- **Lombok** - сокращение boilerplate кода в моделях
- **JavaFaker** - генерация реалистичных тестовых данных
- **Maven** - управление зависимостями и сборка

### Принципы тестирования
- **AAA паттерн** - Arrange-Act-Assert для структуры тестов
- **Page Object** - адаптированный для API клиентов
- **Изоляция тестов** - независимое выполнение каждого теста
- **Читаемость** - понятные названия и аннотации Allure
- **Обработка ошибок** - корректные сообщения и статус коды

### Команды Maven
```bash
# Запуск всех тестов
mvn clean test
# Генерация Allure отчета
mvn allure:report
# Просмотр Allure отчета
mvn allure:serve
# Запуск конкретного тестового класса
mvn test -Dtest=UserRegistrationTest
```

</details>

**Примечание:** Проект создан для учебных целей и демонстрирует лучшие практики тестирования REST API на Java.