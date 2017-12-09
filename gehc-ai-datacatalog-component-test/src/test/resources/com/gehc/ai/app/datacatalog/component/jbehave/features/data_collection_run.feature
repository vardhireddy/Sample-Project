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
  
  Scenario: Retrieve DataSet by Type return empty
  Given Return empty array list
  When Get data collection by Type is not valid
  Then verify data collection by Type is not valid

  Scenario: Save DataSet
  Given Save DataSet - DataSetUp Provided
  When save DataSet
  Then verify Saving DataSet

  Scenario: Retrieve Image Set with ID when no imageset
  Given Retrieve Image Set with ID DataSetUp Provided when no imageset
  When Get data collection image-set details by its id when no imageset
  Then verify data collection image-set details by its id when no imageset

  Scenario: DataCatalog Raw Target Data
  Given DataCatalog Raw Target Data - DataSetUp Provided
  When get DataCatalog Raw Target Data
  Then verify DataCatalog Raw Target Data
  
  Scenario: DataCatalog Raw Target Data with invalid Id- throws Exception
  Given DataCatalog Raw Target Data with invalid Id
  When get DataCatalog Raw Target Data with invalid Id
  Then verify DataCatalog Raw Target Data with invalid Id- throws Exception
  
  Scenario: DataCatalog Raw Target Data with long Id- throws Exception
  Given DataCatalog Raw Target Data with long Id
  When get DataCatalog Raw Target Data with long Id
  Then verify DataCatalog Raw Target Data with long Id- throws Exception
  
  Scenario: DataCatalog Raw Target Data with invalid annotationType- throws Exception
  Given DataCatalog Raw Target Data with invalid annotationType
  When get DataCatalog Raw Target Data with invalid annotationType
  Then verify DataCatalog Raw Target Data with invalid annotationType- throws Exception
  
  Scenario: DataCatalog Raw Target Data with long annotationType- throws Exception
  Given DataCatalog Raw Target Data with long annotationType
  When get DataCatalog Raw Target Data with long annotationType
  Then verify DataCatalog Raw Target Data with long annotationType- throws Exception

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

  Scenario: DataCatalog Raw Target Data for empty DataSet
  Given DataCatalog Raw Target Data for empty DataSet - DataSetUp Provided
  When get DataCatalog Raw Target Data for empty DataSet
  Then verify DataCatalog Raw Target Data for empty DataSet

  Scenario: Retrieve DataSet for Filters by OrgId
  Given Retrieve DataSet for Filters by OrgId DataSetUp Provided
  When Get DataSet for Filters by OrgId
  Then verify DataSet for Filters by OrgId

  Scenario: Retrieve DataSet for Filters by OrgId when Annotation Absent
  Given Retrieve DataSet for Filters by OrgId when Annotation Absent DataSetUp Provided
  When Get DataSet for Filters by OrgId when Annotation Absent
  Then verify DataSet for Filters by OrgId when Annotation Absent

  Scenario: Retrieve DataSet for Filters by OrgId when Annotation count is empty
  Given Retrieve DataSet for Filters by OrgId when Annotation count is empty DataSetUp Provided
  When Get DataSet for Filters by OrgId when Annotation count is empty
  Then verify DataSet for Filters by OrgId when Annotation count is empty

  Scenario: Retrieve DataSet group by ANNOTATIONS_ABSENT
  Given Retrieve DataSet  group by ANNOTATIONS_ABSENT DataSetUp Provided
  When Get DataSet  group by ANNOTATIONS_ABSENT
  Then verify DataSet  group by ANNOTATIONS_ABSENT

  Scenario: Retrieve DataSummary for GE-Class
  Given Retrieve DataSummary for GE-Class
  When Get DataSummary for GE-Class
  Then verify DataSummary for GE-Class
  
  Scenario: Retrieve DataSummary for GE-Class with invalid annotation type
  Given Retrieve DataSummary for GE-Class with invalid annotation type
  When Get DataSummary for GE-Class with invalid annotation type
  Then verify DataSummary for GE-Class with invalid annotation type

  Scenario: Retrieve DataSummary for GE-Class without org id
  Given Retrieve DataSummary for GE-Class without org id
  When Get DataSummary for GE-Class without org id
  Then verify DataSummary for GE-Class without org id

  Scenario: Delete multiple Data Collections by ids 
  Given  Multiple Data Collections by ids 
  When Delete Data collection by ids API is called
  Then verify Data Collection by ids has been deleted