import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.Scanner;
import java.io.*;

public class MyBoggle implements WordSearchGame {
	
   private static final String[] DEFAULT_BOARD = {"E", "E", "C", "A", "A", "L", "E", "P", "H", "N", "B", "O", "Q", "T", "T", "Y"};
   private TreeSet<String> lexicon;
   private String[][] board;
   private boolean lexiconLoaded = false;

   public MyBoggle() {
      lexicon = new TreeSet<String>();
      setBoard(DEFAULT_BOARD);
   }

   /**
    * Loads the lexicon into a data structure for later use. 
    * 
    * @param fileName A string containing the name of the file to be opened.
    * @throws IllegalArgumentException if fileName is null
    * @throws IllegalArgumentException if fileName cannot be opened.
    */

   @Override
   public void loadLexicon(String fileName) {
   
      if (fileName == null) {
         throw new IllegalArgumentException();
      }
   
      try {
         Scanner scanner = new Scanner(new File(fileName));
         while (scanner.hasNextLine()) {
            lexicon.add(scanner.nextLine().toLowerCase());
         }
         scanner.close();
         lexiconLoaded = true;
      } catch (FileNotFoundException e) {
         throw new IllegalArgumentException(fileName);
      }
   }

   /**
    * Stores the incoming array of Strings in a data structure that will make
    * it convenient to find words.
    * 
    * @param letterArray This array of length N^2 stores the contents of the
    *     game board in row-major order. Thus, index 0 stores the contents of board
    *     position (0,0) and index length-1 stores the contents of board position
    *     (N-1,N-1). Note that the board must be square and that the strings inside
    *     may be longer than one character.
    * @throws IllegalArgumentException if letterArray is null, or is  not
    *     square.
    */

   @Override
   public void setBoard(String[] letterArray) {
   
      if (letterArray == null) {
         throw new IllegalArgumentException();
      }
   
      int n = (int) Math.sqrt(letterArray.length);
      
      if (n <= 1) {
         throw new IllegalArgumentException();
      }
      
      board = new String[n][n];
     
      for (int row = 0; row < n; row++) {
         for (int col = 0; col < n; col++) {
            int index = row * n + col;
            board[row][col] = letterArray[index];
         }
      }
   }

   /**
    * Creates a String representation of the board, suitable for printing to
    *   standard out. Note that this method can always be called since
    *   implementing classes should have a default board.
    */

   @Override
   public String getBoard() {
   
      StringBuilder sb = new StringBuilder();
      int n = board.length;
   
      for (int row = 0; row < n; row++) {
         for (int col = 0; col < n; col++) {
            sb.append(board[row][col]).append(" ");
         }
         sb.append("\n");
      }
   
      return sb.toString();
   
   } 

   /**
    * Retrieves all scorable words on the game board, according to the stated game
    * rules.
    * 
    * @param minimumWordLength The minimum allowed length (i.e., number of
    *     characters) for any word found on the board.
    * @return java.util.SortedSet which contains all the words of minimum length
    *     found on the game board and in the lexicon.
    * @throws IllegalArgumentException if minimumWordLength < 1
    * @throws IllegalStateException if loadLexicon has not been called.
    */

   @Override
   public SortedSet<String> getAllScorableWords(int minimumWordLength) {
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException();
      }
   
      if (!lexiconLoaded) {
         throw new IllegalStateException();
      }
   
      SortedSet<String> result = new TreeSet<>();
      int n = board.length;
   
      for (int row = 0; row < n; row++) {
         for (int col = 0; col < n; col++) {
            StringBuilder currentWord = new StringBuilder();
            boolean[][] visited = new boolean[n][n];
            dfs(row, col, currentWord, visited, result, minimumWordLength);
         }
      }
      return result;
   }

   private void dfs(int row, int col, StringBuilder currentWord, boolean[][] visited,
                    SortedSet<String> result, int minimumWordLength) {
   
      int n = board.length;
   
      if (row < 0 || col < 0 || row >= n || col >= n || visited[row][col]) {
         return;
      }
   
      currentWord.append(board[row][col]);
      visited[row][col] = true;
   
      String word = currentWord.toString();
   
      if (word.length() >= minimumWordLength && lexicon.contains(word)) {
         result.add(word);
      }
   
      for (int dr = -1; dr <= 1; dr++) {
         for (int dc = -1; dc <= 1; dc++) {
            if (dr != 0 || dc != 0) {
               int newRow = row + dr;
               int newCol = col + dc;
               if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n) {
                  dfs(newRow, newCol, new StringBuilder(currentWord), visited, result, minimumWordLength);
               }
            }
         }
      }
       
      visited[row][col] = false;
   }

   /**
   * Computes the cummulative score for the scorable words in the given set.
   * To be scorable, a word must (1) have at least the minimum number of characters,
   * (2) be in the lexicon, and (3) be on the board. Each scorable word is
   * awarded one point for the minimum number of characters, and one point for 
   * each character beyond the minimum number.
   *
   * @param words The set of words that are to be scored.
   * @param minimumWordLength The minimum number of characters required per word
   * @return the cummulative score of all scorable words in the set
   * @throws IllegalArgumentException if minimumWordLength < 1
   * @throws IllegalStateException if loadLexicon has not been called.
   */ 

   @Override
   public int getScoreForWords(SortedSet<String> words, int minimumWordLength) {
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException();
      }
   
      if (!lexiconLoaded) {
         throw new IllegalStateException();
      }
   
      int totalScore = 0;
   
      for (String word : words) {
         if (word.length() >= minimumWordLength && lexicon.contains(word)) {
            totalScore += 1 + word.length() - minimumWordLength;
         }
      }
   
      return totalScore;
   }

   /**
    * Determines if the given word is in the lexicon.
    * 
    * @param wordToCheck The word to validate
    * @return true if wordToCheck appears in lexicon, false otherwise.
    * @throws IllegalArgumentException if wordToCheck is null.
    * @throws IllegalStateException if loadLexicon has not been called.
    */

   @Override
   public boolean isValidWord(String wordToCheck) {
      if (wordToCheck == null) {
         throw new IllegalArgumentException();
      }
   
      if (!lexiconLoaded) {
         throw new IllegalStateException();
      }
   
      return lexicon.contains(wordToCheck.toLowerCase());
   }

   /**
    * Determines if there is at least one word in the lexicon with the 
    * given prefix.
    * 
    * @param prefixToCheck The prefix to validate
    * @return true if prefixToCheck appears in lexicon, false otherwise.
    * @throws IllegalArgumentException if prefixToCheck is null.
    * @throws IllegalStateException if loadLexicon has not been called.
    */

   @Override
   public boolean isValidPrefix(String prefixToCheck) {
      if (prefixToCheck == null) {
         throw new IllegalArgumentException();
      }
   
      if (!lexiconLoaded) {
         throw new IllegalStateException();
      }
   
      String ceiling = lexicon.ceiling(prefixToCheck.toLowerCase());
   
      return ceiling != null && ceiling.startsWith(prefixToCheck.toLowerCase());
   }


   /**
    * Determines if the given word is in on the game board. If so, it returns
    * the path that makes up the word.
    * @param wordToCheck The word to validate
    * @return java.util.List containing java.lang.Integer objects with  the path
    *     that makes up the word on the game board. If word is not on the game
    *     board, return an empty list. Positions on the board are numbered from zero
    *     top to bottom, left to right (i.e., in row-major order). Thus, on an NxN
    *     board, the upper left position is numbered 0 and the lower right position
    *     is numbered N^2 - 1.
    * @throws IllegalArgumentException if wordToCheck is null.
    * @throws IllegalStateException if loadLexicon has not been called.
    */

   @Override
   public List<Integer> isOnBoard(String wordToCheck) {
      if (wordToCheck == null) {
         throw new IllegalArgumentException();
      }
   
      if (!lexiconLoaded) {
         throw new IllegalStateException();
      }
   
      List<Integer> path = new ArrayList<>();
   
      int n = board.length;
      for (int row = 0; row < n; row++) {
         for (int col = 0; col < n; col++) {
            boolean[][] visited = new boolean[n][n];
            if (dfsB(board, wordToCheck, row, col, 0, visited, path, n)) {
               return path;
            }
         }
      }
   
      return path;
   }

   private boolean dfsB(String[][] board, String word, int row, int col, int index, boolean[][] visited, List<Integer> path, int n) {
      if (index == word.length()) {
         return true;
      }
   
      if (row < 0 || col < 0 || row >= n || col >= n || visited[row][col] || !board[row][col].equalsIgnoreCase(String.valueOf(word.charAt(index)))) {
         return false;
      }
   
      visited[row][col] = true;
      path.add(row * n + col);
   
      boolean found = dfsB(board, word, row + 1, col, index + 1, visited, path, n) ||
                        dfsB(board, word, row - 1, col, index + 1, visited, path, n) ||
                        dfsB(board, word, row, col + 1, index + 1, visited, path, n) ||
                        dfsB(board, word, row, col - 1, index + 1, visited, path, n) ||
                        dfsB(board, word, row + 1, col + 1, index + 1, visited, path, n) ||
                        dfsB(board, word, row + 1, col - 1, index + 1, visited, path, n) ||
                        dfsB(board, word, row - 1, col + 1, index + 1, visited, path, n) ||
                        dfsB(board, word, row - 1, col - 1, index + 1, visited, path, n);
   
      if (!found) {
         visited[row][col] = false;
         path.remove(path.size() - 1);
      }
   
      return found;
   }
}


























