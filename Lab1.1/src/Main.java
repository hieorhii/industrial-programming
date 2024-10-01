import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Formatter;

public class Main {

    // Функция для вычисления e^x через ряд Тейлора
    public static BigDecimal computeExp(int x, int k) {
        BigInteger divisor = new BigInteger("1");
        BigDecimal element = new BigDecimal("1");
        BigDecimal sum = new BigDecimal("0");
        BigDecimal xBD = BigDecimal.valueOf(x);
        BigDecimal precision = BigDecimal.valueOf(Math.pow(10, -k));

        while (element.abs().compareTo(precision) >= 0) {
            sum = sum.add(element);
            element = element.multiply(xBD).divide(new BigDecimal(divisor), k + 5, BigDecimal.ROUND_HALF_UP);
            divisor = divisor.add(BigInteger.ONE);
        }

        return sum;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter x: ");
        int x = Integer.parseInt(reader.readLine());
        System.out.print("Enter k: ");
        int k = Integer.parseInt(reader.readLine());

        Formatter fmt = new Formatter();

        BigDecimal expResult = computeExp(x, k);

        fmt.format("Octal (x): %#o\n", x);
        fmt.format("Hexadecimal (x): %#x\n", x);

        fmt.format("e^x (calculated): %f", expResult);

        fmt.format("\nStandard exp(x): %f", Math.exp(x));

        System.out.println(fmt);

        fmt.close();
    }
}
