Feature: Study DataCatalog

  Narrative: As a data scientist, I should be able to study data from datacatalog

  Scenario: Get Imageset by study
  Given Get Imageset by study - DataSetUp Provided
  When Get Imageset by study
  Then verify Imageset by study