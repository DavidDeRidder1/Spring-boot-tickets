package repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import domain.Sport;
import domain.Wedstrijd;

public interface WedstrijdRepository extends CrudRepository<Wedstrijd, Long>{
	
	List<Wedstrijd> findBySportOrderByDatumDescAanvangsuurDesc(Sport sport);

}
