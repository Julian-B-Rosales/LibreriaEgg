/*
 * TODO
    Terminar pagina de inicio
 */
package egg.web.libreria.controladores;

import egg.web.libreria.entidades.Libro;
import egg.web.libreria.entidades.Prestamo;
import egg.web.libreria.errores.ErrorServicio;
import egg.web.libreria.repositorios.PrestamoRepositorio;
import egg.web.libreria.servicios.LibroServicio;
import egg.web.libreria.servicios.PrestamoServicio;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    PrestamoServicio prestamoServicio;

    @Autowired
    LibroServicio libroServicio;

    @GetMapping("/listar/{id}")
    public String prestamos(ModelMap modelo, @PathVariable String id) {
        List<Prestamo> prestamos = prestamoServicio.buscarPrestamoPorIDLibro(id);
        modelo.put("prestamos", prestamos);
        return "prestamos.html";
    }

    @GetMapping("/agregar-prestamo/{id}")
    public String nuevoPrestamo(@PathVariable String id, ModelMap modelo) throws ErrorServicio {
        Libro libro = libroServicio.buscarLibroPorID(id);
        modelo.addAttribute("libro", libro);
        return "form-prestamo.html";

    }

    @PostMapping("/agregar-prestamo/{id}")
    public String nuevoPrestamo(@PathVariable String id, ModelMap modelo, @RequestParam @Nullable Long dni, @RequestParam @Nullable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaPrestamo) throws ErrorServicio {

        try {
            prestamoServicio.registrarPrestamo(fechaPrestamo, Boolean.TRUE, id, dni);
            modelo.put("titulo", "Prestamo agregado exitosamente!");
            return "exito.html";
        } catch (ErrorServicio ex) {
            modelo.put("titulo", "Error");
            modelo.put("error", ex.getMessage());
            return "fracaso";
        }
    }

    @GetMapping("/modificar-prestamo/{id}")
    public String modificarPrestamo(@PathVariable String id, ModelMap modelo) throws ErrorServicio {
        Prestamo prestamo = prestamoServicio.buscarPrestamoPorID(id);
        modelo.addAttribute("prestamo", prestamo);
        return "form-mod-prestamo.html";

    }

    @PostMapping("/modificar-prestamo/{id}")
    public String modificarPrestamo(@PathVariable String id, ModelMap modelo, @RequestParam @Nullable Long dni, @RequestParam @Nullable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaPrestamo, @RequestParam @Nullable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDevolucion, @RequestParam @Nullable Boolean alta) {

        try {
            
            if (fechaDevolucion != null && fechaDevolucion.before(fechaPrestamo)) {
                throw new ErrorServicio("La fecha de devolucion no puede ser anterior a la fecha del prestamo");
            }
            
            alta = true;
            String idLibro = prestamoServicio.devolverIDLibroDePrestamo(id);
            
            prestamoServicio.modificarPrestamo(id, fechaPrestamo, fechaDevolucion, alta, idLibro, dni);
            
            modelo.put("titulo", "Prestamo modificado exitosamente!");
            return "exito";
        } catch (ErrorServicio ex) {
            modelo.put("titulo", "Error");
            modelo.put("error", ex.getMessage());
            return "fracaso.html";
        }
    }

    @GetMapping("/dar-de-baja")
    public String bajaPrestamo(String id, ModelMap modelo) {
        try {
            prestamoServicio.altaPrestamo(id, Boolean.FALSE);
            modelo.put("titulo", "Prestamo dado de baja");
            return "exito";
        } catch (Exception ex) {
            modelo.put("titulo", "Error");
            modelo.put("error", ex.getMessage());
            return "fracaso";
        }
    }
    
    @GetMapping("/registrar-devolucion/{id}")
    public String registrarDevolucion(@PathVariable String id, ModelMap modelo) throws ErrorServicio {
        Prestamo prestamo = prestamoServicio.buscarPrestamoPorID(id);
        modelo.addAttribute("prestamo", prestamo);
        return "form-devolucion.html";

    }

    @PostMapping("/registrar-devolucion/{id}")
    public String registrarDevolucion(@PathVariable String id, ModelMap modelo, @RequestParam @Nullable Long dni,  @RequestParam @Nullable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDevolucion) {

        try {
            prestamoServicio.registrarDevolucion(id, fechaDevolucion, dni);
            
            modelo.put("titulo", "Prestamo modificado exitosamente!");
            return "exito";
        } catch (ErrorServicio ex) {
            modelo.put("titulo", "Error");
            modelo.put("error", ex.getMessage());
            return "fracaso.html";
        }
    }
}
