Feature: DataCatalog Component - Cos Notification

Narrative: As a data scientist, I should be able to save Cos Notification event in datacatalog

@functional
@crs_10732

@test_53602
Scenario: Store Cos Notification in DataCatalog
Meta: @automated
Given Store Cos Notification in DataCatalog - DataSetUp Provided
When Store Cos Notification in DataCatalog
Then Verify Store Cos Notification in DataCatalog