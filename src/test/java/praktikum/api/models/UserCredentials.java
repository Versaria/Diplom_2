package praktikum.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель учетных данных пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {
    private String email;
    private String password;
}