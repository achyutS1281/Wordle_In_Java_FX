package wordle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import static wordle.GameManager.fileToString;


public class DictionaryTester extends Application {
    protected ArrayList<String> words1 = new ArrayList<String>();
    protected  ArrayList<String> words_hard1 = new ArrayList<String>();
    protected ArrayList<String> dictionary1 = new ArrayList<String>();
    protected ArrayList<String> invalids = new ArrayList<String>();
    private ProgressBar p;

    public DictionaryTester() throws IOException {
        File wordList = new File("src/main/resources/txt/words.txt");
        File wordList_hard = new File("src/main/resources/txt/wordsharder.txt");
        File dict = new File("src/main/resources/txt/dictionary.txt");
        BufferedReader br = new BufferedReader(new FileReader(wordList));
        BufferedReader br1 = new BufferedReader(new FileReader(dict));

        BufferedReader br2 = new BufferedReader(new FileReader(wordList_hard));
        String currWord = br.readLine();
        while(currWord!=null){


            if(currWord.length()==5){
                words1.add(currWord);
                System.out.println(currWord);
            }
            currWord = br.readLine();

        }
        String currDict = br1.readLine();
        while(currDict!=null){


            if(currDict.length()==5){
                dictionary1.add(currDict);
                System.out.println(currDict);
            }
            currDict = br1.readLine();

        }
        String currHard = br2.readLine();
        while(currHard!=null){


            if(currHard.length()==5){
                words_hard1.add(currHard);
                System.out.println(currHard);
            }
            currHard = br2.readLine();

        }


    }

    @Override
    public void start(Stage stage) throws Exception {
        p = new ProgressBar();
        p.setProgress(0);
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.show();
    }

    public void registerInvalidsHard() throws IOException {

        for(int i = 0; i<words_hard1.size(); i++) {
            clearScreen();
            URL url = new URL("https://www.dictionary.com/browse/"+words_hard1.get(i));
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();

            int responseCode = huc.getResponseCode();
            System.out.println("\n"+responseCode);
            System.out.println(HttpURLConnection.HTTP_OK == responseCode);
            if(!(HttpURLConnection.HTTP_OK == responseCode)){
                invalids.add(words_hard1.get(i));
            }

            progressPercentage(i,words_hard1.size());

        }
        System.out.println(invalids.size());
        System.out.println(invalids);
    }
    public void registerInvalids() throws IOException {
        for(int i = 0; i<words1.size(); i++) {
            System.out.flush();
            URL url = new URL("https://www.dictionary.com/browse/"+words1.get(i));
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();

            int responseCode = huc.getResponseCode();
            System.out.println("\n"+responseCode);
            System.out.println(HttpURLConnection.HTTP_OK == responseCode);

            if(!(HttpURLConnection.HTTP_OK == responseCode)){
                invalids.add(words1.get(i));
            }

            progressPercentage(i,words1.size());

            clearScreen();
        }
        System.out.println(invalids.size());
        System.out.println(invalids);
    }
    public void storeInvalids(){
        String file1 = "src/main/resources/txt/invalids.txt";
        PrintWriter writer1 = null;
        try {
            writer1 = new PrintWriter(file1);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        for(String s : invalids) {
            writer1 = writer1.append("\n"+s);
            writer1.flush();
        }
    }
    public static void progressPercentage(int remain, int total) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }
        int maxBareSize = 10; // 10unit for 100%
        int remainProcent = ((100 * remain) / total) / maxBareSize;
        int remainProcentDisp = ((100*remain) / total);
        char defaultChar = '-';
        String icon = "*";
        String bare = new String(new char[maxBareSize]).replace('\0', defaultChar) + "]";
        StringBuilder bareDone = new StringBuilder();
        bareDone.append("[");
        for (int i = 0; i < remainProcent; i++) {
            bareDone.append(icon);
        }
        String bareRemain = bare.substring(remainProcent);
        System.out.print("\r" + bareDone + bareRemain + " " + remainProcentDisp + "%");
        if (remain == total) {
            System.out.print("\n");
        }
        //Credit to StackExchange for part of this method
    }
    public static void clearScreen() {

        System.out.print("\n\n");

    }
}
