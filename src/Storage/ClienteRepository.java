package src.Storage;

import src.modelo.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    private List<Cliente> clientes;
    private static ClienteRepository instancia;
    private int nextId;

    private ClienteRepository() {
        this.clientes = new ArrayList<>();
        this.nextId = 1;
        inicializarClientesDemo();
    }

    public static ClienteRepository getInstance() {
        if (instancia == null) {
            instancia = new ClienteRepository();
        }
        return instancia;
    }

    private void inicializarClientesDemo() {
        Cliente[] clientesDemo = {
                new Cliente("CLI-001", "Juan Pérez", "001-1234567-8", "809-555-1111", "Calle 1", "juan@gmail.com"),
                new Cliente("CLI-002", "María García", "002-2345678-9", "809-555-2222", "Calle 2", "maria@gmail.com"),
                new Cliente("CLI-003", "Pedro Martínez", "003-3456789-0", "809-555-3333", "Calle 3", "pedro@gmail.com"),
                new Cliente("CLI-004", "Ana Rodríguez", "004-4567890-1", "809-555-4444", "Calle 4", "ana@gmail.com"),
                new Cliente("CLI-005", "Consumidor Final", "000-0000000-0", "N/A", "N/A", "final@cliente.com")
        };

        for (Cliente c : clientesDemo) {
            c.setId(nextId++);
            clientes.add(c);
        }
    }

    public boolean agregarCliente(Cliente cliente) {
        if (cliente == null)
            return false;
        if (cliente.getId() <= 0)
            cliente.setId(nextId++);
        clientes.add(cliente);
        return true;
    }

    public List<Cliente> obtenerTodos() {
        return new ArrayList<>(clientes);
    }

    public Cliente buscarPorId(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id)
                return c;
        }
        return null;
    }

    public Cliente buscarPorDocumento(String documento) {
        for (Cliente c : clientes) {
            if (c.getDocumento().equals(documento))
                return c;
        }
        return null;
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        List<Cliente> resultados = new ArrayList<>();
        for (Cliente c : clientes) {
            if (c.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(c);
            }
        }
        return resultados;
    }
}