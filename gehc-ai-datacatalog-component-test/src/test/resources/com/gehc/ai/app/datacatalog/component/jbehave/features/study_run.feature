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

#  Scenario: Get Multiple Studies
#  Given Get Multiple Studies - DataSetUp Provided
#  When Get Multiple Studies
#  Then verify Get Multiple Studies