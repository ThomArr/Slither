package main;

import food.FoodModel;
import java.awt.*;
import java.util.List;
import java.util.ListIterator;
import javax.swing.*;
import snake.*;

/**
 * Vue principale du jeu, responsable du rendu graphique des serpents et de la nourriture.
 */
public class MainView extends JPanel {
    private final MainModel mainModel;

    /**
     * Constructeur pour créer une nouvelle vue du jeu.
     *
     * @param mainModel Le modèle principal du jeu.
     */
    public MainView(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (SnakeModel model : mainModel.getSnakeModels()) {
            List<SnakeModel.Ball> balls = model.getBalls();
            ListIterator<SnakeModel.Ball> iterator = balls.listIterator(balls.size());

            while (iterator.hasPrevious()) {
                SnakeModel.Ball ball = iterator.previous();
                double diametreSnake = model.getDiametre();
                double rayonSnake = diametreSnake / 2;
                g.setColor(model.getColor());
                g.fillOval((int) (ball.getX() - rayonSnake), (int) (ball.getY() - rayonSnake), (int) (diametreSnake),
                        (int) (diametreSnake));
                g.setColor(model.getBorderColor());
                g.drawOval((int) (ball.getX() - rayonSnake), (int) (ball.getY() - rayonSnake), (int) (diametreSnake),
                        (int) (diametreSnake));
            }
        }

        for (FoodModel food : mainModel.getFoodModels()) {
            double diametreFood = food.getDiametre();
            double rayonFood = diametreFood / 2;
            g.setColor(food.getColor());
            g.fillOval((int) (food.getX() - rayonFood), (int) (food.getY() - rayonFood), (int) (diametreFood),
                    (int) (diametreFood));
        }
    }

    /**
     * Met à jour la vue en redessinant les composants.
     */
    public void updateView() {
        repaint();
        revalidate();
    }
}
