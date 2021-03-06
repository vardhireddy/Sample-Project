Feature: DataCatalog Component - ImageSet

Narrative: As a data scientist, I should be able to Imageset data from datacatalog
Retrieve image series by id
Retrieve image series by series instance uid
Store image set data
Get Image set based on filter criteria with SUID
Get Image set based on filter criteria with ORG ID and Modality
Get Image set based on filter criteria with ORG ID , Modality and Anatomy
Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation
Get Image set based on filter criteria with ORG ID
Get Image set based on filter criteria with ORG ID and Anatomy
Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT
Get Image set based on filter criteria with ORG ID and Modality throws Exception
Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries
Get Image set based on filter criteria with SUID  Thorws runtime exception
Get Image set based on filter criteria with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation
Get Image set based on filter criteria with Equipment
Get Image set based on filter criteria with Institution
Get Image set based on filter criteria with DataFormat
Get Image set based on filter criteria with Institution and Equipment
Get Image set based on filter criteria with DataFormat and  Institution
Get Image set based on filter criteria with DataFormat, Institution and Equipment
Get Image set based on filter criteria with DataFormat and Equipment
Get Image set based on filter criteria with Anatomy and DataFormat
Get Image set based on filter criteria with Anatomy, DataFormat and Institution
Get Image set based on filter criteria with Anatomy, DataFormat, Institution and Equipment
Get Image set based on filter criteria with Anatomy, Institution
Get Image set based on filter criteria with Anatomy, Institution and Equipment
Get Image set based on filter criteria with Anatomy, Equipment
Get Image set based on filter criteria with Modality, Anatomy and DataFormat
Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Institution
Get Image set based on filter criteria with Modality, Anatomy, DataFormat, Institution and Equipment
Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Equipment
Get Image set based on filter criteria with Modality, Anatomy, Institution and Equipment
Get Image set based on filter criteria with Modality, Anatomy, Institution
Get Image set based on filter criteria with Modality, Anatomy, Equipment
Get Image set based on filter criteria with Modality, Equipment
Get Image set based on filter criteria with Modality, Institution
Get Image set based on filter criteria with Modality, DataFormat
Get Image set based on filter criteria with Modality, DataFormat,Institution And Equipment
Get Image set based on filter criteria with Modality, DataFormat And Equipment
Get Image set based on filter criteria with Modality, DataFormat And Institution
Get Image set based on filter criteria with Modality, Institution and Equipment
Get Image set based on filter criteria with Institution name containing accepted special characters like comma
Get Image set based on filter criteria with Equipment name containing accepted special characters like quotes and slashes
Get Image set based on filter criteria with ORG ID , Modality, Anatomy and DateRange
Get count of unique Image sets annotated by Radiologist for given Organization Id
Get count of unique Image sets annotated by Radiologist for given Organization Id should return 204 - No Content
Get count of unique Image sets annotated by Radiologist for given Organization Id API throws Exception

@functional
@crs_10734


@test_53622
Scenario: Retrieve image series by id
Meta: @automated
Given Retrieve image series by id - DataSetUp Provided
When Get image series by image series id
Then verify image series by image series id

@test_53623
Scenario: Retrieve image series by series instance uid
Meta: @automated
Given Retrieve image series by series instance uid - DataSetUp Provided
When Get image series by series instance uid
Then verify image series by series instance uid

@test_53624
Scenario: Store image set data
Meta: @automated
Given Store an image set data - DataSetUp Provided
When Store an image set data
Then verify Store an image set data

@test_53625
Scenario: Get Image set based on filter criteria with SUID
Meta: @automated
Given Get Image set based on filter criteria with SUID - DataSetUp Provided
When Get Image set based on filter criteria with SUID
Then verify Image set based on filter with SUID

@test_53626
Scenario: Get Image set based on filter criteria with ORG ID and Modality
Meta: @automated
Given Get Image set based on filter criteria with ORG ID and Modality - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID and Modality
Then verify Image set based on filter  with ORG ID and Modality

@test_53627
Scenario: Get Image set based on filter criteria with ORG ID , Modality and Anatomy
Meta: @automated
Given Get Image set based on filter criteria with ORG ID , Modality and Anatomy - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID , Modality and Anatomy
Then verify Image set based on filter  with ORG ID , Modality and Anatomy

@test_53628
Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation
Meta: @automated
Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation
Then verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation

@test_53629
Scenario: Get Image set based on filter criteria with ORG ID
Meta: @automated
Given Get Image set based on filter criteria with ORG ID - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID
Then verify Image set based on filter  with ORG ID

@test_53630
Scenario: Get Image set based on filter criteria with ORG ID and Anatomy
Meta: @automated
Given Get Image set based on filter criteria with ORG ID and Anatomy - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID and Anatomy
Then verify Image set based on filter  with ORG ID and Anatomy

@test_53631
Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT
Meta: @automated
Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT
Then verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation ABSENT

@test_53632
Scenario: Get Image set based on filter criteria with ORG ID and Modality throws Exception
Meta: @automated
Given Get Image set based on filter criteria with ORG ID and Modality throws Exception - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID and Modality throws Exception
Then verify Image set based on filter  with ORG ID and Modality throws Exception

@test_53633
Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries
Meta: @automated
Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries
Then verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation ABSENT returns empty ImageSeries

@test_53634
Scenario: Get Image set based on filter criteria with SUID  Thorws runtime exception
Meta: @automated
Given Get Image set based on filter criteria with SUID Thorws runtime exception - DataSetUp Provided
When Get Image set based on filter criteria with SUID Thorws runtime exception
Then verify Image set based on filter with SUID Thorws runtime exception

@test_53635
Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation
Meta: @automated
Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation
Then verify Image set based on filter  with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation

@test_53636
Scenario: Get Image set based on filter criteria with Equipment
Meta: @automated
Given Get Image set based on filter criteria with Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Equipment
Then verify Image set based on filter  with Equipment

@test_53637
Scenario: Get Image set based on filter criteria with Institution
Meta: @automated
Given Get Image set based on filter criteria with Institution - DataSetUp Provided
When Get Image set based on filter criteria with Institution
Then verify Image set based on filter  with Institution

@test_53638
Scenario: Get Image set based on filter criteria with DataFormat
Meta: @automated
Given Get Image set based on filter criteria with DataFormat - DataSetUp Provided
When Get Image set based on filter criteria with DataFormat
Then verify Image set based on filter  with DataFormat

@test_53639
Scenario: Get Image set based on filter criteria with Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Institution and Equipment
Then verify Image set based on filter  with Institution and Equipment

@test_53640
Scenario: Get Image set based on filter criteria with DataFormat and  Institution
Meta: @automated
Given Get Image set based on filter criteria with DataFormat and Institution - DataSetUp Provided
When Get Image set based on filter criteria with DataFormat and Institution
Then verify Image set based on filter  with DataFormat and Institution

@test_53641
Scenario: Get Image set based on filter criteria with DataFormat, Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with DataFormat, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with DataFormat, Institution and Equipment
Then verify Image set based on filter  with DataFormat, Institution and Equipment


@test_53642
Scenario: Get Image set based on filter criteria with DataFormat and Equipment
Meta: @automated
Given Get Image set based on filter criteria with DataFormat and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with DataFormat and Equipment
Then verify Image set based on filter  with DataFormat and Equipment

@test_53643
Scenario: Get Image set based on filter criteria with Anatomy and DataFormat
Meta: @automated
Given Get Image set based on filter criteria with Anatomy and DataFormat - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy and DataFormat
Then verify Image set based on filter  with Anatomy and DataFormat

@test_53644
Scenario: Get Image set based on filter criteria with Anatomy, DataFormat and Institution
Meta: @automated
Given Get Image set based on filter criteria with Anatomy, DataFormat and Institution - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, DataFormat and Institution
Then verify Image set based on filter  with Anatomy, DataFormat and Institution

@test_53645
Scenario: Get Image set based on filter criteria with Anatomy, DataFormat, Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Anatomy, DataFormat, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, DataFormat, Institution and Equipment
Then verify Image set based on filter  with Anatomy, DataFormat, Institution and Equipment

@test_53646
Scenario: Get Image set based on filter criteria with Anatomy, Institution
Meta: @automated
Given Get Image set based on filter criteria with Anatomy, Institution - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, Institution
Then verify Image set based on filter  with Anatomy, Institution

@test_53647
Scenario: Get Image set based on filter criteria with Anatomy, Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Anatomy, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, Institution and Equipment
Then verify Image set based on filter  with Anatomy, Institution and Equipment

@test_53648
Scenario: Get Image set based on filter criteria with Anatomy, Equipment
Meta: @automated
Given Get Image set based on filter criteria with Anatomy, Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, Equipment
Then verify Image set based on filter  with Anatomy, Equipment

@test_53649
Scenario: Get Image set based on filter criteria with Modality, Anatomy and DataFormat
Meta: @automated
Given Get Image set based on filter criteria with Modality, Anatomy and DataFormat - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy and DataFormat
Then verify Image set based on filter  with Modality, Anatomy and DataFormat

@test_53650
Scenario: Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Institution
Meta: @automated
Given Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Institution - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Institution
Then verify Image set based on filter  with Modality, Anatomy, DataFormat and Institution

@test_53651
Scenario: Get Image set based on filter criteria with Modality, Anatomy, DataFormat, Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, Anatomy, DataFormat, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, DataFormat, Institution and Equipment
Then verify Image set based on filter  with Modality, Anatomy, DataFormat, Institution and Equipment

@test_53652
Scenario: Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Equipment
Then verify Image set based on filter  with Modality, Anatomy, DataFormat and Equipment


@test_53653
Scenario: Get Image set based on filter criteria with Modality, Anatomy, Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, Anatomy, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, Institution and Equipment
Then verify Image set based on filter  with Modality, Anatomy, Institution and Equipment

@test_53654
Scenario: Get Image set based on filter criteria with Modality, Anatomy, Institution
Meta: @automated
Given Get Image set based on filter criteria with Modality, Anatomy, Institution - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, Institution
Then verify Image set based on filter  with Modality, Anatomy, Institution

@test_53655
Scenario: Get Image set based on filter criteria with Modality, Anatomy, Equipment
Given Get Image set based on filter criteria with Modality, Anatomy, Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, Equipment
Then verify Image set based on filter  with Modality, Anatomy, Equipment

@test_53656
Scenario: Get Image set based on filter criteria with Modality, Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Equipment
Then verify Image set based on filter  with Modality, Equipment

@test_53657
Scenario: Get Image set based on filter criteria with Modality, Institution
Meta: @automated
Given Get Image set based on filter criteria with Modality, Institution - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Institution
Then verify Image set based on filter  with Modality, Institution

@test_53658
Scenario: Get Image set based on filter criteria with Modality, DataFormat
Meta: @automated
Given Get Image set based on filter criteria with Modality, DataFormat - DataSetUp Provided
When Get Image set based on filter criteria with Modality, DataFormat
Then verify Image set based on filter  with Modality, DataFormat

@test_53659
Scenario: Get Image set based on filter criteria with Modality, DataFormat,Institution And Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, DataFormat,Institution And Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, DataFormat,Institution And Equipment
Then verify Image set based on filter  with Modality, DataFormat,Institution And Equipment

@test_53660
Scenario: Get Image set based on filter criteria with Modality, DataFormat And Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, DataFormat And Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, DataFormat And Equipment
Then verify Image set based on filter  with Modality, DataFormat And Equipment

@test_53661
Scenario: Get Image set based on filter criteria with Modality, DataFormat And Institution
Meta: @automated
Given Get Image set based on filter criteria with Modality, DataFormat And Institution - DataSetUp Provided
When Get Image set based on filter criteria with Modality, DataFormat And Institution
Then verify Image set based on filter  with Modality, DataFormat And Institution

@test_53662
Scenario: Get Image set based on filter criteria with Modality, Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Institution and Equipment
Then verify Image set based on filter  with Modality, Institution and Equipment


@test_54809
Scenario: Get Image set based on filter criteria with Institution name containing accepted special characters like comma
Meta: @automated
Given Get Image set based on filter criteria with Institution name containing accepted special characters- DataSetUp Provided
When Get Image set based on filter criteria with Institution name containing accepted special characters
Then verify Image set based on filter  with Institution name containing accepted special characters

@test_54810
Scenario: Get Image set based on filter criteria with Equipment name containing accepted special characters like quotes and slashes
Meta: @automated
Given Get Image set based on filter criteria with Equipment name containing accepted special characters like quotes and slashes - DataSetUp Provided
When Get Image set based on filter criteria with Equipment name containing accepted special characters like quotes and slashes
Then verify Image set based on filter  with Equipment name containing accepted special characters like quotes and slashes


@test_54811
Scenario: Update institution by ImageSeries Ids
Meta: @automated
Given Update institution by ImageSeries Ids - Data Setup
When Update institution by ImageSeries Ids is called
Then verify institution is updated

@test_54812
Scenario: Delete imageset by id
Meta: @automated
Given  imageset by id
When Delete imageset by id
Then verify imageset by id has been deleted

@test_54813
Scenario: Delete imageseries by id when not a imageSeriesList
Meta: @automated
Given imageseries by id when not a imageSeriesList
When Delete imageseries by id when not a imageSeriesList
Then verify imageseries by id when not a imageSeriesList

@test_54814
Scenario: Throw Exception while Delete imageseries set data for Ids
Meta: @automated
Given Delete imageseries set data for Ids throws exception - DataSetUp Provided
When Delete imageseries set data for Ids throws exception
Then Verify Delete imageseries set data for Ids throws exception

@test_54823
Scenario: Get Image set based on filter criteria with ORG ID , Modality, Anatomy and DateRange
Meta: @automated
Given Get Image set based on filter criteria with ORG ID , Modality, Anatomy and DateRange - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID , Modality, Anatomy and DateRange
Then verify Image set based on filter  with ORG ID , Modality, Anatomy and DateRange

@test
Scenario: Get count of unique Image sets annotated by Radiologist for given Organization Id
Meta: @automated
Given orgId get count of unique Image sets annotated by each Radiologist in that Organization - DataSetUp Provided
When Given a orgId return count of image sets annotated by each Radiologist
Then Verify count of unique Image sets annotated by Radiologist for given orgId

@test
Scenario: Get count of unique Image sets annotated by Radiologist for given Organization Id should return 204 - No Content
Meta: @automated
Given orgId not present in data base the API to get count of unique Image sets annotated by each Radiologist
When Given a orgId not present in data base API should return 204 - No Content
Then Verify if response status code is 204

@test
Scenario: Get count of unique Image sets annotated by Radiologist for given Organization Id API throws Exception
Meta: @automated
Given orgId to get count of image sets annotated by each radiologist the API throws Exception - DataSetUp Provided
When Given orgId to get count of unique Image sets annotated by Radiologist API should throw Exception
Then Verify if the API is throwing exception with status code 500