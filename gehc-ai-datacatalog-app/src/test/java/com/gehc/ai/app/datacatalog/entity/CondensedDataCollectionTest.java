/*
 * DataCollectionWithNumImageSetsTest.java
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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

/**
 * {@code CondensedDataCollectionTest} evaluates the behavior of {@link CondensedDataCollection}.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class CondensedDataCollectionTest {

    ////////////////
    //
    // Test specs //
    //
    ////////////////

    @Test
    public void equalsContract() {
        EqualsVerifier
                .forClass(CondensedDataCollection.class)
                .withNonnullFields("id", "name", "type", "createdBy", "createdDate", "orgId", "numImageSets")
                .verify();
    }

    @Test
    public void itShouldConvertADataSetToADataCollectionWithNumImageSets() {
        // ARRANGE
        DataSet dataSetToConvert = createMockDataSet();
        CondensedDataCollection expectedConvertedDataSet = createMockDataCollectionWithNumImageSets();

        // ACT
        CondensedDataCollection actualConvertedDataSet = CondensedDataCollection.fromDataSetEntity(dataSetToConvert);

        // ASSERT
        assertThat(actualConvertedDataSet, is(equalTo(expectedConvertedDataSet)));
    }

    @Test
    public void itShouldConvertAListOfDataSetsToAListOfDataCollectionWithNumImageSets() {
        // ARRANGE
        DataSet firstDataSetToConvert = createMockDataSet();
        DataSet secondDataSetToConvert = createMockDataSet();
        DataSet thirdDataSetToConvert = createMockDataSet();

        List<DataSet> dataSetsToConvert = Arrays.asList(new DataSet[]{firstDataSetToConvert, secondDataSetToConvert, thirdDataSetToConvert});

        CondensedDataCollection firstExpectedConvertedDataSet = createMockDataCollectionWithNumImageSets();
        CondensedDataCollection secondExpectedConvertedDataSet = createMockDataCollectionWithNumImageSets();
        CondensedDataCollection thirdExpectedConvertedDataSet = createMockDataCollectionWithNumImageSets();

        List<CondensedDataCollection> expectedConvertedDataSets = Arrays.asList(new CondensedDataCollection[]{firstExpectedConvertedDataSet, secondExpectedConvertedDataSet, thirdExpectedConvertedDataSet});

        // ACT
        List<CondensedDataCollection> actualConvertedDataSets = CondensedDataCollection.fromDataSetEntities(dataSetsToConvert);

        // ASSERT
        assertEquals(expectedConvertedDataSets, actualConvertedDataSets);
    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    private static final Long MOCK_ID = 1L;
    private static final String MOCK_NAME = "mock name";
    private static final String MOCK_DESCRIPTION = "mock description";
    private static final String MOCK_COLLECTION_TYPE = "mock collection type";
    private static final String MOCK_CREATED_BY = "mock created by";
    private static final String MOCK_CREATED_DATE = "mock created date";
    private static final String MOCK_ORG_ID = "mock org ID";
    private static final List<Long> MOCK_IMAGE_SET_IDS = Arrays.asList(new Long[]{1L, 2L, 3L});

    private DataSet createMockDataSet() {
        DataSet dataSet = new DataSet();
        dataSet.setId(MOCK_ID);
        dataSet.setName(MOCK_NAME);
        dataSet.setDescription(MOCK_DESCRIPTION);
        dataSet.setType(MOCK_COLLECTION_TYPE);
        dataSet.setCreatedBy(MOCK_CREATED_BY);
        dataSet.setCreatedDate(MOCK_CREATED_DATE);
        dataSet.setOrgId(MOCK_ORG_ID);
        dataSet.setImageSets(MOCK_IMAGE_SET_IDS);

        return dataSet;
    }

    private CondensedDataCollection createMockDataCollectionWithNumImageSets() {
        return new CondensedDataCollection(MOCK_ID, MOCK_NAME, MOCK_DESCRIPTION, MOCK_COLLECTION_TYPE, MOCK_CREATED_BY, MOCK_CREATED_DATE, MOCK_ORG_ID, MOCK_IMAGE_SET_IDS.size());
    }

}
