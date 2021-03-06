/**
 * 
 */
package araujo.santos.marcelo.stockquotemanager.repositories;

/**
 * @author marcelo
 *
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import araujo.santos.marcelo.stockquotemanager.entities.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
	
	Stock findById(@Param("id") String id);

}