package com.example.security.model;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validation {
    //Checks whether the name contains between 2 and 30 characters, and contains only English letters
    public static boolean isValidName(String name){
        return Pattern.matches("^[a-zA-Z]{2,30}$", name);
    }

    //example@example.com
    public static boolean isValidEmail(String email){
        return Pattern.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", email.toLowerCase());
    }

    //050-1234567 / 0501234567
    public static boolean isValidPhoneNumber(String phone) {
        return Pattern.matches("^05[0-9]-?\\d{7}$", phone);
    }

    //Can include spaces, can include numbers, minimum 3 characters
    public static boolean isValidAddress(String address){
        return Pattern.matches("^[a-zA-Z0-9\\s,-]{3,}$", address);
    }

    public static String validateAddress(String address){
        if (!isValidAddress(address)){
            return "The address you entered is not valid. Please make sure it contains only valid characters(letters, numbers, spaces, commas, and hyphens) at least 3 characters";
        }
        return null;
    }

    //Can contain English letters (uppercase and lowercase),
    //Numbers (0-9), underscore, period and dash. Can contain 3 to 20 characters
    public static boolean isValidUsername(String username){
        return Pattern.matches("^[a-zA-Z0-9._-]{3,20}$", username);
    }

    //At least 8 characters, at least one capital letter (A-Z), at least one lowercase letter (a-z), at least one digit (0-9),
    // can include special characters (!@#$%^&*)
    public static boolean isValidPassword(String password) {
        return Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$", password);
    }

    public static String validateUser(String firstName, String lastName, String email, String phone, String address, String username, String password){
        if(!isValidName(firstName)){
            return "First name must contain only between 2 and 30 characters in English";
        }
        if(!isValidName(lastName)){
            return "Last name must contain only between 2 and 30 characters in English";
        }
        if(!isValidEmail(email)){
            return "Invalid email address. Please enter a valid email in the format: example@email.com.";
        }
        if (!isValidPhoneNumber(phone)){
            return "The phone number you entered is not valid. Please enter a valid Israeli phone number in the format: 05X-XXXXXXX / 05XXXXXXXX";
        }
        if (!isValidAddress(address)){
            return "The address you entered is not valid. Please make sure it contains only valid characters(letters, numbers, spaces, commas, and hyphens) at least 3 characters";
        }
        if (!isValidUsername(username)){
            return "Username can contain only English letters(uppercase and lowercase), Numbers(0-9), underscore, period and dash. Can contain 3 to 20 characters";
        }
        if (!isValidPassword(password)){
            return "Password must contain at least 8 characters, one capital letter(A-Z), one lowercase letter(a-z), one digit (0-9). Can include special characters(!@#$%^&*)";
        }
        return null;
    }
}