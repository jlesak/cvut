package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.entities.company.Company;

import java.io.IOException;

/**
 * Created by lesy on 18.5.17.
 */
public class CompanyListViewCell extends ListCell<Company> {

    @FXML
    private Text nameTextFiled;

    @FXML
    private Text smallDescriptionTextFiled;

    private FXMLLoader loader;

    @FXML
    private GridPane gridPane;

    @Override
    protected void updateItem(Company company, boolean empty) {
        super.updateItem(company, empty);
        if(empty || company == null) {
            setText(null);
            setGraphic(null);
        }
        else if (loader == null) {
            loader = new FXMLLoader(getClass().getResource("/CompanyListPane.fxml"));
            loader.setController(this);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            nameTextFiled.setText(company.name);
            smallDescriptionTextFiled.setText(company.description);

            setText(null);
            setGraphic(gridPane);

        }
    }
}
