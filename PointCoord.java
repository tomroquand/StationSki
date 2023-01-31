package solveur;

public class PointCoord extends Point {
  // variables
  public double x;
  public double y;

  // constructor
  public PointCoord(int numP, String nameP, double altP, double Coordx, double Coordy) {
    super(numP, nameP, altP);
    x = Coordx;
    y = Coordy;
  }

  // toString method
  public String toString() {
    return super.toString() + " de coordonnées : (" + x + ";" + y + ")";
  }
}
