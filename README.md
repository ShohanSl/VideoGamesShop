# Video Games Shop (Каталог видеоигр)

Данный проект представляет собой REST-сервис для управления каталогом видеоигр, разработчиками, издателями, категориями и библиотеками пользователей. Сервис реализован с использованием Spring Boot, Spring Data JPA, PostgreSQL и демонстрирует принципы построения многослойной архитектуры, обработку HTTP-запросов, DTO, маппинг, обработку ошибок, транзакционность и оптимизацию запросов.

---
## [SonarCloud](https://sonarcloud.io/summary/new_code?id=ShohanSl_VideoGamesShop)

## Технологический стек

- **Java 17** — язык программирования
- **Spring Boot 3.x** — основной фреймворк
- **Spring Web** — создание REST API
- **Spring Data JPA** — работа с базой данных
- **PostgreSQL** — реляционная база данных
- **Hibernate** — ORM
- **Spring Boot Validation** — валидация входящих данных
- **Lombok** — сокращение шаблонного кода
- **Maven** — система сборки
- **Postman** — тестирование API

## Архитектура

Приложение разделено на стандартные слои:

- **Controller** — обрабатывает HTTP-запросы, возвращает ответы.
- **Service** — содержит бизнес-логику, управляет транзакциями.
- **Repository** — интерфейсы Spring Data JPA для работы с БД.
- **Entity** — JPA-сущности, отображаемые на таблицы.
- **DTO** — объекты передачи данных для разных сценариев (создание, обновление, ответы).
- **Mapper** — преобразование Entity ↔ DTO. Часть мапперов оформлены как Spring-бины (для внедрения зависимостей), часть — как утилитные классы со статическими методами.
- **Exception** — кастомные исключения и глобальный обработчик ошибок.

## Модель данных

### Сущности и связи

- **Game** — игра. Содержит поля: id, title, price, releaseDate, description.
  - Связана с **Developer** (ManyToOne) — разработчик.
  - Связана с **Publisher** (ManyToOne) — издатель.
  - Связана с **Category** (ManyToMany) — категории.
  - Связана с **Library** (ManyToMany) — библиотеки пользователей.
- **Developer** — разработчик. Поля: id, name, country, foundedDate.
  - Имеет список игр (OneToMany, mappedBy = "developer") с каскадным сохранением (CascadeType.ALL) и orphanRemoval.
- **Publisher** — издатель. Поля: id, name, country, foundedDate.
  - Имеет список игр (OneToMany, mappedBy = "publisher").
- **Category** — категория (жанр). Поля: id, name.
  - Имеет список игр (ManyToMany, mappedBy = "categories").
- **Library** — библиотека игр пользователя (ранее User). Поля: id, username.
  - Имеет список игр (ManyToMany, владелец связи, таблица `library_game`) с жадной загрузкой (FetchType.EAGER).

### База данных

Используется PostgreSQL. Схема создается автоматически через Hibernate (`spring.jpa.hibernate.ddl-auto=update`). Основные таблицы:
- `games`
- `developers`
- `publishers`
- `categories`
- `libraries`
- `game_category` (связующая)
- `library_game` (связующая)

## API Endpoints

### Категории (Categories)

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/categories` | Список всех категорий |
| GET | `/categories/{id}` | Получить категорию по ID |
| POST | `/categories` | Создать новую категорию |
| PUT | `/categories/{id}` | Обновить категорию |
| DELETE | `/categories/{id}` | Удалить категорию |

### Разработчики (Developers)

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/developers` | Список всех разработчиков (краткая информация) |
| GET | `/developers/{id}` | Полная информация о разработчике с его играми |
| POST | `/developers` | Создать разработчика |
| PUT | `/developers/{id}` | Обновить разработчика |
| DELETE | `/developers/{id}` | Удалить разработчика |
| POST | `/developers/{developerId}/games/{gameId}` | Добавить игру разработчику |
| DELETE | `/developers/{developerId}/games/{gameId}` | Удалить игру у разработчика |
| POST | `/developers/with-games/with-tx` | Создать разработчика с играми (в транзакции) — демонстрация отката |
| POST | `/developers/with-games/without-tx` | Создать разработчика с играми (без транзакции) — демонстрация частичного сохранения |

### Издатели (Publishers)

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/publishers` | Список всех издателей |
| GET | `/publishers/{id}` | Полная информация об издателе с его играми |
| POST | `/publishers` | Создать издателя |
| PUT | `/publishers/{id}` | Обновить издателя |
| DELETE | `/publishers/{id}` | Удалить издателя |
| POST | `/publishers/{publisherId}/games/{gameId}` | Добавить игру издателю |
| DELETE | `/publishers/{publisherId}/games/{gameId}` | Удалить игру у издателя |

### Игры (Games)

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/games` | Получить каталог игр (краткая информация с категориями) |
| GET | `/games?categoryIds=1,2` | Фильтр игр по нескольким категориям (логическое И) |
| GET | `/games/{id}` | Полная информация об игре (включая разработчика, издателя, категории) |
| POST | `/games` | Создать игру (требуется developerId, publisherId, список categoryIds) |
| PUT | `/games/{id}` | Обновить игру |
| DELETE | `/games/{id}` | Удалить игру |

### Библиотеки (Libraries)

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/libraries` | Список всех библиотек |
| GET | `/libraries/{id}` | Полная информация о библиотеке со списком игр |
| POST | `/libraries` | Создать библиотеку (требуется username) |
| PUT | `/libraries/{id}` | Обновить библиотеку |
| DELETE | `/libraries/{id}` | Удалить библиотеку |
| POST | `/libraries/{libraryId}/games/{gameId}` | Добавить игру в библиотеку |
| DELETE | `/libraries/{libraryId}/games/{gameId}` | Удалить игру из библиотеки |

## DTO (Data Transfer Objects)

Для каждой сущности используются несколько DTO:

- `CreateRequest` — для создания (например, `GameCreateRequest` содержит поля + `developerId`, `publisherId`, `categoryIds`).
- `UpdateRequest` — для обновления (все поля опциональны).
- `CatalogResponse` — краткая информация для списка.
- `FullResponse` — полная информация, включая связанные сущности.

Также существуют простые DTO для вложенных объектов (например, `CategoryDto`, `GameSimpleDto`).

## Мапперы (Mappers)

Преобразование между Entity и DTO выполняется в классах-мапперах. Часть из них (например, `GameMapper`, `DeveloperMapper`) являются Spring-бинами (`@Component`), что позволяет внедрять их в сервисы и использовать нестатические методы. Другие мапперы (например, `CategoryMapper`, `PublisherMapper`) оформлены как утилитные классы со статическими методами. Такое смешанное решение выбрано для демонстрации разных подходов.

## Обработка ошибок

В проекте реализована централизованная обработка исключений с помощью `@ControllerAdvice`.

- Для каждого типа ресурса созданы кастомные исключения: `GameNotFoundException`, `DeveloperNotFoundException`, `PublisherNotFoundException`, `CategoryNotFoundException`, `LibraryNotFoundException` (все унаследованы от `RuntimeException`).
- Глобальный обработчик `GlobalExceptionHandler` перехватывает эти исключения и возвращает клиенту JSON с соответствующим HTTP-статусом (404 Not Found) и понятным сообщением.
- Также обрабатываются ошибки валидации (`MethodArgumentNotValidException`) с возвратом 400 Bad Request и списком ошибок по полям.
- Все непредвиденные исключения логируются и возвращают 500 Internal Server Error.

Пример ответа при ошибке:
```json
{
  "timestamp": "2026-03-05T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Game not found with id: 99"
}
```
## Транзакционность и демонстрация частичного сохранения

Сервисы аннотированы `@Transactional` (на уровне класса), что обеспечивает выполнение операций в транзакции. Однако для демонстрации проблем без транзакции созданы специальные методы:

- `createDeveloperWithGamesWithoutTransaction` — выполняется без транзакции (`propagation = Propagation.NOT_SUPPORTED`). При возникновении ошибки в середине процесса (например, при сохранении второй игры с некорректными данными) разработчик и первая игра сохраняются, а вторая — нет. Это демонстрирует **частичное сохранение**.
- `createDeveloperWithGamesWithTransaction` — выполняется в транзакции. При любой ошибке все изменения откатываются (разработчик и игры не сохраняются).

## Оптимизация запросов (N+1 проблема)

Для избежания проблемы N+1 при загрузке связанных коллекций в репозиториях используются методы с `JOIN FETCH`:

- `findAllWithCategories()` в `GameRepository` — загружает игры вместе с категориями одним запросом.
- `findByIdWithGames()` в `DeveloperRepository` — загружает разработчика с его играми.
- Аналогичные методы для других сущностей.

Это позволяет сократить количество запросов к БД и повысить производительность.
