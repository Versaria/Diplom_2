package praktikum.api.constants;

/**
 * Константы сообщений об ошибках от API
 */
@SuppressWarnings({"unused", "JavaDoc"})
public class ErrorMessages {
    public static final String INGREDIENTS_REQUIRED = "Ingredient ids must be provided";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String REQUIRED_FIELDS_MISSING = "Email, password and name are required fields";
    public static final String INVALID_CREDENTIALS = "email or password are incorrect";
    public static final String UNAUTHORIZED = "You should be authorised";
    public static final String EMAIL_ALREADY_EXISTS = "User with such email already exists";

    private ErrorMessages() {
        // Утилитный класс
    }
}