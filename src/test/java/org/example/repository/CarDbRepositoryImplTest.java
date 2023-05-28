package org.example.repository;

import junit.framework.Assert;
import org.example.dbconnection.H2DbEmbedded;
import org.example.model.Car;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

class CarDbRepositoryImplTest {

    Car car = new Car("1", "Lada");
    @org.junit.jupiter.api.Test
    void createOrUpdate() {
        try {
            H2DbEmbedded.initDb();
            CarRepository carRepository = new CarDbRepositoryImpl(H2DbEmbedded.getConnection());
            assertEquals(car, carRepository.createOrUpdate(car));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void findById() {
        try {
            H2DbEmbedded.initDb();
            CarRepository carRepository = new CarDbRepositoryImpl(H2DbEmbedded.getConnection());
            carRepository.createOrUpdate(car);
            assertEquals(Optional.empty(), carRepository.findById("100"));
            assertEquals(Optional.of(car), carRepository.findById("1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void findByModel() {
        try {
            H2DbEmbedded.initDb();
            CarRepository carRepository = new CarDbRepositoryImpl(H2DbEmbedded.getConnection());
            carRepository.createOrUpdate(car);
            assertEquals(Set.of(), carRepository.findByModel("BMW"));
            assertEquals(Set.of(car), carRepository.findByModel("Lada"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void findAll() {
        try {
            H2DbEmbedded.initDb();
            CarRepository carRepository = new CarDbRepositoryImpl(H2DbEmbedded.getConnection());
            Collection<Car> cars = new HashSet<>();
            for (int i = 0; i < 5; i++) {
                cars.add(new Car("id-" + i, "new Model-" + i));
            }
            carRepository.createAll(cars);
            assertEquals(cars, carRepository.findAll());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void deleteById() {
        try {
            H2DbEmbedded.initDb();
            CarRepository carRepository = new CarDbRepositoryImpl(H2DbEmbedded.getConnection());
            carRepository.createOrUpdate(car);
            Assert.assertTrue(carRepository.deleteById("1"));
            Assert.assertFalse(carRepository.deleteById("100"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void deleteAll() {
        try {
            H2DbEmbedded.initDb();
            CarRepository carRepository = new CarDbRepositoryImpl(H2DbEmbedded.getConnection());
            carRepository.createOrUpdate(car);
            Assert.assertTrue(carRepository.deleteAll());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void createAll() {
        try {
            H2DbEmbedded.initDb();
            CarRepository carRepository = new CarDbRepositoryImpl(H2DbEmbedded.getConnection());
            carRepository.createOrUpdate(car);
            Collection<Car> cars = new HashSet<>();
            for (int i = 0; i < 5; i++) {
                cars.add(new Car("id-" + i, "new Model-" + i));
            }
            assertEquals(cars, carRepository.createAll(cars));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}