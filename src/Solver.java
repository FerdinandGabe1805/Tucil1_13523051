import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


public class Solver {
    private GameInfo iqPuzzler;
    private int[][] solutionBoard;
    public boolean done = false;
    private String charSolution;
    private int casesChecked= 1;
  
    public Solver(String filePath) throws FileNotFoundException {
        this.iqPuzzler = new GameInfo(filePath);
        this.solutionBoard = new int[iqPuzzler.getRow()][iqPuzzler.getColumn()];
  
        initializeBoard();
        for (int i =0; i<iqPuzzler.getListOfPieces().size(); i++){
          iqPuzzler.getListOfPieces().get(i).makesPieceLeft();
        }
    }
  
    public String getCharSolution() {
        return charSolution;
    }
  
    public int getCasesChecked() {
        return casesChecked;
    }
  
    private void initializeBoard() {
      for (int i = 0; i < iqPuzzler.getRow(); i++) {
          for (int j = 0; j < iqPuzzler.getColumn(); j++) {
              int[][] a = iqPuzzler.getBoard();
              this.solutionBoard[i][j] = a[i][j];
          }
      }
    }
  
    public Character getPieceAbjadInBoard(int row, int col){
      for (PuzzlePiece piece : this.iqPuzzler.getListOfPieces()) {
        for (Point p : piece.getPiece()) {
          if (p.getPoint1() == row && p.getPoint2()== col){
            return piece.getAbjad();
          }
        }
      }
      return '*';
    }
  
  
    public void makeTxtFile() {
      StringBuilder textFile = new StringBuilder();
      
      for (int i = 0; i < iqPuzzler.getRow(); i++) {
          for (int j = 0; j < iqPuzzler.getColumn(); j++) {
              if (solutionBoard[i][j] == -1) {
                  textFile.append(" "); 
              } else if (solutionBoard[i][j] == 1) {
                  textFile.append(getPieceAbjadInBoard(i, j));
              } else {
                  textFile.append("X"); 
              }
          }
          textFile.append("\n"); 
      }
      
      this.charSolution = textFile.toString();
  }
  
    public void saveToFile(String fileName) {
      try (FileWriter writer = new FileWriter(fileName)) {
          writer.write(this.charSolution);
          System.out.println("File berhasil disimpan: " + fileName);
      } catch (IOException e) {
          System.out.println("Terjadi kesalahan saat menyimpan file.");
          e.printStackTrace();
      }
    }
  
    public void printSolution() {
      for (int i = 0; i < iqPuzzler.getRow(); i++) {
        for (int j = 0; j < iqPuzzler.getColumn(); j++) {
            if (solutionBoard[i][j] == -1) {
                System.out.print(".");
            } else if(solutionBoard[i][j] == 1){
                System.out.print("B");
            }
            else if(solutionBoard[i][j] == 0){
              System.out.print("X");
            }
        }
        System.err.println();
      }
      System.err.println();
    }
  
  
  
    public void trysolve(List<PuzzlePiece> listpuzzle) {
      if (done || isBoardFull()) {  
          this.done = true;
          System.out.println("Puzzle solved!");
          return;
      }
  
      if (listpuzzle.isEmpty()) { 
          return;
      }
  
      PuzzlePiece head = listpuzzle.get(0);
      List<PuzzlePiece> tail = new ArrayList<>(listpuzzle.subList(1, listpuzzle.size()));
  
      for (int mirror = 0; mirror < 2; mirror++) {  
          for (int rotation = 0; rotation < 4; rotation++) {
              head.normalize();
  
  
              for (int i = 0; i < solutionBoard.length; i++) {
                  for (int j = 0; j < solutionBoard[0].length; j++) {
                      if (this.done) return; 
  
                      head.shiftTo(i, j);
  
                      if (canPlace(head)) {
                          placePiece(head);
                          printSolution();
                          trysolve(tail);
                          if (done || isBoardFull()) return;
                          this.casesChecked++;
                          removePiece(head);
                      }
                  }
              }
              head.rotate90(head.getPiece()); 
          }
  
          if (mirror == 0) {
              head.mirrorHorizontally(); 
          } else if (mirror == 1) {
              head.mirrorVertically(); 
          }
      }
  }
  
  
  
    private void placePiece(PuzzlePiece piece) {
      List<Point> puzzle = piece.getPiece();
      for (int i = 0; i < puzzle.size(); i++) {
          int row = puzzle.get(i).getPoint1();
          int column = puzzle.get(i).getPoint2();
          this.solutionBoard[row][column] = 1;
      }
    }
  
    private void removePiece(PuzzlePiece piece) {
      List<Point> puzzle = piece.getPiece();
      for (int i = 0; i < puzzle.size(); i++) {
          int row = puzzle.get(i).getPoint1();
          int column = puzzle.get(i).getPoint2();
          this.solutionBoard[row][column] = 0;
      }
    }
  
    private boolean canPlace(PuzzlePiece piece) {
      List<Point> puzzle = piece.getPiece();
      for (int i=0; i<puzzle.size(); i++){
        int row = puzzle.get(i).getPoint1();
        int column = puzzle.get(i).getPoint2();
        if (row < 0 || row >= solutionBoard.length || column < 0 || column >= solutionBoard[0].length) {
          return false;
        }
        if (solutionBoard[row][column] == 1 || solutionBoard[row][column] == -1){
            return false;
        }
      }
      return true;
    }
  
    private boolean isBoardFull() {
      for (int i = 0; i < this.solutionBoard.length; i++) {
          for (int j = 0; j < this.solutionBoard[0].length; j++) {
              if (solutionBoard[i][j] == 0 ) {
                  return false; 
              }
          }
      }
  
      return true; 
    }
  
    public GameInfo getIqPuzzler() {
        return iqPuzzler;
    }

  }