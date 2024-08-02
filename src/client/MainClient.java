package client;

import food.FoodModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.util.List;
import javax.swing.*;
import snake.SnakeModel;
import tools.DataParser;

/**
 * Classe principale du client qui gère la connexion au serveur, l'affichage et la réception des données.
 */
public class MainClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private static MainModelClient mainModel;
    private static MainViewClient mainView;

    /**
     * Point d'entrée principal du programme. Initialise le modèle et la vue, 
     * établit la connexion avec le serveur et gère les événements de la souris.
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        mainModel = new MainModelClient();
        mainView = new MainViewClient(mainModel);

        // Création et configuration de la fenêtre principale
        JFrame frame = new JFrame("Slither.io");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 1300);
        frame.add(mainView);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        // Tentative de connexion au serveur
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            new Thread(new Reader(socket)).start(); // Lancement du thread de lecture des données
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Gestion des mouvements de la souris pour envoyer les coordonnées au serveur
            mainView.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    out.println(e.getX() + "," + e.getY());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Classe interne qui lit les données envoyées par le serveur.
     */
    private static class Reader implements Runnable {
        private Socket socket;
        private BufferedReader in;

        /**
         * Constructeur de Reader.
         * @param socket La connexion socket utilisée pour lire les données.
         * @throws IOException Si une erreur d'entrée/sortie se produit.
         */
        public Reader(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        /**
         * Lit les données depuis le socket et met à jour le modèle et la vue en conséquence.
         */
        @Override
        public void run() {
            try {
                String input;
                while ((input = in.readLine()) != null) {
                    List<Object> data = MainClient.parseData(input);
                    mainModel.setSnakeModels((List<SnakeModel>) data.get(0));
                    mainModel.setFoodModels((List<FoodModel>) data.get(1));
                    mainView.updateView(); // Mise à jour de l'affichage
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Parse les données reçues du serveur.
     * @param data Les données sous forme de chaîne de caractères.
     * @return Une liste contenant les modèles de serpents et de nourriture.
     */
    public static List<Object> parseData(String data) {
        return DataParser.parse(data);
    }
}
