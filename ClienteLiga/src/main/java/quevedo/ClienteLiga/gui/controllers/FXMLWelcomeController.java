package quevedo.ClienteLiga.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import quevedo.ClienteLiga.gui.utils.ConstantesGUI;
import quevedo.common.modelos.UsuarioDTO;

public class FXMLWelcomeController {


    @FXML
    private Label fxWelcomeTitle;


    public void setLogin(UsuarioDTO usuarioDTO) {
        fxWelcomeTitle.setText(ConstantesGUI.MENSAJE_BIENVENIDA + usuarioDTO.getUserName());
    }


}
