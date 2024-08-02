package main;

import food.FoodModel;
import java.awt.Dimension;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import snake.SnakeModel;

/**
 * Modèle principal du jeu, responsable de la gestion des serpents et de la nourriture.
 */
public class MainModel {
    public static Random rand = new Random();

    private List<SnakeModel> snakeModels;
    private List<FoodModel> foodModels;
    private Dimension dimension;

    /**
     * Constructeur pour créer le modèle principal avec les dimensions spécifiées.
     * 
     * @param dimension Les dimensions du cadre de jeu.
     */
    public MainModel(Dimension dimension) {
        this.dimension = dimension;
        this.snakeModels = new CopyOnWriteArrayList<>();
        this.foodModels = new CopyOnWriteArrayList<>();
    }

    /**
     * Retourne la liste des modèles de serpents.
     * 
     * @return La liste des modèles de serpents.
     */
    public List<SnakeModel> getSnakeModels() {
        return snakeModels;
    }

    /**
     * Retourne la liste des modèles de nourriture.
     * 
     * @return La liste des modèles de nourriture.
     */
    public List<FoodModel> getFoodModels() {
        return foodModels;
    }

    /**
     * Retourne les dimensions du cadre de jeu.
     * 
     * @return Les dimensions du cadre de jeu.
     */
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * Ajoute un modèle de serpent à la liste des serpents.
     * 
     * @param snakeModel Le modèle de serpent à ajouter.
     */
    public void addSnakeModel(SnakeModel snakeModel) {
        snakeModels.add(snakeModel);
    }

    /**
     * Ajoute un modèle de nourriture à la liste de la nourriture.
     * 
     * @param foodModel Le modèle de nourriture à ajouter.
     */
    public void addFoodModel(FoodModel foodModel) {
        foodModels.add(foodModel);
    }
}
