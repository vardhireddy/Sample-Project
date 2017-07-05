Feature: DataCatalog

  Narrative: As a data scientist, I should be able to retrieve data from datacatalog

  Scenario: Verify Health Chek Page for  experiment from the learning factory app
    Given datacatalog health check
    Then  verify success