package com.example.security.service;

import com.example.security.model.user.CustomUser;
import com.example.security.model.enums.Role;
import com.example.security.model.Validation;
import com.example.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(CustomUser user) {
        //Checking that there are no empty fields
        if (user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null || user.getPhone() == null ||
                user.getAddress() == null || user.getUsername() == null || user.getPassword() == null) {
            return "User not created: first name, last name, email, phone, address, username and password are required";
        }
        //Input integrity check
        String userValidateResult = Validation.validateUserForRegister(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(),
                user.getAddress(), user.getUsername(), user.getPassword());
        if(userValidateResult != null){
            return "User not created: "+userValidateResult;
        }
        if (user.getRole() == Role.ADMIN){
            return "User not created: You are not allowed to set your role to ADMIN";
        }

        //Unique fields dose not exist yet in database
        CustomUser userWithTheSameEmail = getUserByEmail(user.getEmail());
        if(userWithTheSameEmail != null){
            return "User not created: This email already exists in the system.";
        }
        CustomUser userWithTheSameUsername = getUserByUsername(user.getUsername());
        if(userWithTheSameUsername != null){
            return "User not created: This username already exists in the system.";
        }

        //password encryption with Bcrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("Encoded password: " + user.getPassword()); // Log the encoded password

        //Default settings
        user.setEmail(user.getEmail().toLowerCase());
        return userRepository.register(user);
    }

    public CustomUser getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public CustomUser getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public List<CustomUser> getAllUsers() {
        return userRepository.findAllUsers();
    }

    public String updateUser(CustomUser updatedUser) {
        //Checking that there are no empty fields
        if (updatedUser.getFirstName() == null || updatedUser.getLastName() == null || updatedUser.getEmail() == null || updatedUser.getPhone() == null ||
                updatedUser.getAddress() == null) {
            return "User not updated: first name, last name, email and address cannot be empty";
        }

        //Input integrity check
        String userValidateResult = Validation.validateUserForUpdate(updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getEmail(),
                updatedUser.getPhone(), updatedUser.getAddress(), updatedUser.getUsername());
        if(userValidateResult != null){
            return "User not updated: "+userValidateResult;
        }

        //Unique fields dose not exist yet in database
        CustomUser userFromDb = userRepository.findUserByUsername(updatedUser.getUsername());
        //Check if user want to change his email
        if (!userFromDb.getEmail().equals(updatedUser.getEmail())){
            //Check if this email already taken
            CustomUser userWithTheSameEmail = getUserByEmail(updatedUser.getEmail());
            if(userWithTheSameEmail != null){
                return "User not updated: This email already exists in the system.";
            }
        }

        //Default settings
        updatedUser.setEmail(updatedUser.getEmail().toLowerCase());

        return userRepository.updateUser(updatedUser);
    }

    public String deleteUser(String username) {
        CustomUser registeredUser = userRepository.findUserByUsername(username);
        if (registeredUser == null) {
            return "The user with this username does not exist, so it cannot be deleted";
        }
        return userRepository.deleteUser(registeredUser.getUsername());
    }

}