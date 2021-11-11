/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.controladores;

import egg.web.libreria.entidades.Libro;
import egg.web.libreria.errores.ErrorServicio;
import egg.web.libreria.servicios.LibroServicio;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    LibroServicio libroServicio;

    @GetMapping("")
    public String libros(ModelMap modelo) {
        List<Libro> libros = libroServicio.listarLibros();
        modelo.put("libros", libros);
        return "libros.html";
    }

    @GetMapping("/libros-baja")
    public String baja(ModelMap modelo) {
        List<Libro> libros = libroServicio.listarLibros();
        modelo.put("libros", libros);
        return "libros-baja.html";
    }

    @GetMapping("/form-libro")
    public String formLibro() {
        return "form-libro.html";
    }

    @PostMapping("/agregar-libro")
    public String agregarLibro(ModelMap modelo, @RequestParam @Nullable Long isbn, @RequestParam @Nullable String titulo, @RequestParam @Nullable Integer anio, @RequestParam @Nullable Integer ejemplares, @RequestParam @Nullable Integer ejemplaresPrestados, @RequestParam @Nullable Boolean alta, @RequestParam @Nullable String nombreAutor, @RequestParam @Nullable String nombreEditorial) {

        Integer ejemplaresRestantes = ejemplares - ejemplaresPrestados;
        if (alta == null) {
            alta = false;
        }
        try {
            libroServicio.registrarLibro(isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, nombreAutor, nombreEditorial);
        } catch (ErrorServicio ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo);
            modelo.put("ejemplares", ejemplares);
            modelo.put("ejemplaresPrestados", ejemplaresPrestados);
            modelo.put("nombreAutor", nombreAutor);
            modelo.put("nombreEditorial", nombreEditorial);
            return "form-libro.html";
        }
        modelo.put("titulo", "Libro agregado exitosamente!");
        return "exito.html";
    }

    @GetMapping("/modificar-libro/{id}")
    public String formModLibro(@PathVariable String id, ModelMap modelo) throws ErrorServicio {
        Libro libro = libroServicio.buscarLibroPorID(id);
        modelo.addAttribute("libro", libro);
        return "form-mod-libro.html";

    }

    @PostMapping("/modificar-libro/{id}")
    public String formModLibro(@PathVariable String id, ModelMap modelo, @RequestParam @Nullable Long isbn, @RequestParam @Nullable String titulo, @RequestParam @Nullable Integer anio, @RequestParam @Nullable Integer ejemplares, @RequestParam @Nullable Integer ejemplaresPrestados, @RequestParam @Nullable Boolean alta, @RequestParam @Nullable String nombreAutor, @RequestParam @Nullable String nombreEditorial) {
        Integer ejemplaresRestantes;
        if(ejemplares != null && ejemplaresPrestados != null){
            ejemplaresRestantes = ejemplares - ejemplaresPrestados;
        }else{
            ejemplaresRestantes = 0;
        }
        try {
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo);
            modelo.put("ejemplares", ejemplares);
            modelo.put("ejemplaresPrestados", ejemplaresPrestados);
            modelo.put("nombreAutor", nombreAutor);
            modelo.put("nombreEditorial", nombreEditorial);
            if (alta == null) {
                alta = false;
            }else{
                alta = true;
            }
            libroServicio.modificarLibro(id, alta, isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, nombreAutor, nombreEditorial);
            modelo.put("titulo", "Libro modificado exitosamente!");
            return "exito.html";
        } catch (ErrorServicio ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "form-mod-libro.html";
        }
    }

    @GetMapping("/eliminar-libro")
    public String eliminarLibro(String id, ModelMap modelo, RedirectAttributes red) {
        try {
            libroServicio.modificarAltaLibro(Boolean.FALSE, id);
            return "libros";
        } catch (Exception ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "redirect:/libros";
        }
    }

    @GetMapping("/revivir-libro")
    public String revivirLibro(String id, ModelMap modelo) {
        try {
            libroServicio.modificarAltaLibro(Boolean.TRUE, id);
            return "libros";
        } catch (Exception ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "redirect:/libros";
        }
    }
}
