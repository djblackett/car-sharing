package carsharing;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDao {

    void saveCustomer(Customer customer, Connection connection) throws SQLException;
    void save(Customer customer);


    List<Customer> getAllCustomers(Connection conn) throws SQLException;

    Customer getCustomerById(int id, Connection conn) throws SQLException;

    void updateCustomer(int id, Integer rentalId, Connection conn) throws SQLException;
}
