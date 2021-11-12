/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.controladores;

import egg.web.libreria.entidades.Editorial;
import egg.web.libreria.errores.ErrorServicio;
import egg.web.libreria.servicios.EditorialServicio;
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
@RequestMapping("/editoriales")
public class EditorialController {

    @Autowired
    EditorialServicio editorialServicio;

    @GetMapping("")
    public String editoriales(ModelMap modelo) {
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.put("editoriales", editoriales);
        return "editoriales.html";
    }

    @GetMapping("/editoriales-baja")
    public String baja(ModelMap modelo) {
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.put("editoriales", editoriales);
        return "editoriales-baja.html";
    }

    @GetMapping("/form-editorial")
    public String formEditorial() {
        return "form-editorial.html";
    }

    @PostMapping("/agregar-editorial")
    public String agregarEditorial(ModelMap modelo, @RequestParam @Nullable String nombre, @RequestParam @Nullable Boolean alta) {

        
        try {
            if (alta == null) {
                alta = false;
            } else {
                alta = true;
            }
            editorialServicio.registrarEditorial(nombre, alta);
            modelo.put("titulo", "Editorial agregado exitosamente!");
            return "exito.html";
        } catch (ErrorServicio ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "form-editorial.html";
        }

    }

    @GetMapping("/modificar-editorial/{id}")
    public String formModEditorial(@PathVariable String id, ModelMap modelo) {
        modelo.addAttribute("editorial", editorialServicio.buscarEditorialPorID(id));
        return "form-mod-editorial.html";
    }

    @PostMapping("/modificar-editorial/{id}")
    public String formModEditorial(@PathVariable String id, ModelMap modelo, @RequestParam @Nullable String nombre, @RequestParam @Nullable Boolean alta) {
        try {
            if (alta == null) {
                alta = false;
            } else {
                alta = true;
            }
            editorialServicio.modificarEditorial(id, nombre, alta);
            modelo.put("titulo", "Editorial modificado exitosamente!");
            return "exito.html";
        } catch (ErrorServicio ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            return "form-mod-editorial.html";
        }
    }

    @GetMapping("/eliminar-editorial")
    public String eliminarEditorial(String id, ModelMap modelo) {
        try {
            editorialServicio.altaEditorial(id, Boolean.FALSE);
            return "editoriales.html";
        } catch (Exception ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "editoriales.html";
        }
    }

    @GetMapping("/revivir-editorial")
    public String revivirEditorial(String id, ModelMap modelo) {
        try {
            editorialServicio.altaEditorial(id, Boolean.TRUE);
            return "editoriales.html";
        } catch (Exception ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "editoriales.html";
        }
    }

}
