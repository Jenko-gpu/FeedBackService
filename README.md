# Feedback System

Микросервисная платформа для сбора, обработки, модерации и хранения пользовательских отзывов, обращений в службу поддержки и баг-репортов с поддержкой загрузки изображений и видео.

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)

---

## Особенности

- **Многомодульная архитектура** — четкое разделение на основную бизнес-сервис и сервис хранения файлов.
- **Асинхронная обработка** событий через Apache Kafka.

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
