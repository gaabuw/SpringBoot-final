# API REST de Gestión de Productos (UTN)

Trabajo Práctico (TP) integrador de **Programación III** de la Tecnicatura Universitaria en Programación (UTN).

Es una API REST para la gestión de productos que implementa una arquitectura en capas, validaciones (DTOs), manejo global de excepciones y documentación interactiva con Swagger.

## 🛠️ Tecnologías Utilizadas

* **Java 17**
* **Spring Boot** (v3.2.5)
* **Spring Data JPA**
* **H2 Database** (Base de datos en memoria)
* **Spring Validation**
* **Lombok**
* **Springdoc OpenAPI (Swagger)** (v2.5.0)
* **Gradle**

## 🚀 Cómo Ejecutar

1.  Clona el repositorio:
    ```bash
    git clone [URL_DE_TU_REPOSITORIO]
    ```
2.  Navega al directorio:
    ```bash
    cd productos-api
    ```
3.  Ejecuta la aplicación con el wrapper de Gradle:
    ```bash
    ./gradlew bootRun
    ```

La API estará corriendo en `http://localhost:8080`.

## 📍 Herramientas

* **Documentación API (Swagger):** `http://localhost:8080/swagger-ui/index.html`
* **Base de Datos (H2 Console):** `http://localhost:8080/h2-console`
    * **JDBC URL:** `jdbc:h2:mem:testdb`
    * **User Name:** `sa`
    * **Password:** (dejar en blanco)

## 📖 Endpoints

| Método | Ruta | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/productos` | Listar todos los productos. |
| `GET` | `/api/productos/{id}` | Obtener un producto por su ID. |
| `GET` | `/api/productos/categoria/{categoria}` | Filtrar productos por categoría. |
| `POST` | `/api/productos` | Crear un nuevo producto. |
| `PUT` | `/api/productos/{id}` | Actualizar un producto completo por ID. |
| `PATCH` | `/api/productos/{id}/stock` | Actualizar solo el stock de un producto. |
| `DELETE` | `/api/productos/{id}` | Eliminar un producto por su ID. |

>    **Nota importante:**  
> Repositorio de Github para el trabajo práctico Spring-Boot FINAL        
> Materia: Desarrollo de Software 3k10 - 2025  
> Alumno: Gabriel Villalobos
