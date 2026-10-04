# SpeedFast - Desarrollo Orientado a Objetos II

## Autor del proyecto

* **Nombre completo:** Emilio Nuñez Jara
* **Sección:** PRY2203
* **Carrera:** Analista Programador Computacional
* **Sede:** Campus Virtual

---

## Descripción general del sistema

Proyecto desarrollado en Java para la empresa de repartos **SpeedFast**.

Esta versión corresponde a la actividad sumativa de la **Semana 8** y completa el ciclo funcional desarrollado durante las actividades anteriores, incorporando persistencia de datos mediante **JDBC**, operaciones **CRUD** y una interfaz gráfica desarrollada con **Java Swing**.

El sistema permite gestionar de forma persistente las tres entidades principales definidas para el caso SpeedFast:

* Repartidores.
* Pedidos.
* Entregas.

Para organizar el acceso a la base de datos se utiliza el patrón **DAO (Data Access Object)**.

Cada entidad dispone de una clase especializada encargada de realizar las operaciones:

```java
create()
readAll()
update()
delete()
```

La aplicación se conecta a una base de datos **MySQL**, utilizando consultas parametrizadas mediante `PreparedStatement` y recuperación de información mediante `ResultSet`.

También se incorporan validaciones en los formularios y manejo de excepciones para evitar operaciones con información incorrecta.

El proyecto aplica conceptos de Programación Orientada a Objetos, acceso a datos e interfaces gráficas, entre ellos:

* Encapsulamiento.
* Enumeraciones (`enum`).
* Separación por capas.
* Patrón DAO.
* Operaciones CRUD.
* JDBC.
* MySQL.
* `Connection`.
* `DriverManager`.
* `PreparedStatement`.
* `ResultSet`.
* Manejo de excepciones mediante `try-catch`.
* Cierre automático de recursos mediante `try-with-resources`.
* Java Swing.
* `JFrame`.
* `JPanel`.
* `JTabbedPane`.
* `JTable`.
* `JTextField`.
* `JComboBox`.
* `JButton`.
* `JOptionPane`.
* Validación de datos.
* Relaciones mediante claves foráneas.

---

## Funcionalidades del sistema

### Gestión de repartidores

El sistema permite realizar las operaciones CRUD correspondientes a los repartidores.

Cada repartidor posee:

```plaintext
id
nombre
```

Las operaciones disponibles son:

* Registrar un repartidor.
* Listar los repartidores almacenados.
* Seleccionar un repartidor desde la tabla.
* Editar su nombre.
* Eliminar un repartidor.
* Mostrar mensajes de éxito o error después de cada operación.

Los repartidores se muestran mediante un:

```java
JTable
```

El identificador del repartidor es generado automáticamente por MySQL mediante:

```sql
AUTO_INCREMENT
```

Para evitar datos inválidos se comprueba que:

* El nombre no esté vacío.
* El nombre no supere los 100 caracteres.

---

### Gestión de pedidos

El sistema permite administrar pedidos mediante operaciones CRUD.

Cada pedido posee:

```plaintext
id
direccion
tipo
estado
```

Los tipos disponibles se encuentran definidos mediante el enum:

```java
TipoPedido
```

con los valores:

```java
COMIDA
ENCOMIENDA
EXPRESS
```

Los estados disponibles se encuentran definidos mediante:

```java
EstadoPedido
```

con los valores:

```java
PENDIENTE
EN_REPARTO
ENTREGADO
```

Desde la interfaz gráfica se puede:

* Registrar un pedido.
* Listar pedidos.
* Editar su dirección.
* Modificar el tipo.
* Modificar el estado.
* Eliminar un pedido.

Los valores de tipo y estado se seleccionan mediante:

```java
JComboBox
```

Esto evita que el usuario ingrese manualmente valores que no se encuentren definidos dentro de las reglas del sistema.

---

### Gestión de entregas

La entidad `Entrega` representa la relación entre un pedido y un repartidor.

Cada entrega posee:

```plaintext
id
idPedido
idRepartidor
fecha
hora
```

Desde la interfaz gráfica se puede:

* Registrar una entrega.
* Seleccionar un pedido.
* Seleccionar un repartidor.
* Registrar fecha.
* Registrar hora.
* Listar entregas.
* Editar una entrega existente.
* Eliminar una entrega.

Los pedidos y repartidores disponibles se obtienen desde la base de datos y se muestran mediante:

```java
JComboBox<Pedido>
JComboBox<Repartidor>
```

Los elementos se muestran de forma legible mediante los métodos `toString()` de las clases.

Ejemplo:

```plaintext
#2 - Ñuñoa
#2 - Sofia
```

Aunque se muestra un texto descriptivo, el objeto conserva internamente su identificador.

De esta manera, al registrar una entrega se utilizan:

```java
pedido.getId()
repartidor.getId()
```

permitiendo almacenar correctamente las claves foráneas correspondientes.

---

## Estructura general del proyecto

La estructura principal del proyecto es:

```plaintext
SpeedFast-CRUD/
│
├── lib/
│   └── mysql-connector-j.jar
│
└── src/
    │
    ├── app/
    │   └── Main.java
    │
    ├── conexion/
    │   └── ConexionDB.java
    │
    ├── dao/
    │   ├── RepartidorDAO.java
    │   ├── PedidoDAO.java
    │   └── EntregaDAO.java
    │
    ├── model/
    │   ├── Repartidor.java
    │   ├── Pedido.java
    │   ├── Entrega.java
    │   ├── TipoPedido.java
    │   └── EstadoPedido.java
    │
    └── vista/
        └── VentanaPrincipal.java
```

Las responsabilidades se encuentran separadas por paquetes para facilitar la organización y mantenibilidad del proyecto.

---

## Clases principales

### Repartidor

La clase `Repartidor` representa un repartidor registrado dentro del sistema.

Sus atributos son:

```plaintext
id
nombre
```

Dispone de un constructor para representar registros recuperados desde la base de datos:

```java
Repartidor(int id, String nombre)
```

y otro constructor utilizado al registrar un nuevo repartidor:

```java
Repartidor(String nombre)
```

También implementa:

```java
toString()
```

para mostrar información legible dentro del `JComboBox` de entregas.

Por ejemplo:

```plaintext
#2 - Sofia
```

---

### Pedido

La clase `Pedido` representa un pedido registrado en SpeedFast.

Sus atributos son:

```plaintext
id
direccion
tipo
estado
```

Utiliza los enums:

```java
TipoPedido
EstadoPedido
```

para mantener controlados los valores posibles.

Dispone de un constructor para pedidos existentes:

```java
Pedido(
    int id,
    String direccion,
    TipoPedido tipo,
    EstadoPedido estado
)
```

y otro para pedidos nuevos:

```java
Pedido(
    String direccion,
    TipoPedido tipo,
    EstadoPedido estado
)
```

El método:

```java
toString()
```

permite mostrar el pedido de forma legible dentro del `JComboBox` de entregas.

Ejemplo:

```plaintext
#2 - Ñuñoa
```

---

### Entrega

La clase `Entrega` representa una entrega asociada a un pedido y a un repartidor.

Sus atributos son:

```plaintext
id
idPedido
idRepartidor
fecha
hora
```

Para representar fecha y hora se utilizan:

```java
LocalDate
LocalTime
```

Dispone de un constructor para registros existentes:

```java
Entrega(
    int id,
    int idPedido,
    int idRepartidor,
    LocalDate fecha,
    LocalTime hora
)
```

y otro constructor utilizado al registrar una nueva entrega:

```java
Entrega(
    int idPedido,
    int idRepartidor,
    LocalDate fecha,
    LocalTime hora
)
```

---

### TipoPedido

Enum encargado de definir los tipos de pedido permitidos.

Sus valores son:

```java
COMIDA
ENCOMIENDA
EXPRESS
```

Esto evita almacenar tipos que no formen parte de las reglas del sistema.

---

### EstadoPedido

Enum utilizado para controlar los estados válidos de un pedido.

Sus valores son:

```java
PENDIENTE
EN_REPARTO
ENTREGADO
```

---

### ConexionDB

La clase `ConexionDB` centraliza la creación de conexiones hacia MySQL.

Utiliza:

```java
DriverManager.getConnection()
```

y retorna un objeto:

```java
Connection
```

Su método principal es:

```java
conectar()
```

El método declara:

```java
throws SQLException
```

de modo que las clases DAO puedan capturar y manejar correctamente los problemas producidos durante el acceso a la base de datos.

Los datos principales utilizados para establecer la conexión son:

```java
URL
USER
PASSWORD
```

---

### RepartidorDAO

La clase `RepartidorDAO` administra la persistencia de los repartidores.

Implementa:

```java
create()
readAll()
update()
delete()
```

El método `create()` registra un nuevo repartidor.

El método `readAll()` obtiene todos los registros utilizando:

```java
ResultSet
```

El método `update()` modifica el nombre de un repartidor existente.

El método `delete()` elimina un repartidor según su identificador.

Todas las consultas se ejecutan mediante:

```java
PreparedStatement
```

---

### PedidoDAO

La clase `PedidoDAO` administra las operaciones CRUD correspondientes a los pedidos.

Implementa:

```java
create()
readAll()
update()
delete()
```

Durante el registro y actualización convierte los enums mediante:

```java
pedido.getTipo().name()
pedido.getEstado().name()
```

Al recuperar los registros desde MySQL realiza la conversión inversa mediante:

```java
TipoPedido.valueOf(...)
EstadoPedido.valueOf(...)
```

De esta forma, los valores de MySQL se convierten nuevamente en objetos Java.

---

### EntregaDAO

La clase `EntregaDAO` administra la persistencia de las entregas.

Implementa:

```java
create()
readAll()
update()
delete()
```

Las entregas almacenan las relaciones correspondientes a:

```plaintext
Pedido
Repartidor
```

mediante:

```plaintext
id_pedido
id_repartidor
```

La clase también convierte:

```java
LocalDate
LocalTime
```

a:

```java
java.sql.Date
java.sql.Time
```

mediante:

```java
Date.valueOf()
Time.valueOf()
```

Al recuperar información desde MySQL realiza la conversión inversa mediante:

```java
toLocalDate()
toLocalTime()
```

---

### VentanaPrincipal

La clase `VentanaPrincipal` implementa la interfaz gráfica del sistema.

Hereda de:

```java
JFrame
```

La ventana contiene:

```java
JTabbedPane
```

con tres pestañas:

```plaintext
Repartidores
Pedidos
Entregas
```

Sus responsabilidades principales son:

* Crear los formularios.
* Capturar los datos ingresados.
* Validar los campos.
* Ejecutar las operaciones DAO.
* Mostrar mensajes mediante `JOptionPane`.
* Mostrar resultados mediante `JTable`.
* Actualizar las tablas.
* Actualizar los `JComboBox`.
* Mantener sincronizada la interfaz con MySQL.

La clase también utiliza métodos reutilizables para evitar repetir lógica innecesariamente.

Entre ellos se encuentran métodos destinados a:

```plaintext
validar textos
obtener identificadores seleccionados
confirmar eliminaciones
mostrar mensajes
crear modelos de tablas
limpiar formularios
cargar información desde la base de datos
```

---

### Main

La clase `Main` representa el punto de entrada del sistema.

La interfaz se inicia mediante:

```java
SwingUtilities.invokeLater()
```

Esto permite crear la ventana principal dentro del hilo de eventos de Swing.

La clase ejecutada es:

```java
app.Main
```

---

## Patrón DAO aplicado

El proyecto utiliza el patrón:

```plaintext
DAO - Data Access Object
```

para mantener separado el acceso a la base de datos del resto de la aplicación.

Las clases encargadas de la persistencia son:

```plaintext
RepartidorDAO
PedidoDAO
EntregaDAO
```

Cada una implementa:

```java
create()
readAll()
update()
delete()
```

La interfaz no contiene directamente las sentencias SQL.

En su lugar utiliza los métodos proporcionados por las clases DAO.

Esta separación facilita la organización y permite mantener responsabilidades claramente definidas.

---

## Operaciones CRUD

### Create

Permite registrar nuevos elementos mediante sentencias:

```sql
INSERT
```

Por ejemplo:

```sql
INSERT INTO repartidores (nombre)
VALUES (?)
```

---

### Read

Permite recuperar los registros mediante:

```sql
SELECT
```

Los resultados son procesados utilizando:

```java
ResultSet
```

---

### Update

Permite modificar registros existentes mediante:

```sql
UPDATE
```

Las operaciones utilizan el identificador correspondiente para determinar el registro que debe modificarse.

---

### Delete

Permite eliminar registros mediante:

```sql
DELETE
```

Las relaciones definidas mediante claves foráneas evitan eliminar pedidos o repartidores que aún se encuentren asociados a una entrega.

---

## PreparedStatement

Las operaciones SQL utilizan:

```java
PreparedStatement
```

en lugar de construir consultas concatenando directamente los valores ingresados por el usuario.

Por ejemplo:

```java
String sql =
        "UPDATE repartidores "
                + "SET nombre = ? "
                + "WHERE id = ?";
```

Los valores son asignados posteriormente:

```java
ps.setString(
        1,
        repartidor.getNombre()
);

ps.setInt(
        2,
        repartidor.getId()
);
```

Esto permite ejecutar las consultas de manera parametrizada y evita insertar directamente los datos ingresados dentro de la sentencia SQL.

---

## ResultSet

Las consultas de lectura utilizan:

```java
ResultSet
```

para recorrer los registros obtenidos desde MySQL.

Ejemplo:

```java
while (rs.next()) {

    Repartidor repartidor =
            new Repartidor(
                    rs.getInt("id"),
                    rs.getString("nombre")
            );

    repartidores.add(repartidor);
}
```

De esta forma, cada fila recuperada desde la base de datos se transforma nuevamente en un objeto Java.

---

## Manejo de conexiones y recursos

Las clases DAO utilizan:

```java
try-with-resources
```

para manejar los recursos utilizados durante las operaciones JDBC.

Por ejemplo:

```java
try (
        Connection conexion = ConexionDB.conectar();
        PreparedStatement ps =
                conexion.prepareStatement(sql)
) {
    ...
}
```

Para las consultas de lectura también se incluye:

```java
ResultSet
```

dentro del bloque de recursos.

Esto permite cerrar automáticamente:

```plaintext
Connection
PreparedStatement
ResultSet
```

una vez finalizada cada operación.

---

## Validaciones de entrada

La interfaz valida los datos antes de ejecutar operaciones sobre la base de datos.

### Validaciones de repartidores

Se comprueba que:

* El nombre sea obligatorio.
* El nombre no esté vacío.
* El nombre no supere los 100 caracteres.

---

### Validaciones de pedidos

Se comprueba que:

* La dirección sea obligatoria.
* La dirección no esté vacía.
* La dirección no supere los 100 caracteres.
* Exista un tipo seleccionado.
* Exista un estado seleccionado.

---

### Validaciones de entregas

Antes de registrar o actualizar una entrega se comprueba que:

* Exista un pedido seleccionado.
* Exista un repartidor seleccionado.
* La fecha sea obligatoria.
* La fecha tenga un formato correcto.
* La hora sea obligatoria.
* La hora tenga un formato correcto.

El formato esperado para la fecha es:

```plaintext
AAAA-MM-DD
```

Ejemplo:

```plaintext
2026-10-04
```

Para la hora se permite:

```plaintext
HH:MM
```

o:

```plaintext
HH:MM:SS
```

Ejemplo:

```plaintext
18:30
18:30:45
```

La validación se realiza utilizando:

```java
LocalDate.parse()
LocalTime.parse()
```

y capturando:

```java
DateTimeParseException
```

cuando el formato ingresado no es válido.

---

## Manejo de errores

Las operaciones de acceso a datos capturan:

```java
SQLException
```

mediante bloques:

```java
try
catch
```

Por ejemplo:

```java
catch (SQLException e) {

    System.out.println(
            "Error al registrar pedido: "
                    + e.getMessage()
    );

    return false;
}
```

La interfaz recibe el resultado de las operaciones DAO y muestra mensajes mediante:

```java
JOptionPane
```

El usuario recibe mensajes diferenciados de:

```plaintext
Exito
Validacion
Error
Confirmacion
```

Cuando un pedido o repartidor posee una entrega asociada, la base de datos impide eliminar el registro debido a las claves foráneas.

La interfaz informa al usuario que el registro puede tener entregas asociadas.

---

## Interfaz gráfica

La interfaz fue desarrollada utilizando **Java Swing**.

La ventana principal contiene las pestañas:

```plaintext
Repartidores
Pedidos
Entregas
```

Los principales componentes utilizados son:

```plaintext
JFrame
JPanel
JTabbedPane
JLabel
JTextField
JComboBox
JButton
JTable
JScrollPane
JOptionPane
```

---

## Interfaz de repartidores

La pestaña permite:

```plaintext
Ingresar nombre
Agregar
Editar
Eliminar
Listar
```

Los registros se muestran dentro de una tabla con las columnas:

```plaintext
ID
Nombre
```

Al seleccionar una fila, el nombre del repartidor se carga nuevamente en el formulario para permitir su modificación.

---

## Interfaz de pedidos

La pestaña permite seleccionar:

```plaintext
Direccion
Tipo
Estado
```

Los pedidos se muestran en una tabla con:

```plaintext
ID
Direccion
Tipo
Estado
```

Los enums son presentados mediante `JComboBox`.

Al seleccionar una fila, sus datos vuelven a cargarse dentro del formulario para facilitar la edición.

---

## Interfaz de entregas

La pestaña de entregas permite seleccionar:

```plaintext
Pedido
Repartidor
Fecha
Hora
```

Pedido y repartidor se obtienen desde la base de datos.

Los valores mostrados pueden visualizarse como:

```plaintext
#2 - Ñuñoa
#2 - Sofia
```

Las entregas son mostradas en una tabla con:

```plaintext
ID
Pedido
Repartidor
Fecha
Hora
```

---

## Actualización de JTable

Después de realizar operaciones CRUD se vuelven a cargar los registros desde la base de datos.

Los métodos principales son:

```java
cargarRepartidores()
cargarPedidos()
cargarEntregas()
```

Cada uno obtiene nuevamente los registros desde su DAO correspondiente.

De esta manera, la interfaz refleja el estado actual de MySQL después de cada operación.

---

## Actualización de JComboBox

Los combos utilizados para registrar entregas se actualizan mediante:

```java
cargarCombosEntrega()
```

Este método consulta:

```java
pedidoDAO.readAll()
repartidorDAO.readAll()
```

y carga nuevamente:

```java
comboPedidoEntrega
comboRepartidorEntrega
```

Cuando se crea, modifica o elimina un repartidor o pedido, los combos se vuelven a cargar.

De esta manera se mantienen sincronizados con la base de datos.

---

## Base de datos

El proyecto utiliza una base de datos MySQL denominada:

```plaintext
speedfast_db
```

Las tablas utilizadas son:

```plaintext
repartidores
pedidos
entregas
```

---

## Tabla repartidores

Contiene:

```plaintext
id
nombre
```

El campo:

```plaintext
id
```

es clave primaria y utiliza:

```sql
AUTO_INCREMENT
```

---

## Tabla pedidos

Contiene:

```plaintext
id
direccion
tipo
estado
```

El campo `tipo` admite:

```plaintext
COMIDA
ENCOMIENDA
EXPRESS
```

El campo `estado` admite:

```plaintext
PENDIENTE
EN_REPARTO
ENTREGADO
```

---

## Tabla entregas

Contiene:

```plaintext
id
id_pedido
id_repartidor
fecha
hora
```

Mantiene claves foráneas hacia:

```plaintext
pedidos
repartidores
```

Esto permite mantener la integridad entre los registros relacionados.

---

## Script de creación de la base de datos

El esquema utilizado por el proyecto corresponde a:

```sql
CREATE DATABASE IF NOT EXISTS speedfast_db;

USE speedfast_db;

CREATE TABLE repartidores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(100) NOT NULL,
    tipo ENUM(
        'COMIDA',
        'ENCOMIENDA',
        'EXPRESS'
    ) NOT NULL,
    estado ENUM(
        'PENDIENTE',
        'EN_REPARTO',
        'ENTREGADO'
    ) NOT NULL
);

CREATE TABLE entregas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT,
    id_repartidor INT,
    fecha DATE,
    hora TIME,

    FOREIGN KEY (id_pedido)
        REFERENCES pedidos(id),

    FOREIGN KEY (id_repartidor)
        REFERENCES repartidores(id)
);
```

---

## Configuración JDBC

La conexión se encuentra configurada dentro de:

```plaintext
src/conexion/ConexionDB.java
```

La clase contiene:

```java
URL
USER
PASSWORD
```

El formato utilizado para la URL es:

```plaintext
jdbc:mysql://127.0.0.1:PUERTO/speedfast_db
```

En el equipo utilizado para desarrollar esta actividad, MySQL fue configurado mediante el puerto:

```plaintext
3307
```

Sin embargo, el puerto puede variar dependiendo de la instalación local de MySQL.

El puerto habitual también puede ser:

```plaintext
3306
```

Antes de ejecutar el proyecto se deben verificar:

```plaintext
Host
Puerto
Base de datos
Usuario
Contraseña
```

Ejemplo:

```java
private static final String URL =
        "jdbc:mysql://127.0.0.1:3306/speedfast_db";

private static final String USER =
        "root";

private static final String PASSWORD =
        "CONTRASENA_MYSQL";
```

La contraseña debe reemplazarse por la correspondiente a la instalación local.

---

## MySQL Connector/J

El proyecto utiliza el controlador JDBC oficial de MySQL:

```plaintext
MySQL Connector/J
```

El archivo `.jar` se encuentra dentro de:

```plaintext
lib/
```

y debe estar agregado como dependencia del proyecto.

En IntelliJ IDEA puede verificarse mediante:

```plaintext
File
→ Project Structure
→ Modules
→ Dependencies
```

El conector debe encontrarse disponible con alcance:

```plaintext
Compile
```

---

## Conceptos aplicados

### Encapsulamiento

Los atributos de las clases del modelo se encuentran declarados como:

```java
private
```

El acceso a la información necesaria se realiza mediante métodos `get`.

---

### Enumeraciones

El proyecto utiliza:

```java
TipoPedido
EstadoPedido
```

para restringir los valores permitidos para pedidos.

Esto evita utilizar textos arbitrarios para representar tipos o estados.

---

### Separación por capas

El sistema se encuentra organizado en:

```plaintext
app
conexion
dao
model
vista
```

Cada paquete posee una responsabilidad específica.

```plaintext
app       → punto de entrada
conexion  → conexión JDBC
dao       → operaciones de persistencia
model     → entidades del sistema
vista     → interfaz gráfica
```

---

### Patrón DAO

Las operaciones SQL se encuentran centralizadas dentro de:

```plaintext
RepartidorDAO
PedidoDAO
EntregaDAO
```

Esto evita mezclar sentencias SQL directamente con la interfaz gráfica.

---

### Modularización

La lógica de la interfaz se divide en métodos específicos encargados de:

```plaintext
crear paneles
cargar tablas
cargar combos
obtener información de formularios
validar datos
limpiar formularios
seleccionar elementos
mostrar mensajes
confirmar eliminaciones
```

Esto favorece la reutilización de código y evita concentrar toda la lógica dentro de un único método.

---

### Persistencia

Los datos administrados por la aplicación se almacenan dentro de MySQL.

Esto permite mantener los registros aunque la aplicación sea cerrada y ejecutada nuevamente.

---

### Integridad referencial

La tabla:

```plaintext
entregas
```

se relaciona con:

```plaintext
pedidos
repartidores
```

mediante claves foráneas.

Esto impide crear asociaciones hacia registros inexistentes y protege las relaciones almacenadas en la base de datos.

---

## Instrucciones para clonar y ejecutar el proyecto

### 1. Clonar el repositorio

```bash
git clone REEMPLAZAR_POR_URL_DEL_REPOSITORIO
```

### 2. Abrir el proyecto

Abrir la carpeta:

```plaintext
SpeedFast-CRUD
```

mediante **IntelliJ IDEA**.

### 3. Verificar MySQL Connector/J

Comprobar que el archivo ubicado en:

```plaintext
lib/
```

se encuentre agregado dentro de las dependencias del módulo.

### 4. Crear la base de datos

Ejecutar en MySQL Workbench el script correspondiente a:

```plaintext
speedfast_db
```

y comprobar la existencia de:

```plaintext
repartidores
pedidos
entregas
```

### 5. Configurar la conexión

Modificar si corresponde:

```plaintext
src/conexion/ConexionDB.java
```

verificando:

```plaintext
URL
USER
PASSWORD
```

### 6. Ejecutar el sistema

Ejecutar:

```plaintext
src/app/Main.java
```

o ejecutar la clase:

```java
app.Main
```

---

## Flujo general de uso

### Repartidores

```plaintext
1. Ingresar nombre.
2. Presionar Agregar.
3. Seleccionar un registro para editarlo.
4. Modificar el nombre.
5. Presionar Editar.
6. Seleccionar un registro.
7. Presionar Eliminar.
```

### Pedidos

```plaintext
1. Ingresar dirección.
2. Seleccionar tipo.
3. Seleccionar estado.
4. Presionar Agregar.
5. Seleccionar un pedido para modificarlo.
6. Presionar Editar.
7. Seleccionar un pedido.
8. Presionar Eliminar.
```

### Entregas

```plaintext
1. Seleccionar un pedido.
2. Seleccionar un repartidor.
3. Ingresar fecha.
4. Ingresar hora.
5. Presionar Agregar.
6. Seleccionar una entrega para modificarla.
7. Presionar Editar.
8. Seleccionar una entrega.
9. Presionar Eliminar.
```

---

## Resultado esperado

Al ejecutar la aplicación se abrirá la ventana:

```plaintext
SpeedFast CRUD
```

con las pestañas:

```plaintext
Repartidores
Pedidos
Entregas
```

Las operaciones realizadas desde la interfaz se reflejan directamente en:

```plaintext
speedfast_db
```

Después de cada operación, las tablas y selectores correspondientes se actualizan para representar el estado actual de los datos.

---

## Tecnologías utilizadas

* Java.
* Java Swing.
* JDBC.
* MySQL.
* MySQL Connector/J.
* IntelliJ IDEA.
* Git.
* GitHub.
* Programación Orientada a Objetos.
* Patrón DAO.
* Operaciones CRUD.

---

## Repositorio GitHub

```plaintext
https://github.com/enunezj/SpeedFast-CRUD
```

---

## Fecha de Entrega

05/10/2026
