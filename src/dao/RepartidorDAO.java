package dao;

import conexion.ConexionDB;
import model.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de realizar las operaciones CRUD
 * asociadas a los repartidores.
 */
public class RepartidorDAO {

    /**
     * Registra un nuevo repartidor.
     */
    public boolean create(Repartidor repartidor) {

        String sql =
                "INSERT INTO repartidores (nombre) VALUES (?)";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    repartidor.getNombre()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al registrar repartidor: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Obtiene todos los repartidores registrados.
     */
    public List<Repartidor> readAll() {

        List<Repartidor> repartidores =
                new ArrayList<>();

        String sql =
                "SELECT id, nombre FROM repartidores";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql);
                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                Repartidor repartidor =
                        new Repartidor(
                                rs.getInt("id"),
                                rs.getString("nombre")
                        );

                repartidores.add(repartidor);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar repartidores: "
                            + e.getMessage()
            );
        }

        return repartidores;
    }

    /**
     * Actualiza los datos de un repartidor.
     */
    public boolean update(Repartidor repartidor) {

        String sql =
                "UPDATE repartidores "
                        + "SET nombre = ? "
                        + "WHERE id = ?";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    repartidor.getNombre()
            );

            ps.setInt(
                    2,
                    repartidor.getId()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar repartidor: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Elimina un repartidor según su ID.
     */
    public boolean delete(int id) {

        String sql =
                "DELETE FROM repartidores WHERE id = ?";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    id
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar repartidor: "
                            + e.getMessage()
            );

            return false;
        }
    }
}