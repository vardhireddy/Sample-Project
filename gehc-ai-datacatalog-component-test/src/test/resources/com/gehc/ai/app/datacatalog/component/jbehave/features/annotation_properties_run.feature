Feature: DataCatalog Component - Annotation Properties

Narrative: As a data scientist, I should be able to set or get Annotation Properties data from datacatalog
I should be able to verify Get Annotation Properties Throws Service Exception
I should be able to Get Annotation Properties set data Throws Exception
I should be able to Post Annotation Properties set data Throws Exception

@functional
@crs

@test
Scenario: Store an Annotation Properties set data
Meta: @automated
Given Store an Annotation Properties set data - DataSetUp Provided
When Store an Annotation Properties set data
Then Verify Store an Annotation Properties set data

@test
Scenario: Get Annotation Properties set data
Meta: @automated
Given Get Annotation Properties set data - DataSetUp Provided
When Get Annotation Properties set data
Then Verify Get Annotation Properties set data

@test
Scenario: Get Annotation Properties - Throws Service Exception
Meta: @automated
Given Get Annotation Properties set data Throws Service Exception - DataSetUp Provided
When Get Annotation Properties set data - Throws Service Exception
Then Verify Get Annotation Properties set data Throws Service Exception

@test
Scenario: Get Annotation Properties - Throws Exception
Meta: @automated
Given Get Annotation Properties set data Throws Exception - DataSetUp Provided
When Get Annotation Properties set data - Throws Exception
Then Verify Get Annotation Properties set data Throws Exception

@test
Scenario: Post Annotation Properties - Throws Exception
Meta: @automated
Given Post Annotation Properties set data Throws Exception - DataSetUp Provided
When Post Annotation Properties set data - Throws Exception
Then Verify Post Annotation Properties set data Throws Exception