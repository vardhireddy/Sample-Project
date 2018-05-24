package com.gehc.ai.app.datacatalog.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * {@code DataCollectionBatchingTest} evaluates the behavior of {@link DataCatalogUtils#getDataCollectionBatches(DataSet, Integer)}.
 *
 * @author Madhu Y (305024964), andrew.c.wong@ge.com (212069153)
 */
public class DataCollectionBatchingTest {

    private final static ObjectMapper mapper = new ObjectMapper();
    private static final DefaultResourceLoader resLoader = new DefaultResourceLoader();

    ////////////////////
    //
    // Positive cases //
    //
    ////////////////////

    @Test
    public void itShouldReturnOneDataCollectionIfNoCollectionSizeIsSpecified() throws Exception {
        // ARRANGE
        DataSet validDataSetFromResource = getDataSetFromResource("ValidDataSet.json");
        final int expectedNumCollections = 1;

        // ACT
        List<DataSet> batches = DataCatalogUtils.getDataCollectionBatches(validDataSetFromResource, null);

        // ASSERT
        assertThat(batches.size(), is(equalTo(expectedNumCollections)));
    }

    @Test
    public void itShouldReturnOneDataCollectionIfTheCollectionSizeEqualsTheNumberOfImageSets() throws Exception {
        // ARRANGE
        DataSet dataSetFromResource = getDataSetFromResource("ValidDataSet.json");
        final int collectionSize = dataSetFromResource.getImageSets().size();
        final int expectedNumBatches = 1;

        // ACT
        List<DataSet> batches = DataCatalogUtils.getDataCollectionBatches(dataSetFromResource, collectionSize);

        // ASSERT
        assertThat(batches.size(), is(equalTo(expectedNumBatches)));
    }

    @Test
    public void itShouldReturnAsManyDataCollectionsAsThereAreImageSetsIfTheCollectionSizeIsOne() throws Exception {
        // ARRANGE
        DataSet dataSetFromResource = getDataSetFromResource("ValidDataSet.json");
        final int collectionSize = 1;
        final int expectedNumBatches = dataSetFromResource.getImageSets().size();

        // ACT
        List<DataSet> batches = DataCatalogUtils.getDataCollectionBatches(dataSetFromResource, collectionSize);

        // ASSERT
        assertThat(batches.size(), is(equalTo(expectedNumBatches)));
    }

    ////////////////////
    //
    // Negative cases //
    //
    ////////////////////

    @Test(expected = DataCatalogException.class)
    public void itShouldThrowAnExceptionIfNoDataCollectionIsDefined() throws Exception {
        // ACT
        DataCatalogUtils.getDataCollectionBatches(null, 2);

        // ASSERT - Expected exception is defined in @Test annotation
    }

    @Test(expected = DataCatalogException.class)
    public void itShouldThrowAnExceptionIfNoImageSetIDsAreDefined() throws Exception {
        // ARRANGE
        DataSet dataSetFromResource = getDataSetFromResource("InvalidDataSetWithEmptyImageSets.json");

        // ACT
        DataCatalogUtils.getDataCollectionBatches(dataSetFromResource, 2);

        // ASSERT - Expected exception is defined in @Test annotation
    }

    @Test(expected = DataCatalogException.class)
    public void itShouldReturnExceptionWhenInvokedWithCollectionSizeThatIsLessThanOne() throws Exception {
        // ARRANGE
        final int[] illegalCollectionSizes = new int[]{-1, 0};
        DataSet dataSetFromResource = getDataSetFromResource("ValidDataSet.json");

        // ACT
        for (int illegalCollectionSize : illegalCollectionSizes) {
            DataCatalogUtils.getDataCollectionBatches(dataSetFromResource, illegalCollectionSize);
        }

        // ASSERT - Expected exception is defined in @Test annotation
    }

    @Test(expected = DataCatalogException.class)
    public void itShouldReturnExceptionWhenInvokedWithCollectionSizeThatIsGreaterThanNumberOfImageSets() throws Exception {
        // ARRANGE
        DataSet dataSetFromResource = getDataSetFromResource("ValidDataSet.json");
        final int illegalCollectionSize = dataSetFromResource.getImageSets().size() + 1;

        // ACT
        DataCatalogUtils.getDataCollectionBatches(dataSetFromResource, illegalCollectionSize);

        // ASSERT - Expected exception is defined in @Test annotation
    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    private DataSet getDataSetFromResource(String resource) throws Exception {
        return mapper.readValue(resLoader.getResource(resource).getInputStream(), DataSet.class);
    }
}