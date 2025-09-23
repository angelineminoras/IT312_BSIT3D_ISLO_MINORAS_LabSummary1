import java.util.Scanner;

public class Tabular {

    // Encryption 
    public static String encrypt(String plaintext, int key) {
        plaintext = plaintext.toUpperCase();

        int rows = (int) Math.ceil((double) plaintext.length() / key);
        char[][] table = new char[rows][key];

        int idx = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < key; c++) {
                if (idx < plaintext.length()) {
                    table[r][c] = plaintext.charAt(idx++);
                } else {
                    table[r][c] = '-'; 
                }
            }
        }

        // Print encryption table
        System.out.println("\nEncryption Table:");
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < key; c++) {
                System.out.print(table[r][c] + " ");
            }
            System.out.println();
        }

        // Build ciphertext
        StringBuilder ciphertext = new StringBuilder();
        for (int c = 0; c < key; c++) {
            for (int r = 0; r < rows; r++) {
                if (table[r][c] != '-') {
                    ciphertext.append(table[r][c]);
                }
            }
        }

        return ciphertext.toString();
    }

    // Decryption
    public static String decrypt(String ciphertext, int key) {
        int numRows = ciphertext.length() / key;
        int extra = ciphertext.length() % key;

        int[] colLengths = new int[key];
        for (int i = 0; i < key; i++) {
            if (i < extra) {
                colLengths[i] = numRows + 1;
            } else {
                colLengths[i] = numRows;
            }
        }

        // Split ciphertext into columns
        String[] cols = new String[key];
        int start = 0;
        for (int i = 0; i < key; i++) {
            cols[i] = ciphertext.substring(start, start + colLengths[i]);
            start += colLengths[i];
        }

        // Print decryption table
        System.out.println("\nDecryption Table:");
        int rows = numRows + (extra > 0 ? 1 : 0);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < key; c++) {
                if (r < cols[c].length()) {
                    System.out.print(cols[c].charAt(r) + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }

        // Reconstruct plaintext
        StringBuilder plaintext = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < key; c++) {
                if (r < cols[c].length()) {
                    plaintext.append(cols[c].charAt(r));
                }
            }
        }
        return plaintext.toString();
    }

   
    public static boolean isAlphabetic(String text) {
        return text.matches("[a-zA-Z]+");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        boolean running = true;
        while (running) {
            System.out.print("\nEnter plaintext: ");
            String input = sc.nextLine();
            String plaintext = input.replaceAll("\\s+", "");

            // validate plaintext
            if (!isAlphabetic(plaintext)) {
                System.out.println(" Invalid input, alphabets only.");
                continue;
            }

            System.out.print("Enter key: ");
            int key;
            try {
                key = Integer.parseInt(sc.nextLine());
                if (key <= 0) {
                    System.out.println(" Invalid key. It must be greater than 0.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println(" Invalid key, it must be a number.");
                continue;
            }

            // Encrypt
            String encrypted = encrypt(plaintext, key);
            System.out.println("\n Encrypted text: " + encrypted);

        
            System.out.print("\nDo you want to decrypt this message? (Y/N): ");
            String choice = sc.nextLine().trim().toUpperCase();

            if (choice.equals("Y")) {
                String decrypted = decrypt(encrypted, key);
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
