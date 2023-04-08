package wordle;
public class Guess{
  private String guess;
  private boolean isCorrect;
  public Guess(String word, String actual){
    guess = word;
    isCorrect = isCorrect(actual);
  }
  public boolean isCorrect(String actual){
    return guess.equals(actual);
  }
  public String analyzeLetter(int i, String actual){
    if(actual.substring(i, i+1).equals(guess.substring(i, i+1))){
      return "g";
    }else if(actual.indexOf(guess.substring(i, i+1))>=0){
      return "y";
    }
    return "gr";
  }
  public String toString(){
    if(isCorrect){
      return guess+" | CORRECT";
    }
    return guess+" | INCORRECT";
  }
}