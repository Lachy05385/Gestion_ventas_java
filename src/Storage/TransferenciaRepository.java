package src.Storage;

import src.modelo.Transferencia;
import src.modelo.DetalleTransferencia;
import java.util.ArrayList;
import java.util.List;

public class TransferenciaRepository {
    private List<Transferencia> transferencias;
    private static TransferenciaRepository instancia;
    private int siguienteId = 1;

    private TransferenciaRepository() {
        this.transferencias = new ArrayList<>();
    }

    public static TransferenciaRepository getInstance() {
        if (instancia == null) {
            instancia = new TransferenciaRepository();
        }
        return instancia;
    }

    public boolean agregarTransferencia(Transferencia transferencia) {
        if (transferencia == null)
            return false;

        transferencia.setId(siguienteId++);

        // Generar código si no tiene
        if (transferencia.getCodigo() == null || transferencia.getCodigo().isEmpty()) {
            String codigo = "TRF-" + String.format("%04d", transferencia.getId());
            transferencia.setCodigo(codigo);
        }

        transferencias.add(transferencia);
        System.out.println("✅ Transferencia agregada: " + transferencia.getCodigo());
        return true;
    }

    public List<Transferencia> obtenerTodas() {
        return new ArrayList<>(transferencias);
    }

    public Transferencia buscarPorId(int id) {
        for (Transferencia t : transferencias) {
            if (t.getId() == id)
                return t;
        }
        return null;
    }

    public Transferencia buscarPorCodigo(String codigo) {
        for (Transferencia t : transferencias) {
            if (t.getCodigo().equalsIgnoreCase(codigo))
                return t;
        }
        return null;
    }

    public List<Transferencia> buscarPorAlmacenOrigen(int almacenId) {
        List<Transferencia> resultados = new ArrayList<>();
        for (Transferencia t : transferencias) {
            if (t.getAlmacenOrigen().getId() == almacenId) {
                resultados.add(t);
            }
        }
        return resultados;
    }

    public List<Transferencia> buscarPorAlmacenDestino(int almacenId) {
        List<Transferencia> resultados = new ArrayList<>();
        for (Transferencia t : transferencias) {
            if (t.getAlmacenDestino().getId() == almacenId) {
                resultados.add(t);
            }
        }
        return resultados;
    }

    public List<Transferencia> buscarPorEstado(String estado) {
        List<Transferencia> resultados = new ArrayList<>();
        for (Transferencia t : transferencias) {
            if (t.getEstado().equalsIgnoreCase(estado)) {
                resultados.add(t);
            }
        }
        return resultados;
    }

    public boolean actualizarTransferencia(Transferencia transferenciaActualizada) {
        for (int i = 0; i < transferencias.size(); i++) {
            if (transferencias.get(i).getId() == transferenciaActualizada.getId()) {
                transferencias.set(i, transferenciaActualizada);
                return true;
            }
        }
        return false;
    }

    public boolean eliminarTransferencia(int id) {
        for (int i = 0; i < transferencias.size(); i++) {
            if (transferencias.get(i).getId() == id) {
                transferencias.remove(i);
                return true;
            }
        }
        return false;
    }
}