package K8BackEnd.Estaciones.services;

import K8BackEnd.Estaciones.application.requests.CreateEstacionRequest;
import K8BackEnd.Estaciones.model.Estacion;
import K8BackEnd.Estaciones.repositories.EstacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstacionServiceImp implements EstacionService{

    private final EstacionRepository estacionRepository;
    public EstacionServiceImp(EstacionRepository estacionRepositorys){
        this.estacionRepository=estacionRepositorys;
    }

    @Override
    public List<Estacion> mostrarTodos() {
        return this.estacionRepository.findAll();
    }

    @Override
    public Estacion buscarMasCercano(double latitud, double longitud) {
        List<Estacion> estaciones = this.estacionRepository.findAll();
        //consideramos que solamente se miden distancias en argentina
        //argentina esta entre 0<latitud<90 y 0<longitud< 90 aprox
        Estacion estacionCercana = null;
        double distanciaMinima = 0;
        for (Estacion estacion : estaciones){
            // d = √((x2 - x1)^2 + (y2 - y1)^2) --> distancia euclidea
            double distancia = Math.sqrt(  Math.pow((Math.abs(estacion.getLatitud()) - Math.abs(latitud)),2) + Math.pow((Math.abs(estacion.getLongitud()) - Math.abs(longitud)),2) );
            //System.out.println(distancia + estacion.getNombre());
            if (distanciaMinima == 0){
                distanciaMinima = distancia;
                estacionCercana = estacion;
            }

            //poco probable que tiren unas coordenadas y sea el mismo lugar
            if (distancia == 0){
                distanciaMinima = distancia;
                estacionCercana = estacion;
                break;
            }

            if (distanciaMinima > distancia){
                distanciaMinima = distancia;
                estacionCercana = estacion;
            }
        }
        return estacionCercana;
    }

    @Override
    public Object publish(CreateEstacionRequest request) {
        int id = this.estacionRepository.getMaxId() + 1;
        Estacion estacion = new Estacion(id, request.getNombre(), LocalDateTime.now(), request.getLatitud(), request.getLongitud());
        return estacionRepository.save(estacion);
    }
}
