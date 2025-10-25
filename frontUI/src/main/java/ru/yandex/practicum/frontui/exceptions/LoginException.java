package ru.yandex.practicum.frontui.exceptions;

public class LoginException extends ServiceException{
    public LoginException(String message, String login) {
        super(message, login);
    }
}
