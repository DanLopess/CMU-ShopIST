package pt.ulisboa.tecnico.cmov.shopist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ListNotFoundException extends Exception {
    public ListNotFoundException(String message) {
    }
}
