package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Arbitru;
import model.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.PunctajDTO;
import model.RaportDTO;
import services.ITriatlonObserver;
import services.ITriatlonServices;
import services.TriatlonException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable, ITriatlonObserver {
    @FXML
    private Label arbLabel;

    private ObservableList<Participant> modelParticipants = FXCollections.observableArrayList();
    @FXML
    private TableView<Participant> tableView;
    @FXML
    private TableColumn<Participant, Integer> idColumn;
    @FXML
    private TableColumn<Participant, String> numeColumn;
    @FXML
    private TableColumn<Participant, String> prenumeColumn;
    @FXML
    private TableColumn<Participant, Integer> punctajColumn;

    @FXML
    private TextField resultBox;

    private Arbitru arb;
    private ITriatlonServices server;

    public MainController() {
        System.out.println("Constructor main controller");
    }

    public MainController(ITriatlonServices server) {
        this.server = server;
        System.out.println("Constructor main controller cu parametru server");
    }

    public void setServer(ITriatlonServices s) {
        server = s;
    }

    public void setArbitru(Arbitru arb){
        this.arb = arb;
        arbLabel.setText("Welcome, " + arb.getPrenume() + " " + arb.getNume());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        numeColumn.setCellValueFactory(new PropertyValueFactory<>("Nume"));
        prenumeColumn.setCellValueFactory(new PropertyValueFactory<>("Prenume"));
        punctajColumn.setCellValueFactory(new PropertyValueFactory<>("Punctaj"));

        tableView.setItems(modelParticipants);
    }

    @Override
    public void updateReceived() throws TriatlonException {
        Platform.runLater(()->{
            loadTableData();
        });
    }

    public void logoutBtnPressed(ActionEvent actionEvent){
        logout();
        System.exit(0);
    }

    public void logout(){
        try {
            server.logout(arb, this);
        } catch (TriatlonException e) {
            e.printStackTrace();
        }
    }

    public void loadTableData(){
        try {
            List<Participant> participants = server.getParticipantiProba(arb.getProba().getID(), this);
            modelParticipants.setAll(participants);
        } catch (TriatlonException e) {
            e.printStackTrace();
        }
    }

    public void addScore(){
        String resultString = resultBox.getText();

        if(resultString.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.NONE, "Punctaj neintrodus", ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }
        else{
            try {
                int result = Integer.parseInt(resultString);

                Participant p = tableView.getSelectionModel().getSelectedItem();
                if (p!= null){
                    PunctajDTO dto = new PunctajDTO(arb.getProba().getID(), p.getID(), result, arb.getID());
                    server.addPunctaj(dto, this);
                }
            }
            catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.NONE, "Punctaj introdus gresit", ButtonType.OK);
                alert.setTitle("ERROR");
                alert.show();
            }
            catch (TriatlonException e){
                Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
                alert.setTitle("ERROR");
                alert.show();
            }
        }
    }

    public void generateReport(){
        try {
            List<RaportDTO> participants = server.getRaportParticipanti(arb.getProba().getID(), this);

            Stage raport = new Stage();
            raport.initModality(Modality.APPLICATION_MODAL);

            TableView<RaportDTO> raportTable= new TableView<>();

            TableColumn<RaportDTO, Integer> idColumnRaport = new TableColumn<>();
            idColumnRaport.setCellValueFactory(new PropertyValueFactory<>("ID"));
            TableColumn<RaportDTO, String> numeColumnRaport = new TableColumn<>();
            numeColumnRaport.setCellValueFactory(new PropertyValueFactory<>("Nume"));
            TableColumn<RaportDTO, String> prenumeColumnRaport = new TableColumn<>();
            prenumeColumnRaport.setCellValueFactory(new PropertyValueFactory<>("Prenume"));
            TableColumn<RaportDTO, Integer> punctajColumnRaport = new TableColumn<>();
            punctajColumnRaport.setCellValueFactory(new PropertyValueFactory<>("Punctaj"));

            raportTable.getColumns().add(idColumnRaport);
            raportTable.getColumns().add(numeColumnRaport);
            raportTable.getColumns().add(prenumeColumnRaport);
            raportTable.getColumns().add(punctajColumnRaport);

            ObservableList<RaportDTO> modelRaport = FXCollections.observableArrayList();
            modelRaport.setAll(participants);

            idColumnRaport.resizableProperty().set(false);
            numeColumnRaport.resizableProperty().set(false);
            prenumeColumnRaport.resizableProperty().set(false);
            punctajColumnRaport.resizableProperty().set(false);

            idColumnRaport.setText("ID");
            numeColumnRaport.setText("Nume");
            prenumeColumnRaport.setText("Prenume");
            punctajColumnRaport.setText("Punctaj");

            idColumnRaport.sortableProperty().set(false);
            numeColumnRaport.sortableProperty().set(false);
            prenumeColumnRaport.sortableProperty().set(false);
            punctajColumnRaport.sortableProperty().set(false);

            raportTable.setItems(modelRaport);

            VBox box = new VBox(20);
            box.getChildren().add(raportTable);

            Scene dialogScene = new Scene(box, 320, 300);
            raport.setTitle("Raport punctaje " + arb.getProba().getNume().toLowerCase(Locale.ROOT));

            raport.resizableProperty().set(false);
            raport.setScene(dialogScene);
            raport.showAndWait();
        } catch (TriatlonException e) {
            e.printStackTrace();
        }
    }
}