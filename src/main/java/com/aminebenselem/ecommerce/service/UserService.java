package com.aminebenselem.ecommerce.service;

import com.aminebenselem.ecommerce.entity.User;
import com.aminebenselem.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepo;
    public User getUser(String username) {
        try {
            return userRepo.findByUsername(username).get();
        }catch(UsernameNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + username, e);
        }
    }

    public User addUser(User user){
        return userRepo.save(user);
    }
    public void banUser(String username) {
        User user = getUser(username); // Reuse getUser for consistent error handling
        user.setBanned(true);
        userRepo.save(user);
    }

    // Unban a user by username
    public void unbanUser(String username) {
        User user = getUser(username); // Reuse getUser for consistent error handling
        user.setBanned(false);
        userRepo.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
    public Optional<User> getUserByUsernameOrEmail(String username, String email) {
        return userRepo.findByUsernameOrEmail(username, email);
    }

}
