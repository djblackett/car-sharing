package carsharing;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CompanyDao {
    List<Company> getAllCompanies(Connection conn) throws SQLException;
    Company getCompany(long id, Connection conn) throws SQLException;
    void updateCompany(Company company);
    void deleteCompany(Company company);
    void save(Company company);
}
