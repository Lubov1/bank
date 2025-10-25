package ru.yandex.practicum.frontui.exceptions;

public class CashServiceResponseException extends ServiceException {
    public CashServiceResponseException(String message, String login){
        super(message, login);
    }
}
