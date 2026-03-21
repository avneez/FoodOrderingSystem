package service;

import model.User;

public interface UserService {
    long userRegistration(String userName, String email, String phoneNumber);
    User getUser(long userId);
}
