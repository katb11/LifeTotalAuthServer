package model;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String email;

    public User() {}

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
