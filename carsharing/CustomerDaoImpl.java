package carsharing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    List<Customer> customers;

    public CustomerDaoImpl() {
        customers = new ArrayList<>();
    }

    @Override
    public void saveCustomer(Customer customer, Connection connection) throws SQLException {
        String sql = "INSERT INTO CUSTOMER VALUES (?, ?, null)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, customer.getId());
        ps.setString(2, customer.getName());
        ps.executeUpdate();

    }

    @Override
    public List<Customer> getAllCustomers(Connection conn) throws SQLException {


        String sql = "SELECT * FROM CUSTOMER";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<Customer> customers = new ArrayList<>();
        while (rs.next()) {
            customers.add(new Customer(rs.getInt(1), rs.getString(2), (Integer) rs.getObject(3)));
        }
        return customers;
    }

    @Override
    public Customer getCustomerById(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM CUSTOMER WHERE id = ?";
        Connection connection;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        return (Customer) rs.getObject(1);
    }

    @Override
    public void updateCustomer(int id, Integer carRentalId, Connection conn) throws SQLException {
        String sql = "UPDATE CUSTOMER " +
                "SET rented_car_id = ?" +
                "WHERE id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, carRentalId);
        ps.setInt(2, id);
        ps.executeUpdate();
    }

    public void save(Customer customer) {
        customers.add(customer);
    }

}
