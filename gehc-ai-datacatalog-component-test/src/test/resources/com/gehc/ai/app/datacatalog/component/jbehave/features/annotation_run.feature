Feature: Annotation DataCatalog

  Narrative: As a data scientist, I should be able to study data from datacatalog

  Scenario: Store an annotation set data
  Given Store an annotation set data - DataSetUp Provided
  When Store an annotation set data
  Then Verify Store an annotation set data

  Scenario: Get annotation set data
  Given Get annotation set data - DataSetUp Provided
  When Get annotation set data
  Then Verify Get annotation set data

  Scenario: Get annotation set data
  Given Get annotation set data by Imageset Id - DataSetUp Provided
  When Get annotation set data by Imageset Id
  Then Verify Get annotation set data by Imageset Id

#  Scenario: Get annotation set data for Ids
#  Given Get annotation set data for Ids - DataSetUp Provided
#  When Get annotation set data for Ids
#  Then Verify Get annotation set data for Ids