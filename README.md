# UPS Evaluacion WS Loja

Sistema web para gestionar personas y titulos academicos asociados a una cedula.

Esta segunda fase reconstruye el proyecto original (`Prueba` + `ClienteRest`) como una aplicacion moderna y ejecutable en Windows con Java 21, Docker y Maven Wrapper.

## Tecnologias

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Jakarta Bean Validation
- Flyway
- PostgreSQL en Docker Compose
- Frontend web estatico con HTML, CSS y JavaScript
- Pruebas con JUnit, MockMvc y H2

## Requisitos

- Java JDK 21 en PATH
- Docker Desktop
- Git

No se requiere Maven global. El proyecto incluye `mvnw` y `mvnw.cmd`.

## Estructura

```text
.
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java/ec/edu/ups/evaluacionwsloja
    │   │   ├── config
    │   │   ├── controller
    │   │   ├── dto
    │   │   ├── entity
    │   │   ├── exception
    │   │   ├── repository
    │   │   └── service
    │   └── resources
    │       ├── db/migration
    │       ├── static
    │       └── application.yml
    └── test
        ├── java
        └── resources
```

## Ejecutar la base de datos

Desde la raiz del proyecto:

```powershell
docker compose up -d
```

La base queda disponible en:

- Host: `localhost`
- Puerto: `5432`
- Base: `evaluacion_ws_loja`
- Usuario: `postgres`
- Password: `postgres`

Para detenerla:

```powershell
docker compose down
```

Para eliminar tambien los datos:

```powershell
docker compose down -v
```

## Ejecutar el backend y frontend

Con la base de datos levantada:

```powershell
.\mvnw.cmd spring-boot:run
```

Abrir:

```text
http://localhost:8080
```

El frontend web se sirve desde Spring Boot y consume la API con rutas relativas.

Para detener la aplicacion cuando se ejecuta con `spring-boot:run`, vuelve a la terminal donde esta corriendo y presiona `Ctrl + C`.

Si ejecutaste el JAR en segundo plano o perdiste la terminal, puedes localizar el proceso Java y detenerlo:

```powershell
Get-CimInstance Win32_Process -Filter "Name = 'java.exe'" |
  Where-Object { $_.CommandLine -like '*evaluacion-ws-loja*' } |
  Select-Object ProcessId,CommandLine

Stop-Process -Id <PROCESS_ID>
```

Si la aplicacion fue iniciada con `spring-boot:run` y no identificas el proceso por nombre, busca quien escucha en el puerto 8080:

```powershell
Get-NetTCPConnection -LocalPort 8080 -State Listen |
  Select-Object LocalAddress,LocalPort,OwningProcess

Stop-Process -Id <OwningProcess>
```

## Construir el proyecto

```powershell
.\mvnw.cmd clean package
```

Ejecutar el JAR generado:

```powershell
java -jar target/evaluacion-ws-loja-0.1.0-SNAPSHOT.jar
```

## Ejecutar pruebas

```powershell
.\mvnw.cmd test
```

Las pruebas usan H2 en memoria y no requieren PostgreSQL.

## Configuracion

La aplicacion toma valores por variables de entorno con defaults de desarrollo:

```text
SERVER_PORT=8080
DB_URL=jdbc:postgresql://localhost:5432/evaluacion_ws_loja
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

Ejemplo en PowerShell:

```powershell
$env:SERVER_PORT="8081"
$env:DB_URL="jdbc:postgresql://localhost:5432/evaluacion_ws_loja"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
.\mvnw.cmd spring-boot:run
```

## Endpoints principales

### Crear persona

```http
POST /api/personas
Content-Type: application/json
```

```json
{
  "cedula": "0102030405",
  "nombre": "Ana Loja",
  "telefono": "0999999999"
}
```

### Consultar persona por cedula

```http
GET /api/personas/0102030405
```

### Listar personas

```http
GET /api/personas
```

### Registrar titulo para una persona

```http
POST /api/personas/0102030405/titulos
Content-Type: application/json
```

```json
{
  "nombre": "Ingenieria en Sistemas",
  "universidad": "Universidad Politecnica Salesiana"
}
```

### Listar titulos por cedula

```http
GET /api/personas/0102030405/titulos
```

### Consultar titulo por ID

```http
GET /api/titulos/1
```

### Documentacion rapida de la API

```http
GET /api
```

## Ejemplos con curl

Crear persona:

```powershell
curl.exe -X POST http://localhost:8080/api/personas `
  -H "Content-Type: application/json" `
  -d "{\"cedula\":\"0102030405\",\"nombre\":\"Ana Loja\",\"telefono\":\"0999999999\"}"
```

Registrar titulo:

```powershell
curl.exe -X POST http://localhost:8080/api/personas/0102030405/titulos `
  -H "Content-Type: application/json" `
  -d "{\"nombre\":\"Ingenieria en Sistemas\",\"universidad\":\"Universidad Politecnica Salesiana\"}"
```

Listar titulos:

```powershell
curl.exe http://localhost:8080/api/personas/0102030405/titulos
```

## Problemas comunes

### `docker compose` no responde

Inicia Docker Desktop y vuelve a ejecutar:

```powershell
docker compose up -d
```

### Puerto 5432 ocupado

Cambia el puerto publicado en `docker-compose.yml`, por ejemplo:

```yaml
ports:
  - "5433:5432"
```

Y ejecuta la aplicacion con:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5433/evaluacion_ws_loja"
.\mvnw.cmd spring-boot:run
```

### Primer uso de Maven Wrapper lento

La primera ejecucion descarga Apache Maven y las dependencias del proyecto desde Maven Central.

## Datos de prueba

Flyway crea una persona y un titulo de ejemplo:

- Cedula: `0102030405`
- Nombre: `Ana Loja`
- Titulo: `Ingenieria en Sistemas`

## Cambios frente al proyecto original

- Se reemplazo WildFly por Spring Boot ejecutable.
- Se reemplazo el cliente Swing incompleto por frontend web estatico.
- Se elimino la duplicacion de modelos entre backend y cliente.
- Se agrego Docker Compose para PostgreSQL.
- Se agregaron migraciones Flyway.
- Se agregaron pruebas automatizadas.
- Se retiraron archivos de IDE y temporales versionados.
