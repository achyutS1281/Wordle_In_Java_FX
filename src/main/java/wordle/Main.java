package wordle;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
public class Main {
  protected static String[] arg;
  protected static Stage sg;
  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    arg = args;
    File run = new File("src/main/resources/txt/runAnyway.txt");
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(run));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String runAnyway = null;
    try {
      runAnyway = br.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    boolean ai = false;
    if (runAnyway.equals("false")) {
      System.out.println("\n\n\n\n\nMODE: ");

      boolean b = true;
      while (b) {
        String mode = s.nextLine().toLowerCase();

        if (mode.equals("dictionary")) {
          b = false;
          System.out.println("Dictionary Mode: ");
          String dictMode = s.nextLine();
          if (dictMode.toLowerCase().equals("easy")) {
            try {
              DictionaryTester d = new DictionaryTester();

              d.registerInvalids();
              d.storeInvalids();
            } catch (IOException e) {
              e.printStackTrace();
            }
          } else if (dictMode.toLowerCase().equals("hard")) {
            try {
              DictionaryTester d = new DictionaryTester();

              d.registerInvalidsHard();
              d.storeInvalids();
            } catch (IOException e) {
              e.printStackTrace();
            }
          } else if (dictMode.equals("all")) {
            try {
              DictionaryTester d = new DictionaryTester();

              d.registerInvalidsHard();
              d.registerInvalids();
              d.storeInvalids();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }

        } else if (mode.equals("wordle")) {
          b = false;
          System.out.println("\n\n\n\n\nUse GPT: ");
          String ChatGpt = s.nextLine().toUpperCase();
          ai = true;
          if (ChatGpt.equals("NO")) {
            ai = false;
          } else {
            ai = true;
          }
          System.out.println(ai);
          Wordle.run(args, ai);
        } else {

        }
      }
    } else {
      Wordle.run(args, ai);
    }


  }



}