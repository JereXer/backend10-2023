package K8BackEnd.Alquileres.repositories;

import K8BackEnd.Alquileres.model.Estacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstacionesRepository extends JpaRepository<Estacion,Integer> {
    Estacion findById(int id);
}
