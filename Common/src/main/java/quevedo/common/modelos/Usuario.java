package quevedo.common.modelos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private String idUsuario;
    private String correo;
    private String pass;
    private String codActivacion;
    private boolean confirmacion;
    private LocalDate fechaAlta;
    private String idTipoUsuario;
}
