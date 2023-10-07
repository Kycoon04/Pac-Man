module cr.ac.una.pac.man {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.base;
    
    opens cr.ac.una.pac.man.controller;
    opens cr.ac.una.pac.man;
    exports cr.ac.una.pac.man;
}
