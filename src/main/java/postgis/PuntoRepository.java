package postgis;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuntoRepository extends CrudRepository<Punto, Integer> {

	List<Punto> findByPId(Integer pId);

//	@Query("select c from app c where within(c.geom, ?1) = true")
//	List<Punto> findWithin(Geometry filter);
}