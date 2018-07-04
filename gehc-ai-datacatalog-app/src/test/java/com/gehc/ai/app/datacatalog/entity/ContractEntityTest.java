package com.gehc.ai.app.datacatalog.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.constraints.AssertTrue;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ContractEntityTest {

    @Test
    public void equalsContract() {
        Contract contract = createMockCompleteContract();
        Contract contract1 = createMockCompleteContract();
        contract1.setId(2L);

        Date date1 = Date.valueOf("2017-03-31");
        Date date2 = Date.valueOf("2017-03-30");
        EqualsVerifier
                .forClass(Contract.class)
                .withPrefabValues(Contract.class, contract, contract1)
                .withPrefabValues(Date.class, date1, date2)
                .withNonnullFields("schemaVersion", "orgId", "deidStatus", "agreementBeginDate", "active", "uploadBy", "agreementName", "primaryContactEmail", "dataUsagePeriod", "useCases", "dataOriginCountriesStates", "dataLocationAllowed", "uploadStatus")
                .withIgnoredFields("isExpired")
                .verify();
    }

    private Contract createMockCompleteContract() {
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT);
        contract.setAgreementBeginDate("2017-03-02");
        contract.setDataUsagePeriod("365");
        contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(ContractUseCase.DataUser.GE_GLOBAL, ContractUseCase.DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}));
        contract.setDataOriginCountriesStates(Arrays.asList(new ContractDataOriginCountriesStates[]{new ContractDataOriginCountriesStates("USA", "CA")}));
        contract.setActive("true");
        contract.setDataLocationAllowed(Contract.DataLocationAllowed.GLOBAL);
        contract.setSchemaVersion("v1");
        List<String> uriList = new ArrayList<>();
        uriList.add("bla.pdf");
        contract.setUri(uriList);
        contract.setUploadStatus(Contract.UploadStatus.UPLOAD_IN_PROGRESS);
        contract.setUploadBy("user");
        contract.setUploadDate(Date.valueOf("2017-03-31"));
        contract.setExpired(true);
        return contract;
    }
}