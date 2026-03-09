# qc-order-manage

REST-сервис управления заказами на Spring Boot.

## Запуск

### 1. Запустить PostgreSQL

```bash
docker-compose up -d
```

### 2. Запустить приложение

```bash
./mvnw spring-boot:run
```

Приложение будет доступно по адресу `http://localhost:8080`.

---

## API

### Customers

| Метод  | URL                  | Описание          |
|--------|----------------------|-------------------|
| GET    | /customers           | Список клиентов   |
| GET    | /customers/{id}      | Клиент по ID      |
| POST   | /customers           | Создать клиента   |
| PUT    | /customers/{id}      | Обновить клиента  |
| DELETE | /customers/{id}      | Удалить клиента   |

### Orders

| Метод  | URL                  | Описание          |
|--------|----------------------|-------------------|
| GET    | /orders              | Список заказов    |
| GET    | /orders/{id}         | Заказ по ID       |
| POST   | /orders              | Создать заказ     |
| PUT    | /orders/{id}         | Обновить заказ    |
| DELETE | /orders/{id}         | Удалить заказ     |
| POST   | /orders/{id}/pay     | Оплатить заказ    |
| POST   | /orders/{id}/cancel  | Отменить заказ    |

---

## Примеры запросов

### Создать клиента
```json
POST /customers
{
  "name": "John Doe",
  "email": "john@example.com"
}
```

### Создать заказ
```json
POST /orders
{
  "customerId": 1,
  "amount": 500.00
}
```

### Оплатить заказ
```
POST /orders/1/pay
```

### Отменить заказ
```
POST /orders/1/cancel
```

---

## Статусы заказа

- `NEW` — новый (начальный статус)
- `PAID` — оплачен
- `CANCELLED` — отменён

Правила:
- Оплатить можно только заказ со статусом `NEW`
- Отменить можно заказы со статусами `NEW` и `CANCELLED` (но не `PAID`)
