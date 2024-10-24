package gd.testtask.golovanova.bankAPI.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Deposit")
public class Deposit {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="client_id",referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name="bank_id",referencedColumnName = "id")
    private Bank bank;

    @Column(name="create_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date createDate;

    @Column(name="percentage")
    @Min(value = 0)
    private int percentage;

    @Column(name="period")
    @Min(value = 0)
    private int period;
}
