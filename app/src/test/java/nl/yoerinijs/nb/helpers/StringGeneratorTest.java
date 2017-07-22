package nl.yoerinijs.nb.helpers;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

/**
 * Unit tests for the {@link StringGenerator} class.
 */
public class StringGeneratorTest {

    private static final String EXPECTED_GENERATED_STRING = "11010100010000111010000100010011011101000011101010";
    private static String m_charsString;
    private static int m_length;

    @BeforeClass
    public static void setup() {
        m_charsString = "01";
        m_length = 50;
    }

    @Test
    public void generateString_run_expectValidRandomString() {
        String generatedString = StringGenerator.generateString(new Random(229348756533112L), m_charsString, m_length);
        Assert.assertTrue(generatedString.equals(EXPECTED_GENERATED_STRING));
    }

    @Test
    public void generateString_runWithRandom_expectInvalidRandomString() {
        String generatedString = StringGenerator.generateString(new Random(), m_charsString, m_length);
        Assert.assertFalse(generatedString.equals(EXPECTED_GENERATED_STRING));
    }
}
