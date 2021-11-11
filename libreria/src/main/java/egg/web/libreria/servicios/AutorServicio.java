/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.servicios;

import egg.web.libreria.entidades.Autor;
import egg.web.libreria.errores.ErrorServicio;
import egg.web.libreria.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {
    
    @Autowired
    private AutorRepositorio autorRepositorio;
    
    public void validar(String nombre) throws ErrorServicio{
        
        if(nombre == null || nombre.isEmpty()){
            throw new ErrorServicio("Nombre del autor vacio o nulo");
        }
    }
    
    @Transactional  
    public void registrarAutor(String nombre, Boolean alta) throws ErrorServicio{
        validar(nombre);
        
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(alta);
        
        autorRepositorio.save(autor);
    }
    @Transactional 
    public void modificarAutor(String id, String nombre) throws ErrorServicio{
        validar(nombre);
        Optional<Autor> res = autorRepositorio.findById(id);
        if(res.isPresent()){
            Autor autor = res.get();
            
            autor.setNombre(nombre);
            
            autorRepositorio.save(autor);
        }else{
            throw new ErrorServicio("No se encontro autor con ese ID");
        }
    }
    @Transactional 
    public void altaAutor(String id, Boolean alta) throws ErrorServicio{
        Optional<Autor> res = autorRepositorio.findById(id);
        if(res.isPresent()){
            Autor autor = res.get();
            
            autor.setAlta(alta);
            
            autorRepositorio.save(autor);
        }else{
            throw new ErrorServicio("No se encontro autor con ese ID");
        }
    }
    
    public Autor buscarAutorPorNombre(String nombre) throws ErrorServicio{
        validar(nombre);
        return autorRepositorio.buscarAutorPorNombre(nombre);
    }
    public Autor buscarAutorPorID(String id) throws ErrorServicio{
        return autorRepositorio.getById(id);
    }
    
    public List<Autor> listarAutores(){
        return autorRepositorio.findAll();
    }
    
}
