package service.impl;

import model.User;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserService implements UserService {

    private final Map<Long, User> users = new HashMap<>();
    private long nextUserId = 1;

    @Override
    public long userRegistration(String userName, String email, String phoneNumber) {
        long id = nextUserId++;
        User user = new User(id, userName, email, phoneNumber);
        users.put(id, user);
        return id;
    }

    @Override
    public User getUser(long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return user;
    }
}
