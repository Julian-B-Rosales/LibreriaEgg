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

    @Autowired
    LibroServicio libroServicio;

    @Autowired
    ClienteServicio clienteServicio;

    public void validarPrestamo(Date fechaPrestamo, Boolean alta, String idLibro, Long dniCliente) throws ErrorServicio {
        if (fechaPrestamo == null) {
            throw new ErrorServicio("La fecha de prestamo no puede ser nula");
        }
        if (alta == null) {
            throw new ErrorServicio("El valor del alta no debe ser nulo");
        }

        Libro libro = libroRepositorio.getById(idLibro);

        if (libro == null) {
            throw new ErrorServicio("El libro no existe o no se encuentra. Si no esta registrado complete este paso antes de pedir el prestamo");
        }
        if (dniCliente == null) {
            throw new ErrorServicio("Se debe completar el valor dni del cliente");
        }

    }

    @Transactional
    public void registrarPrestamo(Date fechaPrestamo, Boolean alta, String idLibro, Long dniCliente) throws ErrorServicio {
        validarPrestamo(fechaPrestamo, alta, idLibro, dniCliente);
        Libro libro = libroServicio.buscarLibroPorID(idLibro);
        if (libro.getEjemplaresRestantes() > 0) {
            Prestamo prestamo = new Prestamo();
            Cliente cliente;
            if (clienteRepositorio.buscarClientePorDNI(dniCliente) == null) {
                throw new ErrorServicio("El cliente no se encuentra en la base de datos, ingrese sus datos antes de pedir un prestamo");
            } else {
                cliente = clienteRepositorio.buscarClientePorDNI(dniCliente);
                prestamo.setCliente(cliente);
                prestamo.setFechaPrestamo(fechaPrestamo);
                prestamo.setAlta(alta);
                libro.setEjemplaresPrestados(libro.getEjemplaresPrestados()+1);
                libro.setEjemplaresRestantes(libro.getEjemplares()-libro.getEjemplaresPrestados());
                libroRepositorio.save(libro);
                prestamo.setLibro(libro);

                prestamoRepositorio.save(prestamo);
            }

        } else {
            throw new ErrorServicio("El prestamo no se pudo registrar");
        }
    }

    @Transactional
    public void modificarPrestamo(String id, Date fechaPrestamo, Date fechaDevolucion, Boolean alta, String idLibro, Long dniCliente) throws ErrorServicio {
        validarPrestamo(fechaPrestamo, alta, idLibro, dniCliente);
        Prestamo prestamo = prestamoRepositorio.getById(id);

        prestamo.setCliente(clienteRepositorio.buscarClientePorDNI(dniCliente));
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setAlta(alta);
        prestamo.setLibro(libroRepositorio.getById(idLibro));

        prestamoRepositorio.save(prestamo);
    }

    @Transactional
    public void altaPrestamo(String id, Boolean alta) throws ErrorServicio {
        if (alta != null) {
            Prestamo prestamo = prestamoRepositorio.getById(id);
            prestamo.setAlta(alta);

            prestamoRepositorio.save(prestamo);
        } else {
            throw new ErrorServicio("El valor del alta no puede ser nulo");
        }
    }

    @Transactional
    public void registrarDevolucion(String idPrestamo, Date fechaDevolucion, Long dni) throws ErrorServicio {
        Prestamo prestamo = prestamoRepositorio.getById(idPrestamo);
        if (fechaDevolucion != null && prestamo.getCliente().getDni().equals(dni)) {
            if (fechaDevolucion.before(prestamo.getFechaPrestamo())) {
                throw new ErrorServicio("La fecha de devolucion no puede ser anterior a la del prestamo");
            } else {
                prestamo.setFechaDevolucion(fechaDevolucion);
                
                Libro libro = prestamo.getLibro();
                libro.setEjemplaresPrestados(libro.getEjemplaresPrestados()-1);
                libro.setEjemplaresRestantes(libro.getEjemplares()-libro.getEjemplaresPrestados());
                libroRepositorio.save(libro);
                
                prestamoRepositorio.save(prestamo);
            }
        }else{
            throw new ErrorServicio("Revise los datos ingresados, el dni no corresponde al cliente que pidio el prestamo o la fecha ingfresada es nula");
        }

    }

    public List<Prestamo> listarPrestamos() {
        return prestamoRepositorio.findAll();
    }

    public List<Prestamo> listarPrestamosPorCliente(String nombre, String apellido) {
        return prestamoRepositorio.listarPrestamosPorCliente(nombre, apellido);
    }

    public Prestamo buscarPrestamoPorID(String id) {
        return prestamoRepositorio.getById(id);
    }

    public List<Prestamo> buscarPrestamoPorIDLibro(String id) {
        return prestamoRepositorio.buscarPrestamoPorIDLibro(id);
    }
    
    public Prestamo buscarPrestamoPorIDLibroYDNI(String id, Long dni) {
        return prestamoRepositorio.buscarPrestamoPorIDLibroYDNI(id, dni);
    }
    
    public String devolverIDLibroDePrestamo(String idPrestamo){
        return prestamoRepositorio.devolverIDLibroDePrestamo(idPrestamo);
    }
}
