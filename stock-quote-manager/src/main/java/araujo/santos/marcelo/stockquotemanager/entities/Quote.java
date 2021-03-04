/**
 * 
 */
package araujo.santos.marcelo.stockquotemanager.entities;

import java.util.Date;

/**
 * @author marcelo
 *
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "quotes")
public class Quote {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "quote_id")
	private Long quoteId;

	public Long getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Long quoteId) {
		this.quoteId = quoteId;
	}

	@Column(name = "id")
	private String id;

	@Column(name = "date")
	private Date date;

	@Column(name = "value")
	private Double value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getValue() {
		return value;
	}
	


	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Quote [getQuoteId()=" + getQuoteId() + ", getId()=" + getId() + ", getDate()=" + getDate()
				+ ", getValue()=" + getValue() + "]";
	}

}
