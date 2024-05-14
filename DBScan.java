// Othmane Daali 300260580


import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.lang.Math;

public class DBScan {

	private static List<Point3D> points; //Liste contenant l'ensemble des points
	private static Stack<Point3D> s; //pile pour pronlonger la liste des voisins 
	private static double eps;  
	private static int minPts;
	private static int numberOfClusters;

    // Constructeur 
	public DBScan(List<Point3D> list){ 
		this.points = list;
	}

    // Fonction main
	public static void main(String args[]){
    	points = read(args[0]); // Sauvegarde les points du fichier dans la liste points
    	setEps(Double.parseDouble(args[1])); // Définie epsilone la distance minimal entre deux points pour appartenir au même groupe
    	setMinPts(Integer.parseInt(args[2])); // Définie le nombre de point minimal pour définir un groupe de point
    	findClusters(); //Regroupe les points et définit à chaque point son étiquette
    	setRgb(); //Définie la couleur de chaque point selon son étiquette
    	StringBuffer sb = new StringBuffer(args[0]);
        sb.delete(args[0].length()-4,args[0].length()); // Enléve le .csv du nom de fichier original pour reformuler son nom
        // Reformulation du nom du fichier
        String savefilename = sb.toString() + "_" + "Clusters"+ "_" +Double.toString(eps) + "_" + Integer.toString(minPts) + "_" + Integer.toString(numberOfClusters) + ".csv";
		save(savefilename); // Sauvegarde les points à etiquettes dans le fichier de sortie
		printClusters(); // Affiche la taille de chaque groupe trouver du plus grand au plus petit + les points noises

    } 


	public static void findClusters(){
        numberOfClusters = 0; // compteur de groupes
        s = new Stack(); //pile pour pronlonger la liste des voisins 
        for(Point3D point : points){
        	if(point.label != 0){ // Déja définit
        		continue;
        	}
        	NearestNeighbors n = new NearestNeighbors(points,point,eps); // Crée la liste des voisins
        	if(n.getSize() < minPts){ // Nombre insuffisant de point
        		point.label = -1; // Etiquette Noise
        		continue;
        	}
        	numberOfClusters++; // Prochain groupe de points
        	point.label = numberOfClusters; // Etiquette du point
        	for(Point3D p : n.neighbors){ // Ajoute les points de la liste des voisins à une pile
        		s.push(p);
        	}
        	while(!s.empty()){
        		Point3D q = s.pop(); // point à traiter
        		if(q.label == -1){ 
        			q.label = numberOfClusters; // Point à étiquette noise devient élément du groupe 
        		}
        		if(q.label != 0){ // Déja définit
        			continue; 
        		}
        		q.label = numberOfClusters; // Définit l'étiquette de la liste des voisins au point
        		NearestNeighbors n2 = new NearestNeighbors(points,q,eps); // Créer une autre liste de voisin du point traiter
        		if(n2.getSize() >= minPts){ // Ajoute la liste à la pile
        		    for(Point3D p2 : n2.neighbors){
        		        s.push(p2);
        	        }	
        		}
        	}

        }
	}

    // Définit et retourne epsilone
	public static double setEps(double eps1){ 
		eps = eps1;
		return eps;
	}

    // Définit et retourne minPts
	public static int setMinPts(int minPts1){
		minPts = minPts1;
		return minPts;
	}

    // Retourne le nombre de groupe
	public int getNumberOfClusters(){
		return this.numberOfClusters;
	}

    // Retourne la liste de points
	public List<Point3D> getPoints(){
		return this.points;
	}

    // Définit la couleur des points
    public static void setRgb(){
    	// Génere des nombres aléatoires entre 0 et 1 pour chaque groupe de points
        Random rand = new Random();
        double[][] rgb = new double[numberOfClusters+1][3];
        for(int i=1; i<=numberOfClusters;i++){
        	rgb[i][0] = rand.nextDouble();
        	rgb[i][1] = rand.nextDouble();
        	rgb[i][2] = rand.nextDouble();
        }
        // Traverse chaque points de la liste et lui assigne sa couleur
        for(Point3D p : points){
        	if(p.label == -1){
        		p.setRgb(0.52,0.52,0.52); // Couleur des points Noise
        	}else{
        		p.setRgb(rgb[p.label][0],rgb[p.label][1],rgb[p.label][2]); // Définit la couleut du point
        	}
        }
    }

    // Retourne la liste des points que contier le fichier
	public static List<Point3D> read(String filename){
		List<Point3D> filepoints = new ArrayList(); // Liste à retourner des points du fichier
		String line;
		try{
		   BufferedReader br = new BufferedReader(new FileReader(filename)); // Ouvre le fichier pour lecture
		   br.readLine(); // Saute la première ligne
		   while((line = br.readLine()) != null){ // Traite chaque ligne du fichier
			    String[] coordinates = line.split(","); // Départage les coordonnées du point
			    // Créer un point avec les coordonnés correspendants
			    filepoints.add(new Point3D(Double.parseDouble(coordinates[0]),Double.parseDouble(coordinates[1]),Double.parseDouble(coordinates[2])));
		    }
		    br.close(); // Ferme le fichier
		}catch(IOException e){ // En cas d'erreur
			System.out.println(e);
		}

		return filepoints; // Retourne la liste de fichier

	}

	
    // Sauvegarde les points traiter dans un fichier csv
	public static void save(String filename){
		try{
			File file = new File(filename);
			if(!file.exists()){ // Crée le fichier s'il n'existe pas
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw); // Ouvre le fichier pour ecrtiture
			bw.write("x,y,z,C,R,G,B"); // Ecrit la première ligne
			for(Point3D p : points){ // Ecrit les coordonnées de chaque points dans le fichier
				bw.newLine(); // Saute à la prochaine ligne
				// Ecrit les coordonnées des points + l'étiquette + la couleur 
				String s = Double.toString(p.getX()) + "," + Double.toString(p.getY()) + "," + Double.toString(p.getX()) + "," + Integer.toString(p.label) + "," ;
	
				s = s + p.getR() + "," + p.getG() + "," + p.getB();
				bw.write(s);
			}
			bw.close();// Ferme le fichier
		}catch(IOException e){ // En cas d'erreur
			System.out.println(e);
		}
	}
    

    // Affiche la taille de chaque groupe du plus grand au plus petit + taille des points noise
	public static void printClusters(){
		int[] display = new int[numberOfClusters+1]; // Tableau contenant la taille de chaque groupe
		int noise = 0; //Nombre de point nois 
		for(int i=0;i<=numberOfClusters;i++){
			display[i] = 0; // Initialise la taille de chaque groupe
		}
		for(Point3D p : points){ 
			if(p.label == -1){ // Incrémente noise si c'est un point noise
                noise++;
				continue;
			}
            display[p.label]++; // Incrémentle l'element du tableau du groupe
		}
		display = bubble(display); // Trie le tableau du plus petit au plus grand
		for(int j=numberOfClusters;j>=1;j--){ // Affiche les éléments du tableau du plus grand au plus petit
			System.out.println(display[j]);
		}
		System.out.println(noise); // Affiche le nombre de point noise
	}
    

    // Algorithme de trie à bule
	public static int[] bubble(int[] data) {
        for (int m = 0; m<data.length-1; m++) {
            for (int i = data.length-1; i>m; i--) {
                if (data[i] < data[i-1]) {
                    int temp = data[i];
                    data[i] = data[i-1];
                    data[i-1] = temp; 
                }
            }
        }
        return data;
    }


}