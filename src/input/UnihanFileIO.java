import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Parses Unihan_DictionaryLikeData.txt to get character stroke counts and
 * Unihan_Variants.txt to get character simplified and/or traditional variants.
 * Then outputs this info into a JSON file.
 *
 * @author Jasmine Lang
 */
public class UnihanFileIO {

  /**
   * Turns a line into a ChineseChar object if the line is usable.
   * Only lines containing "kTotalStrokes", "kSimplifiedVariant", and
   * "kTraditionalVariant" should be used.
   *
   * Note: If there are two kTotalStrokes values, grab only first one for now.
   * First is preferred for CH, and second is preferred for TW.
   * TODO change this in later iteration?
   *
   * @param line line to possibly turn into new char
   * @param scan Scanner for variant file
   * @return ChineseChar object to add to ArrayList
   */
  private static ChineseChar readChar(String line, Scanner scan) {
    Scanner lineScan = new Scanner(line);

    try {
      // Get Unicode string and field from line
      // If field == "kTotalStrokes", save stroke value, and use it with Unicode
      // string to create new ChineseChar
      String uni = lineScan.next();
      String field = lineScan.next();
      if (field.equals("kTotalStrokes")) {
        int strNum = lineScan.nextInt();
        ChineseChar chinChar = new ChineseChar(uni, strNum);

        // TODO
        // Check if the ChineseChar has any simplified or traditional variants,
        // and add them

        // TODO how to stop scanner in time for next char? it will go one line too far
        // TODO Check if a char can have both a simp and trad variation?
        // Scan file while it hasNextLine() && both variants !found
        // Get next line (or current line?) if not documentation or empty line
        // Check codepoint of 1st token in line
        // If codepoint < target, keep going
        // If codepoint = target and field is valid, save info (all tokens)
        // If codepoint > target, stop (and make simp/trad fields empty?)
        /*
        boolean varFound = false;
        while (scan.hasNextLine() && !varFound) {
          String line2 = scan.nextLine();
          if (line2.length() > 0 && line2.charAt(0) != '#') {
            Scanner s = new Scanner(line2);
            String u = s.next();
            String f = s.next();
            int uniCode = Integer.parseInt(uni.substring(2), 16);
            int uCode = Integer.parseInt(u.substring(2), 16);
            if (uCode == uniCode && (f.equals("kTraditionalVariant") || f.equals("kSimplifiedVariant"))) {
              System.out.println(line2);
              varFound = true;
            } else if (uCode > uniCode) {
              varFound = true;
            }
          }
        }
        */

        // Return new ChineseChar to add to ArrayList
        lineScan.close();
        return chinChar;
      }

      lineScan.close();
      return null;
    } catch (Exception e) {
      // Return null if line is invalid
      lineScan.close();
      return null;
    }
  }

  /**
   * Converts the ArrayList of ChineseChars into a JSON file.
   *
   * @param list list of ChineseChars to add to JSON file
   * @throws IOException if file can't be found or read
   */
  private static void writeJSONFile(ArrayList<ChineseChar> list) throws IOException {
    PrintStream writer = new PrintStream(new File("charData.json"));

    writer.println("{");
    for (int i = 0; i < list.size(); i++) {
      String separator = ",";
      if (i == list.size() - 1) {
        // If last item in list, don't add comma
        separator = "";
      }
      writer.println(list.get(i).toString() + separator);
    }
    writer.println("}");

    writer.close();
  }

  /**
   * TODO
   *
   * @param args command line arguments
   */
  public static void main(String[] args) throws FileNotFoundException {
    // Create two scanners - one for stroke counts, and the other for variants
    Scanner strReader = new Scanner(new File("Unihan_DictionaryLikeData.txt"));
    Scanner varReader = new Scanner(new File("Unihan_Variants.txt"));

    // Construct a new ArrayList for holding all of the ChineseChars
    ArrayList<ChineseChar> charList = new ArrayList<ChineseChar>();

    // Process each line in strReader file
    while (strReader.hasNextLine()) {
      String nextLine = strReader.nextLine();

      // Ignore lines that are empty or start with '#' (documentation)
      if (nextLine.length() > 0 && nextLine.charAt(0) != '#') {
        ChineseChar cc = readChar(nextLine, varReader);
        if (cc != null) {
          charList.add(cc);
        }
      }
    }

    // Write lines to new file in JSON format
    try {
      writeJSONFile(charList);
    } catch (Exception e) {
      System.out.println("Sorry, file could not be generated because of exception: " + e);
    }

    // Close the scanners
    strReader.close();
    varReader.close();
  }

  /***************************************************************************/
  /* ChineseChar inner class below */
  /***************************************************************************/

  /**
   * Defines one character object within the JSON file of all characters.
   */
  private static class ChineseChar {
    /** Chinese character */
    private String ch;
    /** Number of strokes in character */
    private int strokes;
    /** Any simplified variants of character */
    private String[] simp;
    /** Any traditional variants of character */
    private String[] trad;

    /**
     * Constructs a Chinese char object.
     */
    public ChineseChar(String unicode, int strNum) {
      String ch = unicodeToChar(unicode);
      setCh(ch);
      setStrokes(strNum);
    }

    /**
     * Converts Unicode string to a Chinese character.
     *
     * @param unicode Unicode string
     * @return char in String form converted from Unicode string
     */
    public static String unicodeToChar(String unicode) {
      int codept = Integer.parseInt(unicode.substring(2), 16);
      char[] ch = Character.toChars(codept);
      return new String(Character.toChars(codept));
    }

    private void setCh(String ch) {
      this.ch = ch;
    }

    private void setStrokes(int strokes) {
      this.strokes = strokes;
    }

    // TODO dynamically increase size of simp and trad arrays if needed
    // Is using ArrayList for this also overkill?
    public void setSimp(String simp) {

    }

    public void setTrad(String trad) {

    }

    public String getCh() {
      return this.ch;
    }

    public int getStrokes() {
      return this.strokes;
    }

    public String[] getSimp() {
      return this.simp;
    }

    public String[] getTrad() {
      return this.trad;
    }

    /**
     * Formats the ChineseChar into a JavaScript object.
     *
     * @return character as JS object
     */
    public String toString() {
      String chStr = "\"character\": \"" + getCh() + "\"";
      String strokesStr = "\"strokes\": " + getStrokes();
      return "  {\n    " + chStr + ",\n    " + strokesStr + "\n  }";
    }
  }
}
