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
   * Only lines containing "kTotalStrokes" should be used.
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
        lineScan.close();
        return chinChar; // Return new ChineseChar to add to ArrayList
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
    // writer.println("[");
    for (int i = 0; i < list.size(); i++) {
      String separator = ",";
      if (i == list.size() - 1) {
        // If last item in list, don't add comma
        separator = "";
      }
      writer.println(list.get(i).toString() + separator);
    }
    writer.println("}");
    // writer.println("]");

    writer.close();
  }

  /**
   * Grabs character and stroke count data from Unihan_DictionaryLikeData.txt.
   * Then grabs simplified and/or traditional variants for each character from
   * Unihan_Variants.txt. Finally outputs character data into JSON file, where
   * each object consists of a character, its stroke counts, and any simplified
   * or traditional variants.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) throws FileNotFoundException {
    // Create two scanners - one for stroke counts, and the other for variants
    Scanner strReader = new Scanner(new File("Unihan_IRGSources.txt"));
    Scanner varReader = new Scanner(new File("Unihan_Variants.txt"));

    // Construct a new ArrayList for holding all of the ChineseChars
    ArrayList<ChineseChar> charList = new ArrayList<ChineseChar>();

    // Create arrays for the variant unicode strings and process each line
    // in varReader file
    ArrayList<String> uniList =  new ArrayList<String>();
    ArrayList<String> varList =  new ArrayList<String>();
    while (varReader.hasNextLine()) {
      String nextLine = varReader.nextLine();

      // Ignore lines that are empty or start with '#' (documentation)
      if (nextLine.length() > 0 && nextLine.charAt(0) != '#') {
        Scanner line = new Scanner(nextLine);
        String uni = line.next();
        String field = line.next();
        if (field.equals("kTraditionalVariant") || field.equals("kSimplifiedVariant")) {
          // Read Unicode
          uniList.add(uni);
          // Read rest of varReader line into varList
          int startIdx = (uni + " " + field + " ").length() - 1;
          varList.add(field + " " + nextLine.substring(startIdx, nextLine.length()));
        }
      }
    }

    // Process each line in strReader file
    while (strReader.hasNextLine()) {
      String nextLine = strReader.nextLine();

      // Ignore lines that are empty or start with '#' (documentation)
      if (nextLine.length() > 0 && nextLine.charAt(0) != '#') {
        ChineseChar cc = readChar(nextLine);
        if (cc != null) {
          // // Check for variants before adding to list
          // if (uniList.contains(cc.getUni())) {
          //   String varStr = varList.get(uniList.indexOf(cc.getUni()));
          //   Scanner lineScan = new Scanner(varStr);
          //   String field = lineScan.next();
          //   ArrayList<String> varArr = new ArrayList<String>();
          //   while (lineScan.hasNext()) {
          //     varArr.add(lineScan.next());
          //   }
          //
          //   if (field.equals("kTraditionalVariant")) {
          //     // Set traditional variant(s)
          //     cc.setTrad(varArr.toArray(new String[varArr.size()]));
          //   } else {
          //     // Set simplified variant(s)
          //     cc.setSimp(varArr.toArray(new String[varArr.size()]));
          //   }
          // }

          charList.add(cc);
        }
      }
    }

    // Add variants with their stroke counts to list
    for (ChineseChar c : charList) {
      // Check for variants before adding to list
      if (uniList.contains(c.getUni())) {
        // Get Unicodes for variants
        String varStr = varList.get(uniList.indexOf(c.getUni()));
        Scanner lineScan = new Scanner(varStr);
        String field = lineScan.next();
        ArrayList<String> varUnis = new ArrayList<String>();
        while (lineScan.hasNext()) {
          varUnis.add(lineScan.next());
        }

        // Create variant chars with their stroke counts
        ChineseChar[] varChars = new ChineseChar[varUnis.size()];
        for (int i = 0; i < varChars.length; i++) {
          String uni = varUnis.get(i);

          // Get index of unicode to get strokes
          int idx = 0;
          for (idx = 0; idx < charList.size(); idx++) {
            if (uni.equals(charList.get(idx).getUni())) {
              break;
            }
          }
          int strokes = charList.get(idx).getStrokes();
          varChars[i] = new ChineseChar(uni, strokes);
        }

        if (field.equals("kTraditionalVariant")) {
          // Set traditional variant(s)
          c.setTrad(varChars);
        } else {
          // Set simplified variant(s)
          c.setSimp(varChars);
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
    /** Chinese character's unicode string */
    private String uni;
    /** Number of strokes in character */
    private int strokes;
    /** Any simplified variants of character */
    // private String[] simp;
    private ChineseChar[] simp;
    /** Any traditional variants of character */
    // private String[] trad;
    private ChineseChar[] trad;

    /**
     * Constructs a Chinese char object with stroke count.
     *
     * @param unicode Unicode string
     * @param strNum number of strokes
     */
    public ChineseChar(String unicode, int strNum) {
      String ch = unicodeToChar(unicode);
      setCh(ch);
      setUni(unicode);
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

    private void setUni(String u) {
      this.uni = u;
    }

    private void setStrokes(int strokes) {
      this.strokes = strokes;
    }

    public void setSimp(ChineseChar[] simp) {
      this.simp = new ChineseChar[simp.length];
      for (int i = 0; i < simp.length; i++) {
        this.simp[i] = simp[i];
      }
    }

    public void setTrad(ChineseChar[] trad) {
      this.trad = new ChineseChar[trad.length];
      for (int i = 0; i < trad.length; i++) {
        this.trad[i] = trad[i];
      }
    }

    // public void setSimp(String[] simp) {
    //   this.simp = new String[simp.length];
    //   for (int i = 0; i < simp.length; i++) {
    //     this.simp[i] = unicodeToChar(simp[i]);
    //   }
    // }
    //
    // public void setTrad(String[] trad) {
    //   this.trad = new String[trad.length];
    //   for (int i = 0; i < trad.length; i++) {
    //     this.trad[i] = unicodeToChar(trad[i]);
    //   }
    // }

    public String getCh() {
      return this.ch;
    }

    public String getUni() {
      return this.uni;
    }

    public int getStrokes() {
      return this.strokes;
    }

    public ChineseChar[] getSimp() {
      return this.simp;
    }

    public ChineseChar[] getTrad() {
      return this.trad;
    }

    // public String[] getSimp() {
    //   return this.simp;
    // }
    //
    // public String[] getTrad() {
    //   return this.trad;
    // }

    /**
     * Formats the ChineseChar into a JavaScript object.
     *
     * @return character as JS object
     */
    @Override
    public String toString() {
      // String chStr = "\"character\": \"" + getCh() + "\"";
      String chStr = "\"" + getCh() + "\": ";
      String strokesStr = "\"strokes\": " + getStrokes();
      String simpStr = "";
      String tradStr = "";

      // String[] s = getSimp();
      ChineseChar[] s = getSimp();
      if (s != null && s.length > 0) {
        simpStr = "\"simp\": [ ";
        for (int i = 0; i < s.length; i++) {
          String ch = s[i].getCh();
          int str = s[i].getStrokes();
          if (i < s.length - 1) {
            // simpStr += "\"" + s[i].getCh() + "\", ";
            simpStr += "{ \"" + ch + "\": " + str + " }, ";
          } else {
            // simpStr += "\"" + ch + "\" ]";
            simpStr += "{ \"" + ch + "\": " + str + " } ]";
          }
        }
      }

      // String[] t = getTrad();
      ChineseChar[] t = getTrad();
      if (t != null && t.length > 0) {
        tradStr = "\"trad\": [ ";
        for (int i = 0; i < t.length; i++) {
          String ch = t[i].getCh();
          int str = t[i].getStrokes();
          if (i < t.length - 1) {
            // tradStr += "\"" + t[i].getCh() + "\", ";
            tradStr += "{ \"" + ch + "\": " + str + " }, ";
          } else {
            // tradStr += "\"" + t[i].getCh() + "\" ]";
            tradStr += "{ \"" + ch + "\": " + str + " } ]";
          }
        }
      }

      StringBuilder obj = new StringBuilder();
      // obj.append("  {\n    ");
      // obj.append(chStr);
      // obj.append(",\n    ");
      obj.append("  " + chStr + "{\n    ");
      obj.append(strokesStr);
      if (s != null) {
        obj.append(",\n    ");
        obj.append(simpStr);
      }
      if (t != null) {
        obj.append(",\n    ");
        obj.append(tradStr);
      }
      obj.append("\n  }");
      return obj.toString();
    }
  }
}
