package K8BackEnd.Alquileres.services;

import K8BackEnd.Alquileres.application.requests.InicioAlquilerRequest;
import K8BackEnd.Alquileres.model.Alquiler;
import K8BackEnd.Alquileres.model.Estacion;
import K8BackEnd.Alquileres.model.Tarifa;
import K8BackEnd.Alquileres.repositories.AlquilerRepository;
import K8BackEnd.Alquileres.repositories.EstacionesRepository;
import K8BackEnd.Alquileres.repositories.TarifaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AlquilerServiceImp implements AlquilerService{

    private final AlquilerRepository alquilerRepository;
    private final EstacionesRepository estacionesRepository;
    private final TarifaRepository tarifaRepository;
    public AlquilerServiceImp(AlquilerRepository alquilerRepositorys, EstacionesRepository estacionesRepositorys,
                            TarifaRepository tarifaRepositorys){
        this.estacionesRepository=estacionesRepositorys;
        this.alquilerRepository=alquilerRepositorys;
        this.tarifaRepository=tarifaRepositorys;
    }

    private final String apiUrl = "http://data.fixer.io/api/latest?access_key=210eadfce059d207b606b010f0f9b61a&format=1"; // Reemplaza con la URL de la API externa
    @Override
    public List<Alquiler> mostrarTodos() {
        return this.alquilerRepository.findAll();
    }

    @Override
    public Alquiler inicioAlquiler(InicioAlquilerRequest inicioAlquilerRequest) {
        Alquiler alquilerNuevo = new Alquiler();
        alquilerNuevo.setId(this.alquilerRepository.getMaxId()+1);
        alquilerNuevo.setIdCliente(inicioAlquilerRequest.getIdCliente());
        alquilerNuevo.setEstado(1);
        alquilerNuevo.setEstacionRetiro(this.estacionesRepository.findById(inicioAlquilerRequest.getEstacionRetiro()));

        //calculos de fecha
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        //LocalDateTime date = LocalDateTime.parse(inicioAlquilerRequest.getFechaRetiro(), formatter);
        LocalDateTime date = LocalDateTime.now();
        Date dateD = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());

        alquilerNuevo.setFechaHoraRetiro(date);

        //obtenemos el dia de la semana
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateD);
        // Obtener el día de la semana en forma numérica (1=domingo, 2=lunes, 3=martes, 4=miercoles, 5=jueves, 6=viernes, 7=sábado)
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1){dayOfWeek = 7;} else { dayOfWeek -= 1;}

        final int finalDayOfWeek = dayOfWeek;
        System.out.println(finalDayOfWeek);

        //calculo de Tarifa
        List<Tarifa> tarifasSemanales = this.tarifaRepository.findAll().stream()
                .filter(x -> x.getDefinicion().equals("S"))
                .filter(x->x.getDiaSemana() == finalDayOfWeek)
                .toList();
        List<Tarifa> tarifasEspeciales = this.tarifaRepository.findAll().stream()
                .filter(x -> x.getDefinicion().equals("C"))
                .filter(x->x.getDiaMes() == date.getDayOfMonth() &&
                        x.getMes() == date.getMonthValue() &&
                        x.getAnio() == date.getYear())
                .toList();
        if (tarifasEspeciales.isEmpty()){
            if (tarifasSemanales.isEmpty()){
                System.out.println("no se encontro coincidencia");
            } else{
                Tarifa tarifaSemanal = tarifasSemanales.get(0);
                alquilerNuevo.setIdTarifa(tarifaSemanal);
            }
        } else {
            Tarifa tarifaespecial = tarifasEspeciales.get(0);
            alquilerNuevo.setIdTarifa(tarifaespecial);
        }

        //guardamos el inicio de alquiler
        this.alquilerRepository.save(alquilerNuevo);

        return alquilerNuevo;
    }

    @Override
    public Object finAlquiler(String idCliente, Integer estacionDevolucion, String moneda) {
        Optional<Alquiler> alquilerFiltrado = this.alquilerRepository.findByIdCliente(idCliente).stream()
                .filter(x -> x.getEstado() == 1)
                .findFirst();

        if (alquilerFiltrado.isPresent()) {
            Alquiler alquiler = alquilerFiltrado.get();

            Estacion estacion = estacionesRepository.findById(estacionDevolucion).orElseThrow();
            alquiler.setEstacionDevolucion(estacion);
            alquiler.setEstado(2);
            alquiler.setFechaHoraDevolucion(LocalDateTime.now());

//            calculos para fijar el monto
            double costo = 0;
            costo += alquiler.getIdTarifa().getMontoFijoAlquiler();

            // calcular tiempo total
            Duration duracion = Duration.between(alquiler.getFechaHoraRetiro(), alquiler.getFechaHoraDevolucion());
            long minutosDuracion = duracion.toMinutes();

            // calcular hora y minutos
            long horas = minutosDuracion / 60;
            long minutos = minutosDuracion % 60;

            if (minutos > 30) {
                horas += 1;
                minutos = 0;
            }

            costo += horas * alquiler.getIdTarifa().getMontoHora();
            costo += minutos * alquiler.getIdTarifa().getMontoMinutoFraccion();

            double latitud = alquiler.getEstacionRetiro().getLatitud();
            double longitud = alquiler.getEstacionRetiro().getLongitud();
            double distanciaKM = Math.sqrt(Math.pow((Math.abs(estacion.getLatitud()) - Math.abs(latitud)), 2) + Math.pow((Math.abs(estacion.getLongitud()) - Math.abs(longitud)), 2)) * 110;

            costo += distanciaKM * alquiler.getIdTarifa().getMontoKM();

            alquiler.setMonto(costo);

            this.alquilerRepository.save(alquiler);

            //Conversion del monto
            JsonNode conversor = consumeApi();
            int ars = conversor.get("rates").get("ARS").asInt();
            int mone = conversor.get("rates").get(moneda).asInt();
            alquiler.setMonto(costo/(ars/mone));
            return alquiler;
        }

        return null;
    }

    //Conexion a la api de Conversion
    @Autowired
    private RestTemplate restTemplate;
    public JsonNode consumeApi() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convierte la cadena en un objeto JsonNode
            JsonNode jsonNode = objectMapper.readTree(restTemplate.getForObject(apiUrl, String.class));
            return jsonNode;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Alquiler> buscarPendientes() {
        return this.alquilerRepository.findAll().stream().filter(x->x.getEstado()==1).toList();
    }
    public JsonNode mostrarResponse(){
        return consumeApi();
    }

}
