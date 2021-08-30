package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {

    List<Company> companies;

    public CompanyDaoImpl() {
        companies = new ArrayList<>();
    }

    @Override
    public List<Company> getAllCompanies(Connection conn) throws SQLException {

        Statement s = conn.createStatement();
        CarDaoImpl carDao = new CarDaoImpl();

        String sql = "SELECT * FROM COMPANY ORDER BY id ASC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Company> companies = new ArrayList<>();

        while (rs.next()) {
            companies.add(new Company(rs.getInt(1), rs.getString(2), null));
        }

        for (Company c: companies) {
            List<Car> cars = carDao.getAllCarsByCompanyId((int) c.getId(), s);
            cars.sort(Comparator.comparingInt(Car::getId));
            c.setCars(cars);
        }

        return companies;
    }

    @Override
    public Company getCompany(long id, Connection conn) throws SQLException {
        CarDaoImpl carDao = new CarDaoImpl();

        String sql = "SELECT * FROM COMPANY WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, (int) id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        Company company = new Company(rs.getLong(1), rs.getString(2), null);
        List<Car> cars = carDao.getAllCarsByCompanyId((int) id, conn.createStatement());
        company.setCars(cars);
        return company;
    }

    @Override
    public void updateCompany(Company company) {
        companies.get((int) company.getId()).setName(company.getName());
    }

    @Override
    public void deleteCompany(Company company) {
        companies.remove((int) company.getId());
    }

    @Override
    public void save(Company company) {
        companies.add(company);
    }
}
