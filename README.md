# ms-psicologos

Catálogo de psicólogos con búsqueda por especialidad/modalidad (**RF-02**) y publicación
de su disponibilidad semanal (**RF-03**). ms-agenda valida la disponibilidad contra este servicio.

**Puerto:** 8082 · **Base de datos:** `psicologos_db` · **Swagger:** http://localhost:8082/swagger-ui.html

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/psicologos` | Crea un psicólogo (201) |
| GET | `/api/psicologos?especialidad=&modalidad=` | Busca con filtros opcionales |
| GET | `/api/psicologos/{id}` | Obtiene un psicólogo (404 si no existe) |
| PUT | `/api/psicologos/{id}` | Actualiza el psicólogo |
| DELETE | `/api/psicologos/{id}` | Elimina el psicólogo (204) |
| POST | `/api/psicologos/{id}/disponibilidades` | Agrega bloque de disponibilidad (201) |
| GET | `/api/psicologos/{id}/disponibilidades` | Lista la disponibilidad |
| DELETE | `/api/psicologos/{id}/disponibilidades/{dispId}` | Elimina el bloque (204) |

Modalidades: `ONLINE`, `PRESENCIAL`, `HIBRIDA`. Días: `MONDAY` … `SUNDAY`.

## Cómo correr localmente

```bash
docker compose -f ../docker-compose.yml up -d   # Postgres + Redis (una sola vez)
mvn spring-boot:run
```

## Arquitectura

Capas `api` → `service` → `repository` → `domain`. Regla de negocio: la hora de término
debe ser posterior a la de inicio (409 en caso contrario).
Relación JPA destacada: `Psicologo` **OneToMany** `Disponibilidad` (y **ManyToOne** inversa).

## Despliegue

Pensado para **Amazon ECS Fargate** con Aurora Serverless. Imagen: `docker build -t ms-psicologos .`
