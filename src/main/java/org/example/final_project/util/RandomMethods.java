package org.example.final_project.util;

public class RandomMethods {
    /*public static String generateUsername(String email) {
        String usernamePart = email.split("@")[0];
        int minLength = 5;
        int maxLength = usernamePart.length();
        Random random = new Random();
        int charsToRemove = random.nextInt(maxLength - minLength + 1);
        StringBuilder username = new StringBuilder(usernamePart);
        for (int i = 0; i < charsToRemove; i++) {
            int removeIndex = random.nextInt(username.length());
            username.deleteCharAt(removeIndex);
        }
        return username.toString();
    }*/

    public static String generateUsername(String email) {
        return email.split("@")[0];
    }
}
