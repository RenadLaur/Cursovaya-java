package kz.enu.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Точка входа в Spring Boot-приложение.
 * Запускает встроенный сервер и поднимает REST-сервисы.
 */
@SpringBootApplication
public class RestaurantApplication {
    /**
     * Старт приложения.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }
}
