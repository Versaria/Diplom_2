package praktikum.api.utils;

import com.github.javafaker.Faker;
import praktikum.api.models.User;

import java.util.Locale;

/**
 * Утилита для генерации тестовых данных
 */
public class DataGenerator {
    // Генератор случайных данных на русском языке
    private static final Faker faker = new Faker(new Locale("ru"));

    // Создает случайного пользователя с уникальным email
    public static User generateRandomUser() {

        // Добавляем timestamp для гарантии уникальности email
        String timestamp = String.valueOf(System.currentTimeMillis());
        String username = faker.name().username().toLowerCase() + timestamp;

        return new User(
                username + "@test.com", // Уникальный email
                faker.internet().password(8, 12), // Случайный пароль
                faker.name().firstName() + " " + faker.name().lastName() // Полное имя
        );
    }
}