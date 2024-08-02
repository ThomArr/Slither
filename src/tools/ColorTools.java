package tools;

import java.awt.Color;
import main.MainModel;
import server.MainServer;

/**
 * Classe utilitaire pour les opérations sur les couleurs.
 */
public class ColorTools {
    /**
     * Génère une couleur aléatoire.
     * 
     * @return une instance de {@link Color} représentant une couleur aléatoire.
     */
    public static Color generateRandomColor() {
        int red = MainServer.rand.nextInt(256);
        int green = MainServer.rand.nextInt(256);
        int blue = MainServer.rand.nextInt(256);
        return new Color(red, green, blue);
    }

    /**
     * Renvoie une couleur plus foncée basée sur la couleur donnée.
     * 
     * @param color la couleur de base.
     * @return une nouvelle instance de {@link Color} qui est plus foncée que la
     *         couleur de base.
     */
    public static Color getDarkerColor(Color color) {
        int red = (int) (color.getRed() * 0.8);
        int green = (int) (color.getGreen() * 0.8);
        int blue = (int) (color.getBlue() * 0.8);
        return new Color(red, green, blue);
    }
}