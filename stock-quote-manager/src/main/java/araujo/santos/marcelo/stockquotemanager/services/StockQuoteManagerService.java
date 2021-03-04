/**
 * 
 */
package araujo.santos.marcelo.stockquotemanager.services;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;

import araujo.santos.marcelo.stockquotemanager.entities.Quote;
import araujo.santos.marcelo.stockquotemanager.entities.Stock;
import araujo.santos.marcelo.stockquotemanager.payloads.StockQuotePayload;
import araujo.santos.marcelo.stockquotemanager.repositories.QuoteRepository;
import araujo.santos.marcelo.stockquotemanager.repositories.StockRepository;

/**
 * @author marcelo
 *
 */
@Service
@CacheConfig(cacheNames = { "stocks" })
public class StockQuoteManagerService {

	@Autowired
	CacheManager cacheManager;

	@Autowired
	QuoteRepository quoteRepo;
	@Autowired
	StockRepository stockRepo;

	public boolean createStockQuote(StockQuotePayload sqPayload) {
		String id = sqPayload.getId();

		boolean idExistsInCache = false;

		if (cacheManager.getCache("stocks") != null) {
			idExistsInCache = getStocks().stream().anyMatch(stock -> stock.getId().equals(id));
		}

		if (sqPayload != null && sqPayload.getQuotes() != null && sqPayload.getQuotes().size() >= 1) {

			if (!idExistsInCache) {

				RestTemplate restTemplate = new RestTemplate();
				String url = "http://localhost:8080/stock/" + id;

				try {

					restTemplate.getForEntity(url, Stock.class);
				} catch (Exception e) {

					return false;
				}
			}

			Set<Date> keys = sqPayload.getQuotes().keySet();
			for (Iterator<Date> it = keys.iterator(); it.hasNext();) {
				Date date = it.next();
				if (date != null) {

					Quote quote = new Quote();
					quote.setDate(date);
					quote.setValue(sqPayload.getQuotes().get(date));
					quote.setId(id);
					quoteRepo.save(quote);
				}

				return true;
			}

		}

		return false;

	}

	@Cacheable
	public List<Stock> getStocks() {

		String url = "http://localhost:8080/stock";
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<List<Stock>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Stock>>() {
				});
		List<Stock> stocks = responseEntity.getBody();

		return stocks;
	}

	public void flushCache(String cacheName) {
		cacheManager.getCache(cacheName).clear();
	}

}
