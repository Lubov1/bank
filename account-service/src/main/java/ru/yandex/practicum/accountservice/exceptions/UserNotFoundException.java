package ru.yandex.practicum.accountservice.exceptions;

public class UserNotFoundException extends AccountException {
    public UserNotFoundException() {
        super("User not found");
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
