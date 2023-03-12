package ru.yandex.practicum.filmorate.message;

public enum Message {
    ADD_MODEL("added to the service {}"),
    UPDATED_MODEL("updated to the service {}"),
    EMAIL_CANNOT_BE_EMPTY("Email cannot be empty and must contain the \"@\" character"),
    LOGIN_MAY_NOT_CONTAIN_SPACES("Login may not be empty or contain spaces"),
    DUPLICATE("the model already exists"),
    RELEASE_DATE("The release date can't be earlier - "),
    MODEL_NOT_FOUND("model was not found by the passed ID: "),
    REQUEST_TO_LIKE("Received a request to like the movie {} from the user {}"),
    REQUEST_ALL("Request all models"),
    REQUEST_POPULAR("Request popular films"),
    REQUEST_BY_ID("model request by id {}"),
    ADD_FRIEND("Received a request from a user {} to add friends user {}"),
    REQUEST_LIST_FRIENDS("Received a request for a list of friends user {}"),
    REQUEST_MUTUAL_FRIENDS("Received a request from the user {} for a list of mutual friends with the user {}"),
    DELETE_FRIENDS("Received a request to delete a friend {} from a user {}"),
    DELETE_LIKE("Received a request to remove the like of the movie {} from the user {}"),
    REQUEST_ALL_GENRES("Request genres"),
    REQUEST_ALL_MPA("Request MPA"),
    REQUEST_GENRE("Request genre by ID {}"),
    REQUEST_MPA("Request MPA by ID {}");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}