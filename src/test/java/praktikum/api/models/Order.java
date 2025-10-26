package praktikum.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Модель данных заказа
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private List<String> ingredients;
}