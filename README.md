# Kosmos
# API de Gestión de Citas Médicas

Esta API proporciona endpoints para gestionar citas médicas, incluyendo la creación de doctores, salas de consulta y citas, así como la consulta de citas existentes.

Este sistema de gestión de citas médicas expone varias APIs RESTful que puedes consumir utilizando herramientas como Postman, Insomnia o cualquier cliente HTTP de tu preferencia.

# Consumo por medio de Swagger UI
Cómo consumir el Swagger UI para interactuar con las APIs de forma visual:

---

# Instrucciones para Consumir las APIs

## Uso de Swagger UI

Swagger UI es una interfaz visual que permite probar las APIs de forma sencilla, sin necesidad de realizar configuraciones adicionales ni usar herramientas como Postman.

### Acceso a Swagger UI

1. **Iniciar el Servidor**:

   * Si estás usando Spring Boot o cualquier otro servidor backend, asegúrate de que el servidor esté en ejecución en tu entorno local o de desarrollo (por ejemplo, en `http://localhost:8080`).

2. **Acceder a Swagger UI**:

   * Una vez que tu servidor esté funcionando, abre un navegador web y navega a la siguiente URL:

     ```
     http://localhost:8080/swagger-ui/index.html
     ```
   * Esta URL abrirá la interfaz de Swagger UI que te permitirá interactuar con las APIs de manera visual.

## Endpoints

### 1. **Crear Doctor**
Crea un nuevo doctor en el sistema.

**Endpoint**: `POST /api/doctors`

**Cuerpo de la solicitud** (JSON):
```json
{
  "firstName": "Pedro",
  "lastName": "Sanchez",
  "middleName": "Valdivia",
  "specialty": "Cardiologo"
}
````

**Respuesta (200 OK)**:

```json
{
  "status": "success",
  "message": "Doctor created successfully",
  "data": {
    "id": 1,
    "firstName": "Pedro",
    "lastName": "Sanchez",
    "middleName": "Valdivia",
    "specialty": "Cardiologo"
  }
}
```

### 2. **Crear Sala de Consulta**

Crea una nueva sala de consulta en el sistema.

**Endpoint**: `POST /api/consultingRooms`

**Cuerpo de la solicitud** (JSON):

```json
{
  "roomNumber": "101",
  "floor": "1"
}
```

**Respuesta (200 OK)**:

```json
{
  "status": "success",
  "message": "Consulting room created successfully",
  "data": {
    "id": 1,
    "roomNumber": "101",
    "floor": "1"
  }
}
```

### 3. **Crear Cita Médica**

Crea una nueva cita médica asociada a un doctor y una sala de consulta.

**Endpoint**: `POST /api/appointments`

**Cuerpo de la solicitud** (JSON):

```json
 {
    "doctor": {
      "id": 1
    },
    "consultingRoom": {
      "id": 1
    },
    "patientName": "Juan Perez",
    "startTime": "2025-05-10T19:00:00",
    "endTime": "2025-05-10T20:00:00",
    "status": "ACTIVE"
}
```

**Respuesta (201 Created)**:

```json
{
    "status": "success",
    "message": "Appointment created successfully",
    "data": {
        "id": 1,
        "doctor": {
            "id": 1,
            "firstName": null,
            "lastName": null,
            "middleName": null,
            "specialty": null
        },
        "consultingRoom": {
            "id": 1,
            "roomNumber": null,
            "floor": null
        },
        "patientName": "Juan Perez",
        "startTime": "2025-05-10T19:00:00",
        "endTime": "2025-05-10T20:00:00",
        "status": "ACTIVE"
    }
}
```

### Explicación de las reglas para creacion de cita medica:
- **Sala ya reservada**: Evita que se puedan programar citas en la misma sala y el mismo horario.
- **Doctor ya reservado**: Asegura que un doctor no pueda atender dos citas en el mismo horario.
- **Paciente con cita cercana**: Limita a los pacientes a solo tener una cita activa dentro de un período de 2 horas, evitando la sobrecarga de citas en un corto período de tiempo.

Estas reglas aseguran que las citas sean programadas sin conflictos de horarios, permitiendo que cada recurso (doctor, sala, paciente) esté correctamente gestionado.


### 4. **Obtener Citas**

Obtiene una lista de citas médicas filtradas por fecha, doctor o sala.

**Endpoint**: `GET /api/appointments`

**Parámetros opcionales**:

* `doctorId` (long): Filtra citas por ID del doctor.
* `roomId` (long): Filtra citas por ID de la sala.

**Parámetros requerido**:

* `date` (string, formato `YYYY-MM-DD`): Filtra citas por fecha.

**Ejemplo de solicitud**:

```http
GET /api/appointments?date=2025-05-10&doctorId=1
```

**Respuesta (200 OK)**:

```json
{
    "status": "success",
    "message": "Appointments retrieved successfully",
    "data": [
        {
            "id": 1,
            "doctor": {
                "id": 1,
                "firstName": "Carlos",
                "lastName": "Fernandez",
                "middleName": "Perez",
                "specialty": "Traumatologo"
            },
            "consultingRoom": {
                "id": 1,
                "roomNumber": "305",
                "floor": "3"
            },
            "patientName": "Juan Perez",
            "startTime": "2025-05-10T19:00:00",
            "endTime": "2025-05-10T20:00:00",
            "status": "ACTIVE"
        }
    ]
}
```
---

## Notas Adicionales

* Asegúrate de que el servidor esté corriendo y accesible en la URL correspondiente (por ejemplo, `http://localhost:8080`).
* Los endpoints de tipo `GET` devuelven un conjunto de datos basado en los filtros proporcionados.
* Los parámetros como `doctorId`, `roomId` y `date` son opcionales, pero pueden ayudar a filtrar los resultados.
* Todos los datos de entrada deben seguir el formato correcto (como la fecha en el formato `YYYY-MM-DD` o `YYYY-MM-DDTHH:MM:SS` para `LocalDateTime`).

---

## Tecnologías

* **Backend**: Spring Boot, Java
* **Base de datos**: H2
* **Formato de los datos**: JSON

```

Este README cubre todos los endpoints que mencionaste para gestionar los doctores, salas de consulta y citas médicas, además de cómo consumirlos con ejemplos. Puedes usarlo como guía para la documentación de tu API.
```