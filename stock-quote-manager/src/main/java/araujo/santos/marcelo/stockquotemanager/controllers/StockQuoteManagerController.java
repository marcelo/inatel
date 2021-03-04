/**
 * 
 */
package araujo.santos.marcelo.stockquotemanager.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import araujo.santos.marcelo.stockquotemanager.entities.Stock;
import araujo.santos.marcelo.stockquotemanager.payloads.StockQuotePayload;
import araujo.santos.marcelo.stockquotemanager.repositories.StockRepository;
import araujo.santos.marcelo.stockquotemanager.services.StockQuoteManagerService;
import net.minidev.json.JSONObject;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/")
public class StockQuoteManagerController {

	private static final String CACHE_NAME = "stocks";

	@Autowired
	StockRepository stockRepo;

	@Autowired
	StockQuoteManagerService sqmService;

	@GetMapping("/stock")
	public ResponseEntity<List<Stock>> getAllStocks(@RequestParam(required = false) String title) {
		try {
			List<Stock> stocks = sqmService.getStocks();

			if (stocks.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(stocks, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/stock/{id}")
	public ResponseEntity<Stock> getStockById(@PathVariable("id") String id) {
		Optional<Stock> stock = Optional.ofNullable(stockRepo.findById(id));

		if (stock.isPresent()) {
			return new ResponseEntity<>(stock.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/stock")
	public ResponseEntity<String> createStock(@RequestBody StockQuotePayload stockQuotePayload) {
		try {

			boolean stockExists = sqmService.createStockQuote(stockQuotePayload);

			if (stockExists) {
				return new ResponseEntity<>(null, HttpStatus.CREATED);

			}

			return new ResponseEntity<>("There is no registered Stock ID", HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/stockcache")
	public ResponseEntity<HttpStatus> deleteStockCache() {

		sqmService.flushCache(CACHE_NAME);

		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void registerItself() {

		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/notification";
		JSONObject json = new JSONObject();
		json.put("host", "localhost");
		json.put("port", "8081");

		restTemplate.postForObject(url, json, ResponseEntity.class);

	}

}
