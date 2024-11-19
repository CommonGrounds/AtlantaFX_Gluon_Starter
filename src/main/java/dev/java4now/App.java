package dev.java4now;

import atlantafx.base.layout.InputGroup;

import atlantafx.base.theme.*;
import atlantafx.base.theme.Styles;

import java.util.Objects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;
import atlantafx.base.controls.ModalPane;

public class App extends Application {

    static final boolean SHOW_KEYBOARD = true;
    static final String ASSETS_DIR = "/assets/";
    static final String APP_ICON_PATH = Objects.requireNonNull(App.class.getResource(ASSETS_DIR + "icons/app-icon.png")).toExternalForm();

    Scene scene;
    StackPane stackPane;
    ModalPane modalPane;
    TextField search_txt;

    //    Theme theme = new PrimerLight();
//    Theme theme = new PrimerDark();
//    Theme theme = new Dracula();
    Theme theme = new CupertinoDark();
//    Theme theme = new CupertinoLight();
//    Theme theme = new NordLight();
//    Theme theme = new NordDark();

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        System.out.println("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        SystemInfo.get_platform();

        if (!SHOW_KEYBOARD) {
            Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());  // TODO - mobile softkeybord error ( -37.81818 ) ali ima custom ikone na text kontroli
        }

        SystemInfo.get_display();
        SystemInfo.get_orientation();

        modalPane = new ModalPane();
        modalPane.displayProperty().addListener((obs, old, val) -> {
            if (!val) {
                modalPane.setAlignment(Pos.CENTER);
                modalPane.usePredefinedTransitionFactories(null);
            }
        });
        modalPane.setId("modalPane");

        stackPane = new StackPane(modalPane, createWelcomePane(),SystemInfo.notification()); // notification create and add to root stack pane
        scene = new Scene(stackPane, SystemInfo.dimension.getWidth(), SystemInfo.dimension.getHeight());

//        scene.getStylesheets().add(ASSETS_DIR + "index.css");       // moze za native image ali ne moze za javafx:run
        // TODO - ovako nema custom ikone na text kontroli
        if (SHOW_KEYBOARD) {
            scene.getStylesheets().add(theme.getUserAgentStylesheet());  // 1. dodajemo glavnu theme a dole index posle
        }
        scene.getStylesheets().add("index.css");                     // prebaciti index.css u resource root folder kao i fonts folder

        stage.setScene(scene);
        stage.setTitle("AtlantaFX");
        stage.getIcons().add(new Image(APP_ICON_PATH));
        stage.setOnCloseRequest(t -> Platform.exit());

        Platform.runLater(() -> {
            stage.show();
            SystemInfo.get_keyboard();
        });
    }


    //---------------------------------------------------
    private BorderPane createWelcomePane() {
        var center_pane = new VBox();
        center_pane.getStyleClass().add("welcome");
        center_pane.setSpacing(20);
        center_pane.setAlignment(Pos.CENTER);

        var leftDialog = make_left_dialog();
        var settingsDialog = make_settings_dialog();
        var searchDialog = make_search_dialog();

        var worksBtn = new Button("It Works", new FontIcon(Feather.THUMBS_UP));
        worksBtn.getStyleClass().add(Styles.ACCENT);
        worksBtn.setContentDisplay(ContentDisplay.RIGHT);

        FontIcon icon = new FontIcon(MaterialDesignS.SCHOOL);
        icon.getStyleClass().add("shool");

        center_pane.getChildren().addAll(
                worksBtn,
                icon,
                new Label(
                        "Hi, this is the AtlantaFX starter project. Check out the README for a quick start and happy coding.")
        );

//-------------------------------- MENU BAR -------------------------------------
        var menuBtn = new Button(null, new FontIcon(Feather.MENU));
        menuBtn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.FLAT
        );
        menuBtn.setOnAction(evt -> {
            modalPane.setAlignment(Pos.TOP_LEFT);
            modalPane.usePredefinedTransitionFactories(Side.LEFT);
            modalPane.show(leftDialog);
        });

        var searchBtn = new Button(null, new FontIcon(Feather.SEARCH));
        searchBtn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.FLAT
        );
        searchBtn.setOnAction(evt -> {
//            searchBtn.requestFocus();
            modalPane.setAlignment(Pos.TOP_CENTER);
            modalPane.usePredefinedTransitionFactories(Side.TOP);
            modalPane.show(searchDialog);
//            searchBtn.requestFocus();
        });

        var settingsBtn = new Button(null, new FontIcon(Feather.SETTINGS));
        settingsBtn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.FLAT
        );
        settingsBtn.setOnAction(evt -> {
            modalPane.setAlignment(Pos.TOP_RIGHT);
            modalPane.usePredefinedTransitionFactories(Side.RIGHT);
            modalPane.show(settingsDialog);
        });

        var left_app_top_bar = new HBox(menuBtn);
        left_app_top_bar.setAlignment(Pos.TOP_LEFT);

        Label lbl = new Label("Title");
        var center_app_top_bar = new HBox(lbl);
        center_app_top_bar.setAlignment(Pos.CENTER);

        var right_app_top_bar = new HBox(searchBtn, settingsBtn);
        right_app_top_bar.setAlignment(Pos.TOP_RIGHT);

        var app_top_bar = new HBox(left_app_top_bar, center_app_top_bar, right_app_top_bar);
//        app_top_bar.setStyle("-fx-padding: 10px;-fx-background-color:rgba(255, 0, 0, 0.1);");

//        var smallSep = new javafx.scene.control.Separator(Orientation.HORIZONTAL);
//        smallSep.getStyleClass().add(Styles.SMALL);
//        var app_top_bar_v = new  VBox(app_top_bar, smallSep);

        var border_pane = new BorderPane() {
            @Override
            protected void layoutChildren() {
                left_app_top_bar.setPrefWidth(getWidth() / 3);
                center_app_top_bar.setPrefWidth(getWidth() / 3);
                right_app_top_bar.setPrefWidth(getWidth() / 3);
                app_top_bar.setPrefWidth(getWidth());
                leftDialog.setMaxWidth(getWidth() / 2);
                super.layoutChildren();
            }
        };
        border_pane.setTop(app_top_bar);
        BorderPane.setAlignment(app_top_bar, javafx.geometry.Pos.TOP_CENTER);
        border_pane.setCenter(center_pane);
        BorderPane.setAlignment(center_pane, Pos.CENTER);
        border_pane.setPadding(new Insets(10, 0, 0, 0));

        return border_pane;
    }


    //------------------------------------------------------------------
    private VBox make_left_dialog() {
        var root = new VBox();
//        leftDialog.setHeight(-1);
        var btn = new Button("Close");
        btn.setOnAction(evt -> {
            modalPane.hide(true);
        });
        root.getChildren().add(btn);//.getDialogPane().setContent(lbl);
        root.setAlignment(Pos.CENTER);
        if (theme.isDarkMode()) {
            root.setBackground(new Background(new BackgroundFill(Color.rgb(20, 20, 20, 1), null, null)));
        } else {
            root.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235, 1), null, null)));
        }
        return root;
    }


    //------------------------------------------------------------------
    private BorderPane make_settings_dialog() {
        var root = new BorderPane();
        root.setPrefWidth(SystemInfo.dimension.getWidth());

        var back_btn = new Button(null, new FontIcon(Feather.ARROW_RIGHT));
        back_btn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.LARGE);
        back_btn.setOnAction(evt -> {
            modalPane.hide(true);
        });

        var app_top_bar = new HBox(back_btn);
        app_top_bar.setPrefWidth(SystemInfo.dimension.getWidth());
        app_top_bar.setAlignment(Pos.CENTER);

        var vBox = new VBox(new Label("Settings"));
        vBox.setAlignment(Pos.CENTER);

        root.setTop(app_top_bar);
        BorderPane.setAlignment(app_top_bar, javafx.geometry.Pos.TOP_CENTER);
        root.setCenter(vBox);
        BorderPane.setAlignment(vBox, Pos.CENTER);
        root.setPadding(new Insets(10, 0, 0, 0));

        if (theme.isDarkMode()) {
            root.setBackground(new Background(new BackgroundFill(Color.rgb(20, 20, 20, 1), null, null)));
        } else {
            root.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235, 1), null, null)));
        }
        return root;
    }


    //------------------------------------------------------------------
    private HBox make_search_dialog() {

        var root = new HBox();

        search_txt = new TextField();
        search_txt.setOnAction(evt -> {    // enter
            System.out.println("Search");
            modalPane.hide(true);
            SystemInfo.show_notification("Nothing to search", Styles.WARNING);  // notifikacija na main screen
        });
        search_txt.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        search_txt.requestFocus();
                    }
                });
            }
        });

        var leftBtn = new Button("", new FontIcon(Feather.SEARCH));
        leftBtn.setCursor(Cursor.HAND);
        leftBtn.getStyleClass().addAll(Styles.BUTTON_ICON/*,Styles.SMALL*/);
        leftBtn.setOnAction(e -> {
            System.out.println("Search");
            modalPane.hide(true);
            SystemInfo.show_notification("Nothing to search", Styles.WARNING);
        });
        var group = new InputGroup(leftBtn, search_txt);
        HBox.setHgrow(search_txt, Priority.ALWAYS);

        root.setMaxWidth(SystemInfo.dimension.getWidth());
        root.setMaxHeight(search_txt.getHeight() + 10);

        root.getChildren().add(group);
        root.setAlignment(Pos.CENTER);

        if (theme.isDarkMode()) {
            root.setBackground(new Background(new BackgroundFill(Color.rgb(20, 20, 20, 1), null, null)));
        } else {
            root.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235, 1), null, null)));
        }

        return root;
    }


    //------------------------------------------------------------------
    public static void main(String[] args) {
        launch();
    }
}