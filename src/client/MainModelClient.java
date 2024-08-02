package client;

import food.FoodModel;
import java.util.ArrayList;
import java.util.List;
import snake.SnakeModel;

/**
 * Modèle principal du client, contenant les données des serpents et de la nourriture.
 */
public class MainModelClient {
    private List<SnakeModel> snakeModels;
    private List<FoodModel> foodModels;

    /**
     * Crée une instance de MainModelClient avec des listes vides pour les serpents et la nourriture.
     */
    public MainModelClient() {
        this.snakeModels = new ArrayList<>();
        this.foodModels = new ArrayList<>();
    }

    /**
     * Obtient la liste des modèles de serpents.
     * @return La liste des modèles de serpents.
     */
    public List<SnakeModel> getSnakeModels() {
        return snakeModels;
    }

    /**
     * Définit la liste des modèles de serpents.
     * @param snakeModels La liste des modèles de serpents à définir.
     */
    public void setSnakeModels(List<SnakeModel> snakeModels) {
        this.snakeModels = snakeModels;
    }

    /**
     * Obtient la liste des modèles de nourriture.
     * @return La liste des modèles de nourriture.
     */
    public List<FoodModel> getFoodModels() {
        return foodModels;
    }

    /**
     * Définit la liste des modèles de nourriture.
     * @param foodModels La liste des modèles de nourriture à définir.
     */
    public void setFoodModels(List<FoodModel> foodModels) {
        this.foodModels = foodModels;
    }
}
