package dao;

import conexion.ConexionDB;
import model.Entrega;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de realizar las operaciones CRUD
 * asociadas a las entregas.
 */
public class EntregaDAO {

    /**
     * Registra una nueva entrega en la base de datos.
     *
     * @param entrega entrega que se desea registrar
     * @return true si el registro fue exitoso
     */
    public boolean create(Entrega entrega) {

        String sql =
                "INSERT INTO entregas "
                        + "(id_pedido, id_repartidor, fecha, hora) "
                        + "VALUES (?, ?, ?, ?)";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    entrega.getIdPedido()
            );

            ps.setInt(
                    2,
                    entrega.getIdRepartidor()
            );

            ps.setDate(
                    3,
                    Date.valueOf(
                            entrega.getFecha()
                    )
            );

            ps.setTime(
                    4,
                    Time.valueOf(
                            entrega.getHora()
                    )
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al registrar entrega: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Obtiene todas las entregas registradas.
     *
     * @return lista de entregas
     */
    public List<Entrega> readAll() {

        List<Entrega> entregas =
                new ArrayList<>();

        String sql =
                "SELECT id, id_pedido, id_repartidor, fecha, hora "
                        + "FROM entregas";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql);
                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                Entrega entrega =
                        new Entrega(
                                rs.getInt("id"),
                                rs.getInt("id_pedido"),
                                rs.getInt("id_repartidor"),
                                rs.getDate("fecha").toLocalDate(),
                                rs.getTime("hora").toLocalTime()
                        );

                entregas.add(entrega);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar entregas: "
                            + e.getMessage()
            );
        }

        return entregas;
    }

    /**
     * Actualiza una entrega existente.
     *
     * @param entrega entrega con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    public boolean update(Entrega entrega) {

        String sql =
                "UPDATE entregas "
                        + "SET id_pedido = ?, "
                        + "id_repartidor = ?, "
                        + "fecha = ?, "
                        + "hora = ? "
                        + "WHERE id = ?";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    entrega.getIdPedido()
            );

            ps.setInt(
                    2,
                    entrega.getIdRepartidor()
            );

            ps.setDate(
                    3,
                    Date.valueOf(
                            entrega.getFecha()
                    )
            );

            ps.setTime(
                    4,
                    Time.valueOf(
                            entrega.getHora()
                    )
            );

            ps.setInt(
                    5,
                    entrega.getId()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar entrega: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Elimina una entrega según su ID.
     *
     * @param id identificador de la entrega
     * @return true si la eliminación fue exitosa
     */
    public boolean delete(int id) {

        String sql =
                "DELETE FROM entregas WHERE id = ?";

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
                    "Error al eliminar entrega: "
                            + e.getMessage()
            );

            return false;
        }
    }
}