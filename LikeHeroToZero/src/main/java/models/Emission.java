package models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Emission implements Comparable<Emission>, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "emission_id")
	private Integer id;

	@Column(name = "emission_year", nullable = false)
	private int year;

	@Column(name = "emission_amount", nullable = false)
	private float amount;

	@Column(name = "emission_draft", nullable = false)
	private boolean draft;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "country_id")
	private Country country;

	public Emission() {
	}

	public Emission(int year, float amount, boolean draft, Country country) {
		this.year = year;
		this.amount = amount;
		this.country = country;
		this.draft = draft;
	}

	public Integer getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return String.format("Year: %d\nAmount: %f\nDraft: %b", this.year, this.amount, this.draft);
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean editable) {
		this.draft = editable;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public int compareTo(Emission o) {
		return this.year - o.year;
	}
	
	@Override
	public Emission clone() {
		Emission emission = new Emission(this.year, this.amount, this.draft, this.country);
		return emission;
	}
}