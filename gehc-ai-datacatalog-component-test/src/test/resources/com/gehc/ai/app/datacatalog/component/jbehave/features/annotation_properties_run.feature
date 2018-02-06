Feature: DataCatalog Component - Annotation Properties

Narrative: As a data scientist, I should be able to set or get Annotation Properties data from datacatalog
I should be able to verify Get Annotation Properties Throws Service Exception
I should be able to Get Annotation Properties set data Throws Exception
I should be able to Post Annotation Properties set data Throws Exception

@functional
@crs_10730

@test_53589
Scenario: Store an Annotation Properties set data
Meta: @automated
Given Store an Annotation Properties set data - DataSetUp Provided
When Store an Annotation Properties set data
Then Verify Store an Annotation Properties set data

@test_53590
Scenario: Get Annotation Properties set data
Meta: @automated
Given Get Annotation Properties set data - DataSetUp Provided
When Get Annotation Properties set data
Then Verify Get Annotation Properties set data

@test_53591
Scenario: Get Annotation Properties - Throws Service Exception
Meta: @automated
Given Get Annotation Properties set data Throws Service Exception - DataSetUp Provided
When Get Annotation Properties set data - Throws Service Exception
Then Verify Get Annotation Properties set data Throws Service Exception

@test_53592
Scenario: Get Annotation Properties - Throws Exception
Meta: @automated
Given Get Annotation Properties set data Throws Exception - DataSetUp Provided
When Get Annotation Properties set data - Throws Exception
Then Verify Get Annotation Properties set data Throws Exception

@test_53593
Scenario: Post Annotation Properties - Throws Exception
Meta: @automated
Given Post Annotation Properties set data Throws Exception - DataSetUp Provided
When Post Annotation Properties set data - Throws Exception
Then Verify Post Annotation Properties set data Throws Exception

@test
Scenario: Get Annotation Properties set data with invalid orgId - Throws Exception
Meta: @automated
Given Get Annotation Properties set data with invalid orgId Throws Exception - DataSetUp Provided
When Get Annotation Properties set data with invalid orgId - Throws Exception
Then Verify Get Annotation Properties set data with invalid orgId Throws Exception

@test
Scenario: Get Annotation Properties set data with long orgId - Throws Exception
Meta: @automated
Given Get Annotation Properties set data with long orgId Throws Exception - DataSetUp Provided
When Get Annotation Properties set data with long orgId - Throws Exception
Then Verify Get Annotation Properties set data with long orgId Throws Exception