package pt.ulisboa.tecnico.cmov.shopist.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Store {
    private String name;
    private String coordinates;
    private String uuid;
}
