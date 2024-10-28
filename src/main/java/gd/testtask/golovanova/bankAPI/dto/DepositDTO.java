package gd.testtask.golovanova.bankAPI.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class DepositDTO {

    @Min(value = 0, message = "percentage should be not less than 0")
    private int percentage;

    @Min(value = 0, message = "period should be not less than 0")
    private int period;

    @NotNull(message = "client_id should not be null")
    private int client_id;

    @NotNull(message = "bank_id should not be null")
    private int bank_id;

}
