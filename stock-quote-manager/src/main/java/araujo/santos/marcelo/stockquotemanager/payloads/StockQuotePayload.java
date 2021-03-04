/**
 * 
 */
package araujo.santos.marcelo.stockquotemanager.payloads;

import java.util.Date;
import java.util.HashMap;

/**
 * @author marcelo
 *
 */
public class StockQuotePayload {

	private String id;
	private HashMap<Date, Double> quotes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public HashMap<Date, Double> getQuotes() {
		return quotes;
	}

	public void setQuotes(HashMap<Date, Double> quotes) {
		this.quotes = quotes;
	}

	

}
