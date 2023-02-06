package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void before() {
        userController = new UserController();
    }

    @Test
    void createUser() {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.of(1985, 4, 4))
                .build();
        userController.createUser(user);
        List<User> usersList = userController.getListUsers();

        assertEquals(user, usersList.get(0), "Пользователи не равны");
        assertEquals(1, usersList.size(), "Размер списка не верно указан");
    }

    @Test
    void create2User() {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.of(1985, 4, 4))
                .build();
        userController.createUser(user);

        User user2 = User.builder()
                .email("mail@yandex.ru")
                .login("dolore2")
                .name("Nick Name 2")
                .birthday(LocalDate.of(1986, 4, 4))
                .build();
        userController.createUser(user2);
        List<User> usersList = userController.getListUsers();

        assertEquals(user2, usersList.get(1), "Пользователи не равны");
        assertEquals(2, usersList.size(), "Размер списка неверно указан");
    }

    @Test
    void shouldThrowExceptionSaveDuplicateUser() {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.of(1985, 4, 4))
                .build();
        userController.createUser(user);

        User user2 = User.builder()
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.of(1985, 4, 4))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user2));

        assertEquals("the model already exists", exception.getMessage()
                , "exception message проверки на дубликат не верна");
    }

    @Test
    void shouldThrowExceptionSaveEmailIsEmptyUser() {
        User user = User.builder()
                .email("")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.of(1985, 4, 4))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user));

        assertEquals("Email cannot be empty and must contain the \"@\" character"
                , exception.getMessage()
                , "exception message проверки email не верна");
    }

    @Test
    void shouldThrowExceptionSaveLoginIsContainSpaces() {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("do lore")
                .name("Nick Name")
                .birthday(LocalDate.of(1985, 4, 4))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user));

        assertEquals("Login may not be empty or contain spaces"
                , exception.getMessage()
                , "exception message проверки Login не верна");
    }

    @Test
    void createUserIsEmptyName() {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("dolore")
                .name("")
                .birthday(LocalDate.of(1985, 4, 4))
                .build();
        userController.createUser(user);
        assertEquals(user.getLogin(), user.getName(), "Логин и имя не совпадают");
    }

    @Test
    void createUserIsNullName() {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("dolore")
                .name(null)
                .birthday(LocalDate.of(1985, 4, 4))
                .build();
        userController.createUser(user);
        assertEquals(user.getLogin(), user.getName(), "Логин и имя не совпадают");
    }

    @Test
    void updateUser() {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("login")
                .name("Nick Name")
                .birthday(LocalDate.of(1985, 4, 4))
                .build();
        userController.createUser(user);
        List<User> usersList = userController.getListUsers();

        assertEquals(1, usersList.size(), "Размер списка пользователей не соответствует ожидаемому");
        assertEquals(1, usersList.get(0).getId(), "ID сформирован не верно");
        assertEquals(user, usersList.get(0), "Модели User не соответствуют");

        User user2 = User.builder()
                .id(1)
                .email("mail@mail.ru")
                .login("Newlogin")
                .name("Nick Name2")
                .birthday(LocalDate.of(1985, 5, 4))
                .build();
        userController.updateUser(user2);
        usersList = userController.getListUsers();

        assertEquals(1, usersList.size(), "Размер списка пользователей не соответствует ожидаемому");
        assertEquals(1, usersList.get(0).getId(), "ID сформирован не верно");
        assertEquals(user2, usersList.get(0), "Модели User не соответствуют");
    }

    @Test
    void updateUserInvalidIdShouldThrowException() {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("login")
                .name("Nick Name")
                .birthday(LocalDate.of(1985, 4, 4))
                .build();
        userController.createUser(user);
        List<User> usersList = userController.getListUsers();

        assertEquals(1, usersList.size(), "Размер списка пользователей не соответствует ожидаемому");
        assertEquals(1, usersList.get(0).getId(), "ID сформирован не верно");
        assertEquals(user, usersList.get(0), "Модели User не соответствуют");

        User user2 = User.builder()
                .id(3)
                .email("mail@mail.ru")
                .login("Newlogin")
                .name("Nick Name2")
                .birthday(LocalDate.of(1985, 5, 4))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.updateUser(user2));
        assertEquals("model was not found by the passed ID: 3", exception.getMessage()
                , "exception проверки неверный");
    }
}