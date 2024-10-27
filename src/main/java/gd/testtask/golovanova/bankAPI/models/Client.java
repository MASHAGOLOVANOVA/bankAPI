package gd.testtask.golovanova.bankAPI.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Сlients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @Size(min = 1, max = 300, message = "Name should be between 2 and 300 characters")
    private String name;

    @Column(name = "short_name")
    @Size(min = 1, max = 100, message = "Short name should be between 2 and 100 characters")
    private String shortName;

    @Column(name = "address")
    @NotEmpty(message = "address should not be empty")
    private String address;

    @ManyToOne
    @JoinColumn(name = "legal_form_id", referencedColumnName = "id")
    private LegalForm legalForm;

    @OneToMany(mappedBy = "client")
    private List<Deposit> deposits;
}
