package quevedo.servidorLiga.dao.utils;

public class Querys {
    public static final String SELECT_FROM_USUARIOS = "select * from usuarios";
    public static final String INSERT_USUARIO_REGISTRO = "INSERT INTO usuarios (username,correo,pass,codActivacion,confirmacion,idTipoUsuario,fechaLimite) values (?,?,?,?,?,?,?)";
    public static final String UPDATE_CONFIRMACION = "update usuarios set confirmacion = ? where codActivacion = ? ";
    public static final String SELECT_NAME_POR_CODIGO = "select userName from usuarios where codActivacion = ? ";
    public static final String CHECK_USERNAME_CORREO = "select count(*) from usuarios where userName = ? or correo = ? ";
    public static final String SELECT_FECHA_LIMITE = "select fechaLimite from usuarios where codActivacion = ?";
    public static final String CAMBIAR_FECHA = "update usuarios set fechaLimite = ? where codActivacion = ? ";
    public static final String UPDATE_USUARIOS = "update usuarios set username = ?,correo = ? where idUsuario = ?";
    public static final String SELECT_USUARIO_POR_ID = "select * from usuarios where idUsuario = ?";
    public static final String DELETE_USUARIO = "delete from usuarios where idUsuario = ? ";
}
