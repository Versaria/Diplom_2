package praktikum.api.clients;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Базовый клиент для настройки REST-запросов
 */
public class BaseClient {
    // URL API Stellar Burgers
    protected static final String BASE_URL = "https://stellarburgers.education-services.ru";

    // Allure-фильтр, JSON-контент и базовый URL
    protected RequestSpecification getBaseSpec() {
        return given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL);
    }
}