/**
 * 
 */
package araujo.santos.marcelo.stockquotemanager;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import araujo.santos.marcelo.stockquotemanager.entities.Stock;
import araujo.santos.marcelo.stockquotemanager.services.StockQuoteManagerService;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class StockTest {

	@Autowired
	private StockQuoteManagerService service;

	@Test
	public void defaultRegisteredStocks() {

		Iterable<Stock> stocks = service.getStocks();

		assertThat(stocks).hasSizeGreaterThanOrEqualTo(2);
	}

}
