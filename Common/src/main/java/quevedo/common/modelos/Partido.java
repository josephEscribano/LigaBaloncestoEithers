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


    public Partido(String idJornada, Equipo equipoLocal, Equipo equipoVisitante, String resultado) {
        this.idJornada = idJornada;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return
                "idPartido='" + idPartido + '\'' +
                        ", idJornada='" + idJornada + '\'' +
                        ", equipoLocal=" + equipoLocal.getNombreEquipo() +
                        ", equipoVisitante=" + equipoVisitante.getNombreEquipo() +
                        ", resultado='" + resultado;
    }
}
