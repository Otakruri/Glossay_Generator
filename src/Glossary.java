import java.io.IOException;

import components.map.Map;
import components.map.Map1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * A program to create a glossary using a text file containing terms and
 * definitions as an input
 *
 * @author Omar Takruri
 */
public final class Glossary {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    public Glossary() {
        // no code needed here
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();
        SimpleReader in = new SimpleReader1L();

        out.print("Enter a file name to create Golsssary from: ");
        String fileIn = in.nextLine();

        out.print("Select an output destination (include .html extention): ");
        String fileOut = in.nextLine();

        SimpleWriter mainHTML = new SimpleWriter1L(fileOut);

        /*
         * First: Call a method, that will organize your given glossary file
         * into a list of string pairs, that will be used in the order (term,
         * definition, term, definition, term, definition). Using the map
         * components family...
         */

        Map<String, String> glossaryMap = generateMapKeysAndValue(fileIn);

        // Print the contents of the glossary map (for demonstration purposes)
        for (Map.Pair<String, String> p : glossaryMap) {
            generateExternalHtmlPage(p, fileOut, glossaryMap);
        }
        /*
         * Second: Call another method, that will take in that map, and create a
         * main html page, using the given .html extension file and list all the
         * terms by taking all the keys of that map.
         */

        generateMainHtmlPage(glossaryMap, mainHTML);

        in.close();
        out.close();
    }

    /*
     * -[1]: check that the method is properly outputting the correct format for
     * a "term", its "definition", and other generic attributes that you don't
     * need to call an external method to have added.
     */

    /**
     * Generates an external HTML page for a glossary term and its definition.
     *
     * @param glossaryMap
     *            A Map.Pair containing the term as the key and its definition
     *            as the value.
     * @param fileOut
     *            The name of the output file where the HTML content will be
     *            written.
     * @throws IOException
     *             if an error occurs while writing the HTML content to the
     *             output file.
     */
    public static void generateExternalHtmlPage(
            Map.Pair<String, String> currentKey_Value, String fileOut,
            Map<String, String> glossaryMap) {

        String url = currentKey_Value.key() + ".html";

        try (SimpleWriter out = new SimpleWriter1L(url)) {
            /*
             * Write the basic HTML structure. I chose this styling of printing
             * each line using a new "out.println();" for ease of readability on
             * both ends of the client and implementer side.
             */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + currentKey_Value.key() + "</title>");
            out.println("</head>");
            out.println("<body>");

            // Write the term and its definition
            out.println("<h2 style=\"color:red;\">");
            out.println("<i><b>" + currentKey_Value.key() + "</b></i></h2>");
            out.println("<blockquote>"
                    + generateDefinition(glossaryMap, currentKey_Value)
                    + "</blockquote>");

            /*
             * Write the horizontal rule and the link to return to the main
             * index
             */
            out.println("<hr>");
            out.println("<p> Return to <a href=\"" + fileOut + "\">index</a>"
                    + ".</p>");

            out.println("</body>");
            out.println("</html>");
        }
    }

    /*
     * Test cases to check for: - [1]: Make sure page displays all definition
     * with hyper links to found matches (key term inside of a definition) -
     * [2]: try having a definition with all terms to see if it handles that
     * properly. -[3: Try a definition with no term matches...
     */
    /**
     * Generates a definition with hyperlinks for terms in the provided glossary
     * map. Replaces occurrences of glossary terms found in the definition with
     * corresponding hyperlinks.
     *
     * @param glossaryMap
     *            The glossary map containing terms and their definitions. It
     *            should be a Map where the keys are glossary terms (String),
     *            and the values are their respective definitions (String).
     * @param currentKey_Value
     *            The current key-value pair representing the term and its
     *            definition for which the hyperlinks generation is being
     *            performed. It should be an object of type Map.Pair<String,
     *            String> from the Ohio State University components library.
     * @return The definition with hyperlink for glossary terms, or the original
     *         definition if no glossary terms were found in it, or if the
     *         definition is null.
     * @throws NullPointerException
     *             if the 'glossaryMap' or 'currentKey_Value' is null.
     */
    public static String generateDefinition(Map<String, String> glossaryMap,
            Map.Pair<String, String> currentKey_Value) {
        if (glossaryMap == null) {
            throw new NullPointerException("The 'glossaryMap' cannot be null.");
        }

        if (currentKey_Value == null) {
            throw new NullPointerException(
                    "The 'currentKey_Value' cannot be null.");
        }

        String definition = currentKey_Value.value();

        if (definition != null) {
            /*
             * Replace each occurrence of a glossary term in the definition with
             * a hyperlink
             */
            for (Map.Pair<String, String> entry : glossaryMap) {
                String term = entry.key();
                String termDefinition = entry.value();

                if (termDefinition != null) {
                    // Create a hyperlink for the term
                    String hyperlink = "<a href=\"" + term + ".html\">" + term
                            + "</a>";

                    /*
                     * Replace occurrences of the term in the definition with
                     * the hyperlink
                     */
                    definition = definition.replaceAll("\\b" + term + "\\b",
                            hyperlink);
                }
            }
        }

        return definition;
    }

    /*
     * Test for: - [1]: The method is creating a properly formatted "main" HTML
     * page. - [2]: The method is creating a "main" page with a duplicate index
     * term to check for behavior.
     */
    /**
     * Generates the main HTML page for the glossary.
     *
     * @param glossaryMap
     *            A Map containing terms as keys and their corresponding
     *            definitions as values.
     * @param out
     *            The SimpleWriter to which the HTML content will be written.
     * @throws RuntimeException
     *             if any error occurs while writing the HTML content to the
     *             SimpleWriter.
     */
    public static void generateMainHtmlPage(Map<String, String> glossaryMap,
            SimpleWriter out) {

        try {
            // Write the basic HTML structure
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Glossary</title>");
            out.println("</head>");
            out.println("<body>");

            // Write the title and horizontal rule
            out.println("<h2>Glossary</h2>");
            out.println("<hr>");

            // Write the section for the index of terms
            out.println(
                    "<h3>Index Of Terms - Click on each term to view definition:</h3>");
            out.println("<ul>");

            /*
             * Iterate through the glossaryMap to create the list of terms with
             * links to their individual pagesw
             */
            for (Map.Pair<String, String> entry : glossaryMap) {
                String term = entry.key();
                out.println("<li> <a href =\"" + term + ".html\">" + term
                        + "</a></li>");
            }

            out.println("</ul>");
            out.println("</body>");
            out.println("</html>");

            out.close();
        } catch (Exception e) {
            /*
             * If any exception occurs during writing the HTML content, print
             * the stack trace
             */
            e.printStackTrace();
            throw new RuntimeException(
                    "Error while generating main HTML page: " + e.getMessage());
        }
    }

    /*
     * Test case to check for: - [1]: Make sure it creates a proper glossary map
     * given a term and a definition. - [2]: Make sure it creates a proper
     * glossary map given a text file that contains multiple properly listed
     * terms and definitions. - [3]: Make sure it does the right thing when
     * given a wrongly formatted term-definition file. - [4]: Make sure it does
     * the right thing when given a wrongly formatted term-definition file, but
     * starting with one or two properly formatted.
     */
    /**
     * Generates a glossary map by reading terms and their definitions from a
     * file.
     *
     * @param fileIn
     *            The name of the input file containing the glossary
     *            information.
     * @return A Map containing terms as keys and their corresponding
     *         definitions as values.
     * @throws RuntimeException
     *             if any error occurs while reading the file or processing its
     *             content.
     */
    public static Map<String, String> generateMapKeysAndValue(String fileIn) {

        // Create a new Map to store the glossary terms and definitions
        Map<String, String> glossaryMap = new Map1L<>();

        try (SimpleReader reader = new SimpleReader1L(fileIn)) {
            String term = "";
            StringBuilder definitionBuilder = new StringBuilder();

            /*
             * Read each line from the input file until the end of the file is
             * reached
             */
            while (!reader.atEOS()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) {
                    // Blank line indicates the end of a definition block
                    if (!term.isEmpty() && definitionBuilder.length() > 0) {
                        // Add the term and its definition to the glossaryMap
                        glossaryMap.add(term, definitionBuilder.toString());
                        definitionBuilder = new StringBuilder();
                    }
                    term = ""; // Reset the term for the next definition block
                } else {
                    // Non-empty line indicates either a term or its definition
                    if (term.isEmpty()) {
                        // This line is a new term
                        term = line;
                    } else {
                        // This line is part of the definition
                        definitionBuilder.append(line).append("\n");
                    }
                }
            }

            /*
             * Check if there's a last term left without a blank line after its
             * definition
             */
            if (!term.isEmpty() && definitionBuilder.length() > 0) {
                glossaryMap.add(term, definitionBuilder.toString());
            }
        } catch (Exception e) {
            // If any exception occurs during the process, print the stack trace
            e.printStackTrace();
            throw new RuntimeException(
                    "Error while generating hypelinks map: " + e.getMessage());
        }

        // Return the generated glossaryMap
        return glossaryMap;
    }

}
