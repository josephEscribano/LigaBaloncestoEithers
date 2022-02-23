package quevedo.servidorLiga.dao.utils;

public class Querys {
    //USUARIO
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
    public static final String UPDATE_CODCAMBIO_USUARIOS = "update usuarios set codCambio = ?,fechaLimite = ? where idUsuario = ?";
    public static final String SELECT_FECHA_CODCAMBIO = "select fechaLimite from usuarios where codCambio = ? ";
    public static final String UPDATE_PASS = "update usuarios set pass = ? where codCambio = ?";
    public static final String SELECT_CORREO = "select correo from usuarios where codActivacion = ?";
    public static final String SELECT_LOGIN = "select * from usuarios where userName = ? and pass = ?";
    public static final String SELECT_PASS = "select pass from usuarios where userName = ?";
    public static final String SELECT_EXISTE_USERNAME = "select count(*) from usuarios where userName = ?";

    //EQUIPO
    public static final String SELECT_FROM_EQUIPOS = "select * from equipos";
    public static final String INSERT_EQUIPO = "INSERT INTO equipos (nombreEquipo) values (?)";
    public static final String UPDATE_EQUIPO = "update equipos set nombreEquipo = ? where idEquipo = ?";
    public static final String SELECT_EQUIPO_POR_ID = "select * from equipos where idEquipo = ?";
    public static final String DELETE_EQUIPO = "delete from equipos where idEquipo = ?";

    //JORNADAS
    public static final String SELECT_FROM_JORNADAS = "Select * from jornadas";
    public static final String INSERT_JORNADA = "INSERT INTO jornadas (idJornada,fechaJornada) values (?,?)";
    public static final String UPDATE_JORNADA = "update jornadas set fechaJornada = ? where idJornada = ?";
    public static final String SELECT_JORNADA_POR_ID = "select * from jornadas where idJornada = ?";
    public static final String DELETE_JORNADA = "delete from jornadas where idJornada = ?";

    //PARTIDOS
    public static final String SELECT_FROM_PARTIDOS = "select idPartido,idJornada,e.idEquipo,e.nombreEquipo,eq.idEquipo,eq.nombreEquipo,resultado from partidos inner join (equipos e,equipos eq) on partidos.idEquipoLocal = e.idEquipo and partidos.idEquipoVisitante = eq.idEquipo";
    public static final String INSERT_PARTIDO = "INSERT INTO partidos (idJornada,idEquipoLocal,idEquipoVisitante,resultado) values (?,?,?,?)";
    public static final String UPDATE_PARTIDO = "update partidos set idJornada = ?, idEquipoLocal = ?, idEquipoVisitante = ?, resultado = ? where idPartido = ?";
    public static final String SELECT_PARTIDO_POR_ID = "select idPartido,idJornada,idEquipoLocal,e.nombreEquipo,idEquipoVisitante,eq.nombreEquipo,resultado from partidos inner join (equipos e,equipos eq) on partidos.idEquipoLocal = e.idEquipo and partidos.idEquipoVisitante = eq.idEquipo where idPartido = ?";
    public static final String DELETE_PARTIDO = "delete from partidos where idPartido = ?";
    public static final String DELETE_PARTIDO_POR_EQUIPO = "delete from partidos where idEquipoLocal = ? or idEquipoVisitante = ?";
    public static final String DELETE_PARTIDO_POR_JORNADA = "delete from partidos where idJornada = ?";
    public static final String FILTRO_EQUIPO = "select idPartido,idJornada,e.idEquipo,e.nombreEquipo,eq.idEquipo,eq.nombreEquipo,resultado from partidos inner join (equipos e,equipos eq) on partidos.idEquipoLocal = e.idEquipo and partidos.idEquipoVisitante = eq.idEquipo where e.nombreEquipo = ? or eq.nombreEquipo = ? ";
    public static final String FILTRO_JORNADA = "select idPartido,idJornada,e.idEquipo,e.nombreEquipo,eq.idEquipo,eq.nombreEquipo,resultado from partidos inner join (equipos e,equipos eq) on partidos.idEquipoLocal = e.idEquipo and partidos.idEquipoVisitante = eq.idEquipo where idJornada = ?";
    public static final String UPDATE_CODIGO = "update usuarios set codActivacion = ?,fechaLimite = ? where userName = ? and confirmacion = ?";
}
