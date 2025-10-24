package praktikum.api.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.clients.UserClient;
import praktikum.api.constants.ErrorMessages;
import praktikum.api.models.User;
import praktikum.api.utils.DataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

/**
 * Тесты для ручки "Создание пользователя" (POST /api/auth/register)
 * Проверяет создание пользователей, обработку дубликатов и обязательные поля
 */
public class UserRegistrationTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    // Инициализирует клиент API и генерирует тестового пользователя
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = DataGenerator.generateRandomUser();
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
     * Общие проверки успешной регистрации
     */
    @Step("Проверка успешной регистрации пользователя")
    private void assertSuccessfulRegistration(Response response) {
        response.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", not(emptyOrNullString()));
    }

    /**
     * Проверка ошибки регистрации с обязательными полями
     */
    @Step("Проверка ошибки обязательных полей")
    private void assertRegistrationRequiredFieldsError(Response response) {
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo(ErrorMessages.REQUIRED_FIELDS_MISSING));
    }

    /**
     * Проверка ошибки дубликата пользователя
     */
    @Step("Проверка ошибки дубликата пользователя")
    private void assertDuplicateUserError(Response response) {
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo(ErrorMessages.USER_ALREADY_EXISTS));
    }

    /**
     * Тест проверяет успешное создание пользователя с валидными данными
     * Проверяет код ответа, структуру ответа и наличие токенов доступа
     */
    @Test
    @DisplayName("Успешное создание пользователя")
    @Description("Проверка создания пользователя с валидными данными и наличия токенов в ответе")
    public void testUserCreationSuccess() {
        // Act: отправляем запрос на создание пользователя
        Response response = userClient.create(user);

        // Assert: проверяем успешный ответ и структуру данных
        assertSuccessfulRegistration(response);

        // Сохраняем токен для последующей очистки в tearDown
        accessToken = response.path("accessToken");
    }

    /**
     * Тест проверяет обработку попытки создания дубликата пользователя
     * Ожидается ошибка с соответствующим сообщением
     */
    @Test
    @DisplayName("Создание дубликата пользователя")
    @Description("Проверка невозможности создания двух одинаковых пользователей")
    public void testDuplicateUserCreation() {
        // Arrange: создаем первого пользователя
        Response firstResponse = userClient.create(user);
        firstResponse.then().statusCode(SC_OK);
        accessToken = firstResponse.path("accessToken");

        // Act: пытаемся создать идентичного пользователя
        Response response = userClient.create(user);

        // Assert: проверяем ошибку конфликта
        assertDuplicateUserError(response);
    }

    /**
     * Тест проверяет обработку отсутствия обязательного поля email
     * Ожидается ошибка с сообщением о необходимости всех полей
     */
    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка создания пользователя без обязательного поля email")
    public void testCreateUserWithoutEmail() {
        // Arrange: создаем пользователя без email
        User userWithoutEmail = new User(null, "password", "name");

        // Act: отправляем запрос с неполными данными
        Response response = userClient.create(userWithoutEmail);

        // Assert: проверяем ошибку валидации
        assertRegistrationRequiredFieldsError(response);
    }

    /**
     * Тест проверяет обработку отсутствия обязательного поле password
     * Ожидается ошибка с сообщением о необходимости всех полей
     */
    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка создания пользователя без обязательного поля password")
    public void testCreateUserWithoutPassword() {
        // Arrange: создаем пользователя без пароля
        User userWithoutPassword = new User("test@test.com", null, "name");

        // Act: отправляем запрос с неполными данными
        Response response = userClient.create(userWithoutPassword);

        // Assert: проверяем ошибку валидации
        assertRegistrationRequiredFieldsError(response);
    }

    /**
     * Тест проверяет обработку отсутствия обязательного поля name
     * Ожидается ошибка с сообщением о необходимости всех полей
     */
    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка создания пользователя без обязательного поля name")
    public void testCreateUserWithoutName() {
        // Arrange: создаем пользователя без имени
        User userWithoutName = new User("test@test.com", "password", null);

        // Act: отправляем запрос с неполными данными
        Response response = userClient.create(userWithoutName);

        // Assert: проверяем ошибку валидации
        assertRegistrationRequiredFieldsError(response);
    }
}