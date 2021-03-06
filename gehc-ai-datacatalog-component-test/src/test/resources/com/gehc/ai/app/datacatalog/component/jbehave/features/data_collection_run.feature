Feature: DataCatalog Component - DataCollection

Narrative: As a data scientist, I should be able to
Verify Health Check Page for  datacatalog from the learning factory app
Retrieve DataSet with ID
Retrieve DataSet by Org ID
Retrieve DataSet by Type
Save DataSet
Retrieve Image Set with ID when no imageset
Retrieve Image Set with ID
Post DataSet by Org ID null
Retrieve DataSet for Filters by OrgId
Retrieve DataSet for Filters by OrgId when Annotation Absent
Retrieve DataSet for Filters by OrgId when Annotation count is empty
Retrieve DataSet group by ANNOTATIONS_ABSENT
Retrieve DataSummary for GE-Class
Retrieve DataSummary for GE-Class without org id
Delete multiple Data Collections by ids
Retrieve DataSet by Type return empty
Retrieve DataSummary for GE-Class with invalid annotation type
Get Annotaition Ids by datacollectionId
Get Annotaition Ids by datacollectionId When ImageSeriesNotFound
Delete Data Collection by id
Export a data collection's annotations as CSV when the data collection contains at least one image set and each image set contains at least one annotation
For a data collection/set ID supported by LF get the contracts associated with the image sets of that data collection
For a data collection/set ID not supported by LF get the contracts associated with the image sets of that data collection
@functional
@crs_10733

@test_53603
Scenario: Verify Health Check Page for  datacatalog from the learning factory app
Meta: @automated
Given datacatalog health check
Then  verify success

@test_53604
Scenario: Verify Health Check Page with lowercase for  datacatalog from the learning factory app
Meta: @automated
Given datacatalog health check with lowercase
Then  verify success for with lowercase

@test_53605
Scenario: Retrieve DataSet with ID
Meta: @automated
Given Retrieve DataCatalog with ID DataSetUp Provided
When Get data collection details by its id
Then verify data collection details by its id

#@test_53606
#Scenario: Retrieve DataSet by Org ID
#Meta: @automated
#Given Retrieve DataCatalog with Org ID DataSetUp Provided
#When Get data collection by Org Id
#Then verify data collection by Org Id

#@test_53607
#Scenario: Retrieve DataSet by Type
#Meta: @automated
#Given Retrieve DataSet by Type DataSetUp Provided
#When Get data collection by Type -  Annotation
#Then verify data collection by Type -  Annotation

@test_53608
Scenario: Save DataSet
Meta: @automated
Given Save DataSet - DataSetUp Provided
When save DataSet
Then verify Saving DataSet

@test_53609
Scenario: Retrieve Image Set with ID when no imageset
Meta: @automated
Given Retrieve Image Set with ID DataSetUp Provided when no imageset
When Get data collection image-set details by its id when no imageset
Then verify data collection image-set details by its id when no imageset

@test_53611
Scenario: Retrieve Image Set with ID
Meta: @automated
Given Retrieve Image Set with ID DataSetUp Provided
When Get data collection image-set details by its id
Then verify data collection image-set details by its id

@test_53612
Scenario: Post DataSet by Org ID null
Meta: @automated
Given Post DataCatalog with Org ID null DataSetUp Provided
When Post data collection by Org Id null
Then verify data collection by Org Id null

@test_53615
Scenario: Retrieve DataSet for Filters by OrgId
Meta: @automated
Given Retrieve DataSet for Filters by OrgId DataSetUp Provided
When Get DataSet for Filters by OrgId
Then verify DataSet for Filters by OrgId

@test_53616
Scenario: Retrieve DataSet for Filters by OrgId when Annotation Absent
Meta: @automated
Given Retrieve DataSet for Filters by OrgId when Annotation Absent DataSetUp Provided
When Get DataSet for Filters by OrgId when Annotation Absent
Then verify DataSet for Filters by OrgId when Annotation Absent

@test_53617
Scenario: Retrieve DataSet for Filters by OrgId when Annotation count is empty
Meta: @automated
Given Retrieve DataSet for Filters by OrgId when Annotation count is empty DataSetUp Provided
When Get DataSet for Filters by OrgId when Annotation count is empty
Then verify DataSet for Filters by OrgId when Annotation count is empty

@test_53618
Scenario: Retrieve DataSet group by ANNOTATIONS_ABSENT
Meta: @automated
Given Retrieve DataSet  group by ANNOTATIONS_ABSENT DataSetUp Provided
When Get DataSet  group by ANNOTATIONS_ABSENT
Then verify DataSet  group by ANNOTATIONS_ABSENT

@test_53619
Scenario: Retrieve DataSummary for GE-Class
Meta: @automated
Given Retrieve DataSummary for GE-Class
When Get DataSummary for GE-Class
Then verify DataSummary for GE-Class

@test_53620
Scenario: Retrieve DataSummary for GE-Class without org id
Meta: @automated
Given Retrieve DataSummary for GE-Class without org id
When Get DataSummary for GE-Class without org id
Then verify DataSummary for GE-Class without org id

@test_53621
Scenario: Delete multiple Data Collections by ids
Meta: @automated
Given  Multiple Data Collections by ids
When Delete Data collection by ids API is called
Then verify Data Collection by ids has been deleted

#@test_54805
#Scenario: Retrieve DataSet by Type return empty
#Meta: @automated
#Given Return empty array list
#When Get data collection by Type is not valid
#Then verify data collection by Type is not valid

@test_54806
Scenario: Retrieve DataSummary for GE-Class with invalid annotation type
Meta: @automated
Given Retrieve DataSummary for GE-Class with invalid annotation type
When Get DataSummary for GE-Class with invalid annotation type
Then verify DataSummary for GE-Class with invalid annotation type

@test_54807
Scenario: Get Annotaition Ids by datacollectionId
Meta: @automated
Given Get Annotaition Ids by datacollectionId - Data Setup
When Get Annotaition Ids by datacollectionId is called
Then verify Get Annotaition Ids by datacollectionId

@test_54808
Scenario: Get Annotaition Ids by datacollectionId When ImageSeriesNotFound
Meta: @automated
Given Get Annotaition Ids by datacollectionId When ImageSeriesNotFound - Data Setup
When Get Annotaition Ids by datacollectionId is called When ImageSeriesNotFound
Then verify Get Annotaition Ids by datacollectionId  When ImageSeriesNotFound

@test_54822
Scenario: Delete Data Collection by id
Meta: @automated
Given  Delete Data Collections by id
When Delete Data collection by id API is called
Then verify Data Collection by id has been deleted

@test
Scenario: Export a data collection's annotations as CSV when the data collection contains at least one image set and each image set contains at least one annotation
Meta: @automated
Given a data collection contains at least one image set and each image set contains at least one annotation
When the API which exports a data collection's annotations as CSV is called
Then the response's status code should be 200
Then the response's content type should be CSV
Then the response's body should contain a string
