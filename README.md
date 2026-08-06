# Finance Tracker API

API REST para el control de ingresos y gastos personales, construida con **Java 17** y **Spring Boot 3**. Permite registrar transacciones categorizadas y consultar un resumen mensual con balance e ingresos/gastos desglosados por categoria.

## Tecnologias

- Java 17
- Spring Boot 3 (Web, Data JPA, Validation)
- Hibernate / JPA
- H2 (en memoria, perfil por defecto) y MySQL (perfil `mysql`)
- Maven
- JUnit 5 + Mockito + MockMvc
- springdoc-openapi (Swagger UI)

## Funcionalidades

- CRUD de categorias (ingreso / gasto)
- CRUD de transacciones
- Resumen mensual: ingresos totales, gastos totales, balance y desglose por categoria
- Validacion de datos de entrada y manejo centralizado de errores
- Documentacion interactiva via Swagger UI

## Como ejecutar

Requisitos: Java 17 y Maven.

```bash
git clone https://github.com/nelsongomezsa/finance-tracker-api.git
cd finance-tracker-api
mvn spring-boot:run
```

La API arranca en `http://localhost:8080` con una base de datos H2 en memoria (con datos de ejemplo precargados) y consola disponible en `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:financetracker`, usuario `sa`, sin contraseña).

Para usar MySQL en lugar de H2:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Configura `DB_USER` y `DB_PASSWORD` como variables de entorno si tu instalacion de MySQL no usa `root` / `root`. La base de datos `finance_tracker` se crea automaticamente si no existe.

## Documentacion de la API

Con la aplicacion arrancada: `http://localhost:8080/swagger-ui.html`

## Endpoints principales

| Metodo | Endpoint | Descripcion |
|---|---|---|
| GET | `/api/categories` | Lista todas las categorias |
| POST | `/api/categories` | Crea una categoria |
| PUT | `/api/categories/{id}` | Actualiza una categoria |
| DELETE | `/api/categories/{id}` | Elimina una categoria |
| GET | `/api/transactions` | Lista todas las transacciones |
| POST | `/api/transactions` | Crea una transaccion |
| PUT | `/api/transactions/{id}` | Actualiza una transaccion |
| DELETE | `/api/transactions/{id}` | Elimina una transaccion |
| GET | `/api/transactions/summary?year=2026&month=8` | Resumen del mes: ingresos, gastos, balance y desglose por categoria |

## Ejemplo de uso

Crear una categoria:

```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "Nomina", "type": "INCOME"}'
```

Crear una transaccion:

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{"description": "Nomina agosto", "amount": 1800.00, "date": "2026-08-01", "categoryId": 1}'
```

Consultar el resumen del mes:

```bash
curl "http://localhost:8080/api/transactions/summary?year=2026&month=8"
```

## Tests

```bash
mvn test
```

Incluye tests unitarios de la capa de servicio con Mockito (incluyendo el calculo del resumen mensual y sus totales por categoria) y tests de la capa web con MockMvc, cubriendo tanto el flujo correcto como la validacion de errores.

## Estructura del proyecto

```
src/main/java/com/nelsongomez/financetracker/
├── controller/    # Endpoints REST
├── service/       # Logica de negocio
├── repository/    # Acceso a datos (Spring Data JPA)
├── model/         # Entidades JPA
├── dto/           # Records de entrada/salida (DTOs)
└── exception/     # Manejo centralizado de errores
```

## Autor

Nelson Gomez Sanchez · Estudiante de DAM y Master en Big Data e IA
