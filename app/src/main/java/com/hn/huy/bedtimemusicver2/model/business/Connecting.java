package com.hn.huy.bedtimemusicver2.model.business;

import com.hn.huy.bedtimemusicver2.model.entity.Music;

import java.util.List;

/**
 * Created by huy on 7/2/17.
 */

public interface Connecting<T extends Music> {

    Music find(T object);

    boolean insert(T object);

    boolean delete(T object);

    boolean update(T objecct);

    List<Music> findAll();

    List<Music> findAll(String songTitle);

    boolean deleteAll();
}
