package K8BackEnd.Estaciones.application.requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateEstacionRequest {

    private String nombre;
    //private LocalDateTime fechaHoraCreacion;
    private double latitud;
    private double longitud;
}