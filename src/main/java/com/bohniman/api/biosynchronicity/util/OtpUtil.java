package com.bohniman.api.biosynchronicity.util;

import java.util.Random;

public class OtpUtil {
    public static String getSixDigitOtp() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public static boolean fireOtpEmail(String email, String otp) {
        return true;
    }

    public static boolean fireOtpMobile(String mobileNo, String otp) {
        return true;
    }

    public static String getRandomToken(int length) {
        // create a string of uppercase and lowercase characters and numbers
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String characters = "_-*<>?$@#%&!";

        // combine all strings
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers + characters;

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        for (int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphaNumeric.length());

            // get character specified by index
            // from the string
            char randomChar = alphaNumeric.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
