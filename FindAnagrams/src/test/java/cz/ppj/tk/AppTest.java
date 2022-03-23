package cz.ppj.tk;

import junit.framework.Assert;
import org.junit.jupiter.api.*;
import java.util.ArrayList;

public class AppTest {

    @Test
    void FindAnagramsTest() {
        //test
        String testInput = "AB";
        ArrayList<String> testOutput = App.FindAnagrams(testInput);

        //expected
        ArrayList<String> testExpected = new ArrayList<>();
        testExpected.add("AB");
        testExpected.add("BA");

        //compare
        Assert.assertEquals(testExpected, testOutput);
    }
}
