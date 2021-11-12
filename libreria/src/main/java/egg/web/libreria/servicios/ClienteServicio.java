/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.servicios;

import egg.web.libreria.entidades.Cliente;
import egg.web.libreria.errores.ErrorServicio;
import egg.web.libreria.repositorios.ClienteRepositorio;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio {
    
    @Autowired
    ClienteRepositorio clienteRepositorio;
    
    public void validarCliente(Long dni, String nombre, String apellido, String telefono, Boolean alta) throws ErrorServicio{
        if (dni == null || dni <= 0) {
            throw new ErrorServicio("DNI nulo o invalido");
        }
        if (nombre.isEmpty() || nombre == null) {
            throw new ErrorServicio("El nombre es nulo o invalido");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new ErrorServicio("El apellido es nulo o invalido");
        }
        if (telefono.isEmpty() || telefono == null) {
            throw new ErrorServicio("El telefono es nulo o invalido");
        }
        if (alta == null) {
            throw new ErrorServicio("El valor del alta es nulo");
        }
    }
    
    @Transactional
    public void registrarCliente(Long dni, String nombre, String apellido, String telefono, Boolean alta) throws ErrorServicio{
        validarCliente(dni, nombre, apellido, telefono, alta);
        
        Cliente cliente = new Cliente();
        cliente.setAlta(alta);
        cliente.setApellido(apellido);
        cliente.setDni(dni);
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        
        clienteRepositorio.save(cliente);
    }
    
    @Transactional
    public void modificarCliente(String id, Long dni, String nombre, String apellido, String telefono, Boolean alta) throws ErrorServicio{
        validarCliente(dni, nombre, apellido, telefono, alta);
        Cliente cliente = clienteRepositorio.getById(id);
        
        cliente.setAlta(alta);
        cliente.setApellido(apellido);
        cliente.setDni(dni);
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        
        clienteRepositorio.save(cliente);
        
    }
    
    @Transactional
    public void modificarAlta(String id, Boolean alta) throws ErrorServicio{
        if(alta != null){
            Cliente cliente = clienteRepositorio.getById(id);
            cliente.setAlta(alta);
            clienteRepositorio.save(cliente);
        }else{
            throw new ErrorServicio("El valor de alta no debe ser nulo");
        }
    }
    
    public List<Cliente> listarClientes(){
        return clienteRepositorio.findAll();
    }
    
    public Cliente buscarClientePorId(String id){
        return clienteRepositorio.getById(id);
    }
    
    public Cliente buscarClientePorNombre(String nombre){
        return clienteRepositorio.buscarClientePorNombre(nombre);
    }
}
