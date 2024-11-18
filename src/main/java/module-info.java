module com.mycompany.atlantafx_gluon {
    requires javafx.controls;
    requires atlantafx.base;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.kordamp.ikonli.feather;
        requires org.kordamp.ikonli.core;
    // add icon pack modules
    requires org.kordamp.ikonli.fontawesome5;
    requires com.gluonhq.attach.device;
    requires com.gluonhq.attach.display;
    requires java.desktop;
    requires com.gluonhq.attach.keyboard;
    requires com.gluonhq.attach.orientation;
    requires org.kordamp.ikonli.material2;

    exports dev.java4now;
}
