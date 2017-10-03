Feature: Imageset DataCatalog

Narrative: As a data scientist, I should be able to Imageset data from datacatalog


Scenario: Retrieve image series by id
Given Retrieve image series by id - DataSetUp Provided
When Get image series by image series id
Then verify image series by image series id

Scenario: Retrieve image series by series instance uid
Given Retrieve image series by series instance uid - DataSetUp Provided
When Get image series by series instance uid
Then verify image series by series instance uid

Scenario: Store image set data
Given Store an image set data - DataSetUp Provided
When Store an image set data
Then verify Store an image set data

Scenario: Get Image set based on filter criteria with SUID
Given Get Image set based on filter criteria with SUID - DataSetUp Provided
When Get Image set based on filter criteria with SUID
Then verify Image set based on filter with SUID

Scenario: Get Image set based on filter criteria with ORG ID and Modality
Given Get Image set based on filter criteria with ORG ID and Modality - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID and Modality
Then verify Image set based on filter  with ORG ID and Modality

Scenario: Get Image set based on filter criteria with ORG ID , Modality and Anatomy
Given Get Image set based on filter criteria with ORG ID , Modality and Anatomy - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID , Modality and Anatomy
Then verify Image set based on filter  with ORG ID , Modality and Anatomy

Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation
Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation
Then verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation

Scenario: Get Image set based on filter criteria with ORG ID
Given Get Image set based on filter criteria with ORG ID - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID
Then verify Image set based on filter  with ORG ID

Scenario: Get Image set based on filter criteria with ORG ID and Anatomy
Given Get Image set based on filter criteria with ORG ID and Anatomy - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID and Anatomy
Then verify Image set based on filter  with ORG ID and Anatomy

Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT
Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT
Then verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation ABSENT

Scenario: Get Image set based on filter criteria with ORG ID and Modality throws Exception
Given Get Image set based on filter criteria with ORG ID and Modality throws Exception - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID and Modality throws Exception
Then verify Image set based on filter  with ORG ID and Modality throws Exception

Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries
Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries
Then verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation ABSENT returns empty ImageSeries

Scenario: Get Image set based on filter criteria with SUID  Thorws runtime exception
Given Get Image set based on filter criteria with SUID Thorws runtime exception - DataSetUp Provided
When Get Image set based on filter criteria with SUID Thorws runtime exception
Then verify Image set based on filter with SUID Thorws runtime exception

Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation
Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation - DataSetUp Provided
When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation
Then verify Image set based on filter  with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation

Scenario: Get Image set based on filter criteria with Equipment
Given Get Image set based on filter criteria with Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Equipment
Then verify Image set based on filter  with Equipment

Scenario: Get Image set based on filter criteria with Institution
Given Get Image set based on filter criteria with Institution - DataSetUp Provided
When Get Image set based on filter criteria with Institution
Then verify Image set based on filter  with Institution

Scenario: Get Image set based on filter criteria with DataFormat
Given Get Image set based on filter criteria with DataFormat - DataSetUp Provided
When Get Image set based on filter criteria with DataFormat
Then verify Image set based on filter  with DataFormat

Scenario: Get Image set based on filter criteria with Institution and Equipment
Given Get Image set based on filter criteria with Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Institution and Equipment
Then verify Image set based on filter  with Institution and Equipment

Scenario: Get Image set based on filter criteria with DataFormat and  Institution
Given Get Image set based on filter criteria with DataFormat and Institution - DataSetUp Provided
When Get Image set based on filter criteria with DataFormat and Institution
Then verify Image set based on filter  with DataFormat and Institution

Scenario: Get Image set based on filter criteria with DataFormat, Institution and Equipment
Given Get Image set based on filter criteria with DataFormat, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with DataFormat, Institution and Equipment
Then verify Image set based on filter  with DataFormat, Institution and Equipment

Scenario: Get Image set based on filter criteria with DataFormat and Equipment
Given Get Image set based on filter criteria with DataFormat and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with DataFormat and Equipment
Then verify Image set based on filter  with DataFormat and Equipment

Scenario: Get Image set based on filter criteria with Anatomy and DataFormat
Given Get Image set based on filter criteria with Anatomy and DataFormat - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy and DataFormat
Then verify Image set based on filter  with Anatomy and DataFormat

Scenario: Get Image set based on filter criteria with Anatomy, DataFormat and Institution
Given Get Image set based on filter criteria with Anatomy, DataFormat and Institution - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, DataFormat and Institution
Then verify Image set based on filter  with Anatomy, DataFormat and Institution

Scenario: Get Image set based on filter criteria with Anatomy, DataFormat, Institution and Equipment
Given Get Image set based on filter criteria with Anatomy, DataFormat, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, DataFormat, Institution and Equipment
Then verify Image set based on filter  with Anatomy, DataFormat, Institution and Equipment

Scenario: Get Image set based on filter criteria with Anatomy, Institution
Given Get Image set based on filter criteria with Anatomy, Institution - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, Institution
Then verify Image set based on filter  with Anatomy, Institution

Scenario: Get Image set based on filter criteria with Anatomy, Institution and Equipment
Given Get Image set based on filter criteria with Anatomy, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, Institution and Equipment
Then verify Image set based on filter  with Anatomy, Institution and Equipment

Scenario: Get Image set based on filter criteria with Anatomy, Equipment
Given Get Image set based on filter criteria with Anatomy, Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Anatomy, Equipment
Then verify Image set based on filter  with Anatomy, Equipment

Scenario: Get Image set based on filter criteria with Modality, Anatomy and DataFormat
Given Get Image set based on filter criteria with Modality, Anatomy and DataFormat - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy and DataFormat
Then verify Image set based on filter  with Modality, Anatomy and DataFormat

Scenario: Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Institution
Given Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Institution - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Institution
Then verify Image set based on filter  with Modality, Anatomy, DataFormat and Institution

Scenario: Get Image set based on filter criteria with Modality, Anatomy, DataFormat, Institution and Equipment
Given Get Image set based on filter criteria with Modality, Anatomy, DataFormat, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, DataFormat, Institution and Equipment
Then verify Image set based on filter  with Modality, Anatomy, DataFormat, Institution and Equipment

Scenario: Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Equipment
Given Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Equipment
Then verify Image set based on filter  with Modality, Anatomy, DataFormat and Equipment


Scenario: Get Image set based on filter criteria with Modality, Anatomy, Institution and Equipment
Given Get Image set based on filter criteria with Modality, Anatomy, Institution and Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, Institution and Equipment
Then verify Image set based on filter  with Modality, Anatomy, Institution and Equipment

Scenario: Get Image set based on filter criteria with Modality, Anatomy, Institution
Given Get Image set based on filter criteria with Modality, Anatomy, Institution - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, Institution
Then verify Image set based on filter  with Modality, Anatomy, Institution

Scenario: Get Image set based on filter criteria with Modality, Anatomy, Equipment
Given Get Image set based on filter criteria with Modality, Anatomy, Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Anatomy, Equipment
Then verify Image set based on filter  with Modality, Anatomy, Equipment

Scenario: Get Image set based on filter criteria with Modality, Equipment
Given Get Image set based on filter criteria with Modality, Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Equipment
Then verify Image set based on filter  with Modality, Equipment

Scenario: Get Image set based on filter criteria with Modality, Institution
Given Get Image set based on filter criteria with Modality, Institution - DataSetUp Provided
When Get Image set based on filter criteria with Modality, Institution
Then verify Image set based on filter  with Modality, Institution

Scenario: Get Image set based on filter criteria with Modality, DataFormat
Given Get Image set based on filter criteria with Modality, DataFormat - DataSetUp Provided
When Get Image set based on filter criteria with Modality, DataFormat
Then verify Image set based on filter  with Modality, DataFormat

Scenario: Get Image set based on filter criteria with Modality, DataFormat,Institution And Equipment
Given Get Image set based on filter criteria with Modality, DataFormat,Institution And Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, DataFormat,Institution And Equipment
Then verify Image set based on filter  with Modality, DataFormat,Institution And Equipment

Scenario: Get Image set based on filter criteria with Modality, DataFormat And Equipment
Given Get Image set based on filter criteria with Modality, DataFormat And Equipment - DataSetUp Provided
When Get Image set based on filter criteria with Modality, DataFormat And Equipment
Then verify Image set based on filter  with Modality, DataFormat And Equipment

Scenario: Get Image set based on filter criteria with Modality, DataFormat And Equipment
Given Get Image set based on filter criteria with Modality, DataFormat And Institution - DataSetUp Provided
When Get Image set based on filter criteria with Modality, DataFormat And Institution
Then verify Image set based on filter  with Modality, DataFormat And Institution