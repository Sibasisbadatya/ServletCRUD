package org.example.service;

import org.example.model.User;
import org.example.servlet.UserServlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private Map<Integer, User> userDB;

    public UserService() {
        userDB = new HashMap<>();
    }

    public User createUser(User user) {
        userDB.put(user.getId(), user);
        return user;
    }

    public List<User> getAllUsers() {
        return (List<User>) userDB.values();
    }

    public User getById(Integer id) {
        return userDB.get(id);
    }


}
