Feature: DataCatalog

  Narrative: As a data scientist, I should be able to retrieve data from datacatalog

  Scenario: Verify Health Check Page for  experiment from the learning factory app
    Given datacatalog health check
    Then  verify success

  Scenario: Retrieve DataSet with ID
    Given Retrieve DataCatalog with ID DataSetUp Provided
    When Get data collection details by its id
    Then verify data collection details by its id

  Scenario: Retrieve DataSet by Org ID
    Given Retrieve DataCatalog with Org ID DataSetUp Provided
    When Get data collection by Org Id
    Then verify data collection by Org Id

  Scenario: Retrieve DataSet by Type
    Given Retrieve DataSet by Type DataSetUp Provided
    When Get data collection by Type -  Annotation
    Then verify data collection by Type -  Annotation

#  Scenario: Retrieve ImageSet with ID
#    When Get data collection image-set details by its id
#    Then verify data collection image-set details by its id

  Scenario: Save DataSet
    Given Save DataSet - DataSetUp Provided
    When save DataSet
    Then verify Saving DataSet

  Scenario: Retrieve image series by patient id
    Given Retrieve image series by patient id - DataSetUp Provided
    When Get image series by patient id
    Then verify image series by patient id

  Scenario: Retrieve image series by id
    Given Retrieve image series by id - DataSetUp Provided
    When Get image series by image series id
    Then verify image series by image series id

  Scenario: Retrieve image series by series instance uid
    Given Retrieve image series by series instance uid - DataSetUp Provided
    When Get image series by series instance uid
    Then verify image series by series instance uid