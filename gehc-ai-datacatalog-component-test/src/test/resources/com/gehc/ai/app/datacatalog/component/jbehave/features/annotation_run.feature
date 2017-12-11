Feature: DataCatalog Component - Annotation

Narrative: As a data scientist, I should be able to
Store annotation set data
Get annotation set data
Get annotation set data by Imageset Id
Get annotation set data for Ids
Delete annotation set data for Ids
Delete annotation set data for Ids with out org id
Throw Exception while Storing an annotation set data
Throw Exception while Delete annotation set data for Ids

@functional
@crs

@test
Scenario: Store an annotation set data
Meta: @automated
Given Store an annotation set data - DataSetUp Provided
When Store an annotation set data
Then Verify Store an annotation set data

@test
Scenario: Get annotation set data
Meta: @automated
Given Get annotation set data - DataSetUp Provided
When Get annotation set data
Then Verify Get annotation set data

@test
Scenario: Get annotation set data by Imageset Id
Meta: @automated
Given Get annotation set data by Imageset Id - DataSetUp Provided
When Get annotation set data by Imageset Id
Then Verify Get annotation set data by Imageset Id

@test
Scenario: Get annotation set data for Ids
Meta: @automated
Given Get annotation set data for Ids - DataSetUp Provided
When Get annotation set data for Ids
Then Verify Get annotation set data for Ids

@test
Scenario: Delete annotation set data for Ids
Meta: @automated
Given Delete annotation set data for Ids - DataSetUp Provided
When Delete annotation set data for Ids
Then Verify Delete annotation set data for Ids

@test
Scenario: Delete annotation set data for Ids with out org id
Meta: @automated
Given Delete annotation set data for Ids with out org id - DataSetUp Provided
When Delete annotation set data for Ids with out org id
Then Verify Delete annotation set data for Ids with out org id

@test
Scenario: Throw Exception while Storing an annotation set data
Meta: @automated
Given Store an annotation set data for throwing exception - DataSetUp Provided
When Store an annotation set data throws exception
Then Verify Store an annotation set data throws exception

@test
Scenario: Throw Exception while Delete annotation set data for Ids
Meta: @automated
Given Delete annotation set data for Ids throws exception - DataSetUp Provided
When Delete annotation set data for Ids throws exception
Then Verify Delete annotation set data for Ids throws exception

