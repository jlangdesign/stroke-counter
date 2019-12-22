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

  // /**
  //  * Skips all of the lines of documentation at the top of the files (each line
  //  * starts with a '#' char).
  //  *
  //  * @param in Scanner for file
  //  * @return line first line of non-documentation content in file
  //  */
  // private static String skipDoc(Scanner in) {
  //   boolean isDoc = true;
  //   String line = "";
  //   while (in.hasNextLine() && isDoc) {
  //     line = in.nextLine();
  //     if (line.charAt(0) != '#') {
  //       isDoc = false;
  //     }
  //   }
  //
  //   // Return first line of the content
  //   return line;
  // }

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
   * @return ChineseChar object to add to ArrayList
   */
  private static ChineseChar readChar(String line) {
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

    // First, skip all of the documentation in both files to get to the first
    // line of the content (strLine and varLine store first lines of content)
    // String strLine = skipDoc(strReader);
    // String varLine = skipDoc(varReader);

    // Construct a new ArrayList for holding all of the ChineseChars
    ArrayList<ChineseChar> charList = new ArrayList<ChineseChar>();

    // TODO delete after done debugging
    // System.out.println("test: " + strLine);
    // System.out.println("test: " + varLine);

    // Process first line of each file (saved in strLine and varLine) if usable
    while (strReader.hasNextLine()) {
      String nextLine = strReader.nextLine();
      System.out.println(nextLine);
      if (nextLine.length() > 0 && nextLine.charAt(0) != '#') {
        ChineseChar cc = readChar(strReader.nextLine());
        if (cc != null) {
          charList.add(cc);
        }
      }
    }

    // Then process rest of lines
    // do {
    //
    // } while (strReader.hasNextLine());

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
    private char ch;
    /** Number of strokes in character */
    private int strokes;
    /** Any simplified variants of character */
    private char[] simp;
    /** Any traditional variants of character */
    private char[] trad;

    /**
     * Constructs a Chinese char object.
     */
    public ChineseChar(String unicode, int strNum) {
      char ch = unicodeToChar(unicode);
      setCh(ch);
      setStrokes(strNum);
    }

    /**
     * Converts Unicode string to a Chinese character.
     *
     * @param unicode Unicode string
     * @return char converted from Unicode string
     */
    private static char unicodeToChar(String unicode) {
      // TODO fix this - some are too big and are rendering as "?"
      int codept = Integer.parseInt(unicode.substring(2), 16);
      return Character.toChars(codept)[0];
    }

    private void setCh(char ch) {
      this.ch = ch;
    }

    private void setStrokes(int strokes) {
      this.strokes = strokes;
    }

    // TODO dynamically increase size of simp and trad arrays if needed
    public void setSimp(char ch) {

    }

    public void setTrad(int strokes) {

    }

    // TODO Define getter methods and toString()
    public char getCh() {
      return this.ch;
    }

    public int getStrokes() {
      return this.strokes;
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
