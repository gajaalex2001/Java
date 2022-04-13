import gui.LoginController;
import gui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Arbitru;
import rpcprotocol.TriatlonServicesRpcProxy;
import services.ITriatlonServices;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFX extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("triatlon.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("triatlon.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        ITriatlonServices server = new TriatlonServicesRpcProxy(serverIP, serverPort);



        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/login.fxml"));
        Parent root=loader.load();


        LoginController ctrl =
                loader.<LoginController>getController();
        ctrl.setServer(server);

        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/main.fxml"));
        Parent croot=cloader.load();


        MainController mainCtrl =
                cloader.<MainController>getController();
        mainCtrl.setServer(server);

        ctrl.setMainController(mainCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("Log in");
        primaryStage.setScene(new Scene(root, 209, 193));
        primaryStage.show();
    }

    public void switchToMain(Arbitru arb, ITriatlonServices service) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("scenes/main.fxml"));
        AnchorPane root = fxmlLoader.load();
        MainController controller = fxmlLoader.getController();
        controller.setServer(service);
        controller.setArbitru(arb);

        primaryStage.setScene(new Scene(root, 340, 330));
        primaryStage.setTitle("Manage competitions");
        primaryStage.show();
    }

    public void switchToLogin(ITriatlonServices service) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("scenes/login.fxml"));
        AnchorPane root = fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();
        controller.setServer(service);

        primaryStage.setScene(new Scene(root, 209, 193));
        primaryStage.setTitle("Log in");
        primaryStage.show();
    }
}
