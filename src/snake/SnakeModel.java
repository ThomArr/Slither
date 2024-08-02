package snake;

import food.FoodModel;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import server.MainServer;
import tools.Vector2D;

/**
 * Modèle représentant un serpent dans le jeu.
 */
public class SnakeModel {
    private static int counter = 0;
    private int id;
    private Color color;
    private Color borderColor;
    private List<Ball> balls;
    private double speed;
    private boolean isDead;
    private Dimension frameDimension;
    private int level;
    private boolean bot;
    private Vector2D botTargetCoord;

    /**
     * Constructeur pour créer un nouveau serpent.
     * Utile pour le serveur.
     * 
     * @param initialSize    Nombre d'anneaux initiale du serpent.
     * @param startX         Coordonnée X de départ.
     * @param startY         Coordonnée Y de départ.
     * @param color          Couleur du serpent.
     * @param borderColor    Couleur de la bordure du serpent.
     * @param bot            Indique si le serpent est contrôlé par un bot.
     * @param frameDimension Dimensions du cadre de jeu.
     */
    public SnakeModel(int initialSize, double startX, double startY, Color color, Color borderColor,
            boolean bot, Dimension frameDimension) {
        this.id = counter++;
        this.color = color;
        this.borderColor = borderColor;
        this.speed = 1;
        this.isDead = false;
        this.frameDimension = frameDimension;
        this.level = 0;

        this.bot = bot;
        if (bot) {
            int botX = MainServer.rand.nextInt(2);
            if (botX == 0) {
                botX = MainServer.rand.nextInt(frameDimension.width) + 2 * frameDimension.width;
            } else {
                botX = -MainServer.rand.nextInt(frameDimension.width) - frameDimension.width;

            }
            int botY = MainServer.rand.nextInt(2);
            if (botY == 0) {
                botY = MainServer.rand.nextInt(frameDimension.height) + 2 * frameDimension.height;
            } else {
                botY = -MainServer.rand.nextInt(frameDimension.height) - frameDimension.height;

            }
            botTargetCoord = new Vector2D(botX, botY);
        }

        this.balls = new ArrayList<>();
        Ball head = new Ball(startX, startY);
        balls.add(head);

        for (int i = 1; i < initialSize; i++) {
            balls.add(new Ball(startX, startY));
        }

        initFirstDirection();
    }

    /**
     * Constructeur pour créer un nouveau serpent.
     * Utile pour le client.
     * 
     * @param initialSize    Nombre d'anneaux initiale du serpent.
     * @param startX         Coordonnée X de départ.
     * @param startY         Coordonnée Y de départ.
     * @param color          Couleur du serpent.
     * @param borderColor    Couleur de la bordure du serpent.
     * @param bot            Indique si le serpent est contrôlé par un bot.
     * @param frameDimension Dimensions du cadre de jeu.
     */
    public SnakeModel(int level, Color color, Color borderColor, List<Ball> balls) {
        this.level = level;
        this.color = color;
        this.borderColor = borderColor;
        this.balls = balls;
    }

    /**
     * Initialise la direction de la tête du serpent en début de partie.
     */
    private void initFirstDirection() {
        Ball head = balls.get(0);
        head.setDirectorVector(speed, MainServer.rand.nextInt(frameDimension.width),
                MainServer.rand.nextInt(frameDimension.height));
    }

    /**
     * Définit la coordonnée cible pour le serpent.
     * 
     * @param targetX Coordonnée X cible.
     * @param targetY Coordonnée Y cible.
     */
    public void setTargetCoord(double targetX, double targetY) {
        Ball head = balls.get(0);
        head.setDirectorVector(speed, targetX - head.currentCoord.x, targetY - head.currentCoord.y);
    }

    /**
     * Génère un mouvement pour le bot en ajustant sa cible.
     */
    public void generateBotMove() {
        botTargetCoord.addVector(new Vector2D(
                (MainServer.rand.nextInt(2) == 0 ? -1 : 1) * MainServer.rand.nextInt(frameDimension.width / 5),
                (MainServer.rand.nextInt(2) == 0 ? -1 : 1) * MainServer.rand.nextInt(frameDimension.height / 5)));

        if (3 * frameDimension.width < botTargetCoord.x) {
            botTargetCoord.subVector(new Vector2D(frameDimension.width / 5, 0));
        } else {
            if (botTargetCoord.x < -2 * frameDimension.width) {
                botTargetCoord.addVector(new Vector2D(frameDimension.width / 5, 0));
            }
        }

        if (3 * frameDimension.height < botTargetCoord.y) {
            botTargetCoord.subVector(new Vector2D(0, frameDimension.height / 5));
        } else {
            if (botTargetCoord.y < -2 * frameDimension.height) {
                botTargetCoord.addVector(new Vector2D(0, frameDimension.height / 5));
            }
        }

        if (-frameDimension.width <= botTargetCoord.x && botTargetCoord.x <= 2 * frameDimension.width
                && -frameDimension.height <= botTargetCoord.y && botTargetCoord.y <= 2 * frameDimension.height) {

            if (-frameDimension.width <= botTargetCoord.x && botTargetCoord.x < 0) {
                botTargetCoord.subVector(new Vector2D(frameDimension.width / 5, 0));
            }
            if (frameDimension.width <= botTargetCoord.x && botTargetCoord.x < 2 * frameDimension.width) {
                botTargetCoord.addVector(new Vector2D(frameDimension.width / 5, 0));
            }

            if (-frameDimension.height <= botTargetCoord.y && botTargetCoord.y < 0) {
                botTargetCoord.subVector(new Vector2D(0, frameDimension.height / 5));
            }
            if (frameDimension.height <= botTargetCoord.y && botTargetCoord.y < 2 * frameDimension.height) {
                botTargetCoord.addVector(new Vector2D(0, frameDimension.height / 5));
            }
        }

        Ball head = balls.get(0);
        head.setDirectorVector(speed, botTargetCoord.x, botTargetCoord.y);
    }

    /**
     * Ajoute des niveaux au serpent.
     * 
     * @param n Nombre de niveaux à ajouter.
     */
    public void addLevels(int n) {
        if (level <= 300) {
            if (level % 20 == 0) {
                for (int i = 0; i < n; i++) {
                    Ball tail = balls.get(balls.size() - 1);
                    balls.add(new Ball(tail.getX(), tail.getY()));
                }
            }
            level++;
        }
    }

    /**
     * Met à jour la position du serpent.
     */
    public void updatePosition() {
        Ball head = balls.get(0);
        head.updatePosition(this);

        for (int i = 1; i < balls.size(); i++) {
            Ball current = balls.get(i);
            Ball previous = balls.get(i - 1);
            current.follow(previous, getDiametre());
        }
    }

    /**
     * Retourne le diamètre actuel du serpent pour la vue.
     * 
     * @return Diamètre du serpent.
     */
    public double getDiametre() {
        return (double) 20 + ((double) level / (double) 3);
    }

    /**
     * Vérifie si le serpent est mort suite à une collision avec un autre serpent.
     * 
     * @param other L'autre serpent à vérifier pour une collision.
     * @return true si le serpent est mort, sinon false.
     */
    public boolean isDead(SnakeModel other) {
        if (isDead) {
            return true;
        }

        Ball head = balls.get(0);
        double diametreSnake = this.getDiametre();
        double rayonSnake = diametreSnake / 2;
        double diametreOtherSnake = other.getDiametre();
        double rayonOtherSnake = diametreOtherSnake / 2;
        for (Ball ball : other.getBalls()) {
            if (Math.sqrt(
                    Math.pow(head.getX() - ball.getX(), 2) + Math.pow(head.getY() - ball.getY(), 2)) < rayonSnake
                            + rayonOtherSnake) {
                isDead = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si le serpent peut manger un modèle de nourriture donné.
     * 
     * @param foodModel Le modèle de nourriture à vérifier.
     * @return true si le serpent peut manger la nourriture, sinon false.
     */
    public boolean canEat(FoodModel foodModel) {
        Ball head = balls.get(0);
        double diametreSnake = getDiametre();
        double rayonSnake = diametreSnake / 2;
        double rayonFood = foodModel.getDiametre() / 2;
        return Math.sqrt(Math.pow(head.getX() - foodModel.getX(), 2)
                + Math.pow(head.getY() - foodModel.getY(), 2)) < rayonSnake + rayonFood;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SnakeModel snakeModel) {
            return id == snakeModel.id;
        }
        return false;
    }

    // Getters

    public List<Ball> getBalls() {
        return balls;
    }

    public Color getColor() {
        return color;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public int getLevel() {
        return level;
    }

    public boolean isBot() {
        return bot;
    }

    public boolean isDead() {
        return isDead;
    }

    /**
     * Classe représentant une balle qui compose le serpent.
     */
    public static class Ball {
        private Vector2D currentCoord;
        private Vector2D directorVector;
        private Queue<Vector2D> historyCoordinates;

        /**
         * Constructeur pour créer une nouvelle balle.
         * 
         * @param startX Coordonnée X de départ.
         * @param startY Coordonnée Y de départ.
         */
        public Ball(double startX, double startY) {
            this.currentCoord = new Vector2D(startX, startY);
            this.directorVector = new Vector2D(0, 0);
            this.historyCoordinates = new LinkedList<>();
        }

        /**
         * Définit le vecteur directeur pour la balle.
         * 
         * @param speed           Vitesse du mouvement.
         * @param directorVectorX Coordonnée X du vecteur directeur.
         * @param directorVectorY Coordonnée Y du vecteur directeur.
         */
        public void setDirectorVector(double speed, double directorVectorX, double directorVectorY) {
            Vector2D newDirectorVector = new Vector2D(directorVectorX, directorVectorY);
            newDirectorVector.normalize();

            newDirectorVector.multVector(speed);
            this.directorVector = newDirectorVector;
        }

        /**
         * Met à jour la position de la balle en fonction de son vecteur directeur.
         * 
         * @param snakeModel Le modèle du serpent auquel appartient la balle.
         */
        public void updatePosition(SnakeModel snakeModel) {
            historyCoordinates.offer(currentCoord.copy());
            currentCoord.addVector(directorVector);

            if (currentCoord.x < 0)
                currentCoord.x = snakeModel.frameDimension.getWidth();
            if (currentCoord.x > snakeModel.frameDimension.getWidth())
                currentCoord.x = 0;
            if (currentCoord.y < 0)
                currentCoord.y = snakeModel.frameDimension.getHeight();
            if (currentCoord.y > snakeModel.frameDimension.getHeight())
                currentCoord.y = 0;
        }

        /**
         * La balle suit la balle qui la précéde.
         * 
         * @param leader        La balle à suivre.
         * @param diametreSnake Diamètre du serpent.
         */
        public void follow(Ball leader, double diametreSnake) {
            int rayonSnake = (int) diametreSnake / 2;
            if (leader.historyCoordinates.size() >= rayonSnake) {
                currentCoord.setCoordinates(leader.historyCoordinates.poll());
                historyCoordinates.offer(currentCoord.copy());
                if (historyCoordinates.size() > rayonSnake) {
                    historyCoordinates.poll();
                }
            }
        }

        // Getters

        public double getX() {
            return currentCoord.x;
        }

        public double getY() {
            return currentCoord.y;
        }
    }
}
