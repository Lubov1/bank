package ru.yandex.practicum.accountservice.exceptions;

public class UserExistsException extends AccountException {
    public UserExistsException() {
        super("user already exists");
    }
    public UserExistsException(String message) {
        super(message);
    }
}
