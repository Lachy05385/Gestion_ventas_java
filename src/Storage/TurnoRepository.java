package src.Storage;

import src.modelo.Turno;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TurnoRepository {
    private List<Turno> turnos;
    private static TurnoRepository instancia;
    private int nextId;

    private TurnoRepository() {
        this.turnos = new ArrayList<>();
        this.nextId = 1;
    }

    public static TurnoRepository getInstance() {
        if (instancia == null) {
            instancia = new TurnoRepository();
        }
        return instancia;
    }

    public boolean agregarTurno(Turno turno) {
        if (turno == null)
            return false;
        if (turno.getId() <= 0) {
            turno.setId(nextId++);
        }
        turnos.add(turno);
        return true;
    }

    public Turno obtenerTurnoActivoPorUsuario(int usuarioId) {
        for (Turno t : turnos) {
            if (t.getUsuario().getId() == usuarioId && t.getEstado().equals("ACTIVO")) {
                return t;
            }
        }
        return null;
    }

    public Turno obtenerTurnoActivoPorAlmacen(int almacenId) {
        for (Turno t : turnos) {
            if (t.getAlmacen().getId() == almacenId && t.getEstado().equals("ACTIVO")) {
                return t;
            }
        }
        return null;
    }

    public List<Turno> obtenerTurnosPorUsuario(int usuarioId) {
        List<Turno> resultados = new ArrayList<>();
        for (Turno t : turnos) {
            if (t.getUsuario().getId() == usuarioId) {
                resultados.add(t);
            }
        }
        return resultados;
    }

    public List<Turno> obtenerTurnosPorFecha(Date fecha) {
        List<Turno> resultados = new ArrayList<>();
        for (Turno t : turnos) {
            if (t.getFechaInicio().toString().substring(0, 10).equals(fecha.toString().substring(0, 10))) {
                resultados.add(t);
            }
        }
        return resultados;
    }
}