package K8BackEnd.Estaciones.services;

import K8BackEnd.Estaciones.application.requests.CreateEstacionRequest;
import K8BackEnd.Estaciones.model.Estacion;

import java.util.List;

public interface EstacionService {
    List<Estacion> mostrarTodos();

    Estacion buscarMasCercano(double latitud, double longitud);

    Object publish(CreateEstacionRequest request);
}
