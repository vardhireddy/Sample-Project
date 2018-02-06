Feature: DataCatalog Component -  Study

Narrative: As a data scientist, I should be able to study data from datacatalog
Get Imageset by study
Save study
Save study should return existing study if it already exists
Get Multiple Studies
Save Study with Null StudyId
Save new study


@functional
@crs_10735

@test_53663
Scenario: Get Imageset by study
Meta: @automated
Given Get Imageset by study - DataSetUp Provided
When Get Imageset by study
Then verify Imageset by study

@test_53664
Scenario: Get all Studies
Meta: @automated
Given Get all Studies - DataSetUp Provided
When Get all Studies
Then verify Get all Studies

@test_53665
Scenario: Save study
Meta: @automated
Given Save study - DataSetUp Provided
When Save study
Then verify Save study

@test_53666
Scenario: Save study should return existing study if it already exists
Meta: @automated
Given Save existing study - DataSetUp Provided
When Save existing study
Then verify Save study should return existing study if it already exists

@test_53667
Scenario: Get Multiple Studies
Meta: @automated
Given Get Multiple Studies - DataSetUp Provided
When Get Multiple Studies
Then verify Get Multiple Studies

@test_53668
Scenario: Save Study with Null StudyId
Meta: @automated
Given Save Study with Null Study id - DataSetUp Provided
When save Study with Null Study id
Then verify Saving Study with Null Study id

@test_53669
Scenario: Save new study
Meta: @automated
Given Save new study - DataSetUp Provided
When Save new study
Then verify Save new study

@test
Scenario: Save Study with Null PatientDbId
Meta: @automated
Given Save Study with Null PatientDbId - DataSetUp Provided
When save Study with Null PatientDbId
Then verify Saving Study with Null PatientDbId