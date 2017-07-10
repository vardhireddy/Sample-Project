Feature: DataCatalog

  Narrative: As a data scientist, I should be able to retrieve data from datacatalog

  Scenario: Verify Health Check Page for  experiment from the learning factory app
    Given datacatalog health check
    Then  verify success

  Scenario: Retrieve DataSet with ID
    When Get data collection details by its id
    Then verify data collection details by its id

  Scenario: Retrieve DataSet by Org ID
    When Get data collection by Org Id
    Then verify data collection by Org Id

  Scenario: Retrieve DataSet by Type
    When Get data collection by Type -  Annotation
    Then verify data collection by Type -  Annotation

#  Scenario: Retrieve ImageSet with ID
#    When Get data collection image-set details by its id
#    Then verify data collection image-set details by its id

  Scenario: Save DataSet
    When save DataSet
    Then verify Saving DataSet