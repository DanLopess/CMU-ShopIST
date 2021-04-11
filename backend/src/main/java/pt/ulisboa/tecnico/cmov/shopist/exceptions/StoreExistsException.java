package pt.ulisboa.tecnico.cmov.shopist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StoreExistsException extends Exception{
    public StoreExistsException(String message) {
        super(message);
    }
}
