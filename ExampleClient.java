import java.util.SortedSet;
import java.util.TreeSet;


public class ExampleClient {
   public static void main(String[] args) {
   
   
       //WordSearchGame game = WordSearchGameFactory.createGame();
   
      MyBoggle game = new MyBoggle();
   
      game.loadLexicon("words.txt");
      
      System.out.println("We're here.");
      
      String[] a = {"E", "E", "C", "A", "A", "L", "E", "P", "H", "N", "B", "O", "Q", "T", "T", "Y"};
      
      game.setBoard(a);
      
   
      
      //System.out.println(game.getBoard());
      //System.out.println(game.isOnBoard("peace"));
         
      System.out.println(game.getAllScorableWords(5)); 
   }
}