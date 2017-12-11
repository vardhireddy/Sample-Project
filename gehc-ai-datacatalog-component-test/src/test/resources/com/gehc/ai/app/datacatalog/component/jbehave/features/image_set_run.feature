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


@functional
@crs


@test
Scenario: Retrieve image series by id
Meta: @automated
Given Retrieve image series by id - DataSetUp Provided
When Get image series by image series id
Then verify image series by image series id

@test
Scenario: Retrieve image series by series instance uid
Meta: @automated
Given Retrieve image series by series instance uid - DataSetUp Provided
When Get image series by series instance uid
Then verify image series by series instance uid

@test
Scenario: Store image set data
Meta: @automated
Given Store an image set data - DataSetUp Provided
When Store an image set data
Then verify Store an image set data

@test
Scenario: Get Image set based on filter criteria with SUID
Meta: @automated
Given Get Image set based on filter criteria with SUID - DataSetUp Provided
When Get Image set based on filter criteria with SUID
Then verify Image set based on filter with SUID

@test
Scenario: Get Image set based on filter criteria with ORG ID and Modality
Meta: @automated
Given Get Image set based on filter criteria with ORG ID and Modality - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID and Modality
Then verify Image set based on filter  with ORG ID and Modality

@test
Scenario: Get Image set based on filter criteria with ORG ID , Modality and Anatomy
Meta: @automated
Given Get Image set based on filter criteria with ORG ID , Modality and Anatomy - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID , Modality and Anatomy
Then verify Image set based on filter  with ORG ID , Modality and Anatomy

@test
Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation
Meta: @automated
Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation
Then verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation

@test
Scenario: Get Image set based on filter criteria with ORG ID
Meta: @automated
Given Get Image set based on filter criteria with ORG ID - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID
Then verify Image set based on filter  with ORG ID

@test
Scenario: Get Image set based on filter criteria with ORG ID and Anatomy
Meta: @automated
Given Get Image set based on filter criteria with ORG ID and Anatomy - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID and Anatomy
Then verify Image set based on filter  with ORG ID and Anatomy

@test
Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT
Meta: @automated
Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT
Then verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation ABSENT

@test
Scenario: Get Image set based on filter criteria with ORG ID and Modality throws Exception
Meta: @automated
Given Get Image set based on filter criteria with ORG ID and Modality throws Exception - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID and Modality throws Exception
Then verify Image set based on filter  with ORG ID and Modality throws Exception

@test
Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries
Meta: @automated
Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries
Then verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation ABSENT returns empty ImageSeries

@test
Scenario: Get Image set based on filter criteria with SUID  Thorws runtime exception
Meta: @automated
Given Get Image set based on filter criteria with SUID Thorws runtime exception - DataSetUp Provided
When Get Image set based on filter criteria with SUID Thorws runtime exception
Then verify Image set based on filter with SUID Thorws runtime exception

@test
Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation
Meta: @automated
Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation
Then verify Image set based on filter  with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation

@test
Scenario: Get Image set based on filter criteria with Equipment
Meta: @automated
Given Get Image set based on filter criteria with Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Equipment
Then verify Image set based on filter  with Equipment

@test
Scenario: Get Image set based on filter criteria with Institution
Meta: @automated
Given Get Image set based on filter criteria with Institution - DataSetUp Provided
When Get Image set based on filter criteria with Institution
Then verify Image set based on filter  with Institution

@test
Scenario: Get Image set based on filter criteria with DataFormat
Meta: @automated
Given Get Image set based on filter criteria with DataFormat - DataSetUp Provided
When Get Image set based on filter criteria with DataFormat
Then verify Image set based on filter  with DataFormat

@test
Scenario: Get Image set based on filter criteria with Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Institution and Equipment
Then verify Image set based on filter  with Institution and Equipment

@test
Scenario: Get Image set based on filter criteria with DataFormat and  Institution
Meta: @automated
Given Get Image set based on filter criteria with DataFormat and Institution - DataSetUp Provided
When Get Image set based on filter criteria with DataFormat and Institution
Then verify Image set based on filter  with DataFormat and Institution

@test
Scenario: Get Image set based on filter criteria with DataFormat, Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with DataFormat, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with DataFormat, Institution and Equipment
Then verify Image set based on filter  with DataFormat, Institution and Equipment

Scenario: Get Image set based on filter criteria with DataFormat and Equipment
Meta: @automated
Given Get Image set based on filter criteria with DataFormat and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with DataFormat and Equipment
Then verify Image set based on filter  with DataFormat and Equipment

@test
Scenario: Get Image set based on filter criteria with Anatomy and DataFormat
Meta: @automated
Given Get Image set based on filter criteria with Anatomy and DataFormat - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy and DataFormat
Then verify Image set based on filter  with Anatomy and DataFormat

@test
Scenario: Get Image set based on filter criteria with Anatomy, DataFormat and Institution
Meta: @automated
Given Get Image set based on filter criteria with Anatomy, DataFormat and Institution - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, DataFormat and Institution
Then verify Image set based on filter  with Anatomy, DataFormat and Institution

@test
Scenario: Get Image set based on filter criteria with Anatomy, DataFormat, Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Anatomy, DataFormat, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, DataFormat, Institution and Equipment
Then verify Image set based on filter  with Anatomy, DataFormat, Institution and Equipment

@test
Scenario: Get Image set based on filter criteria with Anatomy, Institution
Meta: @automated
Given Get Image set based on filter criteria with Anatomy, Institution - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, Institution
Then verify Image set based on filter  with Anatomy, Institution

@test
Scenario: Get Image set based on filter criteria with Anatomy, Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Anatomy, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, Institution and Equipment
Then verify Image set based on filter  with Anatomy, Institution and Equipment

@test
Scenario: Get Image set based on filter criteria with Anatomy, Equipment
Meta: @automated
Given Get Image set based on filter criteria with Anatomy, Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, Equipment
Then verify Image set based on filter  with Anatomy, Equipment

@test
Scenario: Get Image set based on filter criteria with Modality, Anatomy and DataFormat
Meta: @automated
Given Get Image set based on filter criteria with Modality, Anatomy and DataFormat - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy and DataFormat
Then verify Image set based on filter  with Modality, Anatomy and DataFormat

@test
Scenario: Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Institution
Meta: @automated
Given Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Institution - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Institution
Then verify Image set based on filter  with Modality, Anatomy, DataFormat and Institution

@test
Scenario: Get Image set based on filter criteria with Modality, Anatomy, DataFormat, Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, Anatomy, DataFormat, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, DataFormat, Institution and Equipment
Then verify Image set based on filter  with Modality, Anatomy, DataFormat, Institution and Equipment

@test
Scenario: Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Equipment
Then verify Image set based on filter  with Modality, Anatomy, DataFormat and Equipment


@test
Scenario: Get Image set based on filter criteria with Modality, Anatomy, Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, Anatomy, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, Institution and Equipment
Then verify Image set based on filter  with Modality, Anatomy, Institution and Equipment

@test
Scenario: Get Image set based on filter criteria with Modality, Anatomy, Institution
Meta: @automated
Given Get Image set based on filter criteria with Modality, Anatomy, Institution - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, Institution
Then verify Image set based on filter  with Modality, Anatomy, Institution

@test
Scenario: Get Image set based on filter criteria with Modality, Anatomy, Equipment
Given Get Image set based on filter criteria with Modality, Anatomy, Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, Equipment
Then verify Image set based on filter  with Modality, Anatomy, Equipment

@test
Scenario: Get Image set based on filter criteria with Modality, Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Equipment
Then verify Image set based on filter  with Modality, Equipment

@test
Scenario: Get Image set based on filter criteria with Modality, Institution
Meta: @automated
Given Get Image set based on filter criteria with Modality, Institution - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Institution
Then verify Image set based on filter  with Modality, Institution

@test
Scenario: Get Image set based on filter criteria with Modality, DataFormat
Meta: @automated
Given Get Image set based on filter criteria with Modality, DataFormat - DataSetUp Provided
When Get Image set based on filter criteria with Modality, DataFormat
Then verify Image set based on filter  with Modality, DataFormat

@test
Scenario: Get Image set based on filter criteria with Modality, DataFormat,Institution And Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, DataFormat,Institution And Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, DataFormat,Institution And Equipment
Then verify Image set based on filter  with Modality, DataFormat,Institution And Equipment

@test
Scenario: Get Image set based on filter criteria with Modality, DataFormat And Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, DataFormat And Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, DataFormat And Equipment
Then verify Image set based on filter  with Modality, DataFormat And Equipment

@test
Scenario: Get Image set based on filter criteria with Modality, DataFormat And Institution
Meta: @automated
Given Get Image set based on filter criteria with Modality, DataFormat And Institution - DataSetUp Provided
When Get Image set based on filter criteria with Modality, DataFormat And Institution
Then verify Image set based on filter  with Modality, DataFormat And Institution

@test
Scenario: Get Image set based on filter criteria with Modality, Institution and Equipment
Meta: @automated
Given Get Image set based on filter criteria with Modality, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Institution and Equipment
Then verify Image set based on filter  with Modality, Institution and Equipment
