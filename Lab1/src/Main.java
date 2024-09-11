import java.util.Scanner;

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

        for (; element >= Math.pow(10,-k);) {
            sum += element;
            element = element*x/divisor++;
        }
        System.out.println("\ne^x = " + sum);
        System.out.println("Standart 3e^x = " + Math.exp(x));
    }
}