package ru.yandex.practicum.frontui.exceptions;

public class AccountServiceResponseException extends ServiceException {

    public AccountServiceResponseException(String message, String login) {
        super(message, login);
    }
}
