## Это репозиторий проекта "Filmorate"  

---

![Диаграмма](/add-friends-likes/filmorate.png)

Примеры SQL запросов

Получить пользователя по id
` ` `
SELECT *
FROM user
WHERE user_id = @user_id
` ` `

Получить всех друзей пользователя
` ` `
SELECT f.user1_id AS users_friends
FROM friends AS f
WHERE f.user2_id = @user_id
AND status = 1
UNION
SELECT f.user2_id AS users_friends
FROM friends AS f
WHERE f.user1_id = @user_id
AND status = 1
` ` `

Получить общих друзей двух пользователей
` ` `
SELECT af1.users_friends AS mutual_friends
FROM (SELECT f.user1_id AS users_friends
FROM friends AS f
WHERE f.user2_id = @first_user_id
AND status = 1
UNION
SELECT f.user2_id AS users_friends
FROM friends AS f
WHERE f.user1_id = @first_user_id
AND status = 1) AS af1
INNER JOIN (SELECT f.user1_id AS users_friends
FROM friends AS f
WHERE f.user2_id = @second_user_id
AND status = 1
UNION
SELECT f.user2_id AS users_friends
FROM friends AS f
WHERE f.user1_id = @second_user_id
AND status = 1) AS af2 ON af1.users_friends=af2.users_friends;
` ` `

Получить фильм по id
` ` `
SELECT *
FROM film
WHERE film_id = @film_id
` ` `

Получить 10 популярных фильмов
` ` `
SELECT f.name AS top_films
FROM film AS f
INNER JOIN likes AS l ON l.film_id=f.film_id
GROUP BY top_films
ORDER BY COUNT(l.user_id)
LIMIT 10;
` ` `
