package com.uniajc.dao;

import java.util.List;


public interface BaseDAO<T> {
    boolean insertar(T t);
    boolean actualizar(T t);
    boolean eliminar(int id);
    T buscarPorId(int id);
    List<T> listarTodos();
}
