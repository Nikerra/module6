package org.example.service;

import org.example.model.Car;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

public interface CarService {

    void addCar(String id, String model) throws SQLException;

    void editModel(String id, String model) throws SQLException;

    boolean deleteCar(String id) throws SQLException;
    Set<Car> findByModel(String model) throws SQLException;

    Set<Car> findAll() throws SQLException;

    Boolean deleteAll() throws SQLException;

    Set<Car> createAll(Collection<Car> cars) throws SQLException;
}
