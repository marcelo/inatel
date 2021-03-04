/**
 * 
 */
package araujo.santos.marcelo.stockmanager;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import araujo.santos.marcelo.stockmanager.entities.Stock;
import araujo.santos.marcelo.stockmanager.repositories.StockRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class StockTest {

	@Autowired
	private StockRepository repository;

	@Test
	public void defaultRegisteredStocks() {

		Iterable<Stock> stocks = repository.findAll();

		assertThat(stocks).hasSize(2);
	}

}
