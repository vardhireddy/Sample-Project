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
Retrieve image series by patient id when patient ids list returned null
Retrieve image series by patient id

@functional
@crs

@test
Scenario: Get study by patient
Meta: @automated
Given Retrieve study by patient Provided
When Get study by patient
Then verify study by patient

@test
Scenario: Save Patient
Meta: @automated
Given Save Patient - DataSetUp Provided
When save Patient
Then verify Saving Patient

@test
Scenario: Retrieve image series by patient id
Meta: @automated
Given Retrieve image series by patient id - DataSetUp Provided
When Get image series by patient id
Then verify image series by patient id

@test
Scenario: Retrieve image series by patient ids
Meta: @automated
Given Retrieve image series by patient ids - DataSetUp Provided
When Get image series by patient ids
Then verify image series by patient ids

@test
Scenario: Get a Patient
Meta: @automated
Given Get a Patient - DataSetUp Provided
When Get a Patient
Then verify Get a Patient

@test
Scenario: Get Patients
Meta: @automated
Given Get Patients - DataSetUp Provided
When Get Patients
Then verify Get Patients

@test
Scenario: Get All Patients
Meta: @automated
Given Get All Patients - DataSetUp Provided
When Get All Patients
Then verify Get All Patients

@test
Scenario: Save Patient with Null PtId
Meta: @automated
Given Save Patient with Null Patient id - DataSetUp Provided
When save Patient with Null Patient id
Then verify Saving Patient with Null Patient id

@test
Scenario: Save Patient with Null ID
Meta: @automated
Given Save Patient with Null ID - DataSetUp Provided
When save Patient with Null ID
Then verify Saving Patient with Null ID

@test
Scenario: Save Existing Patient retrievs patient details
Meta: @automated
Given Save Existing Patient - DataSetUp Provided
When save Existing Patient
Then verify Saving Existing Patient

@test
Scenario: Retrieve image series by patient id when patient ids list returned null
Meta: @automated
Given Retrieve image series by patient when patient ids list returned null- DataSetUp Provided
When Get image series by patient ids when patient ids list returned null
Then verify image series by patient ids when patient ids list returned null

@test
Scenario: Retrieve image series by patient id
Meta: @automated
Given Retrieve image series by patient when patient ids list returned empty- DataSetUp Provided
When Get image series by patient ids when patient ids list returned empty
Then verify image series by patient ids when patient ids list returned empty

