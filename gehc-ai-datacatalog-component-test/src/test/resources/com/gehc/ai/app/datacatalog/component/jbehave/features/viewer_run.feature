Feature: DataCatalog Component - Viewer

Narrative: As a data scientist, I should be able to get data from datacatalog for Segmentation/Charectarization result images

@functional
@crs_10736

@test_53670
Scenario: Get Segmentation Results image
Meta: @automated
Given Get Segmentation Results image - DataSetUp Provided
When Get Segmentation Results image
Then verify Segmentation Results image
