/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.controladores;

import egg.web.libreria.entidades.Autor;
import egg.web.libreria.errores.ErrorServicio;
import egg.web.libreria.servicios.AutorServicio;
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
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    AutorServicio autorServicio;

    @GetMapping("")
    public String autores(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        modelo.put("autores", autores);
        return "autores.html";
    }

    @GetMapping("/autores-baja")
    public String baja(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        modelo.put("autores", autores);
        return "autores-baja.html";
    }

    @GetMapping("/form-autor")
    public String formAutor() {
        return "form-autor.html";
    }

    @PostMapping("/agregar-autor")
    public String agregarAutor(ModelMap modelo, @RequestParam @Nullable String nombre, @RequestParam @Nullable Boolean alta) {

        if (alta == null) {
            alta = false;
        }
        try {
            autorServicio.registrarAutor(nombre, alta);
        } catch (ErrorServicio ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            
            modelo.put("titulo", "Error");
            modelo.put("error", ex.getMessage());
            return "fracaso";
        }
        modelo.put("titulo", "Autor agregado exitosamente!");
        return "exito.html";
    }

    @GetMapping("/modificar-autor/{id}")
    public String formModAutor(@PathVariable String id, ModelMap modelo) throws ErrorServicio {

        modelo.put("autor", autorServicio.buscarAutorPorID(id));
        return "form-mod-autor.html";
    }

    @PostMapping("/modificar-autor/{id}")
    public String formModLibro(@PathVariable String id, ModelMap modelo, @RequestParam @Nullable String nombre, @RequestParam @Nullable Boolean alta) {
        try {
            if (alta == null) {
                alta = false;
            }else{
                alta = true;
            }
            autorServicio.modificarAutor(id, nombre, alta);
            modelo.put("titulo", "Autor modificado exitosamente!");
            return "exito.html";
        } catch (ErrorServicio ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            return "form-mod-autor.html";
        }
    }

    @GetMapping("/eliminar-autor")
    public String eliminarAutor(String id, ModelMap modelo) {
        try {
            autorServicio.altaAutor(id, Boolean.FALSE);
            return "autores.html";
        } catch (Exception ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            
            modelo.put("titulo", "Error");
            modelo.put("error", ex.getMessage());
            return "fracaso";
        }
    }

    @GetMapping("/revivir-autor")
    public String revivirAutor(String id, ModelMap modelo) {
        try {
            autorServicio.altaAutor(id, Boolean.TRUE);
            return "autores.html";
        } catch (Exception ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            
            modelo.put("titulo", "Error");
            modelo.put("error", ex.getMessage());
            return "fracaso";
        }
    }

}
