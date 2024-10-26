package gd.testtask.golovanova.bankAPI.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ClientDTO {

    @Size(min = 1, max = 300, message = "Name should be between 2 and 300 characters")
    private String name;

    @Size(min = 1, max = 100, message = "Short name should be between 2 and 100 characters")
    private String shortName;

    @NotEmpty
    private String address;

    private int legalFormId;

}
