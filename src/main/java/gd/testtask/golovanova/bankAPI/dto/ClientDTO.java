package gd.testtask.golovanova.bankAPI.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ClientDTO {

    @Size(min = 1, max = 300, message = "Name should be between 1 and 300 characters")
    private String name;

    @Size(min = 1, max = 100, message = "Short name should be between 1 and 100 characters")
    private String shortName;

    @NotEmpty(message = "address should not be empty")
    private String address;

    @NotNull
    private int legalFormId;

}
