package org.example.proiect_ds.Utils;
public class Encoder {

    private Encoder() {
    }
    private static final Encoder INSTANCE = new Encoder();

    public static Encoder getInstance() {
        return INSTANCE;
    }
    //    @Singleton
    public static String encodingPassword(String password) {
        StringBuilder encodedPassword = new StringBuilder();
        for(char c : password.toCharArray()) {
            encodedPassword.append((char) (c + 1));
        }
        return encodedPassword.toString();
    }
    public static String decodingPassword(String password) {
        StringBuilder decodedPassword = new StringBuilder();
        for(char c : password.toCharArray()) {
            decodedPassword.append((char) (c - 1));
        }
        return decodedPassword.toString();
    }
}