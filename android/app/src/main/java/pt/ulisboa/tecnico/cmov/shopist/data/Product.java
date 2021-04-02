package pt.ulisboa.tecnico.cmov.shopist.data;


import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Product {
    private String name;

    public static List<Product> createProductList() {
        List<Product> list = new ArrayList<>();
        Product product1 = new Product("produto 1");
        Product product2 = new Product("produto 2");
        Product product3 = new Product("produto 3");
        list.add(product1);
        list.add(product2);
        list.add(product3);
        list.add(product1);
        list.add(product2);
        list.add(product3);
        list.add(product1);
        list.add(product2);
        list.add(product3);

        return list;
    }

}
