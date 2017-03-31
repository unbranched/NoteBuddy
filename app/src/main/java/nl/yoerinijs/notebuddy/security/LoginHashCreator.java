package nl.yoerinijs.notebuddy.security;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import nl.yoerinijs.notebuddy.helpers.StringGenerator;
import nl.yoerinijs.notebuddy.storage.KeyValueDB;

/**
 * This class creates a hash based on parts of the password.
 * This is not completely safe and may not securing the app perfectly from others getting in.
 * Thus, improvement is definitely needed. However, it should still be impossible for someone to read the encrypted notes.
 */
public class LoginHashCreator {

    /**
     * Returns a hash based on parts of the password and a random string
     * @param context
     * @param password
     * @return
     * @throws Exception
     */
    public String getLoginHash(@NonNull Context context, @NonNull String password) throws Exception {
        String randomPasswordString = createRandomPasswordString(context);
        String subPassword = createSubPassword(password);
        EncryptionHandler eh = new EncryptionHandler();
        return eh.hashString(sortStringAlphabetically(randomPasswordString + subPassword)).toString();
    }

    /**
     * This method returns parts of the password, but not all of it.
     * Password length is added to ensure that the hash will be somewhat unique, but this is not ideal.
     * Password is multiplied by a number, so that the correct password length will not be stored.
     * Again, this is not ideal, because one can read the source, and can determine what the actual length is.
     * @param password
     * @return
     */
    private String createSubPassword(@NonNull String password) {
        return password.substring(0, 1).toLowerCase() + password.substring(password.length()-1, password.length()).toLowerCase() + password.length();
    }

    /**
     * Method that returns a random string
     * @param context
     * @return
     * @throws Exception
     */
    private String createRandomPasswordString(@NonNull Context context) throws Exception {
        KeyValueDB keyValueDB = new KeyValueDB();
        String randomPasswordString = keyValueDB.getRandomPasswordString(context);
        if (randomPasswordString == null) {
            final String randomChars = "abcdefghijklmnopqrstuvwxyz01234567890";
            StringGenerator rsg = new StringGenerator();
            Random random = new Random();
            randomPasswordString = rsg.generateString(random, randomChars, 50);
            keyValueDB.setRandomPasswordString(context, randomPasswordString);
            return randomPasswordString;
        }
        return randomPasswordString;
    }

    /**
     * Method that sorts a string alphabetically and returns it
     * @param str
     * @return
     */
    private String sortStringAlphabetically(@NonNull String str) {
        Character[] chars = new Character[str.length()];
        for (int i = 0; i < chars.length; i++)
            chars[i] = str.charAt(i);

        Arrays.sort(chars, new Comparator<Character>() {
            public int compare(Character c1, Character c2) {
                int cmp = Character.compare(
                        Character.toLowerCase(c1.charValue()),
                        Character.toLowerCase(c2.charValue())
                );
                if (cmp != 0) return cmp;
                return Character.compare(c1.charValue(), c2.charValue());
            }
        });

        StringBuilder sb = new StringBuilder(chars.length);
        for (char c : chars) sb.append(c);
        return sb.toString();
    }

}
