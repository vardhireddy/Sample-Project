Feature: DataCatalog Component -  Study

Narrative: As a data scientist, I should be able to study data from datacatalog
Get Imageset by study
Save study
Save study should return existing study if it already exists
Get Multiple Studies
Save Study with Null StudyId
Save new study


@functional
@crs

@test
Scenario: Get Imageset by study
Meta: @automated
Given Get Imageset by study - DataSetUp Provided
When Get Imageset by study
Then verify Imageset by study

@test
Scenario: Get all Studies
Meta: @automated
Given Get all Studies - DataSetUp Provided
When Get all Studies
Then verify Get all Studies

@test
Scenario: Save study
Meta: @automated
Given Save study - DataSetUp Provided
When Save study
Then verify Save study

@test
Scenario: Save study should return existing study if it already exists
Meta: @automated
Given Save existing study - DataSetUp Provided
When Save existing study
Then verify Save study should return existing study if it already exists

@test
Scenario: Get Multiple Studies
Meta: @automated
Given Get Multiple Studies - DataSetUp Provided
When Get Multiple Studies
Then verify Get Multiple Studies

@test
Scenario: Save Study with Null StudyId
Meta: @automated
Given Save Study with Null Study id - DataSetUp Provided
When save Study with Null Study id
Then verify Saving Study with Null Study id

@test
Scenario: Save new study
Meta: @automated
Given Save new study - DataSetUp Provided
When Save new study
Then verify Save new study
