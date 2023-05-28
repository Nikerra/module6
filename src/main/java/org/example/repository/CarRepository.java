package org.example.repository;

import org.example.model.Car;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface CarRepository extends Repository<Car, String> {
    Set<Car> findByModel(String model) throws SQLException;
    Optional<Car> findById(String id) throws SQLException;
    Set<Car> findAll() throws SQLException;
    Boolean deleteById(String id) throws SQLException;
    Boolean deleteAll() throws SQLException;
    Set<Car> createAll(Collection<Car> cars) throws SQLException;
}
