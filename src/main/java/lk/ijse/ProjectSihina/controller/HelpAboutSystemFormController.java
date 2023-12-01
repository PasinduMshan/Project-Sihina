package lk.ijse.ProjectSihina.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelpAboutSystemFormController {

    @FXML
    private AnchorPane moveNode;

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Help_Form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.moveNode.getScene().getWindow();
        stage.setScene(scene);
    }

}