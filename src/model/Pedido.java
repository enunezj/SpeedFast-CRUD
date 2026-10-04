package model;

/**
 * Representa un pedido gestionado por SpeedFast.
 */
public class Pedido {

    private int id;
    private final String direccion;
    private final TipoPedido tipo;
    private final EstadoPedido estado;

    public Pedido(
            int id,
            String direccion,
            TipoPedido tipo,
            EstadoPedido estado) {

        this.id = id;
        this.direccion = direccion;
        this.tipo = tipo;
        this.estado = estado;
    }

    public Pedido(
            String direccion,
            TipoPedido tipo,
            EstadoPedido estado) {

        this.direccion = direccion;
        this.tipo = tipo;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    public TipoPedido getTipo() {
        return tipo;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return "#" + id + " - " + direccion;
    }
}