package main.java.types;

import java.time.LocalDate;

/**
 * @author Ahmed Cisse
 * @author Zerubbabel Ashenafi
 */
public class Demographics {
    private char gender;
    private String race;
    private LocalDate birthDate;
    private String phoneNumber;

    public Demographics() {
        this.gender = 'z';
        this.race = "";
        this.birthDate = LocalDate.of(1, 1, 1);
        this.phoneNumber = "";
    }

    public Demographics(char gender, String race, LocalDate birthDate, String phoneNumber) {
        this.gender = gender;
        this.race = race;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
