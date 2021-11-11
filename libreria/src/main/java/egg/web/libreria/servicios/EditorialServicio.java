/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.servicios;

import egg.web.libreria.entidades.Editorial;
import egg.web.libreria.errores.ErrorServicio;
import egg.web.libreria.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialServicio {
    
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    public void validar(String nombre) throws ErrorServicio{
        
        if(nombre == null || nombre.isEmpty()){
            throw new ErrorServicio("Nombre de la editorial vacio o nulo");
        }
    }
    
    @Transactional 
    public void registrarEditorial(String nombre, Boolean alta) throws ErrorServicio{
        validar(nombre);
        
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setAlta(alta);
        
        editorialRepositorio.save(editorial);
    }
    @Transactional 
    public void modificarEditorial(String id, String nombre) throws ErrorServicio{
        validar(nombre);
        Optional<Editorial> res = editorialRepositorio.findById(id);
        if(res.isPresent()){
            Editorial editorial = res.get();
            
            editorial.setNombre(nombre);
            
            editorialRepositorio.save(editorial);
        }else{
            throw new ErrorServicio("No se encontro editorial con ese ID");
        }
    }
    @Transactional 
    public void altaEditorial(String id, Boolean alta) throws ErrorServicio{
        Optional<Editorial> res = editorialRepositorio.findById(id);
        if(res.isPresent()){
            Editorial editorial = res.get();
            
            editorial.setAlta(alta);
            
            editorialRepositorio.save(editorial);
        }else{
            throw new ErrorServicio("No se encontro editorial con ese ID");
        }
    }
    
    public Editorial buscarEditorialPorNombre(String nombre) throws ErrorServicio{
        validar(nombre);
        return editorialRepositorio.buscarEditorialPorNombre(nombre);
    }
    
    public List<Editorial> listarEditoriales(){
        return editorialRepositorio.findAll();
    }
    
    public Editorial buscarEditorialPorID(String id){
        return editorialRepositorio.getById(id);
    }
}
