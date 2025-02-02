package dam.alumno.filmoteca;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    private DatosFilmoteca filmotecaSingleton = DatosFilmoteca.getInstancia();
    private ObservableList<Pelicula> listaPeliculas = filmotecaSingleton.getListaPeliculas();


    @FXML
    private TableColumn<Pelicula, Integer> columnaID;

    @FXML
    private TableColumn<Pelicula, String> columnaTitulo;

    @FXML
    private TableColumn<Pelicula, Integer> columnaYear;

    @FXML
    private TableView<Pelicula> tablaPeliculas;

    public void initialize() {
        columnaID.setCellValueFactory(new PropertyValueFactory<Pelicula, Integer>("id"));
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("title"));
        columnaYear.setCellValueFactory(new PropertyValueFactory<Pelicula, Integer>("year"));
        tablaPeliculas.setItems(listaPeliculas);
    }

}
