Feature: DataCatalog Component - retrieve one or more contracts

Narrative: As a data scientist or radiologist, I should be able to retrieve one or more contracts

@functional

@test
Scenario: For a data collection/set ID not supported by LF get the contracts associated with the image sets of that data collection
Meta: @automated
  Given a data collection/set ID not supported by LF
  When the api that gets contracts associated with the image sets of that data collection is hit
  Then the api must return error message saying no contracts exist for the given dataSet ID
#@test
#Scenario: For a data collection/set ID supported by LF get the contracts associated with the image sets of that data collection
#Meta: @automated
#Given a data collection/set ID supported by LF
#When the api that gets contracts associated with the image sets of that data collection
#Then the api must return a map of active and inactive contracts associated with the data collection