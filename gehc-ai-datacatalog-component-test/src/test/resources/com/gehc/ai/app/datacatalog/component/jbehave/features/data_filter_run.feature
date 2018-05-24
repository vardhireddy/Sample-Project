Feature: DataCatalog Component - Additional Image Set Filtering Test

Narrative: As a data scientist, I should be able to select image set data from datacatalog
Retrieve image series by data collection id
Retrieve image series by filter criteria

@functional


@test
Scenario: Retrieve image series by data collection (data set) id
Meta: @automated
Given data collection id
When Get image series by data collection id
Then verify image series by data collection id is capped

@test
Scenario: Retrieve image series by filter criteria
Meta: @automated
Given image series filter criteria map
When image series filter criteria map
Then verify image series filter criteria map is capped with the limit argument
