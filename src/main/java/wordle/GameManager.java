package wordle;

import javax.sound.sampled.*;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.io.*;
public class GameManager{
    private int numGuesses;
    protected String word;
    private boolean isWon = false;
    private ArrayList<Guess> guesses = new ArrayList<Guess>();
    protected  ArrayList<String> words = new ArrayList<String>();
    protected  ArrayList<String> words_hard = new ArrayList<String>();
    protected ArrayList<String> dictionary = new ArrayList<String>();
    protected ArrayList<String> curr;
    protected String mode;
    protected int points;
    protected String definition;
    public GameManager() throws Exception{

      File wordList = new File("src/main/resources/txt/words.txt");
      File wordList_hard = new File("src/main/resources/txt/wordsharder.txt");
      File dict = new File("src/main/resources/txt/dictionary.txt");
      BufferedReader br = new BufferedReader(new FileReader(wordList));
      BufferedReader br1 = new BufferedReader(new FileReader(dict));

      BufferedReader br2 = new BufferedReader(new FileReader(wordList_hard));
      String currWord = br.readLine();
      while(currWord!=null){


        if(currWord.length()==5){
          words.add(currWord);

        }
        currWord = br.readLine();

      }
      String currDict = br1.readLine();
      while(currDict!=null){


        if(currDict.length()==5){
          dictionary.add(currDict);

        }
        currDict = br1.readLine();

      }
      String currHard = br2.readLine();
      while(currHard!=null){


        if(currHard.length()==5){
          words_hard.add(currHard);

        }
        currHard = br2.readLine();

      }

      BufferedReader modeBr = new BufferedReader(new FileReader(new File("src/main/resources/txt/currMode.txt")));
      String modeS = modeBr.readLine();
      mode = modeS;

      if(modeS.equals("hard")){
        curr = words_hard;
      }else{
        curr = words;
      }

      word = generateWord();
      try {
            // get URL content

            String a = "https://www.dictionary.com/browse/"+word;
            URL url = new URL(a);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader brDefinition = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = brDefinition.readLine()) != null) {

                if(inputLine.contains("class=\"one-click-content css-nnyc96 e1q3nk1v1\"")){
                    String className = "class=\"one-click-content css-nnyc96 e1q3nk1v1\"";
                    inputLine = inputLine.substring(inputLine.indexOf(className)+className.length());
                    definition = inputLine.substring(1, inputLine.indexOf("<")-1);
                    System.out.println(definition);
                    break;
                }
            }
            br.close();

            System.out.println("Done");

      } catch (MalformedURLException e) {
            e.printStackTrace();
      } catch (IOException e) {
            e.printStackTrace();
      }
      System.out.println(word);
      Collections.sort(curr);
      Collections.sort(dictionary);

      br.close();
      br1.close();
    }
    public String generateWord(){

      Random r = new Random();
      int sum =(int) (r.nextDouble()*curr.size());

      return curr.get(sum);
    }
    public void generateNewWord(){

      Random r = new Random();
      int sum =(int) (r.nextDouble()*curr.size());
      word = curr.get(sum);
      try {
            // get URL content

            String a = "https://www.dictionary.com/browse/"+word;
            URL url = new URL(a);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {

                if(inputLine.contains("class=\"one-click-content css-nnyc96 e1q3nk1v1\"")){
                    String className = "class=\"one-click-content css-nnyc96 e1q3nk1v1\"";
                    inputLine = inputLine.substring(inputLine.indexOf(className)+className.length());
                    definition = inputLine.substring(1, inputLine.indexOf("<")-1);
                    System.out.println(definition);
                    break;
                }
            }
            br.close();

            System.out.println("Done");

        } catch (MalformedURLException e) {
          e.printStackTrace();
      } catch (IOException e) {
            e.printStackTrace();
      }
      System.out.println(word);
    }
    public String[] registerGuess(String guess){
      Guess temp = new Guess(guess, word);

      if(!found(0, dictionary.size()-1, guess)){
        return null;
      }
      String[] letterStats = new String[guess.length()];
      guesses.add(temp);
      System.out.println(temp.isCorrect(word));
      isWon = temp.isCorrect(word);
      for(int i = 0; i<guess.length(); i++){
        letterStats[i] = temp.analyzeLetter(i, word);
      }
      return letterStats;
    }
    public boolean found(int l, int r, String x){
      ArrayList<String> arr = dictionary;


      if (r >= l) {
          int mid = l + (r - l) / 2;

          // If the element is present at the middle
          // itself

          if (arr.get(mid).equals(x))
              return true;

          // If element is smaller than mid, then
          // it can only be present in left subarray
          if (arr.get(mid).compareTo(x)>0)
              return found(l, mid - 1, x);

          // Else the element can only be present
          // in right subarray
          return found(mid + 1, r, x);
      }

      // We reach here when element is not
      // present in array
      return false;
    }
    public boolean isWon(){
      return isWon;
    }
    public void reset() throws IOException, InterruptedException{
      StringBuilder cmd = new StringBuilder();
      cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
      for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
        cmd.append(jvmArg + " ");
      }
      cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
      cmd.append(Main.class.getName()).append(" ");


      Thread.currentThread().sleep(100); // 10 seconds delay before restart
      Runtime.getRuntime().exec(cmd.toString());

      System.exit(0);

    }
    public static String fileToString(String filePath) throws Exception{
      String input = null;
      Scanner sc = new Scanner(new File(filePath));
      StringBuffer sb = new StringBuffer();
      while (sc.hasNextLine()) {
        input = sc.nextLine();
        sb.append(input);
      }
      return sb.toString();
    }
    public void reset(String s, String str) throws IOException, InterruptedException{
      StringBuilder cmd = new StringBuilder();
      cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
      for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
        cmd.append(jvmArg + " ");
      }
      cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
      cmd.append(Main.class.getName()).append(" ");
      String file = "src/main/resources/txt/currMode.txt";
      String l = "";
      try {
        l = fileToString(file);
      } catch (Exception e) {
        e.printStackTrace();
      }
      l = l.replaceAll(mode, s);
      System.out.println(l);
      PrintWriter writer = new PrintWriter(new File(file));
      writer.append(l);

      writer.flush();
      String file1 = "src/main/resources/txt/runAnyway.txt";
      String l1 = "";
      try {
        l1 = fileToString(file1);
      } catch (Exception e) {
        e.printStackTrace();
      }
      l1 = l1.replaceAll("false", str);
      System.out.println(l1);
      PrintWriter writer1 = new PrintWriter(new File(file1));
      writer1.append(l1);

      writer1.flush();
      Thread.sleep(100); // 10 seconds delay before restart
      Runtime.getRuntime().exec(cmd.toString());

      System.exit(0);

    }
    public void reset(String s) throws IOException, InterruptedException{
      StringBuilder cmd = new StringBuilder();
      cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
      for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
        cmd.append(jvmArg + " ");
      }
      cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
      cmd.append(Main.class.getName()).append(" ");
      String file = "src/main/resources/txt/currMode.txt";
      String l = "";
      try {
        l = fileToString(file);
      } catch (Exception e) {
        e.printStackTrace();
      }
      l = l.replaceAll(mode, s);
      System.out.println(l);
      PrintWriter writer = new PrintWriter(new File(file));
      writer.append(l);

      writer.flush();

      Thread.sleep(100); // 10 seconds delay before restart
      Runtime.getRuntime().exec(cmd.toString());

      System.exit(0);

    }
    public static void startApplication(String[] args){
      Wordle.run(args);
    }

    public void runSound(String sound) {
      try {
        URL url = this.getClass().getResource("/wav/"+sound+".wav");
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
      } catch (UnsupportedAudioFileException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (LineUnavailableException e) {
        e.printStackTrace();
      }
    }
}