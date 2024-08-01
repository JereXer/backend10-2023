package K8BackEnd.Estaciones.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ALQUILERES")
public class Alquiler {
    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "ESTADO")
    private int estado;
    @OneToOne
    @JoinColumn(name = "ESTACION_RETIRO")
    private Estacion estacionRetiro;
    @OneToOne
    @JoinColumn(name = "ESTACION_DEVOLUCION")
    private Estacion estacionDevolucion;
    @Column(name = "FECHA_HORA_RETIRO")
    private String fechaHoraRetiro;
    @Column(name = "FECHA_HORA_DEVOLUCION")
    private String fechaHoraDevolucion;
    @Column(name = "MONTO")
    private int monto;
    @OneToOne
    @JoinColumn(name = "ID_TARIFA")
    private Tarifa idTarifa;
}
