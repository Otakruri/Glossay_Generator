import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import components.map.Map;
import components.map.Map1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

public class GlossaryTest {

    /*
     * I tried to test for a lot of things here to completely verify the
     * functionality of each method, but I am having a lot of trouble creating
     * the cases. In regards to understanding how certain key values are read by
     * the program while trying to avoid aliasing. Moreover, I managed to fix my
     * code in the main program to behave according to the test plan. Some cases
     * work regardless.
     */

    private Glossary glossary;

    @Before
    public void setUp() {
        this.glossary = new Glossary();
    }

    @After
    public void tearDown() {
        this.glossary = null;
    }

    @Test
    public void testGenerateMapKeysAndValue_singleTerm() {
        Map<String, String> glossaryMap = Glossary
                .generateMapKeysAndValue("single_term.txt");

        assertNotNull(glossaryMap);
        assertEquals(1, glossaryMap.size());
        assertTrue(glossaryMap.hasKey("Test Term"));
        assertEquals("This is a test definition for the term.",
                glossaryMap.value("Test Term"));
    }

    @Test
    public void testGenerateMapKeysAndValue_multipleTerms() {
        Map<String, String> glossaryMap = Glossary
                .generateMapKeysAndValue("multiple_terms.txt");

        assertNotNull(glossaryMap);
        assertEquals(4, glossaryMap.size());
        assertTrue(glossaryMap.hasKey("Term 1"));
        assertEquals("Definition 1.", glossaryMap.value("Term 1"));
        assertTrue(glossaryMap.hasKey("Term 2"));
        assertEquals("Definition 2.", glossaryMap.value("Term 2"));
        assertTrue(glossaryMap.hasKey("Term 3"));
        assertEquals("Definition 3.", glossaryMap.value("Term 3"));
        assertTrue(glossaryMap.hasKey("Term 4"));
        assertEquals("Definition 4.", glossaryMap.value("Term 4"));
    }

    @Test
    public void testGenerateMapKeysAndValue_emptyFile() {
        Map<String, String> glossaryMap = Glossary
                .generateMapKeysAndValue("empty_file.txt");

        assertNotNull(glossaryMap);
        assertEquals(0, glossaryMap.size());
    }

    @Test
    public void testGenerateMapKeysAndValue_blankLinesBetweenTerms() {
        Map<String, String> glossaryMap = Glossary
                .generateMapKeysAndValue("blank_lines.txt");

        assertNotNull(glossaryMap);
        assertEquals(2, glossaryMap.size());
        assertTrue(glossaryMap.hasKey("Term 1"));
        assertEquals("Definition 1.", glossaryMap.value("Term 1"));
        assertTrue(glossaryMap.hasKey("Term 2"));
        assertEquals("Definition 2.", glossaryMap.value("Term 2"));
    }

    @Test
    public void testGenerateDefinition_withHyperlinks() {
        Map<String, String> glossaryMap = new Map1L<>();
        glossaryMap.add("Term 1", "This is the definition of Term 1.");
        glossaryMap.add("Term 2", "Term 1 is related to Term 2.");

        Map.Pair<String, String> pair = glossaryMap.remove("Term 2");

        String definition = Glossary.generateDefinition(glossaryMap, pair);

        assertEquals("Term 1 is related to <a href=\"Term 1.html\">Term 1</a>.",
                definition);
    }

    @Test
    public void testGenerateDefinition_noHyperlinks() {
        Map<String, String> glossaryMap = new Map1L<>();
        glossaryMap.add("Term 1", "This is the definition of Term 1.");
        glossaryMap.add("Term 2", "This is the definition of Term 2.");

        Map.Pair<String, String> pair = glossaryMap.remove("Term 1");

        String definition = Glossary.generateDefinition(glossaryMap, pair);

        assertEquals("This is the definition of Term 1.", definition);
    }

    @Test
    public void testGenerateDefinition_nullDefinition() {
        Map<String, String> glossaryMap = new Map1L<>();
        glossaryMap.add("Term 1", null);

        Map.Pair<String, String> pair = glossaryMap.remove("Term 1");

        String definition = Glossary.generateDefinition(glossaryMap, pair);

        assertEquals("", definition);
    }

    @Test(expected = NullPointerException.class)
    public void testGenerateDefinition_nullPair() {
        Map<String, String> glossaryMap = new Map1L<>();

        Glossary.generateDefinition(glossaryMap, null);
    }

    @Test
    public void testGenerateExternalHtmlPage() throws IOException {
        Map<String, String> glossaryMap = new Map1L<>();
        glossaryMap.add("Term 1", "This is the definition of Term 1.");

        File file = new File("test_page.html");
        String fileOut = file.getPath();

        Glossary.generateExternalHtmlPage(glossaryMap.remove("Term 1"), fileOut,
                glossaryMap);

        // Assert that the file is created and not empty
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testGenerateExternalHtmlPage_nullCurrentKey_Value() {
        Map<String, String> glossaryMap = new Map1L<>();
        glossaryMap.add("Term", "Definition");

        Glossary.generateExternalHtmlPage(null, "file.html", glossaryMap);
    }

    @Test
    public void testGenerateMainHtmlPage() throws IOException {
        Map<String, String> glossaryMap = new Map1L<>();
        glossaryMap.add("Term 1", "This is the definition of Term 1.");
        glossaryMap.add("Term 2", "This is the definition of Term 2.");

        File file = new File("main_page.html");
        SimpleWriter out = new SimpleWriter1L(file.getPath());

        Glossary.generateMainHtmlPage(glossaryMap, out);

        // Assert that the file is created and not empty
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        out.close();
    }

}
