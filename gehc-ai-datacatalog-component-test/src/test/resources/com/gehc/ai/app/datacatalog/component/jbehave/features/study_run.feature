Feature: Study DataCatalog

  Narrative: As a data scientist, I should be able to study data from datacatalog

  Scenario: Get Imageset by study
  Given Get Imageset by study - DataSetUp Provided
  When Get Imageset by study
  Then verify Imageset by study

  Scenario: Get all Studies
  Given Get all Studies - DataSetUp Provided
  When Get all Studies
  Then verify Get all Studies

  Scenario: Save study
  Given Save study - DataSetUp Provided
  When Save study
  Then verify Save study

  Scenario: Save study should return existing study if it already exists
  Given Save existing study - DataSetUp Provided
  When Save existing study
  Then verify Save study should return existing study if it already exists

  Scenario: Get Multiple Studies
  Given Get Multiple Studies - DataSetUp Provided
  When Get Multiple Studies
  Then verify Get Multiple Studies

  Scenario: Save Study with Null StudyId
  Given Save Study with Null Study id - DataSetUp Provided
  When save Study with Null Study id
  Then verify Saving Study with Null Study id

  Scenario: Save Study with Null PatientDbId
  Given Save Study with Null PatientDbId - DataSetUp Provided
  When save Study with Null PatientDbId
  Then verify Saving Study with Null PatientDbId
  
  Scenario: Save new study
    Given Save new study - DataSetUp Provided
    When Save new study
    Then verify Save new study

#  Scenario: Get Multiple Studies with orgid null
#  Given Get Multiple Studies with orgid null- DataSetUp Provided
#  When Get Multiple Studies with orgid null
#  Then verify Get Multiple Studies  with orgid null