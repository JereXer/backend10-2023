package K8BackEnd.Alquileres.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="ESTACIONES")
public class Estacion {
    @Id
    @Column(name="ID")
    private int id;
    @Column(name="NOMBRE")
    private String nombre;
    @Column(name="FECHA_HORA_CREACION")
    private int fechaHoraCreacion;
    @Column(name="LATITUD")
    private double latitud;
    @Column(name="LONGITUD")
    private double longitud;

}
