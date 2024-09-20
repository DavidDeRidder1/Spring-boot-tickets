package repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import domain.Sport;

@Component
public interface SportRepository extends CrudRepository<Sport, Long> {

}
