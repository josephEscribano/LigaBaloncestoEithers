package quevedo.common.modelos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UsuarioUpdateDTO {
    private String idUsuario;
    private String userName;
    private String correo;
}
