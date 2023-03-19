package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserService userService;
	private final FilmService filmService;
	private final GenreService genreService;
	private final MpaService mpaService;
	private User user;

	private Film film;

	@BeforeEach
	void before(){
		film = new Film();
		film.setName("film");
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.of(1999, 1, 1));
		film.setDuration(100);
		Mpa mpa = new Mpa();
		mpa.setId(1);
		mpa.setName("G");
		film.setMpa(mpa);

		user = User.builder()
				.email("mail@mail.ru")
				.login("dolore")
				.name("Nick Name")
				.birthday(LocalDate.of(1985, 4, 4))
				.build();
	}

	@Test
	void testAllStorageMethods() {
		filmService.addModel(film);
		List<Film> filmList = filmService.getAllModels();
		assertEquals(1, filmList.size(), "Размер списка фильмов не соответствует ожидаемому");
		assertEquals(1, filmList.get(0).getId(), "ID сформирован не верно");

		Film film2 = new Film();
		film2.setId(1);
		film2.setName("film2");
		film2.setDescription("Description");
		film2.setReleaseDate(LocalDate.of(1998, 1, 1));
		film2.setDuration(150);
		Mpa mpa = new Mpa();
		mpa.setId(1);
		mpa.setName("G");
		film2.setMpa(mpa);
		Genre genre = new Genre();
		genre.setId(1);
		Genre genre2 = new Genre();
		genre2.setId(3);
		Set<Genre> genres = new HashSet<>();
		genres.add(genre);
		genres.add(genre2);
		film2.setGenres(genres);
		filmService.updateModel(film2);

		filmList = filmService.getAllModels();

		assertEquals(1, filmList.size(), "Размер списка фильмов не соответствует ожидаемому");
		assertEquals(1, filmList.get(0).getId(), "ID сформирован не верно");
		assertEquals(filmService.findModelById(1).getId(), filmList.get(0).getId()
				, "Модели Film не соответствуют");

		Film film3 = new Film();
		film3.setName("film # 3");
		film3.setDescription("Description film # 3");
		film3.setReleaseDate(LocalDate.of(1999, 1, 1));
		film3.setDuration(100);
		Mpa mpa2 = new Mpa();
		mpa2.setId(1);
		mpa2.setName("G");
		film3.setMpa(mpa2);
		filmService.addModel(film3);
		filmService.putLike(2, 1);
		List<Film> films = filmService.getAllModels();
		List<Film> popularFilms = filmService.getPopularFilms(2);

		assertEquals(films.size(), popularFilms.size(), "Размеры списков не равны.");
		assertEquals(2, popularFilms.get(0).getId(), "ИД популярного фильма в списке не равен 2.");

		filmService.deleteLike(2, 1);
		popularFilms = filmService.getPopularFilms(2);

		assertEquals(1, popularFilms.get(0).getId(), "ИД популярного фильма в списке не равен 1.");
	}

	@Test
	void shouldThrowExceptionCreatingInvalidDateFilm() {
		Film film = new Film();
		film.setName("film");
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.of(1888, 1, 1));
		film.setDuration(100);

		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> filmService.addModel(film));

		assertEquals("The release date can't be earlier - 1895-12-28", exception.getMessage()
				, "exception message проверки дата релиза не верна");
	}

	@Test
	void updateFilmInvalidIdShouldThrowException() {
		Film film2 = new Film();
		film2.setId(999);
		film2.setName("film2");
		film2.setDescription("Description");
		film2.setReleaseDate(LocalDate.of(1998, 1, 1));
		film2.setDuration(150);
		Mpa mpa = new Mpa();
		mpa.setId(1);
		mpa.setName("G");
		film2.setMpa(mpa);

		final NotFoundException exception = assertThrows(
				NotFoundException.class,
				() -> filmService.updateModel(film2));
		assertEquals("model was not found by the passed ID: 999", exception.getMessage()
				, "exception проверки неверный");
	}

	@Test
	void findFilmByInvalidIDShouldThrowException(){
		final NotFoundException exception = assertThrows(
				NotFoundException.class,
				() -> filmService.findModelById(999));
		assertEquals("model was not found by the passed ID: 999", exception.getMessage()
				, "exception проверки неверный");
	}

	@Test
	void addAndDeleteLikeInvalidUserIDShouldThrowException() {
		final NotFoundException exception = assertThrows(
				NotFoundException.class,
				() -> filmService.putLike(1,-2));
		assertEquals("model was not found by the passed ID: -2", exception.getMessage()
				, "exception проверки неверный");

		final NotFoundException exception2 = assertThrows(
				NotFoundException.class,
				() -> filmService.deleteLike(1,-2));
		assertEquals("model was not found by the passed ID: -2", exception2.getMessage()
				, "exception проверки неверный");
	}

	@Test
	void GetAllAndGetInvalidIdGenres() {
		Genre genreById = genreService.getGenreById(1);
		assertEquals("Комедия", genreById.getName(), "Название жанров не верное.");

		List<Genre> genres = genreService.getGenres();

		assertEquals(6, genres.size(), "Размер жанров не соответствует ожидаемому.");

		final NotFoundException exception3 = assertThrows(
				NotFoundException.class,
				() -> genreService.getGenreById(-2));
		assertEquals("model was not found by the passed ID: -2", exception3.getMessage()
				, "exception проверки неверный");
	}

	@Test
	void GetAllAndGetInvalidIdRatings() {
		Mpa rating = mpaService.getRatingById(1);

		assertEquals("G", rating.getName(), "Название рейтинга не верное.");

		List<Mpa> ratings = mpaService.getRatings();

		assertEquals(5, ratings.size(), "Размер списка рейтингов не соответствует ожидаемому.");

		final NotFoundException exception4 = assertThrows(
				NotFoundException.class,
				() -> mpaService.getRatingById(-2));
		assertEquals("model was not found by the passed ID: -2", exception4.getMessage()
				, "exception проверки неверный");
	}
	@Test
	void testAllUserStorageMethods() {
		user = userService.addModel(user);
		List<User> usersList = userService.getAllModels();

		assertEquals(user, usersList.get(0), "Пользователи не равны");
		assertEquals(1, usersList.size(), "Размер списка не верно указан");

		User user2 = User.builder()
				.id(user.getId())
				.email("mailUpdate@mail.ru")
				.login("Newlogin")
				.name("Nick Name Update")
				.birthday(LocalDate.of(1985, 5, 4))
				.build();
		user = userService.updateModel(user2);
		usersList = userService.getAllModels();

		assertEquals(1, usersList.size(), "Размер списка пользователей не соответствует ожидаемому");
		assertEquals(user.getId(), usersList.get(0).getId(), "ID сформирован не верно");
		assertEquals(userService.findModelById(user.getId()).getId(), usersList.get(0).getId()
				, "Модели User не соответствуют");

		User friend = User.builder()
				.email("mail@yandex.ru")
				.login("dolore")
				.name("Nick Name")
				.birthday(LocalDate.of(1986, 4, 5))
				.build();
		friend = userService.addModel(friend);
		userService.putFriend(user.getId(), friend.getId());
		List<User> listUser = userService.getFriends(user.getId());
		List<User> listFriends = userService.getFriends(friend.getId());

		assertEquals(1, listUser.size(), "Размер списка друзей User не соответствуют");
		assertTrue(listFriends.isEmpty(), "Размер списка друзей Friend не соответствуют");

		User user3 = User.builder()
				.email("mail2@yandex.ru")
				.login("dolore2")
				.name("Nick Name2")
				.birthday(LocalDate.of(1986, 4, 5))
				.build();
		user3 = userService.addModel(user3);

		userService.putFriend(user.getId(), user3.getId());
		userService.putFriend(friend.getId(), user3.getId());
		listUser = userService.getFriends(user.getId());
		listFriends = userService.getFriends(friend.getId());
		List<User> listMutualFriends = userService.getListMutualFriends(user.getId(), friend.getId());

		assertEquals(2, listUser.size(), "Размер списка друзей User не соответствуют");
		assertEquals(1, listFriends.size(), "Размер списка друзей Friend не соответствуют");
		assertEquals(1, listMutualFriends.size(), "Размер списка общих друзей не равен 1");
		assertEquals(user3.getId(), listMutualFriends.get(0).getId()
				, "Значение в списке общих друзей не соответствует.");

		userService.deleteFriend(user.getId(), friend.getId());
		listUser = userService.getFriends(user.getId());

		assertEquals(1, listUser.size(), "Размер списка друзей User не соответствуют");
		assertEquals(user3.getId(), listUser.get(0).getId(), "Список друзей User после удаления не соответствуют");
	}

	@Test
	void shouldThrowExceptionSaveEmailIsEmptyUser() {
		User user2 = User.builder()
				.email("")
				.login("dolore")
				.name("Nick Name")
				.birthday(LocalDate.of(1985, 4, 4))
				.build();

		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> userService.addModel(user2));

		assertEquals("Email cannot be empty and must contain the \"@\" character"
				, exception.getMessage()
				, "exception message проверки email не верна");
	}

	@Test
	void shouldThrowExceptionSaveLoginIsContainSpaces() {
		User user3 = User.builder()
				.email("mail@mail.ru")
				.login("do lore")
				.name("Nick Name")
				.birthday(LocalDate.of(1985, 4, 4))
				.build();

		final ValidationException exception = assertThrows(
				ValidationException.class,
				() -> userService.addModel(user3));

		assertEquals("Login may not be empty or contain spaces"
				, exception.getMessage()
				, "exception message проверки Login не верна");
	}

	@Test
	void createUserIsEmptyName() {
		User user4 = User.builder()
				.email("mailIsEmptyName@mail.ru")
				.login("IsEmptyName")
				.name("")
				.birthday(LocalDate.of(1985, 4, 4))
				.build();
		user4 = userService.addModel(user4);
		assertEquals(user4.getLogin(), user4.getName(), "Логин и имя не совпадают");
		userService.deleteModelById(user4.getId());
	}

	@Test
	void createUserIsNullName() {
		User isNullName = User.builder()
				.email("mailIsNullName@mail.ru")
				.login("IsNullName")
				.name(null)
				.birthday(LocalDate.of(1985, 4, 4))
				.build();
		isNullName = userService.addModel(isNullName);
		assertEquals(isNullName.getLogin(), isNullName.getName(), "Логин и имя не совпадают");
		userService.deleteModelById(isNullName.getId());
	}

	@Test
	void updateUserInvalidIdShouldThrowException() {
		User user6 = User.builder()
				.id(6)
				.email("mail@mail.ru")
				.login("Newlogin")
				.name("Nick Name2")
				.birthday(LocalDate.of(1985, 5, 4))
				.build();

		final NotFoundException exception = assertThrows(
				NotFoundException.class,
				() -> userService.updateModel(user6));
		assertEquals("model was not found by the passed ID: 6", exception.getMessage()
				, "exception проверки неверный");
	}

	@Test
	void findUserByInvalidIDShouldThrowException() {
		final NotFoundException exception = assertThrows(
				NotFoundException.class,
				() -> userService.findModelById(9999));
		assertEquals("model was not found by the passed ID: 9999", exception.getMessage()
				, "exception проверки неверный");
	}
}