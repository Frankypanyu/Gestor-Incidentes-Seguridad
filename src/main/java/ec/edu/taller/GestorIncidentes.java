package ec.edu.taller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/** Administra incidentes por prioridad (menor número = mayor urgencia). */
public class GestorIncidentes {

    private final Queue<IncidenteSeguridad> colaIncidentes;
    private final int capacidadMaxima;
    private final Map<IncidenteSeguridad, Long> ordenRegistro;
    private final Comparator<IncidenteSeguridad> criterioPrioridad;
    private long siguienteOrden;

    public GestorIncidentes(int capacidadMaxima) {
        if (capacidadMaxima < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa");
        }
        this.capacidadMaxima = capacidadMaxima;
        this.ordenRegistro = new IdentityHashMap<>();
        this.criterioPrioridad = Comparator
                .comparingInt(IncidenteSeguridad::getPrioridad)
                .thenComparingLong(incidente -> ordenRegistro.get(incidente));
        this.colaIncidentes = new PriorityQueue<>(criterioPrioridad);
        this.siguienteOrden = 0;
    }

    /** Registra solo incidentes válidos, no repetidos y cuando existe espacio. */
    public boolean registrarIncidente(IncidenteSeguridad incidente) {
        if (incidente == null || !codigoValido(incidente.getCodigo())
                || contarIncidentesPendientes() >= capacidadMaxima
                || existeIncidente(incidente.getCodigo())) {
            return false;
        }

        ordenRegistro.put(incidente, siguienteOrden++);
        colaIncidentes.offer(incidente);
        return true;
    }

    /** Solo busca entre los incidentes todavía pendientes. */
    public boolean existeIncidente(String codigo) {
        if (!codigoValido(codigo)) {
            return false;
        }
        for (IncidenteSeguridad incidente : colaIncidentes) {
            if (codigo.equals(incidente.getCodigo())) {
                return true;
            }
        }
        return false;
    }

    /** Muestra el próximo incidente sin retirarlo ni cambiar su estado. */
    public IncidenteSeguridad consultarSiguienteIncidente() {
        return colaIncidentes.peek();
    }

    /** Retira el incidente más urgente y marca su estado como ATENDIDO. */
    public IncidenteSeguridad atenderSiguienteIncidente() {
        IncidenteSeguridad atendido = colaIncidentes.poll();
        if (atendido != null) {
            ordenRegistro.remove(atendido);
            atendido.setEstado("ATENDIDO");
        }
        return atendido;
    }

    public int contarIncidentesPendientes() {
        return colaIncidentes.size();
    }

    public int consultarEspaciosDisponibles() {
        return capacidadMaxima - colaIncidentes.size();
    }

    /** Copia independiente, ordenada por prioridad y por llegada en caso de empate. */
    public List<IncidenteSeguridad> listarIncidentes() {
        List<IncidenteSeguridad> resultado = new ArrayList<>(colaIncidentes);
        resultado.sort(criterioPrioridad);
        return resultado;
    }

    private boolean codigoValido(String codigo) {
        return codigo != null && !codigo.trim().isEmpty();
    }
}
