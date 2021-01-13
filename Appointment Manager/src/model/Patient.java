package model;

public class Patient extends DataBasic {

    private int id;
    private String name;
    private Address address;
    private String insuranceProvider;

    public Patient() {
    }

    public Patient(int id, String name, Address address, String insuranceProvider) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.insuranceProvider = insuranceProvider;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Patient other = (Patient) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    

}
