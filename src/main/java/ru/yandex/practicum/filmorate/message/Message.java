package ru.yandex.practicum.filmorate.message;

public enum Message {
    ADD_MODEL("{} added to the service "),
    UPDATED_MODEL("{} updated to the service "),
    EMAIL_CANNOT_BE_EMPTY("Email cannot be empty and must contain the \"@\" character"),
    LOGIN_MAY_NOT_CONTAIN_SPACES("Login may not be empty or contain spaces"),
    DUPLICATE("the model already exists"),
    RELEASE_DATE("The release date can't be earlier - "),
    MODEL_NOT_FOUND("model was not found by the passed ID: ");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}