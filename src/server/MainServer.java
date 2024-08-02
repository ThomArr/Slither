package server;

import food.FoodModel;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import snake.*;
import tools.ColorTools;
import tools.DataParser;

/**
 * Le serveur principal qui gère les connexions des clients, la mise à jour des
 * positions des serpents,
 * et la diffusion des données à tous les clients connectés.
 */
public class MainServer {
    private static final int PORT = 12345; // Le port sur lequel le serveur écoute les connexions.
    private static final int BROADCAST_INTERVAL_MS = 10; // Intervalle de diffusion des données en millisecondes.
    private static final int UPDATE_INTERVAL_MS = 7; // Intervalle de mise à jour des positions des serpents en
                                                     // millisecondes.

    private final static List<Socket> clients = new CopyOnWriteArrayList<>(); // Liste des sockets clients.
    private final static List<SnakeModel> snakeModels = new CopyOnWriteArrayList<>(); // Liste des serpents.
    private final static List<FoodModel> foodModels = new CopyOnWriteArrayList<>(); // Liste des nourritures.

    private final static Dimension dimension = new Dimension(1600, 1200);

    public static Random rand = new Random();

    /**
     * Méthode principale qui démarre le serveur et accepte les connexions des
     * clients.
     * 
     * @param args Arguments de la ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialiser la nourriture
            for (int i = 0; i < 1; i++) {
                foodModels.add(new FoodModel(dimension));
            }

            // Initialiser les bots
            for (int i = 0; i < 9; i++) {
                Color color = ColorTools.generateRandomColor();
                SnakeModel snakeModel = new SnakeModel(15, rand.nextInt(dimension.width),
                        rand.nextInt(dimension.height), color,
                        ColorTools.getDarkerColor(color), true, dimension);
                snakeModels.add(snakeModel);
            }

            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server started...");

                // Créer un exécuteur pour diffuser les données à intervalles réguliers
                ScheduledExecutorService broadcastExecutor = Executors.newScheduledThreadPool(1);
                broadcastExecutor.scheduleAtFixedRate(MainServer::broadcast, 0, BROADCAST_INTERVAL_MS,
                        TimeUnit.MILLISECONDS);

                // Créer un exécuteur pour mettre à jour les positions des serpents à
                // intervalles réguliers
                ScheduledExecutorService updateExecutor = Executors.newScheduledThreadPool(1);
                updateExecutor.scheduleAtFixedRate(MainServer::updateSnakePositions, 0, UPDATE_INTERVAL_MS,
                        TimeUnit.MILLISECONDS);

                // Accepter les connexions des clients et gérer chaque client dans un nouveau
                // thread
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    clients.add(clientSocket);

                    // Créer un modèle de serpent pour chaque nouveau client
                    Color color = ColorTools.generateRandomColor();
                    SnakeModel snakeModel = new SnakeModel(15, rand.nextInt(dimension.width),
                            rand.nextInt(dimension.height), color,
                            ColorTools.getDarkerColor(color), false, dimension);
                    snakeModels.add(snakeModel);

                    new ClientHandler(clientSocket, snakeModel).start();
                }
            } catch (IOException e) {
                System.err.println("Server error: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Met à jour les positions des serpents et gère les collisions avec les autres
     * serpents et les nourritures.
     */
    private static void updateSnakePositions() {
        for (int i = 0; i < snakeModels.size(); i++) {
            SnakeModel model = snakeModels.get(i);
            if (model.isBot()) {
                model.generateBotMove();
            }

            for (int j = 0; j < snakeModels.size(); j++) {
                SnakeModel snakeModel = snakeModels.get(j);
                if (model.equals(snakeModel))
                    continue;

                if (model.isDead(snakeModel)) {

                    killSnake(model);

                    // Ajoute la nourriture à la position du serpent mort
                    for (SnakeModel.Ball ball : model.getBalls()) {
                        foodModels.add(new FoodModel(ball.getX() + (rand.nextInt(2) == 0 ? -1 : 1) *
                                rand.nextInt((int) model.getDiametre()),
                                ball.getY() + (rand.nextInt(2) == 0 ? -1 : 1) * rand.nextInt((int) model.getDiametre()),
                                model.getColor(),
                                rand.nextInt(3) + 1));
                    }
                    i--;
                    break;
                }
            }

            // Vérifie les collisions avec la nourriture
            for (int k = 0; k < foodModels.size(); k++) {
                FoodModel foodModel = foodModels.get(k);
                if (model.canEat(foodModel)) {
                    foodModels.remove(k);
                    foodModels.add(k,new FoodModel(dimension));
                    model.addLevels(foodModel.getLevel());
                    k--;
                }
            }

            model.updatePosition();
        }
    }

    /**
     * Gère les interactions avec un client spécifique.
     */
    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private final SnakeModel snakeModel;
        private final BufferedReader in;
        private final PrintWriter out;

        /**
         * Constructeur pour initialiser les flux de communication avec le client.
         * 
         * @param socket Le socket du client.
         * @throws IOException Si une erreur d'entrée/sortie se produit lors de la
         *                     création des flux.
         */
        public ClientHandler(Socket socket, SnakeModel snakeModel) throws IOException {
            this.clientSocket = socket;
            this.snakeModel = snakeModel;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String input = in.readLine();
                    if (input == null)
                        break;
                    if (!snakeModel.isDead()) {
                        String[] parts = input.split(",");
                        if (parts.length != 2)
                            continue; // Ignorer les entrées mal formatées

                        try {
                            double x = Double.parseDouble(parts[0].trim());
                            double y = Double.parseDouble(parts[1].trim());

                            snakeModel.setTargetCoord(x, y);

                        } catch (NumberFormatException e) {
                            // Ignorer les entrées mal formatées
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Client disconnected.");
            } finally {
                killSnake(snakeModel);
                disconnectClient(clientSocket);
            }
        }
    }

    /**
     * Diffuse les données des modèles de serpents et de nourritures à tous les
     * clients connectés.
     */
    private static void broadcast() {
        String message = DataParser.formatData(snakeModels, foodModels);

        for (Socket client : clients) {
            try {
                if (!client.isClosed()) {
                    PrintWriter clientOut = new PrintWriter(client.getOutputStream(), true);
                    clientOut.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Tue le serpent d'un client et met à jour les listes des serpents.
     * 
     * @param snakeModel Le serpent à tuer.
     */
    private static void killSnake(SnakeModel snakeModel) {
        snakeModels.remove(snakeModel);
    }

    /**
     * Close la socket d'un client et met à jour les listes des client.
     * 
     * @param clientSocket Le socket du client à close.
     */
    private static void disconnectClient(Socket clientSocket) {
        try {
            clients.remove(clientSocket);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
