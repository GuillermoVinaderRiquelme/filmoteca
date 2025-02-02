package dam.alumno.filmoteca;

import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

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
    @FXML
    private ImageView imagePoster;
    @FXML
    private Slider slidePuntuacion;
    @FXML
    private TextArea textoDescripcion;
    @FXML
    private Text textoID;
    @FXML
    private Text textoTitulo;
    @FXML
    private Text textoYear;

    public void initialize() {
        columnaID.setCellValueFactory(new PropertyValueFactory<Pelicula, Integer>("id"));
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("title"));
        columnaYear.setCellValueFactory(new PropertyValueFactory<Pelicula, Integer>("year"));
        //Defino rangos para el slide.
        slidePuntuacion.setMin(0);
        slidePuntuacion.setMax(10);
        tablaPeliculas.setItems(listaPeliculas);  //Se carga la lista de películas en el TableView

        //Para reflejar en el AnchorPane la información de la pelicula seleccionada
        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                textoID.textProperty().bind(Bindings.convert(newValue.idProperty()));   //El ID hay que convertirlo con Bindings.convert al ser de tipo Integer (espera un String)
                textoTitulo.textProperty().bind(newValue.titleProperty());
                textoYear.textProperty().bind(Bindings.convert(newValue.yearProperty()));
                textoDescripcion.textProperty().bind((newValue.descriptionProperty()));
                Image imagen = new Image(newValue.getPoster(), true);  //Para cargar el cartel
                imagePoster.setImage(imagen);
                slidePuntuacion.valueProperty().bind(newValue.ratingProperty());
            }
        }));
    }

    public void cerrarApp(ActionEvent actionEvent) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Salir del programa");
        alerta.setHeaderText("¿Estás seguro de que quieres cerrar la aplicación? Todos los cambios no guardados se perderán.");
        alerta.setContentText("Confirmar para cerrar");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    public void nuevaPelicula(ActionEvent actionEvent) {
        Scene nuevaEscena = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pelicula-view.fxml"));  //Se carga la vista de la nueva ventana
        try{
            nuevaEscena = new Scene(loader.load());
        }catch (IOException e){
            System.out.println("Error al abrir la nueva ventana. +info:\n");
            e.printStackTrace();
        }
        PeliculaController controlador = loader.getController();
        controlador.establecerTitulo("Añadir nueva película");

        Stage st = new Stage();
        st.setScene(nuevaEscena);
        st.setTitle("Añadir nueva película");
        st.showAndWait();

    }

    public void editarPelicula(ActionEvent actionEvent) {
        int indice = tablaPeliculas.getSelectionModel().getSelectedIndex();
        //Si el indice es menor que 0 es porque no hay ninguna película seleccionada
        if (indice < 0) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("ERROR");
            alerta.setHeaderText("Ninguna película seleccionada");
            alerta.setContentText("Para editar una película, selecciona una de la lista");
            alerta.showAndWait();
            return;
        }
        Scene nuevaEscena = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pelicula-view.fxml"));
        try{
            nuevaEscena = new Scene(loader.load());
        }catch (IOException e){
            System.out.println("Error al abrir la nueva ventana. +info:\n");
            e.printStackTrace();
        }
        PeliculaController controlador = loader.getController();
        /*
        Para que aparezcan cargados los valores de la película en la nueva ventana, los establezco desde aquí
        pero usando los setters que están definidos en el controlador de esa vista (PeliculaController)
         */
        controlador.establecerTitulo("Editar película");
        controlador.setPelicula(tablaPeliculas.getSelectionModel().getSelectedItem());  //Pasamos la película seleccionada
        controlador.setEdicion(true);  //Se establece modo edición en el controlador
        controlador.establecerId(tablaPeliculas.getSelectionModel().getSelectedItem().getId());  //El id aparece puesto y no se puede editar
        controlador.setHuecoTitulo(tablaPeliculas.getSelectionModel().getSelectedItem().getTitle());
        controlador.setHuecoYear(tablaPeliculas.getSelectionModel().getSelectedItem().getYear());
        controlador.setHuecoDescripcion(tablaPeliculas.getSelectionModel().getSelectedItem().getDescription());
        controlador.setHuecoSlider(tablaPeliculas.getSelectionModel().getSelectedItem().getRating());
        controlador.setHuecoPoster(tablaPeliculas.getSelectionModel().getSelectedItem().getPoster());
        controlador.setHuecoURL(tablaPeliculas.getSelectionModel().getSelectedItem().getPoster());

        Stage st = new Stage();
        st.setScene(nuevaEscena);
        st.setTitle("Editar película");
        st.showAndWait();
    }

    public void eliminarPelicula(ActionEvent actionEvent) {
        Pelicula peliculaSeleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (peliculaSeleccionada == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Error");
            alerta.setHeaderText("No se ha seleccionado ninguna película");
            alerta.setContentText("Por favor, selecciona la película que deseas borrar.");
            alerta.showAndWait();
        }else{
            Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            alertaConfirmacion.setTitle("Confirmar borrado");
            alertaConfirmacion.setHeaderText("¿Estás seguro de querer eliminar la película seleccionada?");
            alertaConfirmacion.setContentText("La película: " + peliculaSeleccionada.getTitle() + " será eliminada de la lista");
            Optional<ButtonType> resultado = alertaConfirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                listaPeliculas.remove(peliculaSeleccionada);
            }
        }
    }

}
