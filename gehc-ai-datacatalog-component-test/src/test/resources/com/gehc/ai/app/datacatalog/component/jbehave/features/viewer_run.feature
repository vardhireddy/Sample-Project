Feature: Viewer DataCatalog

  Narrative: As a data scientist, I should be able to get data from datacatalog for Segmentation/Charectarization result images

  Scenario: Get Segmentation Results image
  Given Get Segmentation Results image - DataSetUp Provided
  When Get Segmentation Results image
  Then verify Segmentation Results image
