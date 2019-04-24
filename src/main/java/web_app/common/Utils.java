package web_app.common;

import java.util.List;

public class Utils {

    private Utils() {}

    public static boolean isInteger(String string) {
        return string.matches("^\\d+$");
    }

    public static String numbersListToString(List<Integer> numbers, String separator) {
        return numbers.stream()
                .map(String::valueOf)
                .reduce("", (str1, str2) -> str1.isEmpty() ? str2 : str1 + separator + str2);
    }
}
