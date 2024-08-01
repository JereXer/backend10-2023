package K8BackEnd.Alquileres.repositories;

import K8BackEnd.Alquileres.model.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler,Integer> {

    @Query("SELECT coalesce(max(id), 0) from Alquiler ")
    public Integer getMaxId();

    List<Alquiler> findByIdCliente(String  idCliente);
}
