package pt.ulisboa.tecnico.cmov.shopist.pojo.localSource.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;
import lombok.Getter;

@Data
@Entity(tableName = "pantries")
public class Pantry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long pantryId;
    public String name;
    public boolean shared;

    public Pantry(String name) {
        this.name = name;
        shared = false;
    }
}
