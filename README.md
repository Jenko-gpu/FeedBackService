# Feedback System

Микросервисная платформа для сбора, обработки, модерации и хранения пользовательских отзывов, обращений в службу поддержки и баг-репортов с поддержкой загрузки изображений и видео.

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![License](https://img.shields.io/badge/License-MIT-blue)

---

## Особенности

- **Многомодульная архитектура** — четкое разделение на основную бизнес-сервис и сервис хранения файлов.
- **RESTful API** с документацией OpenAPI (Swagger).
- **Асинхронная обработка** событий через Apache Kafka (уведомления, аналитика, интеграции).
- **Хранение файлов** в объектном хранилище (MinIO / S3) с отдельным микросервисом для управления метаданными.
- **Модерация отзывов** с ролевой моделью (USER, MODERATOR, ADMIN, SUPPORT).
- **JWT-аутентификация** и авторизация через Spring Security + OAuth2 Resource Server.
- **Мониторинг** через Micrometer + Prometheus + Grafana.
- **Распределенная трассировка** через Zipkin / Jaeger.
- **Контейнеризация** Docker и оркестрация Kubernetes.

---

## Архитектура и стек

| Компонент              | Технология                                  |
|------------------------|---------------------------------------------|
| **Язык**               | Java 17+                                    |
| **Фреймворк**          | Spring Boot 3.x, Spring Cloud 2023.x        |
| **Сборка**             | Maven (многомодульный)                      |
| **ORM**                | Spring Data JPA + Hibernate                 |
| **Миграции БД**        | Liquibase                                   |
| **Реляционная БД**     | PostgreSQL 15+                              |
| **Объектное хранилище**| MinIO (S3-совместимое) / AWS S3             |
| **Брокер сообщений**   | Apache Kafka                                |
| **API-документация**   | SpringDoc OpenAPI (Swagger UI)              |
| **Безопасность**       | Spring Security + OAuth2 JWT                |
| **Мониторинг**         | Micrometer + Prometheus + Grafana           |
| **Трассировка**        | Zipkin / Jaeger                             |
| **Service Discovery**  | Netflix Eureka (опционально)                |
| **API Gateway**        | Spring Cloud Gateway (опционально)          |
| **Контейнеризация**    | Docker + Docker Compose                     |
| **Оркестрация**        | Kubernetes                                  |
| **Тестирование**       | JUnit 5, Mockito, Testcontainers            |

---

## Структура проекта (многомодульный Maven)
