package domain;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket")
@NamedQuery(name = "Ticket.findByUserOrderBySportNameAndDatum",
            query = "SELECT t FROM Ticket t WHERE t.user = :user ORDER BY t.wedstrijd.sport.naam ASC, t.wedstrijd.datum DESC")
public class Ticket implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    @ToString.Exclude
    private MyUser user;

    @ManyToOne
    private Wedstrijd wedstrijd;
}
