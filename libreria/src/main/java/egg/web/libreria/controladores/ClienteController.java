/*
    
 */
package egg.web.libreria.controladores;

import egg.web.libreria.entidades.Cliente;
import egg.web.libreria.errores.ErrorServicio;
import egg.web.libreria.servicios.ClienteServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    
    @Autowired
    ClienteServicio clienteServicio;
    
    @GetMapping("")
    public String clientes(ModelMap modelo){
        List<Cliente> clientes = clienteServicio.listarClientes();
        modelo.put("clientes", clientes);
        return "clientes.html";
    }
    
    @GetMapping("/mas-info/{id}")
    public String masInfo(ModelMap modelo, @PathVariable String id){
        Cliente cliente = clienteServicio.buscarClientePorId(id);
        modelo.put("cliente", cliente);
        return "mas-info.html";
    }
    
    @GetMapping("/clientes-baja")
    public String clientesBaja(ModelMap modelo){
        List<Cliente> clientes = clienteServicio.listarClientes();
        modelo.put("clientes", clientes);
        return "clientes-baja.html";
    }
    
    @GetMapping("/agregar-cliente")
    public String agregarCliente(){
        return "form-cliente.html";
    }
    
    @PostMapping("/agregar-cliente")
    public String agregarCliente(ModelMap modelo, @RequestParam @Nullable Long dni, @RequestParam @Nullable String nombre, @RequestParam @Nullable String apellido, @RequestParam @Nullable String telefono, @RequestParam @Nullable Boolean alta){
        if (alta == null) {
            alta = false;
        }
        try{
            clienteServicio.registrarCliente(dni, nombre, apellido, telefono, alta);
            modelo.put("titulo", "Cliente agregado exitosamente");  
            return "exito.html";
        } catch (ErrorServicio ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            modelo.put("dni", dni);
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("telefono", telefono);
            return "form-cliente.html";
        }
    }
    
    @GetMapping("/dar-de-baja-cliente")
    public String bajaCliente(String id, ModelMap modelo) {
        try {
            clienteServicio.modificarAlta(id, Boolean.FALSE);
            modelo.put("titulo", "Cliente dado de baja");
            return "exito";
        } catch (Exception ex) {
            
            modelo.put("titulo", "Error");
            modelo.put("error", ex.getMessage());
            return "fracaso";
        }
    }
    
    @GetMapping("/dar-de-alta-cliente")
    public String revivirCliente(String id, ModelMap modelo) {
        try {
            clienteServicio.modificarAlta(id, Boolean.TRUE);
            modelo.put("titulo", "Cliente dado de alta");
            return "exito";
        } catch (Exception ex) {
            
            modelo.put("titulo", "Error");
            modelo.put("error", ex.getMessage());
            return "fracaso";
        }
    }
    
    @GetMapping("/modificar-cliente/{id}")
    public String modificarCliente(@PathVariable String id, ModelMap modelo) throws ErrorServicio {
        Cliente cliente = clienteServicio.buscarClientePorId(id);
        modelo.addAttribute("cliente", cliente);
        return "form-mod-cliente.html";

    }

    @PostMapping("/modificar-cliente/{id}")
    public String modificarCliente(@PathVariable String id, ModelMap modelo, @RequestParam @Nullable Long dni, @RequestParam @Nullable String nombre, @RequestParam @Nullable String apellido, @RequestParam @Nullable String telefono, @RequestParam @Nullable Boolean alta) {
        
        try {
            if (alta == null) {
                alta = false;
            }else{
                alta = true;
            }
            clienteServicio.modificarCliente(id, dni, nombre, apellido, telefono, alta);
            modelo.put("titulo", "Cliente modificado exitosamente!");
            return "exito";
        } catch (ErrorServicio ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            
            modelo.put("titulo", "Error");
            modelo.put("error", ex.getMessage());
            return "fracaso";
        }
    }
    
}
