package org.example.repository;

import org.example.model.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CarDbRepositoryImpl implements CarRepository {
    private final Connection connection;
    private static final String CREATE_CAR_SQL = "INSERT INTO car (id, model) VALUES (?,?)";
    private static final String UPDATE_CAR_SQL = "UPDATE car SET model = ? WHERE id = ?";
    private static final String SELECT_CAR_BY_ID = "SELECT * FROM car WHERE id = ?";
    private static final String SELECT_CAR_BY_MODEL = "SELECT * FROM car WHERE model = ?";
    private static final String SELECT_CAR_FIND_ALL = "SELECT * FROM car";
    private static final String DELETE_CAR_DELETE_BY_ID = "delete from car where id = ?";
    private static final String SELECT_COUNT_FROM_ID = "SELECT COUNT(*) FROM car where id = ?";
    private static final String SELECT_COUNT_FROM_MODEL = "SELECT COUNT(*) FROM car where model = ?";

    private final PreparedStatement createPreStmt;
    private final PreparedStatement updatePreStmt;
    private final PreparedStatement findByIdPreStmt;
    private final PreparedStatement findByModelPreStmt;
    private final PreparedStatement findAll;
    private final PreparedStatement deleteCarById;

    public CarDbRepositoryImpl(Connection connection) throws SQLException {
        this.connection = connection;
        this.createPreStmt = connection.prepareStatement(CREATE_CAR_SQL);
        this.updatePreStmt = connection.prepareStatement(UPDATE_CAR_SQL);
        this.findByIdPreStmt = connection.prepareStatement(SELECT_CAR_BY_ID);
        this.findByModelPreStmt = connection.prepareStatement(SELECT_CAR_BY_MODEL);
        this.findAll = connection.prepareStatement(SELECT_CAR_FIND_ALL);
        this.deleteCarById = connection.prepareStatement(DELETE_CAR_DELETE_BY_ID);
    }

    @Override
    public Car createOrUpdate(Car car) throws SQLException {
        Optional<Car> optCar = findById(car.getId());
        if (optCar.isEmpty()) {
            createPreStmt.setString(1, car.getId());
            createPreStmt.setString(2, car.getModel());
            createPreStmt.executeUpdate();
        } else {
            updatePreStmt.setString(1, car.getModel());
            updatePreStmt.setString(2, car.getId());
            updatePreStmt.executeUpdate();
        }
        return car;
    }

    @Override
    public Optional<Car> findById(String id) throws SQLException {
        // validation
        int rowsCount = countRowsById(id);
        if (rowsCount > 1) {
            throw new RuntimeException("Car with id = " + id + " was found many times (" + rowsCount + ").");
        } else if (rowsCount == 0) {
            return Optional.empty();
        }

        findByIdPreStmt.setString(1, id);
        ResultSet resultSet = findByIdPreStmt.executeQuery();

        resultSet.next();
        Car car = new Car(resultSet.getString(1), resultSet.getString(2));
        return Optional.of(car);
    }

    @Override
    public Set<Car> findByModel(String model) throws SQLException {

        // validation
        int rowsCount = countRowsByModel(model);
        if (rowsCount > 1) {
            throw new RuntimeException("Car with model = " + model + " was found many times (" + rowsCount + ").");
        } else if (rowsCount == 0) {
            return Set.of();
        }

        findByModelPreStmt.setString(1, model);
        ResultSet resultSet = findByModelPreStmt.executeQuery();

        resultSet.next();
        Car car = new Car(resultSet.getString(1), resultSet.getString(2));
        return Set.of(car);
    }

    @Override
    public Set<Car> findAll() throws SQLException {
        Set<Car> cars = new HashSet<>();
        ResultSet resultSet = findAll.executeQuery();

        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String model = resultSet.getString("model");
            Car car = new Car(id, model);
            cars.add(car);
        }
        return cars;
    }

    @Override
    public Boolean deleteById(String id) throws SQLException {

        deleteCarById.setString(1, id);
        int affectedRows = deleteCarById.executeUpdate();
        return affectedRows != 0;
    }

    @Override
    public Boolean deleteAll() throws SQLException {

        ResultSet resultSet = findAll.executeQuery();
        int affectedRows = 0;
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            deleteCarById.setString(1,id);
            affectedRows = deleteCarById.executeUpdate();
        }
        return affectedRows != 0;
    }

    @Override
    public Set<Car> createAll(Collection<Car> cars) throws SQLException {
        Set<Car> carSet = new HashSet<>();
        for (Car car : cars) {
            Optional<Car> optCar = findById(car.getId());
            if (optCar.isEmpty()) {
                createPreStmt.setString(1, car.getId());
                createPreStmt.setString(2, car.getModel());
                createPreStmt.executeUpdate();
            } else {
                updatePreStmt.setString(1, car.getModel());
                updatePreStmt.setString(2, car.getId());
                updatePreStmt.executeUpdate();
            }
            carSet.add(car);
        }
        return carSet;
    }

    private int countRowsById(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_FROM_ID);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount = resultSet.getInt(1);
        }
        return rowCount;
    }

    private int countRowsByModel(String model) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_FROM_MODEL);
        preparedStatement.setString(1, model);
        ResultSet resultSet = preparedStatement.executeQuery();
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount = resultSet.getInt(1);
        }
        return rowCount;
    }
}
