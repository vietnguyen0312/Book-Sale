package com.example.BookSaleProject.Model.Entity;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String sdt;
    private String address;
    private int role;

    public User(int id, String username, String password, String email, String sdt, String address,
            int role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.sdt = sdt;
        this.address = address;
        this.role = role;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
                + ", sdt=" + sdt + ", address=" + address + ", role=" + role + "]" + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        User person = (User) obj;
        return email.equals(person.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
