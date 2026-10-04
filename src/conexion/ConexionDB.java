package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexion
 * con la base de datos MySQL de SpeedFast.
 */
public class ConexionDB {

    private static final String URL =
            "jdbc:mysql://127.0.0.1:3307/speedfast_db";

    private static final String USER =
            "root";

    private static final String PASSWORD =
            "CAMBIAR_POR_CONTRASENA_MYSQL";

    /**
     * Establece una conexion con la base de datos.
     *
     * @return conexion activa con MySQL
     * @throws SQLException si ocurre un error de conexion
     */
    public static Connection conectar()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}