package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.FilmLikeDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.FriendDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final FriendDaoImpl friendDao;
	private final FilmLikeDaoImpl filmLikeDao;

	User user = User.builder()
			.email("aaa@bbb.com")
			.login("Aa777")
			.name("Name")
			.birthday(LocalDate.of(1990, 7, 8))
			.build();

	Film film = Film.builder()
			.name("Inception")
			.id(1)
			.description("The best film")
			.releaseDate(LocalDate.of(2010, 7, 8))
			.duration(148)
			.mpa(new Rating(1, "G"))
			.rate(1)
			.build();

	@Test
	void contextLoads() {
	}

	@Test
	public void testGetUser() {
		User user = userStorage.get(1L);
		Assertions.assertEquals("dolore", user.getLogin());
	}

	@Test
	public void testGetAllUsers() {
		Collection<User> users = userStorage.getAll();
		Assertions.assertEquals(3, users.size());
	}
	@Test
	public void testCreateUser() {
		Long id = userStorage.create(user).getId();
		User testUser = userStorage.get(id);
		Assertions.assertEquals("aaa@bbb.com", user.getEmail());
		userStorage.remove(user);
	}

	@Test
	public void testRemoveUser() {
		userStorage.create(user);
		userStorage.remove(user);
		Collection<User> users = userStorage.getAll();
		Assertions.assertEquals(3, users.size());
	}

	@Test
	public void testUpdateUser() {
		Long id = userStorage.create(user).getId();
		user.setEmail("samara163@yandex.ru");
		userStorage.update(user);
		User testUser = userStorage.get(id);
		Assertions.assertEquals("samara163@yandex.ru", user.getEmail());
		userStorage.remove(user);
	}

	@Test
	public void testGetFriends() {
		List<User> friends = friendDao.getFriends(1L);
		Assertions.assertEquals(3, friends.get(0).getId());
	}

	@Test
	public void testAddFriends() {
		friendDao.addFriend(1L, 2L);
		List<User> friends = friendDao.getFriends(1L);
		Assertions.assertEquals(2, friends.size());
	}

	@Test
	public void testRemoveFriends() {
		friendDao.removeFriend(2L, 3L);
		List<User> friends = friendDao.getFriends(2L);
		Assertions.assertTrue(friends.isEmpty());
	}

	@Test
	public void testGetFilm() {
		Film film = filmStorage.get(1L);
		Assertions.assertEquals("Film first", film.getName());
	}

	@Test
	public void testGetAllFilms() {
		Collection<Film> films = filmStorage.getAll();
		Assertions.assertEquals(2, films.size());
	}
	@Test
	public void testCreateFilm() {
		Long id = filmStorage.create(film).getId();
		Film testFilm = filmStorage.get(id);
		Assertions.assertEquals("Inception", film.getName());
		filmStorage.remove(film);
	}

	@Test
	public void testRemoveFilm() {
		filmStorage.create(film);
		filmStorage.remove(film);
		Collection<Film> films = filmStorage.getAll();
		Assertions.assertEquals(2, films.size());
	}

	@Test
	public void testUpdateFilm() {
		Long id = filmStorage.create(film).getId();
		film.setDescription("Ooo");
		filmStorage.update(film);
		Film testFilm = filmStorage.get(id);
		Assertions.assertEquals("Ooo", film.getDescription());
		filmStorage.remove(film);
	}

	@Test
	public void TestGetTopFilms() {
		filmLikeDao.putLike(1L, 2L);
		List<Film> films = filmLikeDao.getTop(1);
		Assertions.assertEquals("Film first", films.get(0).getName());
	}

	@Test
	public void testPutLike() {
		filmLikeDao.putLike(2L, 1L);
		filmLikeDao.putLike(2L, 2L);
		List<Film> films = filmLikeDao.getTop(1);
		Assertions.assertEquals("New film", films.get(0).getName());
		filmLikeDao.removeLike(2L, 1L);
		filmLikeDao.removeLike(2L, 2L);
	}

	@Test
	public void TestRemoveLike() {
		filmLikeDao.putLike(2L, 1L);
		filmLikeDao.putLike(2L, 2L);

		filmLikeDao.removeLike(2L, 1L);
		filmLikeDao.removeLike(2L, 2L);
		List<Film> films = filmLikeDao.getTop(1);
		Assertions.assertEquals("Film first", films.get(0).getName());
	}

}
