package wordle;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import static wordle.GameManager.fileToString;
import static wordle.Wordle.*;

public class MainViewController implements Initializable {

    public TableView<Order> exampleTable;
    public TableColumn<Order, Integer> orderIdColumn;
    private Stage s;
    private Slider pb;
    private Label l1;
    private boolean manip = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage stage = new Stage(StageStyle.UNDECORATED);
        s = stage;
        Label l = new Label("MODE");
        Label l1 = new Label("Easy");
        this.l1 = l1;
        Slider progressBar = new Slider(0, 1, 0);
        pb = progressBar;
        pb.setBlockIncrement(1.0);
        pb.setMajorTickUnit(1.0);
        pb.valueProperty().addListener((observable, oldValue, newValue) -> {
           String s = "";
            if(newValue.equals(1.0)&&!manip){
               Wordle.g.curr = Wordle.g.words_hard;
               if(Wordle.g.ai) {
                   try {
                       Wordle.g.word = Wordle.g.generateWordAiHard();
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }else {
                   Wordle.g.generateNewWord();
               }
                try {
                    for(int i = 0; i<10; i++) {
                        this.l1.setText("Hard");
                        System.out.println(this.l1.getText()+" 1");
                        Thread.sleep(100);
                    }

                    Wordle.g.reset("hard", "true");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
           }else if(newValue.equals(0.0)&&!manip){
               Wordle.g.curr = Wordle.g.words;
                if(Wordle.g.ai) {
                    try {
                        Wordle.g.word = Wordle.g.generateWordAiHard();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Wordle.g.generateNewWord();
                }
                try {
                    for(int i = 0; i<10; i++) {
                        this.l1.setText("Easy");
                        System.out.println(this.l1.getText()+" 2");
                        Thread.sleep(200);
                    }

                    Wordle.g.reset("easy", "true");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
           }
        });
        VBox vBox = new VBox(l, pb, this.l1);
        vBox.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        vBox.getStyleClass().add("error");
        l.setViewOrder(100);
        progressBar.setViewOrder(200);
        l.setVisible(true);
        progressBar.setVisible(true);
        Scene s = new Scene(vBox);
        stage.setScene(s);

    }

    @FXML
    private void handleExitButtonClicked(ActionEvent event) {
        String file1 = "src/main/resources/txt/runAnyway.txt";
        String l1 = "";
        try {
            l1 = fileToString(file1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        l1 = l1.replaceAll("true", "false");
        System.out.println(l1);
        PrintWriter writer1 = null;
        try {
            writer1 = new PrintWriter(new File(file1));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer1.append(l1);

        writer1.flush();
        Platform.exit();
        event.consume();
    }

    @FXML
    private void handleSettingsButtonClicked(ActionEvent event) {

        if(!s.isShowing()) {
            manip=true;
            if(Wordle.g.mode.equals("easy")){
                pb.setValue(0.0);
                l1.setText("Easy");
            }else{
                pb.setValue(1.0);
                l1.setText("Hard");
            }
            Wordle.s.setFullScreen(false);
            s.show();

        }else{
            Wordle.s.setFullScreen(true);
            s.hide();
        }
        manip = false;
        event.consume();

    }
    @FXML
    private void handleReset(ActionEvent e){
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
            Wordle.g.reset();
        }catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void handleFullScreen(ActionEvent actionEvent) {
        if(Wordle.s.isFullScreen()){
            Wordle.s.setFullScreen(false);
        }else{
            Wordle.s.setFullScreen(true);
        }
    }

    public void handleHint(ActionEvent actionEvent) {
        String points = "src/main/resources/txt/points.txt";
        String newPoints = "0";
        try {
            newPoints = fileToString(points);
            System.out.println(Integer.parseInt(newPoints));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int l2I = Integer.parseInt(newPoints);
        if(l2I>=1000) {

            int col = currentLetterCol+1;

            Label l = (Label) Wordle.sc.lookup("#" + LETTER_IDS[currentLetterRow][col]);
            System.out.println(l);
            l.requestFocus();
            l.setText(g.word.substring(col, col+1).toUpperCase());
            Wordle.currGuess += g.word.substring(col, col+1).toUpperCase();
            currentLetterCol += 1;

            int l2Int = Integer.parseInt(newPoints) - 1000;
            System.out.println(l2Int);
            newPoints = Integer.toString(l2Int);
            System.out.println(newPoints);
            PrintWriter writer2 = null;
            try {
                writer2 = new PrintWriter(new File(points));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            writer2.append(newPoints);
            Label la2 = (Label) Wordle.sc.lookup("#points");
            la2.setText(newPoints);
            writer2.flush();
        }
    }

    public static class Order {
        IntegerProperty id;
        StringProperty state;


        public Order(Integer id, String state) {
            this.id = new SimpleIntegerProperty(id);
            this.state = new SimpleStringProperty(state);

        }

        public int getId() {
            return id.get();
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public IntegerProperty idProperty() {
            return id;
        }

        
  }
}