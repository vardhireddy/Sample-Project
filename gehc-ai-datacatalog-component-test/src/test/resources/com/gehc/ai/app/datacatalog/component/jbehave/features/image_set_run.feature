#Feature: Imageset DataCatalog
#
#  Narrative: As a data scientist, I should be able to Imageset data from datacatalog
#
#  Scenario: Retrieve image series by id
#  Given Retrieve image series by id - DataSetUp Provided
#  When Get image series by image series id
#  Then verify image series by image series id
#
#  Scenario: Retrieve image series by series instance uid
#  Given Retrieve image series by series instance uid - DataSetUp Provided
#  When Get image series by series instance uid
#  Then verify image series by series instance uid
#
#  Scenario: Store image set data
#  Given Store an image set data - DataSetUp Provided
#  When Store an image set data
#  Then verify Store an image set data
#
#  Scenario: Get Image set based on filter criteria with SUID
#  Given Get Image set based on filter criteria with SUID - DataSetUp Provided
#  When Get Image set based on filter criteria with SUID
#  Then verify Image set based on filter with SUID
#
#  Scenario: Get Image set based on filter criteria with ORG ID and Modality
#  Given Get Image set based on filter criteria with ORG ID and Modality - DataSetUp Provided
#  When Get Image set based on filter criteria with ORG ID and Modality
#  Then verify Image set based on filter  with ORG ID and Modality
#
#  Scenario: Get Image set based on filter criteria with ORG ID , Modality and Anatomy
#  Given Get Image set based on filter criteria with ORG ID , Modality and Anatomy - DataSetUp Provided
#  When Get Image set based on filter criteria with ORG ID , Modality and Anatomy
#  Then verify Image set based on filter  with ORG ID , Modality and Anatomy
#
#  Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation
#  Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation - DataSetUp Provided
#  When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation
#  Then verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation
#
#  Scenario: Get Image set based on filter criteria with ORG ID
#  Given Get Image set based on filter criteria with ORG ID - DataSetUp Provided
#  When Get Image set based on filter criteria with ORG ID
#  Then verify Image set based on filter  with ORG ID
#
#  Scenario: Get Image set based on filter criteria with ORG ID and Anatomy
#  Given Get Image set based on filter criteria with ORG ID and Anatomy - DataSetUp Provided
#  When Get Image set based on filter criteria with ORG ID and Anatomy
#  Then verify Image set based on filter  with ORG ID and Anatomy
#
#  Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT
#  Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT - DataSetUp Provided
#  When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT
#  Then verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation ABSENT
#
#  Scenario: Get Image set based on filter criteria with ORG ID and Modality throws Exception
#  Given Get Image set based on filter criteria with ORG ID and Modality throws Exception - DataSetUp Provided
#  When Get Image set based on filter criteria with ORG ID and Modality throws Exception
#  Then verify Image set based on filter  with ORG ID and Modality throws Exception
#
#  Scenario: Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries
#  Given Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries - DataSetUp Provided
#  When Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries
#  Then verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation ABSENT returns empty ImageSeries
#
#  Scenario: Get Image set based on filter criteria with SUID  Thorws runtime exception
#  Given Get Image set based on filter criteria with SUID Thorws runtime exception - DataSetUp Provided
#  When Get Image set based on filter criteria with SUID Thorws runtime exception
#  Then verify Image set based on filter with SUID Thorws runtime exception
#
#  Scenario: Get Image set based on filter criteria with No Annotation
#  Given Get Image set based on filter criteria with No Annotation - DataSetUp Provided
#  When Get Image set based on filter criteria with No Annotation
#  Then verify Image set based on filter  with No Annotation