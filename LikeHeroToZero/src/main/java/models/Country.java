package models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Country implements Comparable<Country> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "country_id")
	private Integer id;

	@Column(name = "country_name", nullable = false, unique = true)
	private String name;

	@Column(name = "country_code", nullable = false, unique = true)
	private String code;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "country", orphanRemoval = true)
	private List<Emission> emissions = new ArrayList<>();

	public Country() {
	}

	public Country(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Emission> getEmissions() {
		return emissions;
	}

	public void setEmissions(List<Emission> emissions) {
		this.emissions = emissions;
	}

	@Override
	public String toString() {
		return this.name + "; " + this.code + "; " + this.id;
	}

	@Override
	public int compareTo(Country o) {
		if (this.name == null) {
			return -1;
		} else {
			return this.name.compareTo(o.getName());
		}
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Country)) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (this.name == null || this.code == null) {
			return false;
		}
		return this.name.equals(((Country) other).getName()) && this.code.equals(((Country) other).getCode());
	}

}
