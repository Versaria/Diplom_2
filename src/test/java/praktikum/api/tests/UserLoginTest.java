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
import praktikum.api.models.UserCredentials;
import praktikum.api.utils.DataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

/**
 * Тесты для эндпоинта авторизации пользователя (POST /api/auth/login)
 * Проверяет различные сценарии входа в систему
 */
public class UserLoginTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    // Инициализирует клиент API и создает тестового пользователя
    @Before
    @Step("Инициализация тестовых данных")
    public void setUp() {
        userClient = new UserClient();
        user = DataGenerator.generateRandomUser();

        // Создаем пользователя перед тестами для проверки авторизации
        Response createResponse = userClient.create(user);
        createResponse.then().statusCode(SC_OK);
        accessToken = createResponse.path("accessToken");
    }

    // Очищает тестовые данные путем удаления созданного пользователя
    @After
    @Step("Очистка тестовых данных")
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
     * Общие проверки успешной авторизации
     */
    @Step("Проверка успешной авторизации")
    private void assertSuccessfulLogin(Response response) {
        response.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", not(emptyOrNullString()));
    }

    /**
     * Общие проверки неуспешной авторизации
     */
    @Step("Проверка неуспешной авторизации")
    private void assertFailedLogin(Response response) {
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo(ErrorMessages.INVALID_CREDENTIALS));
    }

    /**
     * Тест проверяет успешную авторизацию с корректными учетными данными
     * Проверяет структуру ответа и наличие токенов доступа
     */
    @Test
    @DisplayName("Успешный логин пользователя")
    @Description("Проверка авторизации пользователя с валидными данными")
    public void testUserLoginSuccess() {
        // Act: отправляем запрос на авторизацию
        Response response = userClient.login(new UserCredentials(user.getEmail(), user.getPassword()));

        // Assert: проверяем успешную авторизацию и структуру ответа
        assertSuccessfulLogin(response);
    }

    /**
     * Тест проверяет обработку неверного пароля при авторизации
     * Ожидается ошибка аутентификации
     */
    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверка авторизации с неверным паролем")
    public void testUserLoginWithWrongPassword() {
        // Act: отправляем запрос с неверным паролем
        Response response = userClient.login(new UserCredentials(user.getEmail(), "wrong_password"));

        // Assert: проверяем ошибку аутентификации
        assertFailedLogin(response);
    }

    /**
     * Тест проверяет обработку несуществующего пользователя
     * Ожидается ошибка аутентификации
     */
    @Test
    @DisplayName("Логин с несуществующим пользователем")
    @Description("Проверка авторизации несуществующего пользователя")
    public void testLoginWithNonExistentUser() {
        // Act: отправляем запрос с несуществующим email
        Response response = userClient.login(new UserCredentials("nonexistent@test.com", "password"));

        // Assert: проверяем ошибку аутентификации
        assertFailedLogin(response);
    }

    /**
     * Тест проверяет обработку отсутствия email при авторизации
     * Ожидается ошибка аутентификации
     */
    @Test
    @DisplayName("Логин без email")
    @Description("Проверка авторизации без указания email")
    public void testLoginWithoutEmail() {
        // Act: отправляем запрос без email
        Response response = userClient.login(new UserCredentials(null, "password"));

        // Assert: проверяем ошибку аутентификации
        assertFailedLogin(response);
    }

    /**
     * Тест проверяет обработку отсутствия пароля при авторизации
     * Ожидается ошибка аутентификации
     */
    @Test
    @DisplayName("Логин без пароля")
    @Description("Проверка авторизации без указания пароля")
    public void testLoginWithoutPassword() {
        // Act: отправляем запрос без пароля
        Response response = userClient.login(new UserCredentials(user.getEmail(), null));

        // Assert: проверяем ошибку аутентификации
        assertFailedLogin(response);
    }
}