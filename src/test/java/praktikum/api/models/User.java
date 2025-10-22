package praktikum.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель данных пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;
}