package praktikum.api.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import praktikum.api.constants.Endpoints;
import praktikum.api.models.Order;

/**
 * Клиент для работы с API заказов
 */
public class OrderClient extends BaseClient {
    // Создает новый заказ в системе
    @Step("Создание заказа")
    // Создаем базовую спецификацию запроса
    public Response create(Order order, String accessToken) {
        RequestSpecification spec = getBaseSpec();
        // Добавляем заголовок авторизации если передан токен
        if (accessToken != null && !accessToken.isEmpty()) {
            spec = spec.header("Authorization", accessToken);
        }

        return spec
                .body(order)
                .when()
                .post(Endpoints.ORDERS);
    }
    // Получает список всех доступных ингредиентов из системы
    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return getBaseSpec()
                .when()
                .get(Endpoints.INGREDIENTS);
    }
}