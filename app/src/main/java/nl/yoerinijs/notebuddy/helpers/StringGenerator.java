package nl.yoerinijs.notebuddy.helpers;

import android.support.annotation.NonNull;

import java.util.Random;

/**
 * A simple class that generates a string based on some dynamic input.
 */
public final class StringGenerator {

    public static String generateString(@NonNull Random rng, @NonNull String characters, @NonNull int length)
    {
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }
}
