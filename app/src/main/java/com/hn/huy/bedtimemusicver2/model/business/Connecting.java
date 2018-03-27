package com.hn.huy.bedtimemusicver2.model.business;

/**
 * Created by huy on 7/2/17.
 */

public interface Connecting<T> {

    public void find(T object);

    public void insert(T object);

    public void delete(T object);

    public void update(T objecct);

    public void findAll();

    public void deleteAll();
}
