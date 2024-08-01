package K8BackEnd.Alquileres.application.controllers;

import K8BackEnd.Alquileres.application.ResponseHandler;
import K8BackEnd.Alquileres.application.requests.InicioAlquilerRequest;
import K8BackEnd.Alquileres.model.Alquiler;
import K8BackEnd.Alquileres.services.AlquilerService;
import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {
    private final AlquilerService alquilerService;

    public AlquilerController(AlquilerService alquilerServices) {
        this.alquilerService = alquilerServices;
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodos() {
        try {
            return ResponseHandler.success(this.alquilerService.mostrarTodos());
        } catch (Exception e) {
            return ResponseHandler.internalError();
        }
    }

    @PostMapping
    public ResponseEntity<Object> inicioAlquiler(@RequestBody InicioAlquilerRequest inicioAlquiler) {
        return ResponseHandler.success(this.alquilerService.inicioAlquiler(inicioAlquiler));

    }

    @PostMapping("/finAlquiler/{idCliente}/{estacion}/{moneda}")
    public ResponseEntity<Object> finAlquiler(@PathVariable String idCliente, @PathVariable Integer estacion, @PathVariable String moneda) {
        return ResponseHandler.success(this.alquilerService.finAlquiler(idCliente, estacion, moneda));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<Object> obtenerFiltrados() {
        try {
            List<Alquiler> lista = this.alquilerService.buscarPendientes();
            if (lista.isEmpty()) {
                return ResponseHandler.notFound();
            }
            return ResponseHandler.success(lista);
        } catch (Exception e) {
            return ResponseHandler.internalError();
        }
    }

    @GetMapping("/conversor")
    public ResponseEntity<Object> obtenerResponse() {
        return ResponseHandler.success(this.alquilerService.mostrarResponse());
    }
}
