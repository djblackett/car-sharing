package carsharing;

public class Customer {
    private static int customerCount = 1;
    private int id;
    private String name;
    private Integer rentedCarId;

    public Customer(String name) {
        this.id = customerCount++;
        this.name = name;
        this.rentedCarId = null;
    }

    public Customer(int id, String name, Integer rentedCarId) {

        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public static int getCustomerCount() {
        return customerCount;
    }

    public static void setCustomerCount(int customerCount) {
        Customer.customerCount = customerCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
}
