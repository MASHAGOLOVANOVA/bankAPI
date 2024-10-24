package gd.testtask.golovanova.bankAPI.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClientErrorResponse {

    private String message;

    private long timestamp;
}
