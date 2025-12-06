package kz.enu.restaurant.exception;

/**
 * Исключение уровня данных меню.
 * Используется при ошибках чтения файлов с ингредиентами и блюдами
 * (например, отсутствует файл, неверный формат строки и т.д.).
 */
public class MenuDataException extends RuntimeException {

    /**
     * Создаёт новое исключение с сообщением.
     *
     * @param message текст ошибки
     */
    public MenuDataException(String message) {
        super(message);
    }

    /**
     * Создаёт новое исключение с сообщением и исходной причиной.
     *
     * @param message текст ошибки
     * @param cause   исходное исключение
     */
    public MenuDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
