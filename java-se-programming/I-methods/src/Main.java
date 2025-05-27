import java.math.BigDecimal;
import java.math.RoundingMode;

public class Main {
    public static void main(String[] args) {
        BigDecimal number1 = new BigDecimal("-123.456789123456789");

        // Round to 13 digits after the decimal point and take absolute value
        BigDecimal result = number1.setScale(13, RoundingMode.HALF_UP);

        System.out.println(result);


        BigDecimal number = new BigDecimal("-123.456789123456789");

        // Round to whole number (0 digits after decimal) and get absolute value
        BigDecimal result1 = number.setScale(0, RoundingMode.HALF_UP).abs();

        System.out.println(result1);
    }
}
