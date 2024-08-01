package K8BackEnd.Estaciones.application.controllers;

import K8BackEnd.Estaciones.application.ResponseHandler;
import K8BackEnd.Estaciones.application.requests.CreateEstacionRequest;
import K8BackEnd.Estaciones.services.EstacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estaciones")
public class EstacionController {

    private final EstacionService estacionService;
    public EstacionController(EstacionService estacionServices){
        this.estacionService=estacionServices;
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodos(){
        try {
            return ResponseHandler.success(this.estacionService.mostrarTodos());
        } catch (Exception e){
            return ResponseHandler.internalError();
        }
    }

    @GetMapping("/{latitud}/{longitud}")
    public ResponseEntity<Object> obtenerMasCercano(@PathVariable double latitud,@PathVariable double longitud){
        try {
            return ResponseHandler.success(this.estacionService.buscarMasCercano(latitud,longitud));
        } catch (Exception e){
            return ResponseHandler.internalError();
        }
    }

    @PostMapping
    public ResponseEntity<Object> agregarEstacion(@RequestBody CreateEstacionRequest request){
        return ResponseHandler.success(this.estacionService.publish(request));
    }
}
