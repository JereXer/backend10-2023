package K8BackEnd.Estaciones.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

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
    private LocalDateTime fechaHoraCreacion;
    @Column(name="LATITUD")
    private double latitud;
    @Column(name="LONGITUD")
    private double longitud;

    public Estacion(int id, String nombre, LocalDateTime fechaHoraCreacion, double latitud, double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.fechaHoraCreacion = fechaHoraCreacion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Estacion(){

    }
}
