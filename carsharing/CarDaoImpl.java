package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CarDaoImpl implements CarDao {
    @Override
    public Car getCarById(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM CAR WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
         ResultSet resultSet = ps.executeQuery();

         if (resultSet.next()) {
             return new Car(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getBoolean(4));
         }
         throw new SQLException();
    }

    @Override
    public void save(Car car, Connection conn) throws SQLException {
    PreparedStatement insertNew = conn.prepareStatement(
            "INSERT INTO CAR (id, name, company_id, is_rented) VALUES (?, ?, ?, ?)");
            insertNew.setInt(1, car.getId());
            insertNew.setString(2, car.getName());
            insertNew.setInt(3, car.getCompany_id());
            insertNew.setBoolean(4, car.isRented());
            insertNew.executeUpdate();

    }

    public List<Car> getAllUnrentedCarsByCompanyId(int companyId, Statement stmt) throws SQLException {
        String sql = "SELECT * FROM CAR WHERE company_id = " + companyId + " AND is_rented = FALSE";
        ResultSet rs = stmt.executeQuery(sql);
        List<Car> cars = new ArrayList<>();
        while (rs.next()) {
            cars.add(new Car(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getBoolean(4)));
        }

        cars.sort(Comparator.comparingInt(Car::getId));
        return cars;
    }

    @Override
    public List<Car> getAllCarsByCompanyId(int companyId, Statement stmt) throws SQLException {
        String sql = "SELECT * FROM CAR WHERE company_id = " + companyId;
        ResultSet rs = stmt.executeQuery(sql);
        List<Car> cars = new ArrayList<>();
        while (rs.next()) {
            cars.add(new Car(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getBoolean(4)));
        }

        cars.sort(Comparator.comparingInt(Car::getId));
        return cars;
    }

    @Override
    public void updateCarRentalStatus(Car car, Connection conn) throws SQLException {
        String sql = "UPDATE CAR " +
                "SET is_rented = ?" +
                "WHERE id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setBoolean(1, car.isRented());
        ps.setInt(2, car.getId());
        ps.executeUpdate();

    }
}
