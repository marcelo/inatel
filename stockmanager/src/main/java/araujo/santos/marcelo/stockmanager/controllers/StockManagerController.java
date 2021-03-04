/**
 * 
 */
package araujo.santos.marcelo.stockmanager.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import araujo.santos.marcelo.stockmanager.entities.Stock;
import araujo.santos.marcelo.stockmanager.repositories.StockRepository;
import net.minidev.json.JSONObject;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/")
public class StockManagerController {

	@Autowired
	StockRepository stockRepo;

	private static List<Notification> machines = new ArrayList<Notification>();

	@GetMapping("/stock")
	public ResponseEntity<List<Stock>> getAllStocks(@RequestParam(required = false) String title) {
		try {
			List<Stock> stocks = stockRepo.findAll();

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
	public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
		try {

			stockRepo.save(stock);

			if (machines.size() > 0) {

				List<Notification> notificationsList = machines;

				for (Notification notification : notificationsList) {
					RestTemplate restTemplate = new RestTemplate();

					String url = "http://" + notification.getHost() + ":" + notification.getPort() + "/stockcache";
					restTemplate.delete(url);
				}

			}

			return new ResponseEntity<>(stockRepo.save(stock), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/notification")
	public void notification(@RequestBody Notification notification) {

		if (!machines.stream().anyMatch(m -> m.getHost().equals(notification.getHost()))) {
			machines.add(notification);
		}
	}

	@EventListener(ApplicationReadyEvent.class)
	public void defaultStocksRegistration() {

		Stock s1 = new Stock();

		s1.setId("vale5");
		s1.setDescription("vale5");

		Stock s2 = new Stock();

		s2.setId("petr3");
		s2.setDescription("petr3");

		stockRepo.save(s1);
		stockRepo.save(s2);
		
	}

}
