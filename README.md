# qc-order-manage

REST-сервис управления заказами на Spring Boot с архитектурой MVC, PostgreSQL и Docker.

## Технологии

- Java 17+
- Spring Boot
- Spring Web / Spring Data JPA
- PostgreSQL
- Docker / Docker Compose
- Jakarta Validation

---

## Запуск

### Вариант 1 — полностью через Docker (рекомендуется)

Запускает и PostgreSQL, и приложение одной командой:

```bash
docker-compose up -d
```

### Вариант 2 — локальная разработка

Запустить только базу данных:

```bash
docker-compose up -d postgres
```

Затем запустить приложение локально:

```bash
./mvnw spring-boot:run
```

---

Приложение будет доступно по адресу: `http://localhost:8080`

### Параметры базы данных

| Параметр    | Значение  |
|-------------|-----------|
| БД          | `orders`  |
| Пользователь| `admin`   |
| Пароль      | `admin`   |
| Порт (хост) | `5433`    |

---

## Сущности

### Customer

| Поле        | Тип           | Описание                          |
|-------------|---------------|-----------------------------------|
| id          | Long          | Уникальный идентификатор          |
| name        | String        | Имя клиента (обязательное)        |
| email       | String        | Email (обязательный, уникальный, валидный формат) |
| createdAt   | LocalDateTime | Дата и время создания             |

### Order

| Поле        | Тип           | Описание                          |
|-------------|---------------|-----------------------------------|
| id          | Long          | Уникальный идентификатор          |
| customerId  | Long          | ID клиента (ссылка на Customer)   |
| amount      | BigDecimal    | Сумма заказа (должна быть > 0)    |
| status      | Enum          | Статус: `NEW`, `PAID`, `CANCELLED`|
| createdAt   | LocalDateTime | Дата и время создания             |

---

## API

### Customers

| Метод  | URL               | Описание                        |
|--------|-------------------|---------------------------------|
| GET    | /customers        | Получить список всех клиентов   |
| GET    | /customers/{id}   | Получить клиента по ID          |
| POST   | /customers        | Создать нового клиента          |
| PUT    | /customers/{id}   | Обновить данные клиента         |
| DELETE | /customers/{id}   | Удалить клиента                 |

### Orders

| Метод  | URL                  | Описание                        |
|--------|----------------------|---------------------------------|
| GET    | /orders              | Получить список всех заказов    |
| GET    | /orders/{id}         | Получить заказ по ID            |
| POST   | /orders              | Создать новый заказ             |
| PUT    | /orders/{id}         | Обновить заказ                  |
| DELETE | /orders/{id}         | Удалить заказ                   |
| POST   | /orders/{id}/pay     | Оплатить заказ                  |
| POST   | /orders/{id}/cancel  | Отменить заказ                  |

---

## Статусы заказа и бизнес-логика

```
NEW ──► PAID
 └────► CANCELLED
```

- `NEW` — начальный статус при создании заказа
- `PAID` — заказ оплачен (только из статуса `NEW`)
- `CANCELLED` — заказ отменён (только из статусов `NEW`; нельзя отменить `PAID`)

### Правила создания заказа
- Клиент с указанным `customerId` должен существовать
- `amount` должен быть больше 0

---

## Примеры запросов

### Создать клиента
```http
POST /customers
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com"
}
```

### Обновить клиента
```http
PUT /customers/1
Content-Type: application/json

{
  "name": "John Smith",
  "email": "johnsmith@example.com"
}
```

### Создать заказ
```http
POST /orders
Content-Type: application/json

{
  "customerId": 1,
  "amount": 500.00
}
```

### Оплатить заказ
```http
POST /orders/1/pay
```

### Отменить заказ
```http
POST /orders/1/cancel
```

---

## Обработка ошибок

| Ситуация                    | HTTP-статус |
|-----------------------------|-------------|
| Клиент не найден            | 404         |
| Заказ не найден             | 404         |
| Нарушение бизнес-логики     | 400         |
| Ошибки валидации            | 400         |

---

## Архитектура

Приложение реализовано по паттерну MVC:

```
Controller  →  Service  →  Repository  →  PostgreSQL
   (HTTP)     (логика)     (JPA/DB)
```