package quevedo.common.modelos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Partido {
    private String idPartido;
    private String idJornada;
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private String resultado;

}
