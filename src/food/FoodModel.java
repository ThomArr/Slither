package food;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;
import utils.ColorTools;
import utils.Vector2D;

/**
 * Représente un modèle de nourriture dans le jeu.
 */
public class FoodModel {
    private final Random rand = new Random ();
    private final Vector2D currentCoord;
    private final int level;
    private final Color color;                     // affichage.

    /**
     * Constructeur pour créer une instance de FoodModel avec des coordonnées
     * spécifiques, une couleur et un niveau.
     * 
     * @param x     La coordonnée x de la nourriture.
     * @param y     La coordonnée y de la nourriture.
     * @param color La couleur de la nourriture.
     * @param level Le niveau de la nourriture.
     */
    public FoodModel(double x, double y, Color color, int level) {
        this.currentCoord = new Vector2D(x, y);
        this.level = level;
        this.color = color;
    }

    /**
     * Constructeur pour créer une instance de FoodModel avec des coordonnées
     * aléatoires dans les dimensions du cadre de jeu,
     * une couleur aléatoire et un niveau aléatoire.
     * 
     * @param frameDimension Les dimensions du cadre de jeu.
     */
    public FoodModel(Dimension frameDimension) {
        this.currentCoord = new Vector2D((double) rand.nextInt(frameDimension.width),
                (double) rand.nextInt(frameDimension.height));
        this.color = ColorTools.generateRandomColor();
        this.level = rand.nextInt(3) + 1; // Le niveau est aléatoire entre 1 et 3
    }

    /**
     * Retourne le diamètre actuel de la nourriture pour la vue.
     * 
     * @return Diamètre du serpent.
     */
    public double getDiametre() {
        return 6 * getLevel();
    }

    // Getters

    public double getX() {
        return currentCoord.x;
    }

    public double getY() {
        return currentCoord.y;
    }

    public int getLevel() {
        return level;
    }

    public Color getColor() {
        return color;
    }
}
