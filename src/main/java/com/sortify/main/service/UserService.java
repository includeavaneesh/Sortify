package com.sortify.main.service;

import java.util.ArrayList;

import com.sortify.main.repository.SortifyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.sortify.main.model.SortifyUser;
import org.springframework.stereotype.Service;

@Service
public class UserService implements SortifyUserService {

    @Autowired
    private SortifyUserRepository sortifyUserRepository;

    @Override
    public SortifyUser addUser(SortifyUser user) {
        return sortifyUserRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        sortifyUserRepository.deleteById(username);
    }

    @Override
    public ArrayList<SortifyUser> retrieveAll() {
        return (ArrayList<SortifyUser>) sortifyUserRepository.findAll();
    }

    @Override
    public SortifyUser findUserByUsername(String username) {
        if (sortifyUserRepository.findById(username).isPresent()) {
            return sortifyUserRepository.findById(username).get();
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

        sortifyUserRepository.save(oldUser);


        return oldUser;

    }
}
