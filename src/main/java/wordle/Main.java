package wordle;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.*;
import java.nio.Buffer;
import java.util.*;
public class Main {
  protected static String[] arg;
  protected static Stage sg;
  public static void main(String[] args){
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
      runAnyway =  br.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if(runAnyway.equals("false")) {
      System.out.println("\n\n\n\n\nMODE: ");

      boolean b = true;
      while (b) {
        String mode = s.nextLine().toLowerCase();

        if (mode.equals("dictionary")) {
          b = false;
          System.out.println("Dictionary Mode: ");
          String dictMode = s.nextLine();
          if(dictMode.equals("easy")){
            try {
              DictionaryTester d = new DictionaryTester();

              d.registerInvalids();
              d.storeInvalids();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }else if(dictMode.equals("hard")){
            try {
              DictionaryTester d = new DictionaryTester();

              d.registerInvalidsHard();
              d.storeInvalids();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }else if(dictMode.equals("all")){
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
          Wordle.run(args);
        }else{

        }
      }
    }else{
      Wordle.run(args);
    }


  }



}