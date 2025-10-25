package ru.yandex.practicum.accountservice.exceptions;

public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
