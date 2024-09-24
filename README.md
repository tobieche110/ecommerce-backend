# Examen Técnico

## Descripción

Este proyecto es un ecommerce que permite a los usuarios agregar productos al carrito, consultar órdenes pagadas, gestionar productos (para administradores), entre otras funcionalidades. La aplicación está construida utilizando **Spring Boot** en el backend y **PostgreSQL** como base de datos.

## Requisitos Previos

### Dependencias Generales
- Java 17
- Maven
- PostgreSQL (si no usas Docker)
- Docker (si eliges usar contenedores)

---

## Guía para Correr la Aplicación con Docker

### Requisitos
- Docker instalado en tu sistema. Puedes descargarlo desde [Docker](https://www.docker.com/get-started).

### Instrucciones

1. **Clona el repositorio**:

2. **Archivos Necesarios**:
Asegúrate de tener el archivo init.sql en la raíz del proyecto (este archivo contiene la estructura y algunos datos de prueba para la base de datos).
**Levanta los servicios de Docker**: En la raíz del proyecto, ejecuta el siguiente comando para construir la aplicación y levantar los contenedores:
> docker-compose up --build

3. **Accede a la aplicación**:
- La aplicación estará disponible en http://localhost:8080.
- La base de datos PostgreSQL estará disponible en localhost:5432.

4. **Detén los contenedores**: Cuando quieras detener la aplicación, usa:
> docker-compose down

**Nota*
El contenedor de la base de datos cargará automáticamente el script init.sql, que inicializa la base de datos con algunos datos de ejemplo.

## Guía para Correr la Aplicación sin Docker
# Requisitos
- Java 17: Asegúrate de tener el JDK 17 instalado. Puedes descargarlo desde Oracle.
- PostgreSQL: Instalado y corriendo en tu máquina local. Puedes descargarlo desde PostgreSQL.
- Maven: Instalado para gestionar las dependencias del proyecto.
# Instrucciones
1. Clona el repositorio:
2. Configura la base de datos:
  - Crea una base de datos llamada ecommerce.
  - Crea un usuario postgres con la contraseña admin y otórgale permisos sobre la base de datos ecommerce.
  - Importa el archivo init.sql en la base de datos para cargar la estructura y los datos:
> psql -U postgres -d ecommerce -f path/to/init.sql

3. Configura el archivo application.properties: Verifica que los detalles de la base de datos sean correctos en el archivo src/main/resources/application.properties:

**properties**
`
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=postgres
spring.datasource.password=admin
Compila el proyecto: Navega a la carpeta raíz del proyecto y ejecuta:
`

> ./mvnw clean package -DskipTests

4. Ejecuta la aplicación: Después de compilar, puedes ejecutar la aplicación:
> java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
> 
5. Accede a la aplicación: La aplicación estará disponible en http://localhost:8080.

## Datos de Prueba
**Usuario Administrador**:
- username: Admin
- password: admin

**Usuario Regular**:
- username: User
- password: user
  
**Usuario VIP**:
- username: Vip
- password: vip

## Fases en el desarrollo de la aplicación
## 1. Análisis

Se debe desarrollar un ecommerce con funcionalidades básicas. Deberían haber dos tipos de usuario: **común** y **administrador**.

### Funcionalidades del Usuario Común (Cliente):
- Agregar productos al carrito.
- Eliminar productos del carrito.
- Consultar ítems en el carrito y el total a pagar.
- Consultar órdenes pagadas.

El carrito se almacena en cookies y se envía a la BDD una vez terminado el checkout. Luego, se resta la cantidad del producto del stock en la BDD. Si el cliente no finaliza la compra, el carrito automáticamente se destruye. El carrito también se elimina si el usuario cierra sesión o expira.

### Funcionalidades del Usuario Administrador:
- Consultar una lista de clientes (usuarios) VIP.
- Consultar una lista de clientes que pasaron a ser VIP en un determinado mes.
- Consultar una lista de clientes que dejaron de ser VIP en un determinado mes.
- Realizar carritos como los usuarios comunes.
- Agregar un producto.
- Eliminar un producto.
- Manipular stock de productos.

### Tipos de Carritos
1. **Carrito Común**: Utilizado por clientes que no son VIP y no es una fecha promocionable.
2. **Carrito Promocionable por Fecha Especial**: Utilizado cuando es una fecha especial (ej. Navidad, Halloween, etc.).
3. **Carrito VIP**: Utilizado cuando el cliente es VIP.

Las promociones de los carritos no son acumulables.

### Cliente VIP
Un cliente es considerado VIP cuando en un determinado mes realizó compras por más de $10.000. Si en un mes no realiza compras, deja de ser VIP.

El diagrama de flujo del cliente puede encontrarse en el archivo `Examen Tecnico Factor IT.drawio`.

## 2. Diseño y Arquitectura

### Tecnologías a Utilizar:
- **Frontend**: React JS.
- **Backend**: Java Spring Boot con Maven.

### Directorios Web
- **Formulario de Log In**: Usuario y contraseña, con un botón para registrar.
- **Formulario de Registro**: Correo, contraseña, confirmar contraseña.
- **Catálogo de Productos**: Solo accesible si el usuario está logueado. Muestra cartas de productos con nombre, precio y botón para agregar al carrito.
- **Carrito**: Muestra productos y cantidades. Permite agregar/eliminar productos o cantidades. Contiene botón para eliminar todo el carrito y uno para pagar.
- **Administrar Listas de Usuarios VIP**: Filtrable por fecha.
- **Administrar Productos**: CRUD de productos.

No hay opción para recuperar contraseñas o confirmar correos en esta versión.

### Base de Datos
Se utilizará PostgreSQL como motor de base de datos, ya que permite definir atributos como Array.

- **User**:
   - `id`: BigInt
   - `username`: varchar
   - `password`: varchar
   - `role`: enum(“USER”, “ADMIN”, “VIP”)
   - `totalSpent`: double
   - `dateWhenBecameVIP`: LocalDate
   - `dateWhenLostVIP`: LocalDate
- **Sales Order**:
   - `id`: BigInt
   - `user_id`: id del usuario que creó la orden
   - `product_ids`: Array (contiene los ids de los productos)
   - `total_price`: double
   - `date`: LocalDate
- **Product**:
   - `id`: BigInt
   - `name`: varchar
   - `price`: double
   - `stock`: int
   - `image`: varchar (ruta de la imagen)
- **Discount**:
   - `id`: Long
   - `discountAmount`: double
   - `discountPercent`: double
   - `freeCheapestProductCount`: int
   - `productCountMax`: int
   - `productCountMin`: int
   - `roleForDiscount`: String
   - `name`: String
   - `validDates`: Array

### Autenticación y Seguridad
- Se usará Spring Security con JWT Token. El token debe enviarse en el encabezado `Authorization: Bearer <JWTtoken>`.
- Requests para Login y Register serán públicas.
- Requests para consultar productos y crear órdenes serán accesibles solo para usuarios.
- Requests para crear, eliminar o editar productos solo estarán disponibles para administradores.

### Verificación de Usuario VIP
Se verificará si un usuario se convierte en VIP al crear una **Sales Order**. Si un usuario no gasta más de $10,000 en un mes, pierde el estatus VIP.

### Cambio de Fecha
El cambio de fecha se almacena en un contexto de React. Esto no reflejará cambios en el estado de VIP, pero afectará la validez de promociones por fechas especiales.

## 3. Implementación

- Se utilizó **Java Spring Boot** en el backend y **React JS** en el frontend.
- El token de autenticación se guarda en una cookie HTTP Only y en el sistema usando contextos de React.
- Se permite agregar productos al carrito y editarlos. El carrito se elimina al cerrar sesión.
- El sistema busca el descuento más favorable para el usuario según su rol.

### Datos de Prueba en `init.sql`:
- Productos pre-cargados.
- Usuarios de muestra:
   - Administrador: `username = Admin`, `password = admin`
   - Usuario: `username = User`, `password = user`
   - Usuario VIP: `username = Vip`, `password = vip`
- Promociones pre-cargadas: Fechas especiales: 2024-09-21, 2024-09-22, 2024-09-23, 2024-09-24.

### Funcionalidades Pendientes:
- Insertar imágenes en los productos y mostrarlas en el cliente.
- Mejoras visuales, como manejo de stock en el panel de administración.
- Mayor cantidad de comentarios en frontend y optimización de código.

## 4. Testing

Se crearon casos de prueba utilizando **Mockito** para todos los métodos del paquete `service` en el backend.
