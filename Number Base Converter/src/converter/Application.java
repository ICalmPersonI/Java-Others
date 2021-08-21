package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Application {

    private final Pattern dot = Pattern.compile("\\.");
    private final Scanner scanner = new Scanner(System.in);
    private Base base;

    public void run() {
        setBase();

        while (true) {
            System.out.printf("Enter number in base %d to convert to base %d (To go back type /back) ",
                    base.getSource(), base.getTarget());
            String choice = scanner.nextLine();
            System.out.println();
            if ("/back".equals(choice)) {
                setBase();
            } else if (dot.matcher(choice).find()) {
                String[] integer, fractional, numbers, hex;

                if (findLetters(choice)) {
                    StringBuilder chars = new StringBuilder();
                    char[] charsArray = choice.toCharArray();
                    for (char c : charsArray) {
                        if (c > 96) {
                            chars.append(c - 87).append(" ");
                        } else {
                            chars.append(c).append(" ");
                        }
                    }

                    numbers = chars.toString().replace(" . ", ".").split(dot.pattern());
                    integer = numbers[0].split("\\s");
                    fractional = numbers[1].split("\\s");

                } else {

                    numbers = choice.split("\\.");
                    integer = numbers[0].split("");
                    fractional = numbers[1].split("");

                }
                hex = convertToHex(integer, fractional, base.getSource()).split(dot.pattern());

                BigDecimal bigDecimalFractional = new BigDecimal("0." + hex[1]);

                System.out.print("Conversion result:");
                System.out.println(new BigInteger(hex[0], 10).toString(base.getTarget()) + "."
                        + convertFractionalToTargetBase(bigDecimalFractional, base.getTarget()));
            } else {
                System.out.print("Conversion result:");
                System.out.println(new BigInteger(choice, base.getSource()).toString(base.getTarget()));

            };
        }
    }

    private void setBase() {
        System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
        String[] line = scanner.nextLine().split("\\s");
        if ("/exit".equals(line[0])) {
            System.exit(0);
        }
        System.out.println();
        base = new Base(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
    }


    private String convertToHex(String[] integer, String[] fractional, int targetBase) {
        BigDecimal target = new BigDecimal(targetBase);
        BigDecimal sum = BigDecimal.ZERO;

        for (int i = 0, pow = integer.length - 1; i < integer.length; i++, pow--) {
            BigDecimal number = new BigDecimal(integer[i]);
            sum = sum.add(number.multiply(target.pow(pow)));
        }

        for (int i = 0, pow = -1; i < fractional.length; i++, pow--) {
            BigDecimal number = new BigDecimal(fractional[i]);
            sum = sum.add(number.multiply(target.pow(pow, MathContext.DECIMAL64)));
        }

        return String.valueOf(sum);
    }

    private String convertFractionalToTargetBase(BigDecimal fractional, int target) {
        StringBuilder result = new StringBuilder();

        if (fractional.compareTo(new BigDecimal("0.0")) == 0) {
            result.append("0");
        } else {
            for (int i = 0; i < 5; i++) {
                fractional = fractional.multiply(BigDecimal.valueOf(target));
                String[] split = fractional.toString().split(dot.pattern());
                try {
                    if (new BigInteger(split[0]).compareTo(BigInteger.ZERO) == 0) {
                        break;
                    }
                } catch (NumberFormatException e) {
                    break;
                }
                String number = split[0];
                if (split[0].length() > 1) {
                    char symbol = (char) (Long.parseLong(split[0]) + 87);
                    if (symbol > 96 && symbol < 123) {
                        number = String.valueOf(symbol);
                    }
                }
                result.append(number);
                fractional = new BigDecimal("0." + split[1]);
            }
        }

        if (result.length() < 5) {
            IntStream.range(result.length(), 5).forEach(i -> result.append("0"));
        } else if (result.length() > 5) {
            result.setLength(5);
        }

        return result.toString();
    }

    private boolean findLetters(String line) {
        return Pattern.compile("[A-Za-z]").matcher(line).find();
    }
}
