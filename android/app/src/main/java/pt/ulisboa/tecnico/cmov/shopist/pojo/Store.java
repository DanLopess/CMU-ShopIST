package pt.ulisboa.tecnico.cmov.shopist.pojo;

import android.location.Location;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class Store {
    private UUID uuid;
    private String name;
    private Location location;
    private List<StoreProduct> products;
}
