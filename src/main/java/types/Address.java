package main.java.types;

/**
 * @author Zerubbabel Ashenafi
 */
public class Address {
    private String street;
    private String city;
    private String state;
    private String postalCode;

    public Address() {
        this.street = "";
        this.city = "";
        this.state = "";
        this.postalCode = "";
    }

    public Address(String city, String postalCode, String state, String street) {
        this.city = city;
        this.postalCode = postalCode;
        this.state = state;
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

}
