package carsharing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface CarDao {

    Car getCarById(int id, Connection conn) throws SQLException;
    void save(Car car, Connection conn) throws SQLException;
    List<Car> getAllCarsByCompanyId(int companyId, Statement stmt) throws SQLException;
    void updateCarRentalStatus(Car car, Connection conn) throws SQLException;
    List<Car> getAllUnrentedCarsByCompanyId(int companyId, Statement stmt) throws SQLException;
}
