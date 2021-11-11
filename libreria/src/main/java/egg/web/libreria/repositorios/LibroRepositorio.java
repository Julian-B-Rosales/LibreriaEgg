/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.repositorios;

import egg.web.libreria.entidades.Libro;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    @Query("SELECT l FROM Libro l WHERE titulo = :titulo")
    public Libro buscarLibroPorTitulo(@Param("titulo") String titulo);

    @Query("SELECT l FROM Libro l WHERE ISBN = :isbn")
    public Libro buscarLibroPorISBN(@Param("isbn") long isbn);

    @Query("SELECT l FROM Libro l WHERE anio = :anio")
    public ArrayList<Libro> listarLibrosPorAnio(@Param("anio") Integer anio);

    @Query("SELECT l FROM Libro l WHERE editorial.id = :id")
    public ArrayList<Libro> listarLibrosPorEditorial(@Param("id") String id);

    @Query("SELECT l FROM Libro l WHERE autor.id = :id")
    public ArrayList<Libro> listarLibrosPorAutor(@Param("id") String id);

    
//    @Query("UPDATE Libro l SET l.isbn = :isbn, l.titulo = :titulo, l.anio = :anio, l.ejemplares = :ejemplares, l.ejemplaresPrestados = :ejemplaresPrestados, l.ejemplaresRestantes = :ejemplaresRestantes, l.alta = :alta, l.autor.id = :idAutor, l.editorial.id = :idEditorial "
//            + "WHERE l.id = :id")
//    void modLibro(@Param("id")  String id,@Param("alta") Boolean alta,@Param("isbn") Long isbn,@Param("titulo") String titulo,@Param("anio") Integer anio,@Param("ejemplares") Integer ejemplares,@Param("ejemplaresPrestados") Integer ejemplaresPrestados,@Param("ejemplaresRestantes") Integer ejemplaresRestantes,@Param("idAutor") String nombreAutor,@Param("idEditorial") String nombreEditorial);
        
}
