import java.util.Scanner;
import java.util.Formatter;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int divisor = 1;
        double element = 1;
        double sum = 0;

        System.out.print("Enter x: ");
        int x = in.nextInt();
        System.out.print("Enter k: ");
        int k = in.nextInt();

        Formatter fmt = new Formatter();

        for (; Math.abs(element) >= Math.pow(10, -k);) {
            sum += element;
            element = element * x / divisor++;
        }

        fmt.format("Octal (x): %#o\n", x);
        fmt.format("Hexadecimal (x): %#x\n", x);

        fmt.format("e^x : %f", sum);

        fmt.format("\nStandard exp(x): %f", Math.exp(x));

        System.out.println(fmt);

        fmt.close();
    }
}
