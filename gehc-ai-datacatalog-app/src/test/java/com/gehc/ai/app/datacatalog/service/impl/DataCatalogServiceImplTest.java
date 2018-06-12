package com.gehc.ai.app.datacatalog.service.impl;

import com.gehc.ai.app.datacatalog.dao.IDataCatalogDao;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ContractDataOriginCountriesStates;
import com.gehc.ai.app.datacatalog.entity.ContractUseCase;
import com.gehc.ai.app.datacatalog.rest.response.ContractByDataSetId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
public class DataCatalogServiceImplTest {

    @Mock
    private IDataCatalogDao dataCatalogDao;

    @InjectMocks
    DataCatalogServiceImpl service;

    //test cases for getContractsByDataSetID
    @Test
    public void itShouldReturnMapOfActiveAndInactiveContracts() {

        //ARRANGE
        List<Long> imageSetIdList = Arrays.asList(1293000012905L, 1293000012895L, 1293000012901L, 1293000012904L);
        when(dataCatalogDao.getImageSetIdsByDataCollectionId(anyLong())).thenReturn(imageSetIdList);

        List<Contract> contractList = new ArrayList<>();
        Contract contract = buildContractEntity();
        contractList.add(contract);
        int expectedInactiveContracts = 1;

        Contract contract1 = buildContractEntity();
        contract1.setActive("false");
        contract1.setAgreementBeginDate("2017-06-08");
        contractList.add(contract1);
        int expectedActiveContracts = 1;

        when(dataCatalogDao.getContractsByImageSetIds(anyList())).thenReturn(contractList);

        //ACT
        Map<String, List<ContractByDataSetId>> result = service.getContractsByDataCollectionId(1L);

        //ASSERT
        assertEquals(expectedInactiveContracts, result.get("inactive").size());
        assertEquals(expectedActiveContracts, result.get("active").size());
    }

    @Test
    public void itShouldReturnAnEmptyHashMap() {
        //ARRANGE
        List<Long> imageSetIdList = Arrays.asList(1293000012905L, 1293000012895L, 1293000012901L, 1293000012904L);
        when(dataCatalogDao.getImageSetIdsByDataCollectionId(anyLong())).thenReturn(null);

        int expectedInactiveContracts = 0;
        int expectedActiveContracts = 0;

        //ACT
        Map<String, List<ContractByDataSetId>> result = service.getContractsByDataCollectionId(1L);
        //ASSERT
        assertEquals(expectedInactiveContracts, result.get("inactive").size());
        assertEquals(expectedActiveContracts, result.get("active").size());
    }

    @Test
    public void itShouldAlsoReturnEmptyHashMap() {
        //ARRANGE
        List<Long> imageSetIdList = Arrays.asList(1293000012905L, 1293000012895L, 1293000012901L, 1293000012904L);
        when(dataCatalogDao.getImageSetIdsByDataCollectionId(anyLong())).thenReturn(imageSetIdList);
        when(dataCatalogDao.getContractsByImageSetIds(anyList())).thenReturn(new ArrayList());
        int expectedInactiveContracts = 0;
        int expectedActiveContracts = 0;

        //ACT
        Map<String, List<ContractByDataSetId>> result = service.getContractsByDataCollectionId(1L);
        //ASSERT
        assertEquals(expectedInactiveContracts, result.get("inactive").size());
        assertEquals(expectedActiveContracts, result.get("active").size());
    }

    @Test(expected = RuntimeException.class)//ASSERT
    public void testGetContractsByDatasetIdForExceptionRetrievingImageSetList() {

        //ARRANGE
        List<Long> imageSetIdList = Arrays.asList(1293000012905L, 1293000012895L, 1293000012901L, 1293000012904L);
        when(dataCatalogDao.getImageSetIdsByDataCollectionId(anyLong())).thenThrow(new RuntimeException());
        when(dataCatalogDao.getContractsByImageSetIds(anyList())).thenReturn(new ArrayList());

        //ACT
        service.getContractsByDataCollectionId(1L);
    }

    @Test(expected = RuntimeException.class)//ASSERT
    public void testGetContractsByDatasetIdForExceptionRetrievingContractList() {

        //ARRANGE
        List<Long> imageSetIdList = Arrays.asList(1293000012905L, 1293000012895L, 1293000012901L, 1293000012904L);
        when(dataCatalogDao.getImageSetIdsByDataCollectionId(anyLong())).thenReturn(imageSetIdList);
        when(dataCatalogDao.getContractsByImageSetIds(anyList())).thenThrow(new RuntimeException());

        //ACT
        service.getContractsByDataCollectionId(1L);
    }


    public Contract buildContractEntity() {
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        contract.setSchemaVersion("v1");
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT);
        contract.setAgreementBeginDate("9999-06-08");
        contract.setDataUsagePeriod("12");
        contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(ContractUseCase.DataUser.GE_GLOBAL, ContractUseCase.DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}));
        contract.setDataOriginCountriesStates(Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}));
        contract.setActive("true");
        contract.setDataLocationAllowed(Contract.DataLocationAllowed.GLOBAL);
        contract.setUploadBy("user");
        contract.setUploadStatus(Contract.UploadStatus.UPLOAD_IN_PROGRESS);

        return contract;
    }


}
