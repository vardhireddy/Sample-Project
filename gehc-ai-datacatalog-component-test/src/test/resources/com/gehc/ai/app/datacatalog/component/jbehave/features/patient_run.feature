Feature: Patient DataCatalog

  Narrative: As a data scientist, I should be able to patient data from datacatalog

  Scenario: Get study by patient
  Given Retrieve study by patient Provided
  When Get study by patient
  Then verify study by patient

  Scenario: Save Patient
  Given Save Patient - DataSetUp Provided
  When save Patient
  Then verify Saving Patient

  Scenario: Retrieve image series by patient id
  Given Retrieve image series by patient id - DataSetUp Provided
  When Get image series by patient id
  Then verify image series by patient id

  Scenario: Retrieve image series by patient ids
  Given Retrieve image series by patient ids - DataSetUp Provided
  When Get image series by patient ids
  Then verify image series by patient ids

  Scenario: Get a Patient
  Given Get a Patient - DataSetUp Provided
  When Get a Patient
  Then verify Get a Patient

  Scenario: Get Patients
  Given Get Patients - DataSetUp Provided
  When Get Patients
  Then verify Get Patients

  Scenario: Get All Patients
  Given Get All Patients - DataSetUp Provided
  When Get All Patients
  Then verify Get All Patients

  Scenario: Save Patient with Null PtId
  Given Save Patient with Null Patient id - DataSetUp Provided
  When save Patient with Null Patient id
  Then verify Saving Patient with Null Patient id

  Scenario: Save Patient with Null ID
  Given Save Patient with Null ID - DataSetUp Provided
  When save Patient with Null ID
  Then verify Saving Patient with Null ID

#  Scenario: Patient Search by PatientId
#    Given Get Patient Search by PatientId - DataSetUp Provided
#    When Get Patient Search by PatientId
#    Then verify Patient Search by PatientId