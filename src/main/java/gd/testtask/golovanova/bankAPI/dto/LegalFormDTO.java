package gd.testtask.golovanova.bankAPI.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LegalFormDTO {

    @NotEmpty
    private String name;

}
