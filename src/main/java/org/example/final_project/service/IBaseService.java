package org.example.final_project.service;

import java.io.IOException;
import java.util.List;

public interface IBaseService<T, U, K> {
    List<T> getAll();
    T getById(K id);
    int save(U u);
    int update(K k,U u);
    int delete(K id);
}
