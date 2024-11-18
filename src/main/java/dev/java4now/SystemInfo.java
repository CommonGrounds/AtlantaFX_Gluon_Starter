package dev.java4now;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.gluonhq.attach.device.DeviceService;
import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.keyboard.KeyboardService;
import com.gluonhq.attach.orientation.OrientationService;
import javafx.geometry.Dimension2D;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

public class SystemInfo {

    public static Dimension2D dimension = new Dimension2D(800, 600);
    public static String platform = "Android";
    public  static Notification msg;

    public static String javaVersion() {
        return System.getProperty("java.version");
    }
    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }

    //------------------------------------------------------------------
    public static void get_platform(){
        DeviceService.create().map(deviceService -> {
                    platform = deviceService.getPlatform();
                    System.out.println("platform: " + platform);
                    return 1;
                })
                .orElseGet(() -> {
                    System.out.println("DeviceService nema - DESKTOP");
                    return 0;
                });
    }


    //----------------------------------------------------
    public static void get_orientation(){
        // With the orientation service you can detect whether the device is currently oriented horizontally or vertically.
        OrientationService.create().ifPresent(service -> {
            Orientation orientation = service.getOrientation().get();
            System.out.println("Current orientation: " + orientation.name());
            service.orientationProperty().addListener((obs, ov, nv) -> {
                System.out.println("Current orientation: " + nv.name());
//                get_display();
            });
        });
    }


    //------------------------------------------------------------------
    public static void get_display(){
        DisplayService.create().ifPresent(service -> {
            dimension = service.getDefaultDimensions();
            System.out.printf("Screen resolution: %.0fx%.0f\n", dimension.getWidth(), dimension.getHeight());
        });
    }


    //------------------------------------------------------------------
    public static void get_keyboard(){
        KeyboardService.create().ifPresent(service -> {
            System.out.println("KeyboardService: dostupan");
//                service.keepVisibilityForNode(stackPane);       // pomera sve widgete na gore
//                service.keepVisibilityForNode(search_txt);      // nema efekta
            service.visibleHeightProperty().addListener((obs, ov, nv) ->
                    System.out.println("height: " + nv));
        });
    }


    // ----------------- MAIN SCREEN NOTIFICATION ----------------------
    public static Notification notification() {

        msg = new Notification(
                "",
                new FontIcon(Material2OutlinedAL.HELP_OUTLINE)
        );
        msg.getStyleClass().addAll(
                Styles.ELEVATED_1 , Styles.ACCENT                   // .message css je default
        );
        msg.setPrefHeight(20/*Region.USE_PREF_SIZE*/);
        msg.setMaxHeight(20/*Region.USE_PREF_SIZE*/);
        StackPane.setAlignment(msg, Pos.TOP_CENTER);              // Vazno
        msg.setVisible(false);

        return msg;
    }

    public static void show_notification(String message,String style){
        if(!message.isEmpty() && msg != null){
            msg.setMessage(message);
            msg.getStyleClass().remove(msg.getStyleClass().size()-1);
            msg.getStyleClass().add(style);
            msg.setVisible(true);
            var in = Animations.slideInDown(msg, Duration.millis(250));
            in.playFromStart();

            msg.setOnClose(e -> {
                var out = Animations.slideOutUp(msg, Duration.millis(250));
                out.setOnFinished(f -> msg.setVisible(false));
                out.playFromStart();
//            modalPane.hide();
            });
        }
    }
}