# 📱 Messenger Backend API

Бэкенд для мессенджера на Spring Boot с поддержкой личных чатов, групп, каналов и уведомлений.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue.svg)](https://www.postgresql.org)

---

## 📋 Содержание

- [Технологии](#-технологии)
- [Архитектура и структура проекта](#-архитектура-и-структура-проекта)
- [Быстрый старт](#-быстрый-старт)
- [API Endpoints](#-api-endpoints)
- [Модели данных](#-модели-данных)
- [Аутентификация](#-аутентификация)
- [Swagger документация](#-swagger-документация)?
- [Конфигурация](#-конфигурация)
- [Тестирование](#-тестирование)
- [Авторы](#-авторы)

---

## 🛠 Технологии

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
| **Liquibase** | 4.24.0 | Управление миграциями БД (опционально) |

---

## 🏗 Архитектура и структура проекта
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

## 🚀 Быстрый старт

### 1. Проверьте требования

| Компонент | Версия | Команда проверки |
|-----------|--------|-----------------|
| **Java** | 21+ | `java -version` |
| **PostgreSQL** | 15+ | `psql --version` |
| **Maven** | 3.9+ | `mvn -version` |
### 2. Установка и запуск

```bash
# Клонирование репозитория
git clone <repository-url>
cd <project-directory>

# Настройка подключения к базе данных
# Откройте src/main/resources/application.properties 
# и укажите параметры вашего окружения

# Запуск приложения
mvn spring-boot:run

