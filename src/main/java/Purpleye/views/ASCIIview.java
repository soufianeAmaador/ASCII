package Purpleye.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.awt.image.BufferedImage;


public class ASCIIview extends View {

    private final String BUTTON_DESIGN = "-fx-background-color: black; -fx-text-fill: Dimgray;" +
            " -fx-font: 24 arial; -fx-border-color: DimGray;";
    private final String LABEL_DESIGN = "-fx-text-fill: White; -fx-font: 24 arial;";

    private final String SMALL_BUTTON_DESIGN_RIGHT = "-fx-background-color: black; -fx-text-fill: Dimgray; -fx-border-color: Dimgray;  -fx-border-width:0 1 0 0 ;-fx-border-color: Dimgray; -fx-background-radius: 0 ";
    private final String SMALL_BUTTON_DESIGN_LEFT = "-fx-background-color: black; -fx-text-fill: Dimgray; -fx-border-color: Dimgray;  -fx-border-width:0 0 0 1; -fx-border-color: Dimgray; -fx-background-radius: 0";



    private Label selectedImageName;
    private TextArea output;

    private TextField imageCharWidth;
    private TextField imageCharHeight;
    private TextField enterText;

    private Button fileBrowser;
    private Button incrementWidthButton;
    private Button decrementWidthButton;
    private Button incrementHeightButton;
    private Button decrementHeightButton;

    private ComboBox<String> fontsCB;

    public ImageView imageView;
    public BufferedImage image;

    private Parent root;

    public ASCIIview() {
        initLayout();
    }

    private void initLayout(){

        VBox vbox = new VBox();

        //adding image from library section
        fileBrowser = new Button("add Image");
        fileBrowser.setStyle(BUTTON_DESIGN);
        fileBrowser.setMinWidth(90);
        fileBrowser.setMinHeight(20);

        //convert image
        Label convertImageLB = new Label("Convert Image");
        convertImageLB.setStyle(LABEL_DESIGN);

        selectedImageName = new Label();
        selectedImageName.setStyle("-fx-text-fill: white");
        selectedImageName.maxWidth(30);
        selectedImageName.setWrapText(false);

        HBox convertImageHbox = new HBox();
        convertImageHbox.getChildren().add(convertImageLB);
        convertImageHbox.getChildren().add(fileBrowser);
        convertImageHbox.setPadding(new Insets(50,0,0,0));
        convertImageHbox.setSpacing(20);
        convertImageHbox.setAlignment(Pos.CENTER_LEFT);
        convertImageHbox.getChildren().add(selectedImageName);

        //scaling image with -[]+ ; width
        decrementWidthButton = new Button("-");
        decrementWidthButton.setStyle(SMALL_BUTTON_DESIGN_RIGHT);
        decrementWidthButton.setMinWidth(20);
        decrementWidthButton.setMinHeight(30);
        imageCharWidth = new TextField();
        imageCharWidth.setEditable(false);
        imageCharWidth.setMaxWidth(45);
        imageCharWidth.setMinHeight(31);
        imageCharWidth.setStyle("-fx-background-color: Black; -fx-text-fill: Dimgray; -fx-background-radius: 0");
        incrementWidthButton = new Button("+");
        incrementWidthButton.setMinWidth(20);
        incrementWidthButton.setMinHeight(30);
        incrementWidthButton.setStyle(SMALL_BUTTON_DESIGN_LEFT);
        Label W = new Label("W");
        W.setStyle("-fx-text-fill: white");
        HBox scaleWidth = new HBox(W,decrementWidthButton,imageCharWidth,incrementWidthButton);

        //scaling image with -[]+ ; height
        decrementHeightButton = new Button("-");
        decrementHeightButton.setStyle(SMALL_BUTTON_DESIGN_RIGHT);
        decrementHeightButton.setMinWidth(20);
        decrementHeightButton.setMinHeight(30);
        imageCharHeight = new TextField();
        imageCharHeight.setStyle("-fx-background-color: Black; -fx-text-fill: Dimgray; -fx-background-radius: 0");
        imageCharHeight.setEditable(false);
        imageCharHeight.setMaxWidth(45);
        imageCharHeight.setMinHeight(31);
        incrementHeightButton = new Button("+");
        incrementHeightButton.setMinWidth(20);
        incrementHeightButton.setMinHeight(30);
        incrementHeightButton.setStyle(SMALL_BUTTON_DESIGN_LEFT);
        Label H = new Label("H");
        H.setStyle("-fx-text-fill: white");
        HBox scaleHeight = new HBox(H,decrementHeightButton,imageCharHeight,incrementHeightButton);

        Label sizeLB = new Label("Scale");
        sizeLB.setStyle(LABEL_DESIGN);


        HBox sizeHbox = new HBox();

        sizeHbox.getChildren().addAll(sizeLB,scaleWidth,scaleHeight);
        sizeHbox.setPadding(new Insets(50,0,0,0));
        sizeHbox.setSpacing(10);
        sizeHbox.setAlignment(Pos.CENTER_LEFT);

        //Font to ascii converter controls
        VBox enterTextVbox = new VBox();

        enterText = new TextField();
        enterText.setPrefWidth(350);
        enterText.setStyle("-fx-background-color: Black; -fx-text-fill: Dimgray; -fx-background-radius: 0");
        enterText.setMinHeight(26);

        fontsCB = new ComboBox<>();
        fontsCB.setPrefWidth(100);
        fontsCB.setStyle("-fx-background-color: Black; -fx-text-fill: Dimgray; -fx-background-radius: 0; -fx-border-width:0 0 0 1; -fx-border-color: Dimgray;");
        fontsCB.setMinHeight(24);

        Label enterTextLB = new Label("Enter Text");
        enterTextLB.setStyle("-fx-text-fill: White; -fx-font: 24 arial;");

        HBox enterTextAndLabel = new HBox(enterText,fontsCB);

        enterTextVbox.getChildren().addAll(enterTextLB, enterTextAndLabel);
        enterTextVbox.setPadding(new Insets(50,0,0,0));

        output = new TextArea();
        output.setMinWidth(2000);
        output.setMinHeight(480);
        output.setStyle("-fx-opacity: 40%");

        //positioning the items
        vbox.getChildren().addAll(convertImageHbox,sizeHbox,enterTextVbox);
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(vbox);
        borderPane.setRight(output);
        borderPane.setPadding(new Insets(0,0,0,20));
        vbox.setMaxWidth(500);




        root = borderPane;

    }

    @Override
    public Parent getRoot() {
        return root;
    }

    public TextField getImageCharWidth() {
        return imageCharWidth;
    }

    public void setImageCharWidth(TextField imageCharWidth) {
        this.imageCharWidth = imageCharWidth;
    }

    public TextField getImageCharHeight() {
        return imageCharHeight;
    }

    public void setImageCharHeight(TextField imageCharHeight) {
        this.imageCharHeight = imageCharHeight;
    }

    public TextField getEnterText() {
        return enterText;
    }

    public void setEnterText(TextField enterText) {
        this.enterText = enterText;
    }

    public TextArea getOutput() {
        return output;
    }

    public void setOutput(TextArea output) {
        this.output = output;
    }

    public Button getFileBrowser() {
        return fileBrowser;
    }

    public void setFileBrowser(Button fileBrowser) {
        this.fileBrowser = fileBrowser;
    }

    public ComboBox<String> getFontsCB() {
        return fontsCB;
    }

    public void setFontsCB(ComboBox<String> fontsCB) {
        this.fontsCB = fontsCB;
    }

    public Label getSelectedImageName() {
        return selectedImageName;
    }

    public void setSelectedImageName(Label selectedImageName) {
        this.selectedImageName = selectedImageName;
    }

    public Button getIncrementWidthButton() {
        return incrementWidthButton;
    }

    public void setIncrementWidthButton(Button incrementWidthButton) {
        this.incrementWidthButton = incrementWidthButton;
    }

    public Button getDecrementWidthButton() {
        return decrementWidthButton;
    }

    public void setDecrementWidthButton(Button decrementWidthButton) {
        this.decrementWidthButton = decrementWidthButton;
    }

    public Button getIncrementHeightButton() {
        return incrementHeightButton;
    }

    public void setIncrementHeightButton(Button incrementHeightButton) {
        this.incrementHeightButton = incrementHeightButton;
    }

    public Button getDecrementHeightButton() {
        return decrementHeightButton;
    }

    public void setDecrementHeightButton(Button decrementHeightButton) {
        this.decrementHeightButton = decrementHeightButton;
    }
}
