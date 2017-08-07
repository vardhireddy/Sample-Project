Feature: Annotation Properties DataCatalog

  Narrative: As a data scientist, I should be able to set or get Annotation Properties data from datacatalog

  Scenario: Store an Annotation Properties set data
  Given Store an Annotation Properties set data - DataSetUp Provided
  When Store an Annotation Properties set data
  Then Verify Store an Annotation Properties set data

  Scenario: Get Annotation Properties set data
  Given Get Annotation Properties set data - DataSetUp Provided
  When Get Annotation Properties set data
  Then Verify Get Annotation Properties set data

  Scenario: Get Annotation Properties set data - Throws Service Exception
  Given Get Annotation Properties set data Throws Service Exception - DataSetUp Provided
  When Get Annotation Properties set data - Throws Service Exception
  Then Verify Get Annotation Properties set data Throws Service Exception

  Scenario: Get Annotation Properties set data - Throws Exception
  Given Get Annotation Properties set data Throws Exception - DataSetUp Provided
  When Get Annotation Properties set data - Throws Exception
  Then Verify Get Annotation Properties set data Throws Exception

  Scenario: Post Annotation Properties set data - Throws Exception
  Given Post Annotation Properties set data Throws Exception - DataSetUp Provided
  When Post Annotation Properties set data - Throws Exception
  Then Verify Post Annotation Properties set data Throws Exception