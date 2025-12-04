import java.util.regex.*;

public class Test {
    public static void main(String[] args) {
        String password = "Hello123#"; // You can change this to test
        if (isValidPassword(password)) {
            System.out.println("Password is valid!");
        } else {
            System.out.println("Password is invalid!");
        }
    }

    public static boolean isValidPassword(String password) {
        // Regex explanation:
        // (?=.*[A-Z])   -> at least one uppercase
        // (?=.*\\d)     -> at least one digit
        // (?=.*[@#$%^&+=!]) -> at least one special character
        // .{8,}         -> minimum 8 characters
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }
}

