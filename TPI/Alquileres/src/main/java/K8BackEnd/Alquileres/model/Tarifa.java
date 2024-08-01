package K8BackEnd.Alquileres.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="TARIFAS")
public class Tarifa {
    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "TIPO_TARIFA")
    private int tipoTarifa;
    @Column(name = "DEFINICION")
    private String definicion;
    @Column(name = "DIA_SEMANA")
    private int diaSemana;
    @Column(name = "DIA_MES")
    private Integer diaMes;
    @Column(name = "MES")
    private Integer mes;
    @Column(name = "ANIO")
    private Integer anio;
    @Column(name = "MONTO_FIJO_ALQUILER")
    private double montoFijoAlquiler;
    @Column(name = "MONTO_MINUTO_FRACCION")
    private double montoMinutoFraccion;
    @Column(name = "MONTO_KM")
    private double montoKM;
    @Column(name = "MONTO_HORA")
    private double montoHora;

}
