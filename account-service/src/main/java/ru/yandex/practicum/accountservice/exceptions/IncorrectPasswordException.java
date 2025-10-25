package ru.yandex.practicum.accountservice.exceptions;

public class IncorrectPasswordException extends AccountException{
    public IncorrectPasswordException() {
        super("Incorrect Password");
    }
}
