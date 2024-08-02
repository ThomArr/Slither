package tools;

import food.FoodModel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import snake.SnakeModel;
import snake.SnakeModel.Ball;

/**
 * Fournit des méthodes pour analyser et formater les données des modèles de
 * serpent et de nourriture.
 */
public class DataParser {

    /**
     * Analyse une chaîne de données au format spécifique et extrait les modèles de
     * serpents et de nourriture.
     * 
     * @param data La chaîne de données à analyser.
     * @return Une liste contenant deux éléments : la liste des modèles de serpents
     *         et la liste des modèles de nourriture.
     */
    public static List<Object> parse(String data) {
        List<SnakeModel> snakeModels = new ArrayList<>();
        List<FoodModel> foodModels = new ArrayList<>();

        String[] sections = data.split("\\|");

        // Analyse les modèles de serpents
        parseSnakes(snakeModels, sections[0]);

        // Analyse les modèles de nourriture, s'il y en a
        parseFood(foodModels, sections.length > 1 ? sections[1] : "");

        List<Object> result = new ArrayList<>();
        result.add(snakeModels);
        result.add(foodModels);

        return result;
    }

    /**
     * Analyse une section de données représentant les modèles de serpents.
     * 
     * @param snakeModels  La liste où les modèles de serpents seront ajoutés.
     * @param snakeSection La chaîne de données représentant les modèles de
     *                     serpents.
     */
    private static void parseSnakes(List<SnakeModel> snakeModels, String snakeSection) {
        if (snakeSection.equals(""))
            return;
        String[] snakeObjects = snakeSection.split(";");
        for (String snakeObject : snakeObjects) {
            StringTokenizer tokenizer = new StringTokenizer(snakeObject, ",");

            try {
                double coefMultSize = Double.parseDouble(tokenizer.nextToken());
                Color color = new Color(Integer.parseInt(tokenizer.nextToken()));
                Color borderColor = new Color(Integer.parseInt(tokenizer.nextToken()));

                List<SnakeModel.Ball> balls = new ArrayList<>();
                // Lire les positions des balles, s'il y en a
                while (tokenizer.hasMoreTokens()) {
                    if (tokenizer.countTokens() < 2) {
                        System.err.println("Erreur : données de balle incomplètes : " + snakeObject);
                        break;
                    }
                    double x = Double.parseDouble(tokenizer.nextToken());
                    double y = Double.parseDouble(tokenizer.nextToken());
                    balls.add(new SnakeModel.Ball(x, y));
                }

                snakeModels.add(new SnakeModel(coefMultSize, color, borderColor, balls));
            } catch (NumberFormatException e) {
                System.err.println("Erreur de format de nombre dans les données du serpent : " + snakeObject);
                e.printStackTrace();
            } catch (NoSuchElementException e) {
                // snakeSection vide.
            }
        }
    }

    /**
     * Analyse une section de données représentant les modèles de nourriture.
     * 
     * @param foodModels  La liste où les modèles de nourriture seront ajoutés.
     * @param foodSection La chaîne de données représentant les modèles de
     *                    nourriture.
     */
    private static void parseFood(List<FoodModel> foodModels, String foodSection) {
        if (foodSection.equals(""))
            return;
        String[] foodObjects = foodSection.split(";");
        for (String foodObject : foodObjects) {
            StringTokenizer tokenizer = new StringTokenizer(foodObject, ",");
            double x = Double.parseDouble(tokenizer.nextToken());
            double y = Double.parseDouble(tokenizer.nextToken());
            Color color = new Color(Integer.parseInt(tokenizer.nextToken()));
            int level = Integer.parseInt(tokenizer.nextToken());

            foodModels.add(new FoodModel(new Vector2D(x, y), color, level));
        }
    }

    /**
     * Formate les données des modèles de serpents et de nourritures pour la
     * diffusion.
     * 
     * @param snakeModels Liste des modèles de serpents.
     * @param foodModels  Liste des modèles de nourriture.
     * @return Les données formatées sous forme de chaîne.
     */
    public static String formatData(List<SnakeModel> snakeModels, List<FoodModel> foodModels) {
        StringBuilder formattedData = new StringBuilder();
        formattedData.append(DataParser.formatSnakeModels(snakeModels)).append("|")
                .append(DataParser.formatFoodModels(foodModels));
        return formattedData.toString();
    }

    /**
     * Formate les modèles de serpents en une chaîne de données au format
     * spécifique.
     * 
     * @param snakeModels La liste des modèles de serpents à formater.
     * @return La chaîne de données formatée représentant les modèles de serpents.
     */
    public static String formatSnakeModels(List<SnakeModel> snakeModels) {
        StringBuilder formattedSnakeModels = new StringBuilder();

        for (SnakeModel snake : snakeModels) {
            formattedSnakeModels.append(snake.getCoefMultSize()).append(",");
            formattedSnakeModels.append(snake.getColor().getRGB()).append(",");
            formattedSnakeModels.append(snake.getBorderColor().getRGB()).append(",");

            for (Ball ball : snake.getBalls()) {
                formattedSnakeModels.append(ball.getX()).append(",");
                formattedSnakeModels.append(ball.getY()).append(",");
            }

            formattedSnakeModels.setLength(formattedSnakeModels.length() - 1); // Supprime la dernière virgule
            formattedSnakeModels.append(";");
        }

        // Supprime le dernier point-virgule s'il est présent
        if (formattedSnakeModels.length() > 0
                && formattedSnakeModels.charAt(formattedSnakeModels.length() - 1) == ';') {
            formattedSnakeModels.setLength(formattedSnakeModels.length() - 1); // Supprime le dernier point-virgule
        }

        return formattedSnakeModels.toString();
    }

    /**
     * Formate les modèles de nourriture en une chaîne de données au format
     * spécifique.
     * 
     * @param foodModels La liste des modèles de nourriture à formater.
     * @return La chaîne de données formatée représentant les modèles de nourriture.
     */
    public static String formatFoodModels(List<FoodModel> foodModels) {
        StringBuilder formattedFoodModels = new StringBuilder();

        for (FoodModel food : foodModels) {
            formattedFoodModels.append(food.getX()).append(",");
            formattedFoodModels.append(food.getY()).append(",");
            formattedFoodModels.append(food.getColor().getRGB()).append(",");
            formattedFoodModels.append(food.getLevel()).append(";");
        }

        // Supprime le dernier point-virgule s'il est présent
        if (formattedFoodModels.length() > 0 && formattedFoodModels.charAt(formattedFoodModels.length() - 1) == ';') {
            formattedFoodModels.setLength(formattedFoodModels.length() - 1); // Supprime le dernier point-virgule
        }

        return formattedFoodModels.toString();
    }
}
