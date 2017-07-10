Feature: DataCatalog

  Narrative: As a data scientist, I should be able to retrieve data from datacatalog

  Scenario: Verify Health Chek Page for  experiment from the learning factory app
    Given datacatalog health check
    Then  verify success

#  Scenario: Retrieve DataSet with ID
#    When Get data collection details by its id
#    Then verify data collection details by its id
#
#  Scenario: Retrieve DataSet with TYPE
#    When Get data collection details by its type
#    Then verify data collection details by its type
