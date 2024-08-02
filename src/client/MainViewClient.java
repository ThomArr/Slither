package client;

import food.FoodModel;
import java.awt.*;
import java.util.List;
import java.util.ListIterator;
import javax.swing.*;
import snake.SnakeModel;

/**
 * Vue principale du client, responsable de l'affichage des serpents et de la
 * nourriture sur le panneau.
 */
public class MainViewClient extends JPanel {
    private final MainModelClient mainModel;

    /**
     * Crée une instance de MainViewClient avec le modèle principal fourni.
     * 
     * @param mainModel Le modèle principal contenant les données à afficher.
     */
    public MainViewClient(MainModelClient mainModel) {
        this.mainModel = mainModel;
    }

    /**
     * Dessine les serpents et la nourritures.
     * 
     * @param g L'objet Graphics utilisé pour dessiner.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSnakes(g);
        drawFood(g);
    }

    /**
     * Dessine les serpents sur le panneau.
     * 
     * @param g L'objet Graphics utilisé pour dessiner sur le panneau.
     */
    private void drawSnakes(Graphics g) {
        for (SnakeModel model : mainModel.getSnakeModels()) {
            List<SnakeModel.Ball> balls = model.getBalls();
            ListIterator<SnakeModel.Ball> iterator = balls.listIterator(balls.size());

            while (iterator.hasPrevious()) {
                SnakeModel.Ball ball = iterator.previous();
                double diametreSnake = model.getDiametre();
                double rayonSnake = diametreSnake / 2;
                g.setColor(model.getColor());
                g.fillOval((int) (ball.getX() - rayonSnake), (int) (ball.getY() - rayonSnake), (int) diametreSnake,
                        (int) diametreSnake);
                g.setColor(model.getBorderColor());
                g.drawOval((int) (ball.getX() - rayonSnake), (int) (ball.getY() - rayonSnake), (int) diametreSnake,
                        (int) diametreSnake);
            }
        }
    }

    /**
     * Dessine la nourriture sur le panneau.
     * 
     * @param g L'objet Graphics utilisé pour dessiner sur le panneau.
     */
    private void drawFood(Graphics g) {
        for (FoodModel food : mainModel.getFoodModels()) {
            double diametreFood = food.getDiametre();
            double rayonFood = diametreFood / 2;
            g.setColor(food.getColor());
            g.fillOval((int) (food.getX() - rayonFood), (int) (food.getY() - rayonFood), (int) diametreFood,
                    (int) diametreFood);
        }
    }

    /**
     * Met à jour la vue en redessinant le panneau.
     */
    public void updateView() {
        repaint();
        revalidate();
    }
}
