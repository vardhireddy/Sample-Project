Feature: DataCatalog Component - Contract

Narrative: As a data scientist, I should be able to
Store contract data
Retrieve contract data

 @functional
@crs

@test
Scenario: Store contract data
Meta: @automated
Given Store contract data - DataSetUp Provided
When Store contract data
Then verify Store contract data

@test
Scenario: Retrieve contract data
Meta: @automated
Given Retrieve contract data - DataSetUp Provided
When Retrieve contract data
Then verify Retrieve contract data