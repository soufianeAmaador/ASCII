package Purpleye.controller;

import Purpleye.FIDGETControlls.FigFontResources;
import Purpleye.FIDGETControlls.FigletRenderer;
import Purpleye.MainApplication;
import Purpleye.views.ASCIIview;
import Purpleye.views.View;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ASCIIcontroller extends Controller{

    private TextField imageCharWidth;
    private TextField imageCharHeight;
    private TextField enterText;
    private TextArea output;
    private Button fileBrowser;
    private ComboBox<String> fontsCB;
    private FileChooser fileChooser = new FileChooser();

    private ASCIIview view;
    private File selectedFile;
    private BufferedImage bufferedImage;

    private List<String> percentage;
    private List<Integer> scale;

    private int scaleWidth;
    private int scaleHeight;

    private static final String BUTTON_THEME = "-fx-background-color: black; -fx-text-fill: Dimgray; -fx-font: 24 arial; -fx-border-color: DimGray;";
    private static final String BUTTON_THEME_ON_HOVER = "-fx-background-color: DimGrey; -fx-text-fill: black; -fx-font: 24 arial; -fx-border-color: black;";
    private final String SMALL_BUTTON_DESIGN_RIGHT = "-fx-background-color: black; -fx-text-fill: Dimgray; -fx-border-color: Dimgray;  -fx-border-width:0 1 0 0 ;-fx-border-color: Dimgray;";
    private final String SMALL_BUTTON_DESIGN_LEFT = "-fx-background-color: black; -fx-text-fill: Dimgray; -fx-border-color: Dimgray;  -fx-border-width:0 0 0 1; -fx-border-color: Dimgray;";    private static final String SMALL_BUTTON_DESIGN_ON_HOVER_RIGHT = "-fx-background-color: Dimgrey; -fx-text-fill: black; -fx-border-width:0 1 0 0 ;-fx-border-color: black;";
    private static final String SMALL_BUTTON_DESIGN_ON_HOVER_LEFT = "-fx-background-color: Dimgrey; -fx-text-fill: black; -fx-border-width:0 0 0 1; -fx-border-color: black;";



    public ASCIIcontroller() {
        view = new ASCIIview();
        createScale();
        adjustScale();
        onHoverActions();

        view.getFileBrowser().setOnAction(e -> openFileBrowser());
        view.getFontsCB().setItems(FXCollections.observableList(FigFontResources.loadAllAvailableFonts()));
        view.getFontsCB().getSelectionModel().select(0);

        view.getDecrementWidthButton().setOnAction(e -> toggleScaling(view.getImageCharWidth(),false));
        view.getIncrementWidthButton().setOnAction(e -> toggleScaling(view.getImageCharWidth(),true));
        view.getDecrementHeightButton().setOnAction(e -> toggleScaling(view.getImageCharHeight(),false));
        view.getIncrementHeightButton().setOnAction(e -> toggleScaling(view.getImageCharHeight(),true));
        view.getFontsCB().setOnAction(e -> {
            try{convertText(); }    catch(IOException ex){ex.printStackTrace();}
        });

        view.getEnterText().setOnKeyTyped(e -> {
            try{convertText(); }    catch(IOException ex){ex.printStackTrace();}
        });

    }

    private void convertText()throws IOException{
        FigletRenderer figletRenderer = new FigletRenderer(FigFontResources.loadFigFontResource(view.getFontsCB().getValue()));

        figletRenderer.setSmushMode(1);
        String string = figletRenderer.renderText(view.getEnterText().getText());
        String newString = string.replace(" ","...");
        System.out.println(figletRenderer.renderText(view.getEnterText().getText()));


//        view.getOutput().setText(figletRenderer.renderText(view.getEnterText().getText()));

        view.getOutput().setText(newString);

        MainApplication.getStage().setWidth(1000);
        MainApplication.getStage().setHeight(500);

    }

    private void convertImage(){

        String characters = ImageConverter.comvertToASCII(bufferedImage,scaleWidth,scaleHeight);


        view.getSelectedImageName().setText(selectedFile.getName());
        view.getSelectedImageName().setMaxWidth(80);
        view.getOutput().setText(characters);
        view.getOutput().setWrapText(true);

        MainApplication.getStage().setWidth(500 + ImageConverter.getImage().getWidth());
        MainApplication.getStage().setHeight(ImageConverter.getImage().getHeight());



    }

    private void openFileBrowser(){
    try{
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Images", "jpg", "gif", "png"));
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null){
            bufferedImage = ImageIO.read(selectedFile);
            convertImage();
        }


    }catch (IOException EX){
        EX.printStackTrace();
    }

    }

    private void toggleScaling(TextField textField, boolean isIncreased){
        String number = textField.getText();

        int position = percentage.indexOf(number);

        if (selectedFile == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Enter a valid image first", ButtonType.OK);
            alert.setTitle("ASCII - Warning");
            alert.show();
            return;
        }

        if (isIncreased && !number.equals("100%")){
            textField.setText(percentage.get(++position));
            adjustScale();
            convertImage();
        }else if(!isIncreased && !number.equals("1%")){
            textField.setText(percentage.get(--position));
            adjustScale();
            convertImage();
        }

    }

    private void adjustScale(){
        int indexScaleHeight = percentage.indexOf(view.getImageCharHeight().getText());
        int indexScaleWidth = percentage.indexOf(view.getImageCharWidth().getText());

        scaleWidth = scale.get(indexScaleWidth);
        scaleHeight = scale.get(indexScaleHeight);
    }


    private void createScale(){
        scale = new ArrayList<>(List.of(100,20,10,5,4,2,1));
        percentage = new ArrayList<>(List.of("1%","5%","10%","20%","25%","50%","100%"));
        view.getImageCharHeight().setText("100%");
        view.getImageCharWidth().setText("100%");
    }

    private void onHoverActions(){
        view.getFileBrowser().setOnMouseEntered(e -> view.getFileBrowser().setStyle(BUTTON_THEME_ON_HOVER));
        view.getFileBrowser().setOnMouseExited(e -> view.getFileBrowser().setStyle(BUTTON_THEME));

        view.getDecrementHeightButton().setOnMouseEntered(e -> view.getDecrementHeightButton().setStyle(SMALL_BUTTON_DESIGN_ON_HOVER_RIGHT));
        view.getDecrementHeightButton().setOnMouseExited(e -> view.getDecrementHeightButton().setStyle(SMALL_BUTTON_DESIGN_RIGHT));

        view.getIncrementHeightButton().setOnMouseEntered(e -> view.getIncrementHeightButton().setStyle(SMALL_BUTTON_DESIGN_ON_HOVER_LEFT));
        view.getIncrementHeightButton().setOnMouseExited(e -> view.getIncrementHeightButton().setStyle(SMALL_BUTTON_DESIGN_LEFT));

        view.getDecrementWidthButton().setOnMouseEntered(e -> view.getDecrementWidthButton().setStyle(SMALL_BUTTON_DESIGN_ON_HOVER_RIGHT));
        view.getDecrementWidthButton().setOnMouseExited(e -> view.getDecrementWidthButton().setStyle(SMALL_BUTTON_DESIGN_RIGHT));

        view.getIncrementWidthButton().setOnMouseEntered(e -> view.getIncrementWidthButton().setStyle(SMALL_BUTTON_DESIGN_ON_HOVER_LEFT));
        view.getIncrementWidthButton().setOnMouseExited(e -> view.getIncrementWidthButton().setStyle(SMALL_BUTTON_DESIGN_LEFT));

    }


    @Override
    public View getView() {
        return view;
    }
}
