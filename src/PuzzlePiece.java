import java.util.ArrayList;
import java.util.List;
import java.awt.Color;


public class PuzzlePiece {
  private Character abjad;
  private List<Point> piece;
  private Color pieceColor;
  private String ansiColor;

  public PuzzlePiece() {
    this.piece = new ArrayList<>(); 
 }

 public void setPieceColor(Color pieceColor) {
     this.pieceColor = pieceColor;
 }

 public String getAnsiColor() {
     return ansiColor;
 }

 public Color getPieceColor() {
     return pieceColor;
 }

 public void setAnsiColor(String ansiColor) {
     this.ansiColor = ansiColor;
 }

  public Character getAbjad() {
     return abjad;
  }

  public void addToList(int a, int b){
    Point titik = new Point(a, b);
    this.piece.add(titik);
  }

  public void addAbjad(Character a){
    this.abjad = a;
  }

  public Character getPieceName (){
    return this.abjad;
  }

  public List<Point> getPiece(){
    return this.piece;
  }

  public void rotate90(List<Point> bentukPiece){
    Point pusatRotasi = bentukPiece.get(0); 
    int a = pusatRotasi.getPoint1();
    int b = pusatRotasi.getPoint2();

    for (int i =1; i<piece.size(); i++) {
      Point p = bentukPiece.get(i);
      int x = p.getPoint1();
      int y = p.getPoint2();

      int xBaru = -y + a + b;
      int yBaru = x - a + b;

      p.setPoint1(xBaru);
      p.setPoint2(yBaru);
    }
  }

  public void makesPieceLeft(){
    List<Point> wholePiece = this.piece;
    int theMostLeftPoint = 9999;

    for (int i =0; i<wholePiece.size(); i++){
        if (theMostLeftPoint> wholePiece.get(i).getPoint2()){
            theMostLeftPoint = wholePiece.get(i).getPoint2();
        }
    }

    if (theMostLeftPoint>0){
        for (int i =0; i<wholePiece.size(); i++){
            wholePiece.get(i).setPoint2(wholePiece.get(i).getPoint2()-theMostLeftPoint);
        }
    }
    
  }

  public void makesPieceTop(){
    List<Point> wholePiece = this.piece;
    int theMostTop = 9999;

    for (int i =0; i<wholePiece.size(); i++){
        if (theMostTop> wholePiece.get(i).getPoint1()){
            theMostTop = wholePiece.get(i).getPoint1();
        }
    }

    if (theMostTop>0){
        for (int i =0; i<wholePiece.size(); i++){
            wholePiece.get(i).setPoint1(wholePiece.get(i).getPoint1()-theMostTop);
        }
    }
    
  }

  public void normalize() {
    makesPieceLeft();
    makesPieceTop();
  }

  public void shiftTo(int newRow, int newCol) {
    List<Point> wholePiece = this.piece;
    int currentRow = wholePiece.get(0).getPoint1();
    int currentCol = wholePiece.get(0).getPoint2();
    int rowOffset = newRow - currentRow;
    int colOffset = newCol - currentCol;
    
    for (Point p : wholePiece) {
        p.setPoint1(p.getPoint1() + rowOffset);
        p.setPoint2(p.getPoint2() + colOffset);
    }
  }

  public void mirrorHorizontally() {
    for (Point p : this.piece) {
        p.setPoint2(-p.getPoint2()); 
    }
    normalize();
  }

  public void mirrorVertically() {
      for (Point p : this.piece) {
          p.setPoint1(-p.getPoint1()); 
      }
      normalize();
  }

}