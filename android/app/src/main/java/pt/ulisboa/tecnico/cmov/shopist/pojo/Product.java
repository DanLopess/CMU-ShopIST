package pt.ulisboa.tecnico.cmov.shopist.pojo;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private UUID uuid;
    private String name;
    private String description;
    // TODO thumbnail????
    private List<UUID> images;

    // TODO constructor with only name and description?
}
