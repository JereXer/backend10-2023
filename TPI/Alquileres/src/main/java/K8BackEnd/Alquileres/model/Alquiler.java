package K8BackEnd.Alquileres.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "ALQUILERES")
public class Alquiler {
    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "ID_CLIENTE")
    private String idCliente;
    @Column(name = "ESTADO")
    private int estado;
    @OneToOne
    @JoinColumn(name = "ESTACION_RETIRO")
    private Estacion estacionRetiro;
    @OneToOne
    @JoinColumn(name = "ESTACION_DEVOLUCION")
    private Estacion estacionDevolucion;
    @Column(name = "FECHA_HORA_RETIRO")
    private LocalDateTime fechaHoraRetiro;
    @Column(name = "FECHA_HORA_DEVOLUCION")
    private LocalDateTime fechaHoraDevolucion;
    @Column(name = "MONTO")
    private Double monto;
    @OneToOne
    @JoinColumn(name = "ID_TARIFA")
    private Tarifa idTarifa;
}
