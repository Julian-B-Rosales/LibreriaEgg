/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.servicios;

import egg.web.libreria.entidades.Cliente;
import egg.web.libreria.entidades.Libro;
import egg.web.libreria.entidades.Prestamo;
import egg.web.libreria.errores.ErrorServicio;
import egg.web.libreria.repositorios.ClienteRepositorio;
import egg.web.libreria.repositorios.LibroRepositorio;
import egg.web.libreria.repositorios.PrestamoRepositorio;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrestamoServicio {

    @Autowired
    PrestamoRepositorio prestamoRepositorio;

    @Autowired
    LibroRepositorio libroRepositorio;

    @Autowired
    ClienteRepositorio clienteRepositorio;

    public void validarPrestamo(Date fechaPrestamo, Date fechaDevolucion, Boolean alta, String tituloLibro, String nombreCliente) throws ErrorServicio {
        if (fechaPrestamo == null) {
            throw new ErrorServicio("La fecha de prestamo no puede ser nula");
        }
        if (fechaDevolucion != null) {
            if (fechaDevolucion.before(fechaPrestamo)) {
                throw new ErrorServicio("La fecha de devolucion no puede ser anterior a la del prestamo");
            }
        }
        if (alta == null) {
            throw new ErrorServicio("El valor del alta no debe ser nulo");
        }
        Libro libro = libroRepositorio.buscarLibroPorTitulo(tituloLibro);
        Cliente cliente = clienteRepositorio.buscarClientePorNombre(nombreCliente);

        if (libro == null) {
            throw new ErrorServicio("El libro no existe o no se encuentra. Si no esta registrado complete este paso antes de pedir el prestamo");
        }
        if (cliente == null) {
            throw new ErrorServicio("El cliente no existe o no se encuentra. Si no esta registrado complete este paso antes de pedir el prestamo");
        }

    }

    @Transactional
    public void registrarPrestamo(Date fechaPrestamo, Date fechaDevolucion, Boolean alta, String tituloLibro, String nombreCliente) throws ErrorServicio {
        validarPrestamo(fechaPrestamo, fechaDevolucion, alta, tituloLibro, nombreCliente);
        
        Prestamo prestamo = new Prestamo();
        
        prestamo.setCliente(clienteRepositorio.buscarClientePorNombre(nombreCliente));
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setAlta(alta);
        prestamo.setLibro(libroRepositorio.buscarLibroPorTitulo(tituloLibro));
        
        prestamoRepositorio.save(prestamo);
    }
    
    @Transactional
    public void modificarPrestamo(String id, Date fechaPrestamo, Date fechaDevolucion, Boolean alta, String tituloLibro, String nombreCliente) throws ErrorServicio {
        validarPrestamo(fechaPrestamo, fechaDevolucion, alta, tituloLibro, nombreCliente);
        Prestamo prestamo = prestamoRepositorio.getById(id);
        
        prestamo.setCliente(clienteRepositorio.buscarClientePorNombre(nombreCliente));
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setAlta(alta);
        prestamo.setLibro(libroRepositorio.buscarLibroPorTitulo(tituloLibro));
        
        prestamoRepositorio.save(prestamo);
    }
    
    @Transactional
    public void altaPrestamo(String id, Boolean alta) throws ErrorServicio{
        if (alta != null) {
            Prestamo prestamo = prestamoRepositorio.getById(id);
            prestamo.setAlta(alta);
            
            prestamoRepositorio.save(prestamo);
       }else{
            throw new ErrorServicio("El valor del alta no puede ser nulo");
        }
    }
    
    public List<Prestamo> listarPrestamos(){
        return prestamoRepositorio.findAll();
    }
    
    public List<Prestamo> listarPrestamosPorCliente(String nombre, String apellido){
        return prestamoRepositorio.listarPrestamosPorCliente(nombre, apellido);
    }
    
    public Prestamo buscarPrestamoPorID(String id){
        return prestamoRepositorio.getById(id);
    }
}
