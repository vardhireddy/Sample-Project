/*
 * ContractUseCase.java
 *
 * Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Objects;

/**
 * {@code ContractUseCase} represents which country and state the data is coming from.
 *
 * @author uma.tabib1@ge.com (212691936)
 */
@JsonInclude(Include.NON_NULL)
public class ContractDataOriginCountriesStates {

	public ContractDataOriginCountriesStates(){

	}

	//@NotNull
	private String country;

	//@NotNull
	private String state;

	public ContractDataOriginCountriesStates(String country, String state) {
		super();
		this.country = country;
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ContractDataOriginCountriesStates that = (ContractDataOriginCountriesStates) o;
		return Objects.equals(country, that.country) &&
				Objects.equals(state, that.state);
	}

	@Override
	public int hashCode() {

		return Objects.hash(country, state);
	}
	
}
