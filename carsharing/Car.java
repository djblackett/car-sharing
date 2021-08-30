package carsharing;

public class Car {
    static int carCount = 1; // to have sequential and unique ids
    private int id;
    private String name;
    private int company_id;
    private boolean isRented;


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

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public Car(String name, int company_id) {
        this.name = name;
        this.company_id = company_id;
        this.id = carCount++;
        this.isRented = false;
    }

    // only used for creating car objects from the db
    public Car(int id, String name, int company_id, boolean isRented) {
        this.id = id;
        this.name = name;
        this.company_id = company_id;
        this.isRented = false;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }
}
