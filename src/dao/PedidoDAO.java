package dao;

import conexion.ConexionDB;
import model.EstadoPedido;
import model.Pedido;
import model.TipoPedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de realizar las operaciones CRUD
 * asociadas a los pedidos.
 */
public class PedidoDAO {

    /**
     * Registra un nuevo pedido.
     */
    public boolean create(Pedido pedido) {

        String sql =
                "INSERT INTO pedidos "
                        + "(direccion, tipo, estado) "
                        + "VALUES (?, ?, ?)";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    pedido.getDireccion()
            );

            ps.setString(
                    2,
                    pedido.getTipo().name()
            );

            ps.setString(
                    3,
                    pedido.getEstado().name()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al registrar pedido: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Obtiene todos los pedidos registrados.
     */
    public List<Pedido> readAll() {

        List<Pedido> pedidos =
                new ArrayList<>();

        String sql =
                "SELECT id, direccion, tipo, estado "
                        + "FROM pedidos";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql);
                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                Pedido pedido =
                        new Pedido(
                                rs.getInt("id"),
                                rs.getString("direccion"),
                                TipoPedido.valueOf(
                                        rs.getString("tipo")
                                ),
                                EstadoPedido.valueOf(
                                        rs.getString("estado")
                                )
                        );

                pedidos.add(pedido);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar pedidos: "
                            + e.getMessage()
            );
        }

        return pedidos;
    }

    /**
     * Actualiza un pedido existente.
     */
    public boolean update(Pedido pedido) {

        String sql =
                "UPDATE pedidos "
                        + "SET direccion = ?, "
                        + "tipo = ?, "
                        + "estado = ? "
                        + "WHERE id = ?";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    pedido.getDireccion()
            );

            ps.setString(
                    2,
                    pedido.getTipo().name()
            );

            ps.setString(
                    3,
                    pedido.getEstado().name()
            );

            ps.setInt(
                    4,
                    pedido.getId()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar pedido: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Elimina un pedido según su ID.
     */
    public boolean delete(int id) {

        String sql =
                "DELETE FROM pedidos WHERE id = ?";

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
                    "Error al eliminar pedido: "
                            + e.getMessage()
            );

            return false;
        }
    }
}