package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa una entrega asociada a un pedido
 * y a un repartidor.
 */
public class Entrega {

    private int id;
    private final int idPedido;
    private final int idRepartidor;
    private final LocalDate fecha;
    private final LocalTime hora;

    public Entrega(
            int id,
            int idPedido,
            int idRepartidor,
            LocalDate fecha,
            LocalTime hora) {

        this.id = id;
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Entrega(
            int idPedido,
            int idRepartidor,
            LocalDate fecha,
            LocalTime hora) {

        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }
}