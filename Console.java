package application;
import solveur.*;
import parseur.*;

import java.awt.Graphics;
import java.util.Scanner;


public class Console {
	
	static Scanner s = new Scanner(System.in);

	public static void main(String argv[]) throws Exception,PathNonExistant
	{
		
		StatParser P = new StatParser();
		Station S = P.parser();
		
		//Dijkstra test;		
	    //test = new Dijkstra(S, 26, 5);
	    //System.out.println(test);
	    
		// LOOP TO KEEP GOING UNLESS USER WANTS TO STOP
		String choix = "yes", temps;
		int depart, arrivee;
		Dijkstra itineraire;
		
		
		while(choix.equals("yes"))
		{
			System.out.println(S.toString());
			
			depart = choixDepart(S);
			arrivee = choixArrivee(S);
			temps = choixTemps();		
			
			try
			{
				if(temps.equals("abs"))
				{
					System.out.println("Calcul en temps absolu...");
					itineraire = new Dijkstra(S, depart, arrivee, 0);
					System.out.println(itineraire);
				}
				else if(temps.equals("reel"))
				{
					System.out.println("Calcul en temps reel...");
					itineraire = new Dijkstra(S, depart, arrivee, 1);
					System.out.println(itineraire);
				}
				else 
				{
					System.out.println("Veuillez saisir abs ou reel dans le choix du temps");
				}
			}
			catch(PointInexistantException p)
			{
				System.out.println(p);
			}
			catch(PathNonExistant p)
			{
				System.out.println(p);
			}
			
			System.out.println("Nouvel itineraire ? (yes/no)");
			choix = s.next();
		}
		
		System.out.println("Fin du programme..");
		
	}
	
	public static int choixDepart(Station stat)
	{
		int n; 
		System.out.println("Point de départ (numéro): ");
		n = s.nextInt();
		return n;
		
	}
	
	public static int choixArrivee(Station stat)
	{
		int n; 
		System.out.println("Point d'arrivée (numéro): ");
		n = s.nextInt();
		return n; 
	}
	
	public static String choixTemps()
	{
		String t;
		System.out.println("Choix du temps (abs/reel): ");
		t = s.next();
		System.out.println(t);
		return t; 
	}
}
