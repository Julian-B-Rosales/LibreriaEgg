/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.web.libreria.repositorios;

import egg.web.libreria.entidades.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String> {
    @Query("SELECT p FROM Prestamo p WHERE p.cliente.nombre = :nombre AND p.cliente.apellido = :apellido")
    public List<Prestamo> listarPrestamosPorCliente(@Param("nombre") String nombre,@Param("apellido") String apellido);
    
}
