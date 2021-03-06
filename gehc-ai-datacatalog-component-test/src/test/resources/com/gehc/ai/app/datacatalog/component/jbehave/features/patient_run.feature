Feature: DataCatalog Component -  Patient

Narrative: As a data scientist, I should be able to
Retrieve study by patient Provided
Save Patient
Retrieve image series by patient id
Retrieve image series by patient ids
Get a Patient
Get Patients
Get All Patients
Save Patient with Null PatientId
Save Patient with Null ID
Save Patient
Retrieve image series by patient id
Save Existing Patient retrievs patient details
Retrieve image series by patient id with patient ids list returned null
Retrieve image series by patient id

@functional
@crs_10738

@test_53671
Scenario: Get study by patient
Meta: @automated
Given Retrieve study by patient Provided
When Get study by patient
Then verify study by patient

@test_53672
Scenario: Save Patient
Meta: @automated
Given Save Patient - DataSetUp Provided
When save Patient
Then verify Saving Patient

@test_53673
Scenario: Retrieve image series by patient id
Meta: @automated
Given Retrieve image series by patient id - DataSetUp Provided
When Get image series by patient id
Then verify image series by patient id

@test_53674
Scenario: Retrieve image series by patient ids
Meta: @automated
Given Retrieve image series by patient ids - DataSetUp Provided
When Get image series by patient ids
Then verify image series by patient ids

@test_53675
Scenario: Get a Patient
Meta: @automated
Given Get a Patient - DataSetUp Provided
When Get a Patient
Then verify Get a Patient

@test_53676
Scenario: Get Patients
Meta: @automated
Given Get Patients - DataSetUp Provided
When Get Patients
Then verify Get Patients

@test_53677
Scenario: Get All Patients
Meta: @automated
Given Get All Patients - DataSetUp Provided
When Get All Patients
Then verify Get All Patients

@test_53678
Scenario: Save Patient with Null PtId
Meta: @automated
Given Save Patient with Null Patient id - DataSetUp Provided
When save Patient with Null Patient id
Then verify Saving Patient with Null Patient id

@test_53679
Scenario: Save Patient with Null ID
Meta: @automated
Given Save Patient with Null ID - DataSetUp Provided
When save Patient with Null ID
Then verify Saving Patient with Null ID

@test_53680
Scenario: Save Existing Patient retrievs patient details
Meta: @automated
Given Save Existing Patient - DataSetUp Provided
When save Existing Patient
Then verify Saving Existing Patient

@test_53681
Scenario: Retrieve image series by patient id when patient ids list returned null
Meta: @automated
Given Retrieve image series by patient when patient ids list returned null- DataSetUp Provided
When Get image series by patient ids when patient ids list returned null
Then verify image series by patient ids when patient ids list returned null

@test_53682
Scenario: Retrieve image series by patient id when patient ids list returned empty
Meta: @automated
Given Retrieve image series by patient when patient ids list returned empty- DataSetUp Provided
When Get image series by patient ids when patient ids list returned empty
Then verify image series by patient ids when patient ids list returned empty