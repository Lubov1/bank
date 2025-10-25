package ru.yandex.practicum.frontui.exceptions;

public class ServiceException extends RuntimeException {
    private String login;
    public ServiceException(String message, String login) {
        super(message);
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
