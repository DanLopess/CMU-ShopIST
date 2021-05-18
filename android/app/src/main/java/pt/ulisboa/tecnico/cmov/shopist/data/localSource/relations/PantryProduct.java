package pt.ulisboa.tecnico.cmov.shopist.data.localSource.relations;

import androidx.room.Embedded;

import java.util.Objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Pantry;
import pt.ulisboa.tecnico.cmov.shopist.data.localSource.dbEntities.Product;

@Data
@NoArgsConstructor
public class PantryProduct {

    @Embedded
    public Pantry pantry;

    @Embedded
    private Product product;

    private Integer qttAvailable;
    private Integer qttNeeded;

    public void increaseQttNeeded() {
        if (qttNeeded < 99)
            qttNeeded++;
    }

    public void decreaseQttAvailable() {
        if (qttAvailable > 0)
            qttAvailable--;
    }

    public void increaseQttAvailable(Integer qtt) {
        qttAvailable += qtt;
        if (qttAvailable > 99)
            qttAvailable = 99;
    }

    public void decreaseQttNeeded(Integer qtt) {
        qttNeeded -= qtt;
        if (qttNeeded < 0)
            qttNeeded = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PantryProduct that = (PantryProduct) o;
        return Objects.equals(pantry.getPantryId(), that.pantry.getPantryId()) &&
                Objects.equals(product.getProductId(), that.product.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pantry.getPantryId(), product.getProductId());
    }
}
