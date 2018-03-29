/*
 * DeidStatusConverter.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.filters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.gehc.ai.app.datacatalog.entity.Contract.DeidStatus;

/**
 * @author dipshah
 *
 */
@Converter(autoApply = true)
public class DeidStatusConverter implements AttributeConverter<DeidStatus, String>{
	@Override
	public String convertToDatabaseColumn(DeidStatus deidStatus) {
		 return deidStatus.getDisplayName();
	}

	@Override
	public DeidStatus convertToEntityAttribute(String dbData) {
		return DeidStatus.fromDisplayName(dbData);
	}
}
