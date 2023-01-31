package solveur;

import java.util.Scanner;

public class maintest2 {
  static Scanner S = new Scanner(System.in);

  public static void main(String[] args) {
    Point pt1, pt2, pt3, pt4, pt5, pt6;
    pt1 = new Point(1, "1", 0);
    pt2 = new Point(2, "2", 2);
    pt3 = new Point(3, "3", 8);
    pt4 = new Point(4, "4", 3);
    pt5 = new Point(5, "5", 9);
    pt6 = new Point(6, "6", 4);
    


    Station stat = new Station("Les Arcs");// création d'une station afin de récupérer le nb_points
    stat.Lpoint.add(pt1);
    stat.Lpoint.add(pt2);
    stat.Lpoint.add(pt3);
    stat.Lpoint.add(pt4);
    stat.Lpoint.add(pt5);
    stat.Lpoint.add(pt6);

    
    Descente testD12, testD13, testD24,testD35,testD43,testD52,testD23,testD45,testD46,testD56;

    Descente.TypeDescente typeDN = Descente.TypeDescente.N;
    Descente.TypeDescente typeDB = Descente.TypeDescente.B;
    Descente.TypeDescente typeDR = Descente.TypeDescente.R;

    
    testD12 = new Descente(12, "Descente 12", pt1, pt2, typeDB,100);
    testD13 = new Descente(13, "Descente 13", pt1, pt3, typeDR,100);
    testD24 = new Descente(24, "Descente 24", pt2, pt4, typeDN,100);
    testD35 = new Descente(35, "Descente 35", pt3, pt5, typeDB,100);
    testD43 = new Descente(43, "Descente 43", pt4, pt3, typeDR,500);
    testD52 = new Descente(52, "Descente 52", pt5, pt2, typeDN,233.33333333333333);
    testD23 = new Descente(23, "Descente 23", pt2, pt3, typeDB,120);
    testD45 = new Descente(45, "Descente 45", pt4, pt5, typeDR,150);
    testD46 = new Descente(46, "Descente 46", pt4, pt6, typeDB,120);
    testD56 = new Descente(56, "Descente 56", pt5, pt6, typeDR,250);
    

  
    stat.Ltransition.add(testD12);
    stat.Ltransition.add(testD13);
    stat.Ltransition.add(testD23);
    stat.Ltransition.add(testD24);
    stat.Ltransition.add(testD35);
    stat.Ltransition.add(testD43);
    stat.Ltransition.add(testD52);
    stat.Ltransition.add(testD46);
    stat.Ltransition.add(testD45);
    stat.Ltransition.add(testD56);

    /*
    System.out.println(stat.Ltransition.size());
    System.out.println(stat.Lpoint.size());
    System.out.println(stat.Lpoint.get(0));

    System.out.println(pt1.Succ_transi);
    System.out.println(pt1.Succ_transi.size());*/

    //Dijkstra test;
    //test = new Dijkstra(stat, 1, 6, 0);
    //System.out.println(test.getPCC());
  }

}
