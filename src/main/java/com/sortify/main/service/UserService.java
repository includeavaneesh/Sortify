package com.sortify.main.service;

import java.util.ArrayList;

import com.sortify.main.repository.SortifyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.sortify.main.model.SortifyUser;
import org.springframework.stereotype.Service;

@Service
public class UserService implements SortifyUserService {

    @Autowired
    private SortifyUserRepository USER_REPOSITORY;

    @Override
    public SortifyUser addUser(SortifyUser user) {
        return USER_REPOSITORY.save(user);
    }

    @Override
    public void deleteUser(String username) {
        USER_REPOSITORY.deleteById(username);
    }

    @Override
    public ArrayList<SortifyUser> retrieveAll() {
        return (ArrayList<SortifyUser>) USER_REPOSITORY.findAll();
    }

    @Override
    public SortifyUser findUserByUsername(String username) {
        if (USER_REPOSITORY.findById(username).isPresent()) {
            return USER_REPOSITORY.findById(username).get();
        }

        return null;
    }

    @Override
    public SortifyUser modifyUser(String username, SortifyUser newUser) throws IllegalAccessException {
        SortifyUser oldUser = findUserByUsername(username);

        if (oldUser.getUsername() != null && newUser.getUsername() != null) {
            oldUser.setUsername(newUser.getUsername());
        }

        if (oldUser.getUserFirstName() != null && newUser.getUserFirstName() != null) {
            oldUser.setUserFirstName(newUser.getUserFirstName());
        }

        if (oldUser.getUserLastName() != null && newUser.getUserLastName() != null) {
            oldUser.setUserLastName(newUser.getUserLastName());
        }

        if (oldUser.getPassword() != null && newUser.getPassword() != null) {
            oldUser.setPassword(newUser.getPassword());
        }

        if (oldUser.getUserLastName() != null && newUser.getUserLastName() != null) {
            oldUser.setUserLastName(newUser.getUserLastName());
        }

        USER_REPOSITORY.save(oldUser);
        return oldUser;
    }
}
