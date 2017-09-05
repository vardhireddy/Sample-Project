Feature: Annotation DataCatalog

  Narrative: As a data scientist, I should be able to Annotation data from datacatalog

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

  Scenario: Get annotation set data for Ids
  Given Get annotation set data for Ids - DataSetUp Provided
  When Get annotation set data for Ids
  Then Verify Get annotation set data for Ids

  Scenario: Delete annotation set data for Ids
  Given Delete annotation set data for Ids - DataSetUp Provided
  When Delete annotation set data for Ids
  Then Verify Delete annotation set data for Ids

  Scenario: Delete annotation set data for Ids with out org id
  Given Delete annotation set data for Ids with out org id - DataSetUp Provided
  When Delete annotation set data for Ids with out org id
  Then Verify Delete annotation set data for Ids with out org id

  Scenario: Throw Exception while Storing an annotation set data
  Given Store an annotation set data for throwing exception - DataSetUp Provided
  When Store an annotation set data throws exception
  Then Verify Store an annotation set data throws exception

  Scenario: Throw Exception while Delete annotation set data for Ids
  Given Delete annotation set data for Ids throws exception - DataSetUp Provided
  When Delete annotation set data for Ids throws exception
  Then Verify Delete annotation set data for Ids throws exception

#  Scenario: Get annotation set data for Ids null
#  Given Get annotation set data for Ids null - DataSetUp Provided
#  When Get annotation set data for Ids null
#  Then Verify Get annotation set data for Ids null
