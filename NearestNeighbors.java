// Othmane Daali 300260580

import java.util.*;

public class NearestNeighbors{

	private List<Point3D> list; // Liste de tous les points
	public List<Point3D> neighbors; // Liste des voisins


    //Constructeur
	public NearestNeighbors(List<Point3D> points, Point3D q, double eps){
		this.list = points;
		this.neighbors = rangeQuery(eps,q);
	}
    
    //Retourne la liste des voisins
	public List<Point3D> rangeQuery(double eps, Point3D q){
		List<Point3D> n = new ArrayList();
		for(Point3D point : list){ // Traite les points de la liste
			if(q.distance(point)<= eps){ // Vérifie la distance
				n.add(point); // Ajoute le résultat
			}
		}
		return n;
	}
	
    //Retourne la taille de la liste de voisins
	public double getSize(){
		return neighbors.size();
	}
}