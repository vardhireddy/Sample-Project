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

import javax.validation.constraints.NotNull;

/**
 * {@code ContractUseCase} represents who and how data associated with a contract can be used.
 *
 * @author andrew.c.wong@ge.com (212069153), uma.tabib1@ge.com (212691936)
 */
@JsonInclude(Include.NON_NULL)
public class ContractUseCase {

	public ContractUseCase(){

	}

	@NotNull
	private DataUser dataUser;

	@NotNull
	private DataUsage dataUsage;

	public ContractUseCase(DataUser dataUser, DataUsage dataUsage) {
		super();
		this.dataUser = dataUser;
		this.dataUsage = dataUsage;
	}

	public enum DataUser {

		GE_ONSHORE("GE Onshore"), GE_GLOBAL("GE Global"), THIRD_PARTY_PARTNERS_ONSHORE("3rd Party Partners Onshore"), THIRD_PARTY_PARTNERS_GLOBAL("3rd Party Partners Global");

		private String displayName;

		private DataUser(String displayName){
			this.displayName = displayName;
		}

		@Override
		public String toString(){
			return this.displayName;
		}
	}

	public enum DataUsage {
		TRAINING_AND_MODEL_DEVELOPMENT("Training and Model Development"), ANNOTATION_AND_CURATION("Annotation and Curation");

		private String displayName;

		private DataUsage(String displayName){
			this.displayName = displayName;
		}

		@Override
		public String toString(){
			return this.displayName;
		}
	}

	public DataUser getDataUser() {
		return dataUser;
	}

	public void setDataUser(DataUser dataUser) {
		this.dataUser = dataUser;
	}

	public DataUsage getDataUsage() {
		return dataUsage;
	}

	public void setDataUsage(DataUsage dataUsage) {
		this.dataUsage = dataUsage;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ContractUseCase that = (ContractUseCase) o;

		if (getDataUser() != that.getDataUser()) return false;
		return getDataUsage() == that.getDataUsage();
	}

	@Override
	public int hashCode() {
		int result = getDataUser().hashCode();
		result = 31 * result + getDataUsage().hashCode();
		return result;
	}
}
