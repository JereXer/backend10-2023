package K8BackEnd.Estaciones.repositories;

import K8BackEnd.Estaciones.model.Estacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EstacionRepository extends JpaRepository<Estacion,Integer> {
    @Query("SELECT coalesce(max(id), 0) from Estacion ")
    public Integer getMaxId();
}
