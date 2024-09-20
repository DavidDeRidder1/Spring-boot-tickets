package repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import domain.MyUser;
import domain.Ticket;
import domain.Wedstrijd;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
    List<Ticket> findByUser(MyUser user);
    List<Ticket> findByWedstrijd(Wedstrijd wedstrijd);
    
    @Query(name = "Ticket.findByUserOrderBySportNameAndDatum")
    List<Ticket> findByUserOrderBySportNameAndDatum(@Param("user") MyUser user);
}
