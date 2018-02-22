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
Store an annotation set data when Annotation already exists should not save dupicate Annotation
Store annotation set data with invalid annotator id
Store annotation set data with invalid annotation type
Store new Annotation annotation

@functional
@crs_10731

@test_53594
Scenario: Store an annotation set data
Meta: @automated
Given Store an annotation set data - DataSetUp Provided
When Store an annotation set data
Then Verify Store an annotation set data

@test_53595
Scenario: Get annotation set data
Meta: @automated
Given Get annotation set data - DataSetUp Provided
When Get annotation set data
Then Verify Get annotation set data

@test_53596
Scenario: Get annotation set data by Imageset Id
Meta: @automated
Given Get annotation set data by Imageset Id - DataSetUp Provided
When Get annotation set data by Imageset Id
Then Verify Get annotation set data by Imageset Id

@test_53597
Scenario: Get annotation set data for Ids
Meta: @automated
Given Get annotation set data for Ids - DataSetUp Provided
When Get annotation set data for Ids
Then Verify Get annotation set data for Ids

@test_53598
Scenario: Delete annotation set data for Ids
Meta: @automated
Given Delete annotation set data for Ids - DataSetUp Provided
When Delete annotation set data for Ids
Then Verify Delete annotation set data for Ids

@test_53599
Scenario: Delete annotation set data for Ids with out org id
Meta: @automated
Given Delete annotation set data for Ids with out org id - DataSetUp Provided
When Delete annotation set data for Ids with out org id
Then Verify Delete annotation set data for Ids with out org id

@test_53600
Scenario: Throw Exception while Storing an annotation set data
Meta: @automated
Given Store an annotation set data for throwing exception - DataSetUp Provided
When Store an annotation set data throws exception
Then Verify Store an annotation set data throws exception

@test_53601
Scenario: Throw Exception while Delete annotation set data for Ids
Meta: @automated
Given Delete annotation set data for Ids throws exception - DataSetUp Provided
When Delete annotation set data for Ids throws exception
Then Verify Delete annotation set data for Ids throws exception

@test_54815
Scenario: Store an annotation set data when Annotation already exists should not save dupicate Annotation
Meta: @automated
Given Store an annotation set data Annotation already exists - DataSetUp Provided
When Store an annotation set data  Annotation already exists
Then Verify Store an annotation set data if Annotation already exists should not save duplicate Annotation and will return the annotation id

@test_54816
Scenario: Store annotation set data with invalid annotator id
Meta: @automated
Given Store an annotation set data with invalid annotator id- Throws exception
When Store an annotation set data with invalid annotator id
Then Verify Store annotation set data throws exception

@test_54817
Scenario: Store annotation set data with invalid annotation type
Meta: @automated
Given Store an annotation set data with invalid annotation type- Throws exception
When Store an annotation set data with invalid annotation type
Then Verify Store annotation set data throws exception

@test_54818
Scenario: Store new Annotation annotation
Meta: @automated
Given Store new Annotationset data - DataSetUp Provided
When Store new Annotation set data
Then Verify Store new Annotation set data

@test_54821
Scenario: Delete annotation set data for multiple Ids
Meta: @automated
Given Delete annotation set data for multiple Ids - DataSetUp Provided
When Delete annotation set data for multiple Ids
Then Verify Delete annotation set data for multiple Ids


