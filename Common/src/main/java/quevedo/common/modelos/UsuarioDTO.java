package quevedo.common.modelos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UsuarioDTO {
    private String idUsuario;
    private String userName;
    private String correo;
    private String idTipoUsuario;

    @Override
    public String toString() {
        return "idUsuario='" + idUsuario + '\'' +
                ", userName='" + userName + '\'' +
                ", correo='" + correo + '\'' +
                ", idTipoUsuario='" + idTipoUsuario;
    }
}
