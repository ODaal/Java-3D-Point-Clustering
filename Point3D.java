// Othmane Daali 300260580

import java.lang.Math;

public class Point3D {

	public int label = 0; // Etiquette
	private String[] rgb; // Couleur rgb
	
	private double x; // Coordonnées x
	private double y; // Coordonnées y
	private double z; // Coordonnées z


	public Point3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		rgb = new String[3]; // Initiailse la taille de rgb à 3
	}

    // Retourne coordonnée x
	public double getX(){
		return this.x;
	}

    // Retourne coordonnée y 
	public double getY(){
		return this.y;
	}

	// Retourne coordonnée z 
	public double getZ(){
		return this.z;
	}

    // Calcule la distance euclidienne entre les deux points
	public double distance(Point3D pt){
		return Math.sqrt(Math.pow(this.x-pt.getX(),2)+Math.pow(this.y-pt.getY(),2)+Math.pow(this.z-pt.getZ(),2));
	}
    
    //Définit le nombre de chaque élement de rgb en tant que string
	public void setRgb(double r, double g, double b){
        rgb[0] = Double.toString(r);
        rgb[1] = Double.toString(g);
        rgb[2] = Double.toString(b);
	}
	

    //Retourne l'élement r de rgb
	public String getR(){
		return rgb[0];
	}
	//Retourne l'élement g de rgb
	public String getG(){
		return rgb[1];
	}
	//Retourne l'élement b de rgb
	public String getB(){
		return rgb[2];
	}

}