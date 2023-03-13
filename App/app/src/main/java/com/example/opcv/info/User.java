package com.example.opcv.info;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    String name;
    String lastName;
    String email;
    String id;
    String phoneNumber;

    String UriPath;

    public User(String name, String lastName, String email, String id, String phoneNumber, String uriPath) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
        this.phoneNumber = phoneNumber;
        UriPath = uriPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUriPath() {
        return UriPath;
    }

    public void setUriPath(String uriPath) {
        UriPath = uriPath;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("lastName", lastName);
        map.put("id", id);
        map.put("email",email);
        map.put("phoneNumber", phoneNumber);
        map.put("UriPath",UriPath);
        return map;
    }
}
