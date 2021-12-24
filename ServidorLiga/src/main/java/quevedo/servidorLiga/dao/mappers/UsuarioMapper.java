package quevedo.servidorLiga.dao.mappers;


import quevedo.common.modelos.UsuarioDTO;
import quevedo.common.modelos.UsuarioRegistroDTO;
import quevedo.servidorLiga.dao.modelos.Usuario;


public class UsuarioMapper {

    public UsuarioDTO usuarioDTOMapper(Usuario usuario) {
        return new UsuarioDTO(usuario.getIdUsuario(), usuario.getUserName(),usuario.getCorreo(), usuario.getIdTipoUsuario());
    }




}
