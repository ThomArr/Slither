package main;

import food.FoodModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import snake.*;
import utils.ColorTools;

/**
 * Classe principale du jeu.
 */
public class Main {
    private static MainModel mainModel;
    private static MainView mainView;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dimension dimension = new Dimension(1600, 1200);

            mainModel = new MainModel(dimension);

            for (int i = 0; i < 9; i++) {
                Color color = ColorTools.generateRandomColor();
                Color borderColor = ColorTools.getDarkerColor(color);
                SnakeModel snakeModel = new SnakeModel(15, MainModel.rand.nextInt(dimension.width), MainModel.rand.nextInt(dimension.height),
                        color, borderColor, true, dimension);
                mainModel.addSnakeModel(snakeModel);
            }

            final SnakeModel snakeModel = new SnakeModel(15, MainModel.rand.nextInt(dimension.width), MainModel.rand.nextInt(dimension.height),
                    Color.blue, ColorTools.getDarkerColor(Color.blue), false, dimension);
            mainModel.addSnakeModel(snakeModel);

            for (int i = 0; i < 1000; i++) {
                mainModel.addFoodModel((new FoodModel(dimension)));
            }

            mainView = new MainView(mainModel);

            mainView.updateView();
            
            mainView.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    snakeModel.setTargetCoord(e.getX(), e.getY());
                }
            });

            JFrame frame = new JFrame("Slither.io");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1600, 1200);
            frame.add(mainView);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);

            Timer timer = new Timer(10, e -> updateSnakePositions());
            timer.start();
        });
    }

    /**
     * Met à jour les positions des serpents et gère les collisions avec les autres
     * serpents et les nourritures.
     */
    private static void updateSnakePositions() {
        List<SnakeModel> snakeModels = mainModel.getSnakeModels();
        List<FoodModel> foodModels = mainModel.getFoodModels();

        for (int i = 0; i < snakeModels.size(); i++) {
            SnakeModel model = snakeModels.get(i);
            if (model.isBot()) {
                model.generateBotMove();
            }

            // Vérifie les collisions avec les autres serpents.
            for (int j = 0; j < snakeModels.size(); j++) {
                SnakeModel snakeModel = snakeModels.get(j);
                if (model.equals(snakeModel))
                    continue;

                if (model.isDead(snakeModel)) {

                    snakeModels.remove(model);

                    // Ajoute la nourriture à la position du serpent mort.
                    for (SnakeModel.Ball ball : model.getBalls()) {
                        foodModels.add(new FoodModel(ball.getX() + (MainModel.rand.nextInt(2) == 0 ? -1 : 1) *
                                MainModel.rand.nextInt((int) model.getDiametre()),
                                ball.getY() + (MainModel.rand.nextInt(2) == 0 ? -1 : 1)
                                        * MainModel.rand.nextInt((int) model.getDiametre()),
                                model.getColor(),
                                MainModel.rand.nextInt(3) + 1));
                    }
                    i--;
                    break;
                }
            }

            // Vérifie les collisions avec la nourriture.
            for (int k = 0; k < foodModels.size(); k++) {
                FoodModel foodModel = foodModels.get(k);
                if (model.canEat(foodModel)) {
                    foodModels.remove(k);
                    foodModels.add(k, new FoodModel(mainModel.getDimension()));
                    model.addLevels(foodModel.getLevel());
                    k--;
                }
            }

            model.updatePosition();
        }
        mainView.updateView();
    }
}