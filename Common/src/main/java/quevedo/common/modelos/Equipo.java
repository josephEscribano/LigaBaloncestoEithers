package quevedo.common.modelos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Equipo {
    private String idEquipo;
    private String nombreEquipo;

    @Override
    public String toString() {
        return "idEquipo='" + idEquipo + '\'' +
                ", nombreEquipo='" + nombreEquipo;
    }
}
