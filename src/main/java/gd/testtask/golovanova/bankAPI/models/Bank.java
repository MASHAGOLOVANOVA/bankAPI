package gd.testtask.golovanova.bankAPI.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "bank")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @Size(min = 1, max = 300, message = "Name should be between 2 and 300 characters")
    private String name;

    @Column(name = "bank_id_code")
    @Pattern(regexp = "^[0-9]{9}$", message = "Bank id code must have 9 numbers")
    private String bankIdCode;

    @OneToMany(mappedBy = "bank")
    private List<Deposit> deposits;
}
