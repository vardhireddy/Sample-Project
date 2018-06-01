Feature: DataCatalog Component - retreive one or more data collections

Narrative: As a data scientist or radiologist, I should be able to retrieve one or more data collections.

@functional

@test
Scenario Outline: User shall be able to retrieve all data collections belonging to the org specified in the request headers
Meta: @automated
Given an org has one or more data collections
And no internal errors occur when retrieving the data collections for the target org ID
When the API which retrieves data collections is invoked with the target org ID
Then a single request to retrieve all data collections for the target org ID should be made
And the status code resulting from retrieving the data collections should be 200
And the content type resulting from  retrieving the data collections should be JSON

@test
Scenario Outline: User shall be able to retrieve all data collections of a specified type belonging to the org specified in the request headers
Meta: @automated
Given an org has one or more data collections
And no internal errors occur when retrieving the data collections of a target collection type for the target org ID
When the API which retrieves a data collection is invoked with the target org ID and collection type
Then a single request to retrieve all data collections of the target collection type for the target org ID should be made
And the status code resulting from retrieving the data collections should be 200
And the content type resulting from retrieving the data collections should be JSON

@test
Scenario Outline: User shall not be able to retrieve one or more data collections if no org ID is provided
Meta: @automated
Given an org has one or more data collections
When the API which retrieves a data collection is invoked without an org ID
Then the status code resulting from retrieving the data collections should be 400
And the content type resulting from retrieving the data collections should be JSON
And the body resulting from retrieving the data collections should contain an error message saying an org ID needs to be provided

@test
Scenario Outline: User shall not be able to retrieve one or more data collections if an invalid collection type is provided
Meta: @automated
Given an org has one or more data collections
And no internal errors occur when retrieving the data collections of a target collection type for the target org ID
When the API which retrieves a data collection is invoked with the target org ID and invalid collection type
Then the status code resulting from retrieving the data collections should be 400
And the content type resulting from retrieving the data collections should be JSON
And the body resulting from retrieving the data collections should contain an error message saying an invalid collection type was provided