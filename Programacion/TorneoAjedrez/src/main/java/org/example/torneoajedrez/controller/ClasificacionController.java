package org.example.torneoajedrez.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import java.net.URL;
import java.util.ResourceBundle;

public class ClasificacionController implements Initializable {

    private VentanasController ventana;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        instancias();
        initGUI();
        acciones();
    }

    private void acciones()
    {
    }

    private void initGUI()
    {
    }

    private void instancias()
    {
        ventana = new VentanasController();
    }
}
