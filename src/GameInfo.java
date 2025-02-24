import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GameInfo {
  private ColorPalette arrColor = new ColorPalette();
  private int rows;
  private int column;
  private int numberOfPiece;
  private String gameMode;
  private List<PuzzlePiece> listOfPieces;
  private int[][] board;
  private Map<Character,String> AnsiCOlorMap  = new HashMap<>();
  private Map<Character,Color> pieceColor = new HashMap<>();

  private static final List<Character> UPPERCASE_LETTERS = new ArrayList<>();
  static {
      for (char ch = 'A'; ch <= 'Z'; ch++) {
          UPPERCASE_LETTERS.add(ch);
      }
  }

  public GameInfo(String filePath) throws FileNotFoundException {
      try {
          File myObj = new File(filePath);
          Scanner myReader = new Scanner(myObj);
          String[] dataInt = myReader.nextLine().split(" ");

          this.listOfPieces = new ArrayList<>();
          this.rows = Integer.parseInt(dataInt[0]);
          this.column = Integer.parseInt(dataInt[1]);
          this.numberOfPiece = Integer.parseInt(dataInt[2]);
          this.gameMode = myReader.nextLine();
          this.board = new int[this.rows][this.column];

          if (gameMode.equals("CUSTOM")) {
              for (int i = 0; i < this.rows; i++) {
                  String boardPattern = myReader.nextLine();
                  if (boardPattern.length()==column){
                    for (int j = 0; j < this.column; j++) { 
                        if (boardPattern.charAt(j) == '.') {
                            this.board[i][j] = -1;
                        } else if (boardPattern.charAt(j) == 'X'){
                            this.board[i][j] = 0;
                        } else{
                          myReader.close();
                          throw new IllegalArgumentException("Bagian grid tidak memnuhi spesifikasi yaitu berisi character 'X' atau '.' saja.");
                        }
                    }
                  }
                  else{
                    myReader.close();
                          throw new IllegalArgumentException("Terdapat kesalahan dalam membaca custom grid.");
                  }
              }
          } 
          else {
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.column; j++) { 
                    this.board[i][j] = 0;
                }
            }
          }

          Character lastAbjad = null;
          int counter = 0;
          while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            System.err.println("Read line: '" + data + "'"); 
            if (data.isEmpty()) continue; 
        
            int indx = this.isInChar(data); 
            
            if (indx == -1) continue; 
        
            Character currentChar = UPPERCASE_LETTERS.get(indx);
            
            if (lastAbjad == null || !currentChar.equals(lastAbjad)) {
              counter = 0;
              PuzzlePiece puzzle = new PuzzlePiece();
              puzzle.addAbjad(currentChar);
              lastAbjad = currentChar;
      
              for (int i = 0; i < data.length(); i++) {
                  if (data.charAt(i) != ' ') {  
                      if(data.charAt(i) == lastAbjad){
                        puzzle.addToList(counter, i);
                      }
                      else{
                        myReader.close();
                        throw new IllegalArgumentException("Piece tidak valid ditemukan.");
                      }

                  }
              }
              this.listOfPieces.add(puzzle);
              
        
            } else {
                counter++; 
                for (int i = 0; i < data.length(); i++) {
                    if (data.charAt(i) != ' ') {
                        listOfPieces.get(listOfPieces.size() - 1).addToList(counter, i);
                    }
                }
            }
          }
        
        for (int i = 0; i < listOfPieces.size(); i++) {
          listOfPieces.get(i).setPieceColor(arrColor.COLORS[i % arrColor.COLORS.length]);
          Character huruf =listOfPieces.get(i).getAbjad();
          String ansi = arrColor.COLORSANSI[i];
          Color warnaBlok = arrColor.COLORS[i];
          if  (AnsiCOlorMap.containsKey(huruf)){
            myReader.close();
            throw new IllegalArgumentException("Mohon maaf terdapat duplikasi piece dengan huruf yang sama dalam file.");
          }
          AnsiCOlorMap.put(huruf, ansi);
          pieceColor.put(huruf, warnaBlok);
        }
        
        myReader.close();
        if (this.listOfPieces.size()!=this.numberOfPiece){
          throw new IllegalArgumentException("Jumlah piece tidak sesuai dengan data dalam file.");
        }
      } catch (FileNotFoundException e) {
          System.out.println("Terjadi Kesalahan saat memuat File");
          e.printStackTrace();
          throw e;
      }
  }

  public int isInChar(String puzzlePieceString) {
      for (int i = 0; i < puzzlePieceString.length(); i++) {
          char ch = puzzlePieceString.charAt(i);
          if (UPPERCASE_LETTERS.contains(ch)) {
              return UPPERCASE_LETTERS.indexOf(ch); 
          }
      }
      return -1;
  }

  public Map<Character, Color> getPieceColor() {
      return pieceColor;
  }

  public int getRow(){
    return this.rows;
  }

  public int getColumn(){
    return this.column;
  }

  public int getNumberOfPiece(){
    return this.numberOfPiece;
  }

  public String getGameMode(){
    return this.gameMode;
  }

  public List<PuzzlePiece> getListOfPieces() {
      return listOfPieces;
  }

  public int[][] getBoard() {
      return board;
  }

}