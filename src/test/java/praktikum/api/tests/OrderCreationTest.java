package praktikum.api.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.clients.OrderClient;
import praktikum.api.clients.UserClient;
import praktikum.api.constants.ErrorMessages;
import praktikum.api.models.Order;
import praktikum.api.models.User;
import praktikum.api.utils.DataGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

/**
 * Тесты для ручки "Создание заказа" (POST /api/orders)
 * Проверяет создание заказов с различными сценариями авторизации и состава
 */
public class OrderCreationTest {
    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private String accessToken;
    private List<String> validIngredients;

    private static final int MAX_ATTEMPTS = 3;

    // Инициализирует клиенты API и получает валидные ингредиенты для тестирования
    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        user = DataGenerator.generateRandomUser();

        // ИСПРАВЛЕНИЕ: создание пользователя перемещено в @Before метод
        Response createResponse = userClient.create(user);
        createResponse.then().statusCode(SC_OK);
        accessToken = createResponse.path("accessToken");

        // Получаем реальные ингредиенты с сервера с повторными попытками
        validIngredients = getIngredientsWithRetry();
    }

    // Очищает тестовые данные путем удаления созданного пользователя
    @After
    public void tearDown() {
        if (accessToken != null) {
            try {
                userClient.delete(accessToken);
            } catch (Exception e) {
                System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        }
    }

    /**
     * Получает ингредиенты с сервера с повторными попытками при неудаче
     */
    @Step("Получение ингредиентов с повторными попытками")
    private List<String> getIngredientsWithRetry() {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                Response ingredientsResponse = orderClient.getIngredients();
                if (ingredientsResponse.statusCode() == SC_OK) {
                    List<String> ingredients = ingredientsResponse.jsonPath().getList("data._id");
                    if (ingredients != null && !ingredients.isEmpty()) {
                        return ingredients.subList(0, Math.min(2, ingredients.size()));
                    }
                }
                Thread.sleep(1000); // Пауза между попытками
            } catch (Exception e) {
                System.out.println("Попытка " + attempt + " не удалась: " + e.getMessage());
            }
        }

        // Fallback на ингредиенты из документации
        return Arrays.asList(
                "60d3b41abdacab0026a733c6",
                "609646e4dc916e00276b2870"
        );
    }

    /**
     * Общие проверки успешного создания заказа
     */
    @Step("Проверка успешного создания заказа")
    private void assertSuccessfulOrderCreation(Response response) {
        response.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("name", not(emptyOrNullString()))
                .body("order.number", notNullValue());
    }

    /**
     * Тест проверяет создание заказа авторизованным пользователем
     * Проверяет успешное выполнение и структуру ответа с данными заказа
     */
    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка успешного создания заказа авторизованным пользователем")
    public void testCreateOrderWithAuth() {
        // Act: создаем заказ с авторизацией
        // УДАЛЕНО: создание пользователя и получение токена (уже сделано в @Before)
        Order order = new Order(validIngredients);
        Response orderResponse = orderClient.create(order, accessToken);

        // Assert: проверяем успешное создание заказа
        assertSuccessfulOrderCreation(orderResponse);
    }

    /**
     * Тест проверяет создание заказа без авторизации
     * Демонстрирует что система позволяет создавать заказы неавторизованным пользователям
     */
    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка создания заказа неавторизованным пользователем")
    public void testCreateOrderWithoutAuth() {
        // Arrange: создаем заказ с валидными ингредиентами
        Order order = new Order(validIngredients);

        // Act: создаем заказ без передачи токена авторизации
        Response response = orderClient.create(order, null);

        // Assert: проверяем успешное создание без авторизации
        assertSuccessfulOrderCreation(response);
    }

    /**
     * Тест проверяет создание заказа с валидными ингредиентами
     * Проверяет базовую функциональность создания заказа
     */
    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверка создания заказа с валидными ингредиентами")
    public void testCreateOrderWithIngredients() {
        // Arrange: создаем заказ с валидными ингредиентами
        Order order = new Order(validIngredients);

        // Act: отправляем запрос на создание заказа
        // ИСПРАВЛЕНИЕ: добавлена авторизация (передаем accessToken вместо null)
        Response response = orderClient.create(order, accessToken);

        // Assert: проверяем успешное создание заказа
        assertSuccessfulOrderCreation(response);
    }

    /**
     * Тест проверяет обработку отсутствия ингредиентов в заказе
     * Ожидается ошибка с соответствующим сообщением
     */
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при создании заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        // Arrange: создаем заказ без указания ингредиентов
        Order order = new Order(null);

        // Act: отправляем запрос с отсутствующими ингредиентами
        // ИСПРАВЛЕНИЕ: добавлена авторизация (передаем accessToken вместо null)
        orderClient.create(order, accessToken)
                .then()
                // Assert: проверяем ошибку валидации
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", equalTo(ErrorMessages.INGREDIENTS_REQUIRED));
    }

    /**
     * Тест проверяет обработку пустого списка ингредиентов
     * Ожидается ошибка аналогичная отсутствию ингредиентов
     */
    @Test
    @DisplayName("Создание заказа с пустым списком ингредиентов")
    @Description("Проверка ошибки при создании заказа с пустым списком ингредиентов")
    public void testCreateOrderWithEmptyIngredients() {
        // Arrange: создаем заказ с пустым списком ингредиентов
        List<String> emptyIngredients = Collections.emptyList();
        Order order = new Order(emptyIngredients);

        // Act: отправляем запрос с пустым списком ингредиентов
        // ИСПРАВЛЕНИЕ: добавлена авторизация (передаем accessToken вместо null)
        orderClient.create(order, accessToken)
                .then()
                // Assert: проверяем ошибку валидации
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", equalTo(ErrorMessages.INGREDIENTS_REQUIRED));
    }

    /**
     * Тест проверяет обработку невалидных идентификаторов ингредиентов
     * Ожидается внутренняя ошибка сервера при некорректных хешах
     */
    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка ошибки при создании заказа с невалидными ингредиентами")
    public void testCreateOrderWithInvalidIngredients() {
        // Arrange: создаем заказ с невалидными идентификаторами ингредиентов
        List<String> invalidIngredients = Arrays.asList("invalid1", "invalid2");
        Order order = new Order(invalidIngredients);

        // Act: отправляем запрос с невалидными ингредиентами
        // ИСПРАВЛЕНИЕ: добавлена авторизация (передаем accessToken вместо null)
        orderClient.create(order, accessToken)
                .then()
                // Assert: проверяем внутреннюю ошибку сервера
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}