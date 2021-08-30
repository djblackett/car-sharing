package carsharing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:C:\\Users\\davej\\OneDrive\\Documents\\hol-streams-master\\Car Sharing\\Car Sharing\\task\\src\\carsharing\\db\\carsharing";


    public static void main(String[] args) throws IOException {

        CompanyDaoImpl dao = new CompanyDaoImpl();
        CarDaoImpl carDao = new CarDaoImpl();
        CustomerDaoImpl customerDao = new CustomerDaoImpl();

        InputStream in = System.in;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {


            Connection conn = null;
            Statement stmt = null;
            try {
                // STEP 1: Register JDBC driver
                Class.forName(JDBC_DRIVER);

                //STEP 2: Open a connection
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(DB_URL);
                conn.setAutoCommit(true);

                //STEP 3: Execute a query
                System.out.println("Creating table in given database...");
                stmt = conn.createStatement();
                String sqlDropCompanyTable = "DROP TABLE IF EXISTS COMPANY CASCADE ";
                String sql = "CREATE TABLE IF NOT EXISTS  Company " +
                        "(id INTEGER AUTO_INCREMENT not NULL, " +
                        " name VARCHAR(255) UNIQUE NOT NULL, " +
                        " PRIMARY KEY ( id ))";

                String dropCarTable = "DROP TABLE IF EXISTS CAR CASCADE ";
                String createCarTable = "CREATE TABLE IF NOT EXISTS  Car " +
                        "(id INTEGER AUTO_INCREMENT not NULL, " +
                        " name VARCHAR(255) UNIQUE NOT NULL, " +
                        " company_id INTEGER NOT NULL," +
                        " is_rented BOOLEAN, " +
                        " PRIMARY KEY ( id ), " +
                        "FOREIGN KEY (company_id) REFERENCES COMPANY(id)" +
                        ")";

                String dropCustomer = "DROP TABLE IF EXISTS CUSTOMER";
                String createCustomer = "CREATE TABLE IF NOT EXISTS Customer " +
                        "(id integer auto_increment not null, " +
                        "name VARCHAR(255) UNIQUE NOT NULL, " +
                        "rented_car_id INTEGER, " +
                        "PRIMARY KEY (id), " +
                        "FOREIGN KEY (rented_car_id) REFERENCES Car(id)" +
                        ")";


//                stmt.executeUpdate(sqlDropCompanyTable);
//                stmt.executeUpdate(dropCarTable);
//                stmt.executeUpdate(dropCustomer);

                stmt.executeUpdate(sql);
                stmt.executeUpdate(createCarTable);
                stmt.executeUpdate(createCustomer);


                System.out.println("Created tables in given database...");

                // main menu

                String line = "";
                String line2 = "";
                while (true) {
                    System.out.println("1. Log in as a manager\n" +
                            "2. Log in as a customer\n" +
                            "3. Create a customer\n" +
                            "0. Exit");

                    line = bufferedReader.readLine().trim();
                    line = line.toLowerCase(Locale.ROOT);
                    System.out.println();

                    if (line.equals("0")) {
                        break;
                    }

                    if (line.equals("1")) {
                        while (!line2.equals("0")) {
                            System.out.println("1. Company list\n" +
                                    "2. Create a company\n" +
                                    "0. Back");
                            line2 = bufferedReader.readLine().trim();

                            if (line2.equals("1")) {
                                String getAllCompanies = "SELECT name FROM COMPANY ORDER BY id;";
                                List<Company> companies = dao.getAllCompanies(conn);
                                if (companies.isEmpty()) {
                                    System.out.println("The company list is empty.\n");
                                    continue;
                                }
                                System.out.println("Choose a company: ");
                                for (Company c : companies) {
                                    System.out.println(c.getId() + ". " + c.getName());
                                }
                                System.out.println("0. Back");
                                String choice = bufferedReader.readLine().trim();
                                if (choice.equals("0")) {
                                    continue;
                                }
                                List<Company> companyList = companies.stream().filter(e -> e.getId() == Long.parseLong(choice)).collect(Collectors.toList());
                                Company company = null;
                                if (!companyList.isEmpty()) {
                                    company = companyList.get(0);

                                    String carListChoice = "";

                                    while (!carListChoice.equals("0")) {

                                        System.out.println("\n'" + company.getName() + "' company\n" +
                                                "1. Car list\n" +
                                                "2. Create a car\n" +
                                                "0. Back");

                                        carListChoice = bufferedReader.readLine().trim();

                                        if (carListChoice.equals("1")) {

                                            if (company.getCars().isEmpty()) {
                                                System.out.println("The car list is empty!");
                                                continue;
                                            } else {
                                                System.out.println("Car list: ");

                                                List<Car> cars = carDao.getAllCarsByCompanyId((int)company.getId(), stmt);
                                                int i = 1;
                                                for (Car c : cars) {

                                                    System.out.println(i + ". " + c.getName());
                                                    i++;
                                                }
                                                System.out.println();
                                            }
                                        } else if (carListChoice.equals("0")) {
                                            break;
                                        } else if (carListChoice.equals("2")) {
                                            System.out.println("Enter the car name: ");
                                            String carName = bufferedReader.readLine().trim();
                                            Car newCar = new Car(carName, (int) company.getId());
                                            company.getCars().add(newCar);
                                            carDao.save(newCar, conn);

                                        }
                                    }
                                }
                            }



                            if (line2.equals("2")) {
                                System.out.println("Enter the company name: ");
                                String companyName = bufferedReader.readLine().trim();
                                Company newCompany = new Company(companyName);
                                dao.save(newCompany);
                                String sqlSave = String.format("INSERT INTO COMPANY VALUES (%d, '%s')", (int) newCompany.getId(), newCompany.getName());
                                stmt.executeUpdate(sqlSave);
                            }

                        }
                    }

                    if (line.equals("2")) {
                        // login as customer


                        List<Customer> customers = customerDao.getAllCustomers(conn);

                        if (customers == null || customers.isEmpty()) {
                            System.out.println("The customer list is empty!\n");
                            continue;
                        }

                        System.out.println("Choose a customer: ");

                        customers.sort(Comparator.comparingInt(Customer::getId));
                        int i = 1;
                        for (Customer c : customers) {
                            System.out.println(i + ". " + c.getName());
                            i++;
                        }
                        System.out.println("0. Back\n");

                        String customerChoice = bufferedReader.readLine().trim();

                        while (!customerChoice.equals("0")) {


                            Customer chosenCustomer;
                            try {
                                chosenCustomer = customers.get(Integer.parseInt(customerChoice) - 1);

                                System.out.println("1. Rent a car\n" +
                                        "2. Return a rented car\n" +
                                        "3. My rented car\n" +
                                        "0. Back\n");

                                String customerOptionsChoice = bufferedReader.readLine().trim();

                                if (customerOptionsChoice.equals("0")) {
                                    break;
                                }

                                if (customerOptionsChoice.equals("1")) {
                                    // rent a car
                                    List<Company> companies = dao.getAllCompanies(conn);

                                    if (chosenCustomer.getRentedCarId() != null) {
                                        System.out.println("You've already rented a car!");
                                        continue;
                                    }

                                    if (companies.isEmpty()) {
                                        System.out.println("The companies list is empty!");
                                        continue;
                                    }

                                    System.out.println("Choose a company: ");
                                    for (Company c : companies) {
                                        System.out.println(c.getId() + ". " + c.getName());
                                    }

                                    System.out.println("0. Back");
                                    String chosenCompanyName = bufferedReader.readLine().trim();
                                    if (chosenCompanyName.equals("0")) {
                                        continue;
                                    }

                                    Optional<Company> chosenCompanyOpt = companies.stream().filter(company -> company.getName().equals(chosenCompanyName)).findFirst();


                                        Company chosenCompany = companies.get(Integer.parseInt(chosenCompanyName) - 1);
                                        //Company chosenCompany = chosenCompanyOpt.get();
                                        List<Car> companyCars = carDao.getAllUnrentedCarsByCompanyId((int) chosenCompany.getId(), stmt);
                                        //List<Car> companyCarsFiltered = companyCars.stream().filter(car -> !car.isRented()).collect(Collectors.toList());

                                        if (companyCars.isEmpty()) {
                                            System.out.println("No available cars in the '" + chosenCompany.getName() + "' company");
                                            continue;
                                        }

                                        companyCars.sort(Comparator.comparingInt(Car::getId));
                                        int j = 1;
                                        System.out.println("Choose a car: ");
                                        for (Car c : companyCars) {
                                            System.out.println(j + ". " + c.getName() + " - " + c.isRented());
                                            j++;
                                        }
                                        System.out.println("0. Back");

                                        String carChoiceIndex = bufferedReader.readLine().trim();
                                        Car carChoice;

                                        if (carChoiceIndex.equals("0")) {
                                            continue;
                                        }

                                        try {
                                            carChoice = companyCars.get(Integer.parseInt(carChoiceIndex) - 1);
                                            System.out.println("You rented '" + carChoice.getName() + "'");
                                            chosenCustomer.setRentedCarId(carChoice.getId());
                                            carChoice.setRented(true);
                                            carDao.updateCarRentalStatus(carChoice, conn);
                                            customerDao.updateCustomer(chosenCustomer.getId(), carChoice.getId(), conn);
                                            continue;

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("No such car!");
                                        }


                                    }


                                if (customerOptionsChoice.equals("2")) {
                                    // return rented car
                                    if (chosenCustomer.getRentedCarId() == null) {
                                        System.out.println("You didn't rent a car!");
                                        System.out.println();
                                    } else {
                                        int carId = chosenCustomer.getRentedCarId();
                                        chosenCustomer.setRentedCarId(null);
                                        customerDao.updateCustomer(chosenCustomer.getId(), null, conn);

                                        Car rentedCar = carDao.getCarById(carId, conn);
                                        rentedCar.setRented(false);
                                        carDao.updateCarRentalStatus(rentedCar, conn);
                                        System.out.println("You've returned a rented car!\n");
                                        // todo make dao method to update customer column to null
                                    }

                                }

                                if (customerOptionsChoice.equals("3")) {
                                    //my rented car
                                    if (chosenCustomer.getRentedCarId() == null) {
                                        System.out.println("You didn't rent a car!");
                                        System.out.println();
                                        continue;
                                    } else {
                                        Car car = carDao.getCarById(chosenCustomer.getRentedCarId(), conn);
                                        Company company = dao.getCompany((long) car.getCompany_id(), conn);

                                        System.out.println("Your rented car:\n" + car.getName() + "\nCompany:\n" + company.getName());
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(e.getMessage());
                                //System.out.println("Customer not found");
                            }


                        }
                    }

                    if (line.equals("3")) {
                        // create customer

                        System.out.println("Enter the customer name: ");
                        String customerName = bufferedReader.readLine().trim();
                        Customer customer = new Customer(customerName);
                        //customerDao.save(customer);
                        customerDao.saveCustomer(customer, conn);
                        System.out.println("The customer was added!");
                    }


                }


                // STEP 4: Clean-up environment
                stmt.close();
                conn.close();
            } catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            } finally {
                //finally block used to close resources
                try {
                    if (stmt != null) stmt.close();
                } catch (SQLException se2) {
                } // nothing we can do
                try {
                    if (conn != null) conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                } //end finally try
            } //end try
            System.out.println("Goodbye!");
        }
    }


}
