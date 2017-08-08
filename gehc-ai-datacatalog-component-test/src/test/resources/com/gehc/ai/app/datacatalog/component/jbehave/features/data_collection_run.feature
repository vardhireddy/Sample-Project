Feature: DataCollection DataCatalog

  Narrative: As a data scientist, I should be able to retrieve data from datacatalog

  Scenario: Verify Health Check Page for  experiment from the learning factory app
  Given datacatalog health check
  Then  verify success

  Scenario: Verify Health Check Page with lowercase for  experiment from the learning factory app
  Given datacatalog health check with lowercase
  Then  verify success for with lowercase

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

  Scenario: Save DataSet
  Given Save DataSet - DataSetUp Provided
  When save DataSet
  Then verify Saving DataSet

#  Scenario: Retrieve Image Set with ID when no imageset
#  Given Retrieve Image Set with ID DataSetUp Provided when no imageset
#  When Get data collection image-set details by its id when no imageset
#  Then verify data collection image-set details by its id when no imageset

  Scenario: DataCatalog Raw Target Data
  Given DataCatalog Raw Target Data - DataSetUp Provided
  When get DataCatalog Raw Target Data
  Then verify DataCatalog Raw Target Data

  Scenario: Retrieve Image Set with ID
  Given Retrieve Image Set with ID DataSetUp Provided
  When Get data collection image-set details by its id
  Then verify data collection image-set details by its id

  Scenario: Post DataSet by Org ID null
  Given Post DataCatalog with Org ID null DataSetUp Provided
  When Post data collection by Org Id null
  Then verify data collection by Org Id null

  Scenario: DataCatalog Raw Target Data with id null
  Given DataCatalog Raw Target Data with id null - DataSetUp Provided
  When get DataCatalog Raw Target Data with id null
  Then verify DataCatalog Raw Target Data with id null

#  Scenario: DataCatalog Raw Target Data for empty DataSet
#  Given DataCatalog Raw Target Data for empty DataSet - DataSetUp Provided
#  When get DataCatalog Raw Target Data for empty DataSet
#  Then verify DataCatalog Raw Target Data for empty DataSet


