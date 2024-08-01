package K8BackEnd.Alquileres.repositories;

import K8BackEnd.Alquileres.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa,Integer> {
}
