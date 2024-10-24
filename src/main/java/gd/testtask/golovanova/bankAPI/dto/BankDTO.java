package gd.testtask.golovanova.bankAPI.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BankDTO {

    @Size(min = 1, max = 300, message = "Name should be between 2 and 300 characters")
    private String name;

    @Pattern(regexp = "^[0-9]{9}$", message = "Bank id code must have 9 numbers")
    private String bankIdCode;

}