package sample;

public abstract class FormValidator {
    // Validates a string of lowercase or uppercase letters
    public static boolean isValidName(String s) {
        String pattern = "[A-Za-z\\s]+";
        return s.matches(pattern);
    }

    // Validate email format
    // Reference for this regex pattern: https://howtodoinjava.com/java/regex/java-regex-validate-email-address/
    public static boolean isValidEmail(String s) {
        String pattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+" +
                "[a-zA-Z]{2,6}$";
        return s.matches(pattern);
    }

    // Validate password
    public static boolean isValidPassword(String s) {
        String pattern = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$";
        return s.matches(pattern);
    }

    public static boolean isValidNumbers(String s) {
        String pattern = "[+-]?([0-9]*[.])?[0-9]+";
        return s.matches(pattern);
    }

    public static boolean isValidInt(String s) {
        String pattern = "[0-9]+";
        return s.matches(pattern);
    }
}
