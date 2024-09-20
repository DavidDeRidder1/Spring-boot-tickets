package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@EqualsAndHashCode(of = "olympischNr1")
@Table(name = "wedstrijd")
@JsonPropertyOrder({ "datum", "aanvangsuur", "discipline1", 
	                "discipline2", "aantalVrijePlaatsen", "olympischNr1", "olympischNr2", "prijsTicket"})
public class Wedstrijd implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Sport sport;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate datum;
    
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime aanvangsuur;
    
    
    @ManyToOne
    @JsonIgnore
    private Stadium stadium;
    
    @Transient
    @JsonIgnore
    private Long hulpStadiumId;
    
    @Column(nullable = true)
    private String discipline1;
    
    @Column(nullable = true)
    private String discipline2;
    
    @NotNull
    @Min(value = 0, message = "{wedstrijd.aantalVrijePlaatsen.min.message}")
    @Max(value = 50, message = "{wedstrijd.aantalVrijePlaatsen.max.message}")
    @Column(nullable = false)
    private Integer aantalVrijePlaatsen;
    
    @NotBlank
    @Column(nullable = false)
    private String olympischNr1;
    
    @NotBlank
    @Column(nullable = false)
    private String olympischNr2;
    
    @NotNull 
    @DecimalMin(value = "0.01", message = "{wedstrijd.prijsTicket.min.message}")
    @DecimalMax(value = "149.99", message = "{wedstrijd.prijsTicket.max.message}")
    @Column(nullable = false)
    private Double prijsTicket;
    
    @OneToMany(mappedBy = "wedstrijd")
    @JsonIgnore
    private List<Ticket> tickets;
    
    @Override
    public String toString() {
    	return "tostring";
    }
}

