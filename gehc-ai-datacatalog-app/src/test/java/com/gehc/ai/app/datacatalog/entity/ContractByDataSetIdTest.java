package com.gehc.ai.app.datacatalog.entity;

import com.gehc.ai.app.datacatalog.rest.response.ContractByDataSetId;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContractByDataSetIdTest {

    @Test
    public void testEqualsContractMethodInContractByDataSetId() {
        //ARRANGE
        ContractByDataSetId contract = createMockCompleteContract();
        ContractByDataSetId contract1 = createMockCompleteContract2();


        Date date1 = Date.valueOf("2017-03-31");
        Date date2 = Date.valueOf("2017-03-30");
        //ACT && ASSERT
        EqualsVerifier
                .forClass(ContractByDataSetId.class)
                .withPrefabValues(ContractByDataSetId.class, contract, contract1)
                .withPrefabValues(Date.class, date1, date2)
                .withNonnullFields("id", "deidStatus", "hasContractExpired",
                        "agreementBeginDate", "active", "uploadBy",
                        "uploadDate", "agreementName", "primaryContactEmail",
                        "dataUsagePeriod", "useCases", "dataOriginCountriesAndStates",
                        "dataLocationAllowed", "uploadStatus")
                .verify();
    }

    private ContractByDataSetId createMockCompleteContract() {

        return new ContractByDataSetId(1L,
                Contract.DeidStatus.HIPAA_COMPLIANT,
                "true",
                false,
                "user",
                Date.valueOf("2017-03-31"),
                "testAgreement",
                "joe@ge.com",
                "2018-06-08",
                "12",
                Arrays.asList(new ContractUseCase[]{new ContractUseCase(ContractUseCase.DataUser.GE_GLOBAL, ContractUseCase.DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}),
                Contract.UploadStatus.UPLOAD_COMPLETED,
                Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}),
                Contract.DataLocationAllowed.GLOBAL);

    }

    private ContractByDataSetId createMockCompleteContract2() {

        return new ContractByDataSetId(2L,
                Contract.DeidStatus.HIPAA_COMPLIANT,
                "true",
                false,
                "user",
                Date.valueOf("2017-03-31"),
                "testAgreement",
                "joe@ge.com",
                "2018-06-08",
                "12",
                Arrays.asList(new ContractUseCase[]{new ContractUseCase(ContractUseCase.DataUser.GE_GLOBAL, ContractUseCase.DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}),
                Contract.UploadStatus.UPLOAD_COMPLETED,
                Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}),
                Contract.DataLocationAllowed.GLOBAL);

    }
}
