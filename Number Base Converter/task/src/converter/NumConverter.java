package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Scanner;

public class NumConverter {
    char[] abc = new char[26];

    public NumConverter() {
        for (int i = 0; i < 26; i++) {
            abc[i] = (char) (65 + i);
        }
    }

    public String toStr(int number, int sys) {
        String out;
        if (sys <= 10 || number < 10) out = String.valueOf(number);
        else out = String.valueOf(abc[number - 10]);
        return out;
    }

    public int toNumb(String str, int sys) {
        int out;
        if (sys <= 10 || !String.valueOf(abc).contains(str)) out = Integer.parseInt(str);
        else out = 10 + String.valueOf(abc).indexOf(str);
        return out;
    }

    public BigDecimal convertDect(String strFractal, int fromBase) {
        BigDecimal out = new BigDecimal("0.0").setScale(6, RoundingMode.HALF_DOWN);
        char[] chars = strFractal.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            double num = toNumb(String.valueOf(chars[i]), fromBase);
            double del = Math.pow(fromBase, i + 1);
            out = out.add(new BigDecimal(num / del).setScale(6, RoundingMode.HALF_DOWN));
        }
        return out.setScale(5, RoundingMode.HALF_DOWN);
    }

    public String convertInt(String strBi, int fromBase, int toBase) {
        BigInteger bi = new BigInteger(strBi, fromBase);
        return bi.toString(toBase).toUpperCase();
    }

    private String convertFract(String strFractal, int fromBase, int toBase) {
        String ret = "";
        BigDecimal bidDec;
        if (fromBase != 10) bidDec = convertDect(strFractal, fromBase);
        else bidDec = new BigDecimal("0." + strFractal);

        boolean proc = true;
        while (proc) {
            bidDec = bidDec.multiply(new BigDecimal(toBase));
            String oper = bidDec.toString();
            String[] parts = oper.split("\\.");
            String intPart = parts[0];
            String fractPart = parts[1];
            ret += toStr(Integer.parseInt(intPart), toBase);
            proc = !fractPart.equals("0") && ret.length() < 5;
            bidDec = new BigDecimal(("0." + fractPart));
        }
        return "." + ret;
    }

    public static void println(String string) {
        System.out.println(string);
    }
    public static void print(String string) {
        System.out.print(string);
    }

    public static String getString(String input) {
        Scanner scanner = new Scanner(System.in);
        print(input);
        return scanner.nextLine();
    }

    public static int getInt(String input) {
        Scanner scanner = new Scanner(System.in);
        print(input);
        return scanner.nextInt();
    }

    public void getMenu() {
        while (true) {
            String inputStr = getString("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            if (inputStr.equals("/exit")) return;
            getMenuSecond(inputStr);
            println("");
        }
    }

    private void getMenuSecond(String numbers) {
        while(true) {
            String[] bases = numbers.split(" ");
            int fromBase = Integer.parseInt(bases[0]);
            int toBase = Integer.parseInt(bases[1]);
            String inputStr = getString("Enter number in base " + fromBase + " to convert to base " + toBase + " (To go back type /back) ");
            String intPart = "";
            String fractalPart = "";
            if (!inputStr.equals("/back")) {
                if (inputStr.lastIndexOf('.') > 0) {
                    String[] parts = inputStr.split("\\.");
                    intPart = parts[0];
                    fractalPart = parts[1];
                    fractalPart = convertFract(fractalPart.toUpperCase(), fromBase, toBase);
                } else {
                    intPart = inputStr;
                }
                println("Conversion result: " + ((convertInt(intPart, fromBase, toBase) + fractalPart)));
            } else return;
        }
    }
}
