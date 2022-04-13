package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Arbitru;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.ArbitruDTO;
import services.ITriatlonServices;
import services.TriatlonException;

public class LoginController {
    @FXML
    private TextField userBox;

    @FXML
    private PasswordField passBox;

    Parent mainControllerParent;
    private ITriatlonServices server;
    private MainController mainController;

    public void setServer(ITriatlonServices s){
        server=s;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setParent(Parent p){
        mainControllerParent=p;
    }

    public void logIn(ActionEvent actionEvent){
        String user = userBox.getText();
        String password = passBox.getText();

        if(user.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Date invalide", ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }
        else {
            ArbitruDTO arbDTO = new ArbitruDTO(user, password);
            try {
                Arbitru arb = server.login(arbDTO, mainController);

                Stage stage = new Stage();
                stage.setTitle("Manage competitions");
                stage.setScene(new Scene(mainControllerParent));

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        mainController.logout();
                        System.exit(0);
                    }
                });

                stage.show();
                mainController.setArbitru(arb);
                mainController.loadTableData();
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();


            } catch (TriatlonException e) {
                Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
                alert.setTitle("ERROR");
                alert.show();
            }
        }
    }
}