package wordle;

import javafx.scene.control.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.*;

import javafx.scene.control.Alert.AlertType;

import static wordle.GameManager.fileToString;

public class Wordle extends Application {
  private double xOffset;
  private double yOffset;
  private boolean cont;
  protected static boolean ai = false;
  private static String[] args;
  protected static int currentLetterRow = 0;
  protected static int currentLetterCol = -1;
  private Label l;
  protected static Parent p;
  protected static GameManager g;
  protected static Stage s;
  protected static Scene sc;
  private boolean guessing = false;
  protected static String currGuess = "";
  public static final String[][] LETTER_IDS = { { "l_1-1", "l_1-2", "l_1-3", "l_1-4", "l_1-5" },
      { "l_2-1", "l_2-2", "l_2-3", "l_2-4", "l_2-5" }, { "l_3-1", "l_3-2", "l_3-3", "l_3-4", "l_3-5" },
      { "l_4-1", "l_4-2", "l_4-3", "l_4-4", "l_4-5" }, { "l_5-1", "l_5-2", "l_5-3", "l_5-4", "l_5-5" } };
  public static final String[][] BOX_IDS = { { "b_1-1", "b_1-2", "b_1-3", "b_1-4", "b_1-5" },
      { "b_2-1", "b_2-2", "b_2-3", "b_2-4", "b_2-5" }, { "b_3-1", "b_3-2", "b_3-3", "b_3-4", "b_3-5" },
      { "b_4-1", "b_4-2", "b_4-3", "b_4-4", "b_4-5" }, { "b_5-1", "b_5-2", "b_5-3", "b_5-4", "b_5-5" } };
  public static String alphabet = "abcdefghijklmnopqrstuvwxyz ";
  @Override
  public void start(Stage primaryStage) throws Exception, InterruptedException {


    s = primaryStage;
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
    p=root;
    if(ai){
      g = new GameManager(ai);
    }else{
      g = new GameManager();
    }

    root.setOnMousePressed(event -> {
      xOffset = event.getSceneX();
      yOffset = event.getSceneY();
      event.consume();
    });
    root.setOnMouseDragged(event -> {
      primaryStage.setX(event.getScreenX() - xOffset);
      primaryStage.setY(event.getScreenY() - yOffset);
      event.consume();
    });

    Scene scene = new Scene(root);
    sc = scene;
    scene.getRoot().setEffect(new DropShadow(10, Color.rgb(100, 100, 100)));
    scene.setFill(Color.TRANSPARENT);
    l = (Label) scene.lookup("#l_1-1");
    Label l2  = (Label) scene.lookup("#points");
    String filePoints = "src/main/resources/txt/points.txt";
    int points = 0;
    try {
      points = Integer.parseInt(fileToString(filePoints));
    }catch(FileNotFoundException e){
      e.printStackTrace();
    }
    l2.setText(""+points);
    System.out.println(l);
    scene.getRoot().applyCss();
    Button b = (Button) scene.lookup("#r");
    b.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
    b.getStyleClass().add("l");
    primaryStage.setScene(scene);
    primaryStage.initStyle(StageStyle.TRANSPARENT);

    primaryStage.setScene(scene);
    l.requestFocus();
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent k) {
        g.runSound("type");


        if (k.getCode() == KeyCode.ENTER) {
          currentLetterCol = -1;

          String[] lStats = new String[0];
          try {
            lStats = g.registerGuess(currGuess.toLowerCase());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          if (lStats == null) {
            for (int i = 0; i < 5; i++) {
              String id = LETTER_IDS[currentLetterRow][i];
              Label lTemp = (Label) scene.lookup("#" + id);
              lTemp.setText("");
              guessing = true;
            }
            /*Label er = new Label("NOT A VALID WORD");
            er.setAlignment(Pos.CENTER);
            er.resize(200, 100);
        er.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            er.getStyleClass().clear();
            er.getStyleClass().add("error");
            Stage secondaryStage = new Stage(StageStyle.UNDECORATED);
            Scene newS = new Scene(er, 200, 100);
            secondaryStage.setScene(newS);
            secondaryStage.show();*/

            Alert a = new Alert(Alert.AlertType.WARNING, "HEY! THAT'S NOT A VALID WORD!",
            ButtonType.OK); a.setHeaderText("Invalid Word!"); DialogPane ad =
            a.getDialogPane();
            ad.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            ad.getStyleClass().add("error-label");
            a.setDialogPane(ad); a.show();
             

            /*
             try{ TimeUnit.SECONDS.sleep(3); }catch(InterruptedException e){
             
            }
             
            secondaryStage.hide();
            */
            currGuess = "";
            currentLetterCol = -1;
            return;
          }
          for (int i = 0; i < lStats.length; i++) {
            String id = BOX_IDS[currentLetterRow][i];
            if (lStats[i].equals("g")) {
              VBox temp = (VBox) scene.lookup("#" + id);

              temp.getStyleClass().add("letterG");
              g.points += 5;
              // temp.setStyle("-fx-background-color: green");
            } else if (lStats[i].equals("gr")) {
              VBox temp = (VBox) scene.lookup("#" + id);

              temp.getStyleClass().add("letterGr");
              Label l = (Label) scene.lookup("#usedLetters");
              if(!l.getText().substring(12).contains(currGuess.substring(i, i+1))){
                l.setText(l.getText()+"  "+currGuess.substring(i, i+1));
              }

              System.out.println(l.getText());
              // temp.setStyle("-fx-background-color: grey");
            } else if (lStats[i].equals("y")) {
              VBox temp = (VBox) scene.lookup("#" + id);

              temp.getStyleClass().add("letterY");

              g.points += 2;
              // temp.setStyle("-fx-background-color: yellow");
            }
          }
          if (g.isWon()) {

            g.runSound("right");
            g.points += (30*(6-currentLetterRow));
            Alert won = new Alert(AlertType.INFORMATION, "YOU WON! You have "+g.points+" points", ButtonType.OK);
            won.setOnCloseRequest(e ->{
              String file1 = "src/main/resources/txt/runAnyway.txt";
              String l1 = "";
              try {
                l1 = fileToString(file1);
              } catch (Exception ex) {
                ex.printStackTrace();
              }
              l1 = l1.replaceAll("false", "true");

              PrintWriter writer1 = null;
              try {
                writer1 = new PrintWriter(new File(file1));
              } catch (FileNotFoundException ex) {
                ex.printStackTrace();
              }
              writer1.append(l1);

              writer1.flush();

              try {
                String file2 = "src/main/resources/txt/points.txt";
                String l2 = "0";
                try {
                  l2 = fileToString(file2);
                  System.out.println(Integer.parseInt(l2));
                } catch (Exception ex) {
                  ex.printStackTrace();
                }
                int l2Int = Integer.parseInt(l2) + g.points;
                System.out.println(l2Int);
                l2 = Integer.toString(l2Int);
                System.out.println(l2);
                PrintWriter writer2 = null;
                try {
                  writer2 = new PrintWriter(new File(file2));
                } catch (FileNotFoundException ex) {
                  ex.printStackTrace();
                }
                writer2.append(l2);

                writer2.flush();
                g.reset();
              } catch (IOException ex) {
                ex.printStackTrace();
              } catch (InterruptedException ex) {
                ex.printStackTrace();
              }
            });

            won.show();

          }else{
            g.runSound("beep-10");
          }
          if(currentLetterRow==4&&!g.isWon()){

            Alert lose = new Alert(AlertType.INFORMATION, "The word was "+g.word+". The definition of it is: "+g.definition, ButtonType.OK);
            lose.setWidth(400.0);
            lose.setHeight(400.0);
            lose.setTitle("YOU LOST!");
            lose.setHeaderText("You ran out of guesses!");
            lose.setOnCloseRequest(e ->{
              try {
                String file1 = "src/main/resources/txt/runAnyway.txt";
                String l1 = "";
                try {
                  l1 = fileToString(file1);
                } catch (Exception ex) {
                  ex.printStackTrace();
                }
                l1 = l1.replaceAll("false", "true");
                System.out.println(l1);
                PrintWriter writer1 = null;
                try {
                  writer1 = new PrintWriter(new File(file1));
                } catch (FileNotFoundException ex) {
                  ex.printStackTrace();
                }
                writer1.append(l1);

                writer1.flush();
                g.reset();
              } catch (IOException ex) {
                ex.printStackTrace();
              } catch (InterruptedException ex) {
                ex.printStackTrace();
              }
            });
            lose.show();


            return;
          }
          currentLetterRow++;
          currGuess = "";
          guessing = true;

        }
        if (k.getCode() == KeyCode.BACK_SPACE) {
          if(currentLetterCol>=5){
            currentLetterCol--;
          }
          if(currentLetterCol<0){
            currentLetterCol++;
          }
          Label l1 = (Label) scene.lookup("#" + LETTER_IDS[currentLetterRow][currentLetterCol]);
          l1.setText("");
          if(!(currGuess.length() <=0)) {
            currGuess = currGuess.substring(0, currGuess.length() - 1);
          }
          guessing = true;
        }
        System.out.println(k.getCode().isLetterKey());
        if(k.getCode().isLetterKey()){
          currentLetterCol++;
        }

      }
    });
    scene.setOnKeyTyped(e -> {
      String key = e.getCharacter().toUpperCase();

      if (currentLetterCol >= 5) {
        return;
      }
      if (currentLetterCol <= -1) {
        currentLetterCol = 0;
      }
      if (guessing) {

        guessing = false;
        currentLetterCol--;
        return;
      }
      if(alphabet.toUpperCase().contains(key)) {
        l = (Label) scene.lookup("#" + LETTER_IDS[currentLetterRow][currentLetterCol]);

        l.requestFocus();
        l.setText(key);
        currGuess += key;
      }
    });
    primaryStage.setTitle("Wordle, but Harder");

    primaryStage.show();

    primaryStage.setFullScreen(true);

    primaryStage.setAlwaysOnTop(true);
    primaryStage.requestFocus();
    l.requestFocus();
  }

  public static void run(String[] a, boolean chatai) {
    args = a;
    ai = chatai;
    System.out.println("AI: "+ai);
    launch(a);
  }

}
