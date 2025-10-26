package praktikum.api.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.api.constants.Endpoints;
import praktikum.api.models.User;
import praktikum.api.models.UserCredentials;

/**
 * Клиент для работы с API пользователей
 */
public class UserClient extends BaseClient {
    // Создает нового пользователя в системе
    @Step("Создание пользователя")
    public Response create(User user) {
        return getBaseSpec()
                .body(user)
                .when()
                .post(Endpoints.REGISTER);
    }
    // Выполняет авторизацию пользователя в системе
    @Step("Логин пользователя")
    public Response login(UserCredentials credentials) {
        return getBaseSpec()
                .body(credentials)
                .when()
                .post(Endpoints.LOGIN);
    }
    // Удаляет пользователя из системы
    @Step("Удаление пользователя")
    public void delete(String accessToken) {
        getBaseSpec()
                .header("Authorization", accessToken)
                .when()
                .delete(Endpoints.USER);
    }
}