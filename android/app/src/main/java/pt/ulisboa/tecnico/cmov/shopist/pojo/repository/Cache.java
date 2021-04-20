package pt.ulisboa.tecnico.cmov.shopist.pojo.repository;

import java.util.List;

public interface Cache<T> {
    void clearCache();
    void makeCacheDirty();
    void updateCache(List<T> cacheObjects);
}
