package quevedo.servidorLiga.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import quevedo.common.modelos.Equipo;
import quevedo.common.modelos.Partido;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PartidoMapper implements RowMapper<Partido> {
    @Override
    public Partido mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Partido(rs.getString(1), rs.getString(2),
                new Equipo(rs.getString(3), rs.getString(4))
                , new Equipo(rs.getString(5), rs.getString(6))
                , rs.getString(7));
    }
}
