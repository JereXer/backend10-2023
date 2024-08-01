package K8BackEnd.Alquileres.services;

import K8BackEnd.Alquileres.application.requests.InicioAlquilerRequest;
import K8BackEnd.Alquileres.model.Alquiler;

import java.util.List;

public interface AlquilerService {
    List<Alquiler> mostrarTodos();

    Alquiler inicioAlquiler(InicioAlquilerRequest inicioAlquilerRequest);

    Object mostrarResponse();

    Object finAlquiler(String  idCliente, Integer estacion, String moneda);

    List<Alquiler> buscarPendientes();
}
