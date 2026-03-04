# Spring-Security-DaoAuthenticationProvider

## Debug de Spring Security: qué ocurre entre `build()` y un endpoint

Este ejercicio sirve para entender **cómo Spring Security construye y ejecuta la cadena de filtros** cuando una petición llega a nuestra aplicación.

---

# Requisitos previos

Antes de empezar necesitas:

- Eclipse con **Spring Boot** instalado.
- **MySQL** instalado.
- Crear la base de datos:

```sql
CREATE DATABASE securitydb;
```

- Configurar las credenciales en `application.properties`:

```properties
# MySQL user
spring.datasource.username=admin

# MySQL password
spring.datasource.password=admin
```

---

# Paso 1: Colocar los breakpoints

Vamos a colocar **dos puntos de depuración principales** para observar cómo funciona Spring Security.

## 1️⃣ Breakpoint en `SecurityConfig`

Archivo:

`SecurityConfig.java`

Línea:

```java
return httpSecurity.build();
```

Este breakpoint nos permite ver **cuándo Spring construye la configuración de seguridad de la aplicación**.

En este punto Spring todavía **no está gestionando ninguna petición**, simplemente está **construyendo la cadena de seguridad**.

---

## 2️⃣ Breakpoint en el endpoint `/ping`

Archivo:

`PublicController.java`

Método:

```java
@GetMapping("/ping")
public String ping() {
    return "API viva";
}
```

Este breakpoint nos permitirá comprobar **cuándo una petición consigue atravesar toda la seguridad y llegar al controlador**.

---

# Paso 2: Ejecutar la aplicación en modo Debug

1. Ejecuta el proyecto en **Debug Mode**.
2. La ejecución se detendrá primero en:

```
SecurityConfig -> httpSecurity.build()
```

---

# Qué ocurre cuando se ejecuta `httpSecurity.build()`

Cuando se ejecuta:

```java
httpSecurity.build();
```

Spring Security realiza internamente varios pasos importantes.

## 1️⃣ Recoge todos los configuradores de seguridad

Spring busca todos los **SecurityConfigurer** registrados en `HttpSecurity`, por ejemplo:

- `AuthorizeHttpRequestsConfigurer`
- `CsrfConfigurer`
- `HttpBasicConfigurer`
- `SessionManagementConfigurer`
- `LogoutConfigurer`

Estos configuradores **definen cómo se construirá la seguridad de la aplicación**.

---

## 2️⃣ Cada configurador añade filtros de seguridad

Cada `SecurityConfigurer` añade uno o varios **filtros de seguridad**.

Algunos ejemplos de filtros que se añaden a la cadena son:

```
DisableEncodeUrlFilter
WebAsyncManagerIntegrationFilter
SecurityContextHolderFilter
HeaderWriterFilter
LogoutFilter
BasicAuthenticationFilter
AuthorizationFilter
```

Todos estos filtros se guardan en una **lista ordenada**.

---

## 3️⃣ Se crea la `SecurityFilterChain`

Cuando termina el `build()`, Spring crea un objeto llamado:

```
SecurityFilterChain
```

Este objeto contiene:

- Un **RequestMatcher** → define qué URLs protege esta cadena
- Una **lista ordenada de filtros**

---

## 4️⃣ Se registra el `FilterChainProxy`

Finalmente Spring registra un filtro global llamado:

```
FilterChainProxy
```

Este es el **filtro principal de Spring Security**.

Todas las peticiones HTTP pasan primero por este filtro antes de llegar a cualquier controlador.

---

# Paso 3: Hacer una petición al endpoint

Ahora realiza una petición al endpoint:

```
GET http://localhost:8080/api/public/ping
```

La petición seguirá este flujo interno:

```
HTTP Request
      ↓
FilterChainProxy
      ↓
SecurityFilterChain
      ↓
VirtualFilterChain
      ↓
Filtros de seguridad
      ↓
Controller
```

---

# Qué ocurre en medio

Cuando la petición llega a la aplicación:

1. **FilterChainProxy intercepta la petición**
2. Busca una `SecurityFilterChain` cuyo **RequestMatcher coincida con la URL**
3. Si encuentra una coincidencia:
   - obtiene la lista de filtros
4. Ejecuta cada filtro **en orden**
5. Si ningún filtro bloquea la petición, finalmente la ejecución llega al controlador.

---

# Paso final: llegada al controlador

Si la petición supera todos los filtros, el debugger se detendrá en:

```java
@GetMapping("/ping")
public String ping() {
    return "API viva";
}
```

Esto significa que la petición **ha pasado correctamente por toda la cadena de seguridad**.

---

# Resumen del flujo completo

```
httpSecurity.build()
        ↓
Creación de SecurityFilterChain
        ↓
Registro de FilterChainProxy
        ↓
Petición HTTP llega
        ↓
FilterChainProxy intercepta
        ↓
Se ejecutan filtros de seguridad
        ↓
Controller (/ping)
```
