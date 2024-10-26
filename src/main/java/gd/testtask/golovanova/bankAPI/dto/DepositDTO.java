package gd.testtask.golovanova.bankAPI.dto;


import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DepositDTO {

    @Min(value = 0)
    private int percentage;

    @Min(value = 0)
    private int period;

    private int client_id;

    private int bank_id;

}
