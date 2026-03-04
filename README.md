#  Messenger Backend API

Бэкенд для мессенджера на Spring Boot с поддержкой личных чатов, групп, каналов и уведомлений.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue.svg)](https://www.postgresql.org)

---

##  Содержание

- [Технологии](#-технологии)
- [Архитектура и структура проекта](#-архитектура-и-структура-проекта)
- [Быстрый старт](#-быстрый-старт)
- [API Endpoints](#-api-endpoints)
- [Модели данных](#-модели-данных)
- [Аутентификация](#-аутентификация)
- [Конфигурация](#-конфигурация)
- [Тестирование](#-тестирование)
- [Авторы](#-авторы)

---

##  Технологии

| Компонент | Версия | Описание |
|-----------|--------|----------|
| **Java** | 21 | Язык программирования |
| **Spring Boot** | 3.2.3 | Фреймворк для REST API |
| **Spring Data JPA** | 6.1.4 | Работа с базой данных |
| **Hibernate** | 6.4.4.Final | ORM-фреймворк |
| **PostgreSQL** | 15+ | Система управления базами данных |
| **Lombok** | 1.18.30 | Генерация кода (геттеры, сеттеры) |
| **Swagger/OpenAPI** | 3.0 | Интерактивная документация API |
| **Maven** | 3.9+ | Система сборки |
| **Liquibase** | 4.24.0 | Управление миграциями БД  |

---

##  Архитектура и структура проекта
### Архитектура
Проект следует **многослойной (layered) архитектуре** с разделением ответственности между уровнями:
- **Presentation Layer** (Controller) - обработка HTTP запросов
- **Business Layer** (Service) - бизнес-логика
- **Data Access Layer** (Repository) - работа с базой данных
- **Domain Layer** (Entity/Model) - доменные объекты
  src/main/java/org/example
### Структура
- **messenger_db**
  -  **src/main/java/org/example/**
      - **config** - Конфигурация приложения
      - **controller** - REST контроллеры
      - **dto** - Объект передачи данных
        - **Attachment** - DTO для вложений файлов
        - **Auth** - DTO для аутентификации и регистрации
        - **Chat** - DTO для чатов
        - **ChatMember** - DTO для участников чата
        - **Contact** - DTO для контактов
        - **CreateMessage** - DTO для создания сообщений
        - **MarkAsRead** - DTO для отметки прочтения
        - **Message** - DTO для сообщений
        - **MessageRead** - DTO о прочтении сообщений
        - **Notification** - DTO для уведомлений
        - **Reaction** - DTO для реакций на сообщения
        - **Session** - DTO для сессий пользователей
        - **User** - DTO для пользователей
        - **UserSettings** - DTO для настроек пользователя
      - **entity** - JPA-сущности
      - **enums** - Перечисление
      - **exeption** - Обработка ошибок
      - **filter** - Фильтры
      - **repository** - Репозитории (доступ к БД)
      - **service** - Сервисный слой
      - **util** - Вспомогательные утилиты
      - **Main.java** - Главный класс приложения
  - **resourses**
      - **db.changelog/** - Миграции БД (Liquibase)
      - **application.properties** - Основные настройки приложения
  - **src/test/java/** - Тесты
  - **.gitignore** 
  - **pom.xml** - Файл конфигурации Maven

##  Быстрый старт

### 1. Проверьте требования

| Компонент | Версия | Команда проверки |
|-----------|--------|-----------------|
| **Java** | 21+ | `java -version` |
| **PostgreSQL** | 15+ | `psql --version` |
| **Maven** | 3.9+ | `mvn -version` |
### 2. Установка и запуск

    # Клонирование репозитория
    git clone <repository-url>
    cd <project-directory>
    
    # Настройка подключения к базе данных
    # Откройте src/main/resources/application.properties 
    # и укажите параметры вашего окружения
    
    # Запуск приложения
    mvn spring-boot:run
##  API Endpoints

Полная интерактивная документация доступна через **Swagger UI**:

| Интерфейс | URL |
|-----------|-----|
|  **Swagger UI** | http://localhost:8080/swagger-ui.html |

### Основные модули API

| Модуль | Методы | Endpoint | Описание                          |
|--------|--------|----------|-----------------------------------|
|  **Auth** | `POST` | `/api/auth` | Регистрация, вход, refresh токена |
|  **Users** | `GET`, `POST`, `PUT`, `DELETE`, `PATCH` | `/api/users` | Управление пользователями         |
|  **Chats** | `GET`, `POST`, `PUT`, `DELETE` | `/api/chats` | Личные чаты, группы, каналы       |
|  **Messages** | `GET`, `POST`, `PUT`, `DELETE` | `/api/messages` | Отправка и получение сообщений    |
|  **Notifications** | `GET`, `POST`, `PUT`, `DELETE` | `/api/notifications` | Система уведомлений               |
|  **Contacts** | `GET`, `POST`, `PUT`, `DELETE` | `/api/contacts` | Контакты и блокировка             |
|  **Reactions** | `GET`, `POST`, `DELETE` | `/api/reactions` | Реакции на сообщения              |
|  **Attachments** | `GET`, `POST`, `DELETE` | `/api/attachments` | Файлы и медиа-вложения            |
|  **Chat Members** | `GET`, `POST`, `DELETE` | `/api/chat-members` | Участники чатов и роли            |
|  **User Settings** | `GET`, `POST`, `PUT`, `DELETE` | `/api/user-settings` | Настройки пользователя            |
|  **Sessions** | `GET`, `POST`, `PUT`, `DELETE` | `/api/sessions` | Управление сессиями               |
|  **Message Reads** | `GET`, `POST`, `DELETE` | `/api/message-reads` | Статусы прочтения сообщений       |

>  **Совет:** Запустите приложение (`mvn spring-boot:run`) и откройте Swagger UI для тестирования всех эндпоинтов в реальном времени.

###  Аутентификация

API использует **JWT Bearer Token** для защиты эндпоинтов.

**Как использовать в Swagger UI:**
1. Нажмите кнопку  **Authorize** в правом верхнем углу
2. Введите токен в формате: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
3. Нажмите **Authorize**
4. Все защищённые запросы будут отправляться с токеном

**Получение токена:**
    ```http
    POST /api/auth/login
    Content-Type: application/json
    
    {
      "username": "ivan_dev",
      "password": "securePassword123"
    }
    ##  Модели Данных

## Основные сущности проекта. Полные спецификации доступны в **Swagger UI** → `Schemas`.

###  User (Пользователь)

    {
      "id": 1,
      "username": "ivan_dev",
      "email": "ivan@example.com",
      "displayName": "Иван Иванов",
      "avatarUrl": "https://example.com/avatar.jpg",
      "status": "online | offline | away | busy",
      "lastSeen": "2026-02-28T12:00:00",
      "createdAt": "2026-02-28T12:00:00"
    }
###  Chat (Чат)

    {
      "id": 1,
      "name": "Рабочий чат",
      "type": "private_chat | group | channel",
      "avatarUrl": "https://example.com/chat.jpg",
      "createdBy": { "id": 1, "username": "ivan_dev" },
      "createdAt": "2026-02-28T12:00:00",
      "lastMessageTime": "2026-02-28T12:30:00"
    }
###  Message (Сообщение)
    {
      "id": 1,
      "chat": { "id": 1, "name": "Рабочий чат" },
      "sender": { "id": 1, "username": "ivan_dev" },
      "content": "Привет всем!",
      "messageType": "text | image | video | audio | file",
      "isEdited": false,
      "isDeleted": false,
      "deliveryStatus": "sent | delivered | read",
      "createdAt": "2026-02-28T12:00:00"
    }
###  Notification (Уведомление)
    {
      "id": 1,
      "type": "new_message | friend_request | system | mention | group_invite",
      "title": "Новое сообщение",
      "body": "Иван отправил вам сообщение",
      "entityId": 5,
      "entityType": "message",
      "isRead": false,
      "createdAt": "2026-02-28T12:00:00",
      "readAt": "2026-02-28T12:05:00"
    }
###  Contact (Контакт)
    {
      "id": 1,
      "user": { "id": 1, "username": "ivan_dev" },
      "contactUser": { "id": 2, "username": "maria_design" },
      "nickname": "Маша Дизайн",
      "isBlocked": false,
      "createdAt": "2026-02-28T12:00:00"
    }
###  Reaction (Реакция)
    {
      "id": 1,
      "message": { "id": 1 },
      "user": { "id": 2, "username": "maria_design" },
      "emoji": "👍",
      "createdAt": "2026-02-28T12:00:00"
    }
###  Attachment (Вложение)
    {
      "id": 1,
      "message": { "id": 1 },
      "fileUrl": "https://storage.example.com/files/document.pdf",
      "fileName": "document.pdf",
      "fileSize": 2048576,
      "fileType": "application/pdf",
      "thumbnailUrl": "https://storage.example.com/thumbs/document.jpg",
      "createdAt": "2026-02-28T12:00:00"
    }
###  ChatMember (Роль в чате)
    {
      "id": 1,
      "chat": { "id": 1, "name": "Рабочий чат" },
      "user": { "id": 1, "username": "ivan_dev" },
      "role": "owner | admin | member",
      "isActive": true,
      "joinedAt": "2026-02-28T12:00:00",
      "leftAt": null
    }
###  UserSettings (Настройки пользователя)
    {
      "id": 1,
      "user": { "id": 1, "username": "ivan_dev" },
      "pushEnabled": true,
      "emailEnabled": true,
      "soundEnabled": true,
      "theme": "light | dark | system",
      "language": "ru",
      "createdAt": "2026-02-28T12:00:00",
      "updatedAt": "2026-02-28T12:00:00"
    }
###  Session (Сессия)
    {
      "id": 1,
      "user": { "id": 1, "username": "ivan_dev" },
      "deviceName": "Chrome on Windows",
      "deviceType": "mobile | desktop | web",
      "ipAddress": "192.168.1.100",
      "isActive": true,
      "lastActiveAt": "2026-02-28T12:00:00",
      "createdAt": "2026-02-28T12:00:00",
      "expiresAt": "2026-03-28T12:00:00"
    }
###  MessageRead (Статус прочтения)
    {
      "id": 1,
      "message": { "id": 1 },
      "user": { "id": 2, "username": "maria_design" },
      "readAt": "2026-02-28T12:00:00"
    }
##  Аутентификация

API использует **JWT (JSON Web Token)** для защиты эндпоинтов. Все запросы к защищённым ресурсам требуют валидного токена.

---

<<<<<<< HEAD
### Обзор
=======
###  Обзор
>>>>>>> 0cb4328290d657fef550e97e74bab532a015a464

| Параметр | Значение |
|----------|----------|
| **Тип аутентификации** | JWT Bearer Token |
| **Алгоритм** | HS256 |
| **Время жизни токена** | 24 часа (86400 секунд) |
| **Время жизни refresh токена** | 7 дней (604800 секунд) |
| **Защищённые эндпоинты** | Все кроме `/api/auth/**` и `/api/users` |

---

###  Получение токена

### 1. Регистрация нового пользователя

 #### **Endpoint:** `POST /api/auth/register`

#### **Request:**

    {
      "username": "ivan_dev",
      "email": "ivan@example.com",
      "password": "securePassword123",
      "displayName": "Иван Иванов",
      "phoneNumber": "+79001234567"
    }
#### **Response (201 Created):**
    {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpdmFuX2RldiIsImV4cCI6MTcxNDMwMDAwMH0.abc123...",
    "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4uLi4=",
    "expiresIn": 86400,
    "id": 1,
    "username": "ivan_dev",
    "email": "ivan@example.com"
    }
### **2. Вход в систему**
#### **Endpoint:** `POST /api/auth/login`
#### **Request:**
    {
    "username": "ivan_dev",
    "password": "securePassword123"
    }
#### **Response (200 OK):**
    {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpdmFuX2RldiIsImV4cCI6MTcxNDMwMDAwMH0.abc123...",
    "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4uLi4=",
    "expiresIn": 86400,
    "id": 1,
    "username": "ivan_dev",
    "email": "ivan@example.com"
    }
### **3. Обновление токена**
#### **Endpoint:** `POST /api/auth/register`
#### **Request:**
    {
    "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4uLi4="
    }
#### **Response (200 OK):**
    {
    "token": "bmV3IGp3dCB0b2tlbiBoZXJl...",
    "refreshToken": "bmV3IHJlZnJlc2ggdG9rZW4gaGVyZS4uLg==",
    "expiresIn": 86400
    }
## Конфигурация

### application.properties

#### **Основные настройки приложения находятся в** `src/main/resources/application.properties`:

    # ==================== Database ====================
    spring.datasource.url=jdbc:postgresql://localhost:5432/messenger_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.datasource.driver-class-name=org.postgresql.Driver
    
    # ==================== Liquibase ====================
    spring.liquibase.enabled=false
    
    # ==================== JPA / Hibernate ====================
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    
    # ==================== Server ====================
    server.port=8080
    
    # ==================== Swagger ====================
    springdoc.api-docs.path=/api-docs
    springdoc.swagger-ui.path=/swagger-ui.html
    
    # ==================== JWT ====================
    jwt.secret=YourSuperSecretKeyForJWTTokenGenerationMustBeLongEnough2024
    jwt.expiration=86400000
    jwt.refresh-expiration=604800000
##  Тестирование

### Интерактивное тестирование API

Основной инструмент для тестирования — **Swagger UI**:

| Инструмент | URL | Описание |
|------------|-----|----------|
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Интерактивное тестирование всех эндпоинтов |

**Как тестировать:**
1. Запустите приложение: `mvn spring-boot:run`
2. Откройте Swagger UI
3. Нажмите  **Authorize** и введите JWT токен
4. Выберите эндпоинт → **Try it out** → **Execute**
5. Проверьте ответ

---

### Unit тесты

> ⚠ **В разработке** — Unit тесты будут добавлены в будущих версиях.

**Планируемое покрытие:**
- [ ] Service слой (бизнес-логика)
- [ ] Controller слой (HTTP запросы)
- [ ] Repository слой (доступ к БД)
- [ ] Утилиты (JWT, валидация)

**Запуск тестов (когда будут добавлены):**
    mvn test

##  Авторы
- **Gleb Fedorov** — Backend-разработка, архитектура, API  
  - [GitHub](https://github.com/Gleb2503) 
  - [Telegram](https://t.me/hlebusheka)