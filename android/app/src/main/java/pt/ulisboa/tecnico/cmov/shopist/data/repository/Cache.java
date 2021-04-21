package pt.ulisboa.tecnico.cmov.shopist.data.repository;

import java.util.Collection;

public interface Cache {
    void clearCache();
    void makeCacheDirty();
}
