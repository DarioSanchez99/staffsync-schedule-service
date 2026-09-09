<div align="center">

# StaffSync — Schedule Service

**Gestión de turnos y cuadrantes semanales**

![Java](https://img.shields.io/badge/Java%2021-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203.3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)

</div>

---

Microservicio para la planificación de turnos de trabajo. Permite crear, modificar y consultar turnos por empleado, por fecha o por semana. Los cambios se publican en el topic Kafka `schedule-events`.

---

## Tipos de turno

| Tipo | Descripción |
|---|---|
| `MORNING` | Turno de mañana |
| `AFTERNOON` | Turno de tarde |
| `NIGHT` | Turno de noche |
| `REST` | Día de descanso |

---

## API endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/shifts` | Listar turnos (filtros: ?employeeId, ?date) |
| POST | `/shifts` | Crear turno |
| GET | `/shifts/{id}` | Obtener por ID |
| PUT | `/shifts/{id}` | Actualizar |
| DELETE | `/shifts/{id}` | Eliminar |
| GET | `/shifts/employee/{employeeId}` | Turnos de un empleado |
| GET | `/shifts/week/{weekStart}` | Turnos de una semana (YYYY-MM-DD) |

---

## Tecnologías

| Capa | Tecnología |
|---|---|
| Runtime | Java 21 |
| Framework | Spring Boot 3.3.4 |
| Mensajería | Apache Kafka |
| ORM | Spring Data JPA |
| Base de datos | PostgreSQL / H2 |
| Mapeo | MapStruct 1.5.5 |
| API spec | OpenAPI Generator 7.7.0 |

---

## Variables de entorno

| Variable | Valor por defecto | Descripción |
|---|---|---|
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/staffsync_schedule` | Conexión PostgreSQL |
| `DB_USER` / `DB_PASS` | `staffsync` | Credenciales |
| `KAFKA_BOOTSTRAP` | `localhost:9092` | Bootstrap servers |
| `EUREKA_URL` | `http://admin:admin@localhost:8761/eureka/` | URL de Eureka |

---

## Tests

```bash
mvn test
```

3 tests unitarios (crear turno, buscar por empleado, eliminar).

---

## Ejecución local

```bash
mvn spring-boot:run
```

Servicio disponible en: `http://localhost:8083`

---

## Parte de StaffSync

Ver [staffsync](https://github.com/DarioSanchez99/staffsync) para el índice completo del proyecto.
