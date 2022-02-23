package quevedo.common.modelos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Jornada {
    private String idJornada;
    private LocalDate fechaJornada;


    public Jornada(LocalDate fechaJornada) {
        this.fechaJornada = fechaJornada;
    }

    @Override
    public String toString() {
        return "idJornada='" + idJornada + '\'' +
                ", fechaJornada=" + fechaJornada;
    }
}
