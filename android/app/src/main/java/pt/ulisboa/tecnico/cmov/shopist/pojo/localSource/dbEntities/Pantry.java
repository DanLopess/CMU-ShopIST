package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pantries")
public class Pantry {
    @PrimaryKey
    public String pantryId;
    public String name;
    public boolean shared;
}
