package carsharing;

import java.util.ArrayList;
import java.util.List;

public class Company {
    static long companyCount = 1;
    private long id;
    private String name;

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    private List<Car> cars;

    public Company(String name) {
        this.name = name;
        this.id = companyCount++;
        this.cars = new ArrayList<>();
    }

    public Company(long id, String name, List<Car> cars) {
        this.id = id;
        this.name = name;
        this.cars = cars;
    }

    public List<Car> getCars() {
        return cars;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
