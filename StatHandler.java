package parseur;

import solveur.*;
import solveur.Descente.TypeDescente;
import solveur.Navette.TypeNavette;
import solveur.Remontee.TypeRemontee;

import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


public class StatHandler implements ContentHandler {
  String typeCourant; // Type de l'element en cours de reconnaissance
  
  
  Point point, depart, arrivee;
  Descente piste;
  Remontee remontee;
  Navette navette;
  
  int numero, num_dep, num_arr;
  double x, y, altitude, tpsFixe, tpsDeniv, tpsTrajet;
  
  String nom, type;
  Boolean pointCoord = false;
  

 
  
  //liste des points que l'on mettra par la suite dans la station
  List<Point> dicPoint = new ArrayList<>();
  Point nul = new Point(0, "nul", 0.);
  
  //liste des transitions que l'on mettra par la suite dans la station
  List<Transition> dicTransition = new ArrayList<>();
  Transition nulle = new Transition(0, "nulle", nul, nul);
 

  public void startDocument() throws SAXException {
    System.out.println("start document...");
  }

  public void endDocument() throws SAXException {
    System.out.println("\nDocument termine.");
  }

  public List<Point> getPoints()
  {
	  return dicPoint;
  }
  
  public List<Transition> getTransitions()
  {
	  return dicTransition;
  }
  
  
  
  
  // Balise ouvrante d'element : name dans localName
  public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) throws SAXException {
	  switch(localName) {
	  case "point":
		  //System.out.println("point ok");
		  break;
	  case "navette":
		  //System.out.println("navette ok");
		  break;
	  case "piste":
		  //System.out.println("descente ok");
		  break;
	  case "remontee":
		  //System.out.println("remontee ok");
		  break;
	  default : break;
	  }
  typeCourant=localName;
  }

 
  
  
  
  // Contenu de l'element courant...
  public void characters(char[] ch, int start, int length) throws SAXException 
  {
    if (typeCourant != null) 
    {
		String contenu = new String (ch,start,length);
	  
	    switch(typeCourant) 
	    {
	    case "numero":
	    	numero = Integer.parseInt(contenu);
	    	break;
	    
	    case "altitude":
	    	altitude = Double.parseDouble(contenu);
	    	break;
	    
	    case "nom":
	    	nom = contenu;
	    	break;
	    
	    case "x" :
	    	x = Double.parseDouble(contenu);
	    	pointCoord = true;
	    	break;
	    
	    case "y" :
	    	y = Double.parseDouble(contenu);
	    	break;
	    	
	    case "depart":
	    	num_dep = Integer.parseInt(contenu);
	    	break;
	    
	    case "arrivee":
	    	num_arr = Integer.parseInt(contenu);
	    	break;
	    
	    case "tpsFixe":
	    	tpsFixe = Double.parseDouble(contenu);
	    	break;
	    
	    case "tpsDenivele":
	    	tpsDeniv = Double.parseDouble(contenu);
	    	break;
	    	
	    case "tpsTrajet":
	    	tpsTrajet = Double.parseDouble(contenu);
	    	break;
	    
	    case "type":
	    	type = contenu;
	    	
	    default : break;
	    }

    }
  }
  
  
  // Balise fermante : name dans localName
  public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
    if (localName.equals("point")) {
    	
    	// obligé de mettre ici car on ne peut pas ajouter dans la liste avant 
    	// on evite ainsi les problèmes avec les indices 0 ...
    	if(numero == 1)
    	{
        	dicPoint.add(0, nul);
        	dicTransition.add(0, nulle);
    	}
    	
    	if((x != 0) && (y != 0))
    	{
    		point = new PointCoord(numero, nom, altitude, x, y);
    		x = 0;
    		y = 0;
    	}
    	else
    	{
    		point = new Point(numero, nom, altitude);
    	}
    	   	
    	dicPoint.add(point);
    }
    // Remarque: les points sont enregistrés en premier par le parser donc
    // on pourra manipuler la liste de points quand il faudra sauvegarder les transitions
    else if (localName.equals("remontee")) {
    	remontee = new Remontee(numero, nom, dicPoint.get(num_dep), dicPoint.get(num_arr), TypeRemontee.valueOf(type), tpsDeniv, tpsFixe);
    	dicTransition.add(remontee);
    }
    else if (localName.equals("piste")) {
    	piste = new Descente(numero, nom, dicPoint.get(num_dep), dicPoint.get(num_arr), TypeDescente.valueOf(type), tpsDeniv);
    	dicTransition.add(piste);
    }
    else if (localName.equals("navette")) {
    	navette = new Navette(numero, nom, dicPoint.get(num_dep), dicPoint.get(num_arr), TypeNavette.valueOf(type), tpsTrajet);
    	dicTransition.add(navette);
    }
  
     
  typeCourant=null;
  }

  // Sur le reste : NOP
  public void startPrefixMapping(String prefix, String uri) {}
  public void endPrefixMapping(String prefix) {}
  public void ignorableWhitespace(char [] ch, int start, int length) throws SAXException {}
  public void processingInstruction(String target, String data) throws SAXException {}
  public void skippedEntity(String name) throws SAXException {}
  public void setDocumentLocator(Locator locator) {}
}










