package com.zendesk.challenge.exception;

public class UserInputException extends Throwable {

    @Override
    public String getMessage() {
        return "There was an issue processing your input, please try again.\n\n";
    }
}
