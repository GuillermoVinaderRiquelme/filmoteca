package dam.alumno.filmoteca;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PeliculaController {

    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextArea huecoDescripcion;
    @FXML
    private TextField huecoID;
    @FXML
    private ImageView huecoPoster;
    @FXML
    private Slider huecoSlider;
    @FXML
    private TextField huecoTitulo;
    @FXML
    private TextField huecoURL;
    @FXML
    private TextField huecoYear;
    @FXML
    private Label tituloVentana;

    private boolean edicion = false;  //Controla si la ventana se usa para crear o para editar una película
    private Pelicula pelicula;   //Si está en modo edición, apunta a la película a editar

    public void initialize() {
        huecoSlider.setMin(0);
        huecoSlider.setMax(10);
        huecoID.setEditable(false);  //El id no se puede editar
        if(!edicion){
            huecoID.setText(String.valueOf(obtenerSiguienteId())); //El id aparece ya puesto
        }else{
            huecoTitulo.setText(pelicula.getTitle());
        }
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public void setEdicion(boolean edicion) {
        this.edicion = edicion;
    }

    public void establecerId(int id) {
        huecoID.setText(String.valueOf(id));
    }

    public void establecerTitulo(String titulo){
        tituloVentana.setText(titulo);
    }

    public void setHuecoDescripcion(String huecoDescripcion) {
        this.huecoDescripcion.setText(String.valueOf(huecoDescripcion));
    }

    public void setHuecoPoster(String huecoPoster) {
        this.huecoPoster.setImage(new Image(huecoPoster));
    }

    public void setHuecoSlider(float huecoSlider) {
        this.huecoSlider.setValue((float) huecoSlider);
    }

    public void setHuecoTitulo(String huecoTitulo) {
        this.huecoTitulo.setText(huecoTitulo);
    }

    public void setHuecoURL(String huecoURL) {
        this.huecoURL.setText(String.valueOf(huecoURL));
    }

    public void setHuecoYear(int huecoYear) {
        this.huecoYear.setText(String.valueOf(huecoYear));
    }

    public void guardar(ActionEvent actionEvent) {
        if (!edicion) {  //Se crea una película nueva
            //Para incluir una película, al menos hay que poner título y fecha, porque son los datos que se muestran en el TableView
            if (huecoTitulo.getText().isEmpty() || huecoYear.getText().isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("ERROR");
                alerta.setHeaderText("Datos insuficientes");
                alerta.setContentText("Para crear una película, al menos incluye un título y una fecha de lanzamiento");
                alerta.showAndWait();
                return;
            }
            //Cogemos los valores de los textfield y se crea la nueva película con ellos
            Pelicula nuevaPelicula = new Pelicula();
            nuevaPelicula.setId(obtenerSiguienteId());
            nuevaPelicula.setTitle(huecoTitulo.getText());
            nuevaPelicula.setDescription(huecoDescripcion.getText());
            try{
                nuevaPelicula.setYear(Integer.parseInt(huecoYear.getText()));
            }catch(NumberFormatException e){
                //Si se introduce algo distinto a un entero en la fecha, no deja continuar
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("ERROR");
                alerta.setHeaderText("Fecha errónea");
                alerta.setContentText("El año de lanzamiento debe ser un número entero");
                alerta.showAndWait();
                return;
            }
            nuevaPelicula.setRating((float) huecoSlider.getValue());
            nuevaPelicula.setPoster(huecoURL.getText());
            DatosFilmoteca.getInstancia().getListaPeliculas().add(nuevaPelicula);  //Se añade la nueva película a la lista
        }else{
            //A la película recibida por parámetro (la seleccionada) se le asignan los nuevos valores
            pelicula.setTitle(huecoTitulo.getText());
            pelicula.setDescription(huecoDescripcion.getText());
            pelicula.setYear(Integer.parseInt(huecoYear.getText()));
            pelicula.setRating((float) huecoSlider.getValue());
            pelicula.setPoster(huecoURL.getText());
        }
        cerrarVentana();
    }

    public void cancelar(ActionEvent actionEvent) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        // Cerrar la ventana actual
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private int obtenerSiguienteId() {
        ObservableList<Pelicula> listaPeliculas = DatosFilmoteca.getInstancia().getListaPeliculas();
        int maxId = 0;
        for (Pelicula p : listaPeliculas) {
            if (p.getId() > maxId) {
                maxId = p.getId();
            }
        }
        return maxId + 1;
    }
}
