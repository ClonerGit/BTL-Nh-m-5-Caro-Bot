module your.project.name {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires transitive javafx.fxml;

    exports main;
    exports ui;
    exports gamestates;
    exports utils;
    exports menu;
    exports algorithm;
    exports model;
}
