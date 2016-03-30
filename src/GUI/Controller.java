package GUI;

import CustomException.InvalidMapException;
import DataManager.Converter;
import DataManager.Load;
import DataManager.Save;
import Entities.Region;
import LoggerUtils.LoggerManager;
import Resolver.Arguments;
import Resolver.Resolver3;
import Resolver.Test2Resolver;
import com.hexiong.jdbf.JDBFException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public class Controller {

    private final ArrayList<Region> regions = new ArrayList<>();
    public Menu menuName;
    @FXML
    Pane PaneAffichageResult;
    @FXML
    Slider SliderResult;
    @FXML
    Pane PaneAffichageCarte;
    @FXML
    Slider SliderZoomCarte;
    @FXML
    CheckBox NomPaysCarte;
    @FXML
    BorderPane fenetre;
    @FXML
    Menu FirstCountryArgument;
    @FXML
    Menu MenuDirection;
    @FXML
    Menu occurenceMenu;
    @FXML
    Menu selectedCountry;
    private PolyCanvas canvasCarte;
    private HexCanvas canvas;
    private String FirstRegion;
    private int Direction;
    private boolean Clock;
    private int nbTour;

    @FXML
    void initialize() {
        SliderResult.setMin(1);
        SliderResult.setMax(10);
        SliderResult.setBlockIncrement(0.01);

        SliderZoomCarte.setMin(1);
        SliderZoomCarte.setMax(10);
        SliderZoomCarte.setBlockIncrement(0.01);

        try {
            Main.chargement(null, null);
        } catch (InvalidShapeFileException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while loading ShapeFile : " + e.getMessage());
        } catch (JDBFException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while loading ShapeFile DataBase (.dbf) : " + e.getMessage());
        } catch (IOException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while loading ShapeFile from disk : " + e.getMessage());
        } catch (InvalidMapException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while loading ShapeFile (only 2D maps with polygons can be handled) : " + e.getMessage());
        }
        canvasCarte = new PolyCanvas(Main.geoMap);
        canvas = new HexCanvas(GUI.Main.grid);

        PaneAffichageCarte.getChildren().add(canvasCarte);
        PaneAffichageResult.getChildren().add(canvas);


        canvas.heightProperty().bind(PaneAffichageResult.heightProperty());
        canvas.widthProperty().bind(PaneAffichageResult.widthProperty());
        canvasCarte.widthProperty().bind(PaneAffichageCarte.widthProperty());
        canvasCarte.heightProperty().bind(PaneAffichageCarte.heightProperty());

        canvasCarte.zoomProperty().bindBidirectional(SliderZoomCarte.valueProperty());
        canvas.zoomProperty().bindBidirectional(SliderResult.valueProperty());


        canvasCarte.nomPaysProperty().bind(NomPaysCarte.selectedProperty());

        chargementSelectionPays();
        chargementName();
        chargementArgs();

    }

    private void chargementName() {
        menuName.getItems().clear();
        ToggleGroup toggleGroup = new ToggleGroup();
        for (String name : Main.nameColumns) {
            RadioMenuItem radio = new RadioMenuItem(name);
            radio.setToggleGroup(toggleGroup);
            radio.setSelected(true);
            radio.setOnAction(event -> {
                Main.geoMap.getManager().setRegionsName(radio.getText());
                canvas.draw();
                chargementSelectionPays();
                chargementArgumentName();
                canvasCarte.draw();
            });
            menuName.getItems().add(radio);

        }

    }

    private void chargementArgumentName() {
        FirstCountryArgument.getItems().clear();
        ToggleGroup toggleGroup = new ToggleGroup();
        int first = 0;
        for (Region r : regions) {
            RadioMenuItem radio = new RadioMenuItem(r.getName());
            radio.setToggleGroup(toggleGroup);
            if (first == 0) {
                radio.setSelected(true);
                FirstRegion = r.getName();
            }
            radio.setOnAction(event -> FirstRegion = r.getName());
            FirstCountryArgument.getItems().add(radio);
            first++;
        }

    }

    private void chargementSelectionPays() {
        selectedCountry.getItems().clear();
        regions.clear();


        for (Region r : Main.geoMap.getRegions()) {
            RadioMenuItem radio = new RadioMenuItem(r.getName());
            regions.add(r);
            radio.setSelected(true);
            radio.setOnAction(event -> handleRegionsWithRadio( radio, r));
            selectedCountry.getItems().add(radio);
        }

    }

    private void handleRegionsWithRadio( RadioMenuItem radio, Region region) {
        if (radio.isSelected()) {
            regions.add(region);
        } else {
            regions.remove(region);
        }
        chargementArgumentName();
    }

    private Region getRegion() {
        Region region = null;
        for (Region r : Main.geoMap.getRegions()) {
            if (Objects.equals(r.getName(), FirstRegion)) region = r;
        }
        return region;
    }


    private void chargementArgs() {
        Direction = Entities.Direction.NORTH_EAST;
        Clock = true;
        nbTour = -1;
        ToggleGroup toggleGroup = new ToggleGroup();
        MenuDirection.getItems().clear();
        addRadio(MenuDirection, toggleGroup, "North-East", event -> Direction = Entities.Direction.NORTH_EAST, true);
        addRadio(MenuDirection, toggleGroup, "East", event -> Direction = Entities.Direction.EAST, false);
        addRadio(MenuDirection, toggleGroup, "South-East", event -> Direction = Entities.Direction.SOUTH_EAST, false);
        addRadio(MenuDirection, toggleGroup, "South-West", event -> Direction = Entities.Direction.SOUTH_WEST, false);
        addRadio(MenuDirection, toggleGroup, "West", event -> Direction = Entities.Direction.WEST, false);
        addRadio(MenuDirection, toggleGroup, "North-West", event -> Direction = Entities.Direction.NORTH_WEST, false);
        MenuDirection.getItems().add(new SeparatorMenuItem());

        ToggleGroup toggleGroupClock = new ToggleGroup();
        addRadio(MenuDirection, toggleGroupClock, "ClockWork", event -> Clock = true, true);
        addRadio(MenuDirection, toggleGroupClock, "Counter ClockWork", event -> Clock = false, false);

        ToggleGroup toggleOccu = new ToggleGroup();
        occurenceMenu.getItems().clear();
        addRadio(occurenceMenu, toggleOccu, "All", event -> nbTour = -1, true);
        addRadio(occurenceMenu, toggleOccu, "1", event -> nbTour = 1, false);
        addRadio(occurenceMenu, toggleOccu, "2", event -> nbTour = 2, false);
        addRadio(occurenceMenu, toggleOccu, "3", event -> nbTour = 3, false);
        addRadio(occurenceMenu, toggleOccu, "4", event -> nbTour = 4, false);
        addRadio(occurenceMenu, toggleOccu, "5", event -> nbTour = 5, false);
        addRadio(occurenceMenu, toggleOccu, "6", event -> nbTour = 6, false);
        addRadio(occurenceMenu, toggleOccu, "7", event -> nbTour = 7, false);
        addRadio(occurenceMenu, toggleOccu, "8", event -> nbTour = 8, false);
        addRadio(occurenceMenu, toggleOccu, "9", event -> nbTour = 9, false);
        addRadio(occurenceMenu, toggleOccu, "10", event -> nbTour = 10, false);

        chargementArgumentName();

    }

    private void addRadio(Menu menu, ToggleGroup toggle, String name, EventHandler<ActionEvent> handler, boolean selected) {
        RadioMenuItem radio = new RadioMenuItem(name);
        radio.setToggleGroup(toggle);
        radio.setSelected(selected);
        radio.setOnAction(handler);
        menu.getItems().add(radio);
    }

    @FXML
    public void loadDbf() {
        try {
            File dbf = null;
            File shp = loadShp();
            if (shp != null)
                dbf = Load.loadSingle(fenetre.getScene().getWindow(), new FileChooser.ExtensionFilter("DataBase File", "*.dbf"), shp.getParentFile());
            if (dbf != null) Main.chargement(shp.getPath(), dbf.getPath());

        } catch (InvalidShapeFileException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while loading ShapeFile : " + e.getMessage());
            showException(e);
        } catch (JDBFException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while loading ShapeFile DataBase (.dbf) : " + e.getMessage());
            showException(e);
        } catch (IOException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while loading ShapeFile from disk : " + e.getMessage());
            showException(e);
        } catch (InvalidMapException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while loading ShapeFile (only 2D maps with polygons can be handled) : " + e.getMessage());
            showException(e);
        }
        chargement();
    }

    private File loadShp() {
        return Load.loadSingle(fenetre.getScene().getWindow(), new FileChooser.ExtensionFilter("Shapefile", "*.shp"), new File(System.getProperty("user.home")));
    }

    private void chargement() {
        chargementName();
        chargementSelectionPays();
        chargementArgumentName();
        canvas.changeGrid(Main.grid);
        canvasCarte.changeMap(Main.geoMap);
    }

    @FXML
    public void load() {
        try {
            File shp = loadShp();
            if (shp != null) Main.chargement(shp.getPath(), null);

        } catch (InvalidShapeFileException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while loading ShapeFile : " + e.getMessage());
            showException(e);
        } catch (JDBFException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while loading ShapeFile DataBase (.dbf) : " + e.getMessage());
            showException(e);
        } catch (IOException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while loading ShapeFile from disk : " + e.getMessage());
            showException(e);
        } catch (InvalidMapException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while loading ShapeFile (only 2D maps with polygons can be handled) : " + e.getMessage());
            showException(e);
        }
        chargement();
    }

    public void showException(Exception e)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application Exception");
        alert.setHeaderText("An error has occured");
        alert.setContentText(e.getLocalizedMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();
        Label label = new Label("The exception stacktrace was:");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    @FXML
    public void SaveImage() {
        Save.saveToImage(fenetre.getScene().getWindow(), Converter.CanvasToImage(canvas));
    }

    @FXML
    public void refreshAlgo() {
        Test2Resolver algo = new Test2Resolver();
        Region region = getRegion();
        if (region != null) {
            Main.grid = algo.resolve(regions, Direction, Clock, region, nbTour);
            canvas.changeGrid(Main.grid);
        }
    }

    @FXML
    public void bestAlgo()
    {
        Resolver3 algo = new Resolver3();
        Arguments a = algo.resolve(regions);
        String namePays = a.getRegion().getName();
        String direction ="";
        switch (a.getDirection())
        {
            case Entities.Direction.NORTH_EAST:
                direction = "North-East";
                break;
            case Entities.Direction.EAST:
                direction = "East";
                break;
            case Entities.Direction.SOUTH_EAST:
                direction = "South-East";
                break;
            case Entities.Direction.SOUTH_WEST:
                direction = "South-West";
                break;
            case Entities.Direction.WEST:
                direction = "West";
                break;
            case Entities.Direction.NORTH_WEST:
                direction = "North-West";
                break;
        }
        for (MenuItem menu :MenuDirection.getItems()) {
            try {
                selectRadioMenuItem(direction, (RadioMenuItem) menu);
            }catch (ClassCastException ignored)
            {

            }

        }

        for (MenuItem menu :FirstCountryArgument.getItems()) {
            selectRadioMenuItem(namePays, (RadioMenuItem) menu);
        }

        Main.grid = a.getHexGrid();
        canvas.changeGrid(Main.grid);
    }

    private void selectRadioMenuItem(String direction, RadioMenuItem menu) {
        if (menu.getText().equals(direction))
            menu.setSelected(true);
    }

    public void SaveShp() {
        Save.saveToShp(fenetre.getScene().getWindow(),canvas.getHexContainer());
    }
}
