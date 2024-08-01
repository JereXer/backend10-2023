package K8BackEnd.Alquileres.application.requests;

import lombok.Builder;
import lombok.Data;

@Data
public class InicioAlquilerRequest {
    private String idCliente;
    private int estacionRetiro;
    //private String fechaRetiro;

}
