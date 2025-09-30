import java.util.Arrays;
import java.util.Scanner;

public class Tabular {

    public static String encrypt(String plaintext, int[] keyOrder) {
        plaintext = plaintext.toUpperCase();
        int columns = keyOrder.length;
        int rows = (int) Math.ceil((double) plaintext.length() / columns);

        char[][] table = new char[rows][columns];

        int idx = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (idx < plaintext.length()) {
                    table[r][c] = plaintext.charAt(idx++);
                } else {
                    table[r][c] = '-';
                }
            }
        }

        System.out.println("\nEncryption Table:");
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (table[r][c] == '-') {
                    char randomChar = (char) ('A' + (int)(Math.random() * 26));
                    System.out.print(randomChar + " ");
                } else {
                    System.out.print(table[r][c] + " ");
                }
            }
            System.out.println();
        }

        StringBuilder ciphertext = new StringBuilder();
        for (int k = 0; k < columns; k++) {
            int col = keyOrder[k] - 1; 
            for (int r = 0; r < rows; r++) {
                if (table[r][col] != '-') {
                    ciphertext.append(table[r][col]);
                }
            }
        }

        return ciphertext.toString();
    }

    public static String decrypt(String ciphertext, int[] keyOrder) {
        int columns = keyOrder.length;
        int rows = (int) Math.ceil((double) ciphertext.length() / columns);
        int totalCells = rows * columns;
        int emptyCells = totalCells - ciphertext.length();

        char[][] table = new char[rows][columns];

        // Determine how many characters each column gets
        int[] colLengths = new int[columns];
        for (int i = 0; i < columns; i++) {
            colLengths[i] = rows;
        }
        for (int i = columns - emptyCells; i < columns; i++) {
            colLengths[keyOrder[i] - 1] -= 1;
        }

        // Fill the columns based on key order
        int idx = 0;
        for (int k = 0; k < columns; k++) {
            int col = keyOrder[k] - 1;
            for (int r = 0; r < colLengths[col]; r++) {
                table[r][col] = ciphertext.charAt(idx++);
            }
        }

        System.out.println("\nDecryption Table:");
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (r < colLengths[c]) {
                    System.out.print(table[r][c] + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }

        StringBuilder plaintext = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (r < colLengths[c]) {
                    plaintext.append(table[r][c]);
                }
            }
        }

        return plaintext.toString();
    }

    public static boolean isAlphabetic(String text) {
        return text.matches("[a-zA-Z]+");
    }

    public static boolean isNumeric(String text) {
        return text.matches("\\d+");
    }

    public static int[] parseKey(String keyStr) {
        int[] key = new int[keyStr.length()];
        for (int i = 0; i < keyStr.length(); i++) {
            key[i] = Character.getNumericValue(keyStr.charAt(i));
        }
        return key;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        boolean running = true;
        while (running) {
            System.out.print("\nEnter plaintext: ");
            String input = sc.nextLine();
            String plaintext = input.replaceAll("\\s+", "");

            if (!isAlphabetic(plaintext)) {
                System.out.println(" Invalid input, alphabets only.");
                continue;
            }

            System.out.print("Enter key: ");
            String keyStr = sc.nextLine().trim();
            if (!isNumeric(keyStr)) {
                System.out.println(" Invalid key, numbers only.");
                continue;
            }

            int[] keyOrder = parseKey(keyStr);
            boolean validKey = Arrays.stream(keyOrder).distinct().count() == keyOrder.length &&
                               Arrays.stream(keyOrder).allMatch(i -> i > 0 && i <= keyOrder.length);

            if (!validKey) {
                System.out.println(" Invalid key.  " + keyOrder.length + ".");
                continue;
            }

            String encrypted = encrypt(plaintext, keyOrder);
            System.out.println("\n Encrypted text: " + encrypted);

            System.out.print("\nDo you want to decrypt this message? (Y/N): ");
            String choice = sc.nextLine().trim().toUpperCase();

            if (choice.equals("Y")) {
                String decrypted = decrypt(encrypted, keyOrder);
                System.out.println("Decrypted text: " + decrypted);
            }

            System.out.print("\nDo you want to encrypt another message? (Y/N): ");
            String again = sc.nextLine().trim().toUpperCase();

            if (!again.equals("Y")) {
                running = false;
                System.out.println("\n  Goodbye!");
            }
        }
        sc.close();
    }
}
