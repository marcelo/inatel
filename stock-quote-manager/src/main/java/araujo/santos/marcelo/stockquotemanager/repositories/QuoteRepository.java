/**
 * 
 */
package araujo.santos.marcelo.stockquotemanager.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import araujo.santos.marcelo.stockquotemanager.entities.Quote;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

}