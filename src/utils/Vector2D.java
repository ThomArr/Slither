package utils;

/**
 * Représente un vecteur dans un espace 2D avec des coordonnées x et y.
 */
public class Vector2D {
    public double x;
    public double y;

    /**
     * Crée un vecteur avec les coordonnées spécifiées.
     * 
     * @param x La coordonnée x du vecteur.
     * @param y La coordonnée y du vecteur.
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Définit les coordonnées du vecteur en utilisant un autre vecteur.
     * 
     * @param other Le vecteur dont les coordonnées seront copiées.
     */
    public void setCoordinates(Vector2D other) {
        this.x = other.x;
        this.y = other.y;
    }

    /**
     * Crée une copie du vecteur actuel.
     * 
     * @return Une nouvelle instance de Vector2D avec les mêmes coordonnées.
     */
    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    /**
     * Calcule la norme (longueur) du vecteur.
     * 
     * @return La norme du vecteur.
     */
    public double getNorme() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Multiplie les coordonnées du vecteur par un scalaire.
     * 
     * @param n Le facteur de multiplication.
     */
    public void multVector(double n) {
        this.x *= n;
        this.y *= n;
    }

    /**
     * Ajoute un autre vecteur à ce vecteur.
     * 
     * @param other Le vecteur à ajouter.
     */
    public void addVector(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    /**
     * Soustrait un autre vecteur de ce vecteur.
     * 
     * @param other Le vecteur à soustraire.
     */
    public void subVector(Vector2D other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    /**
     * Méthode pour normaliser le vecteur.
     */
    public void normalize() {
        double norme = getNorme();
        x = x / norme;
        y = y / norme;
    }

    /**
     * Vérifie si ce vecteur est égal à un autre objet.
     * 
     * @param obj L'objet à comparer avec ce vecteur.
     * @return true si l'objet est une instance de Vector2D et a les mêmes
     *         coordonnées, sinon false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector2D vector) {
            return this.x == vector.x && this.y == vector.y;
        }
        return false;
    }
}
