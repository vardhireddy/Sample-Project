Feature: DataCatalog Component - retrieve one or more contracts

Narrative: As a data scientist or radiologist, I should be able to retrieve one or more contracts

@functional

@test_getAllContracts1
Scenario: User shall be able to retrieve all contracts data if there are one or more contracts in the database
Meta: @automated
Given there are one or more contracts in the database
And no internal errors occur when retrieving the contracts
When the API which retrieves the contracts is invoked with an org ID
Then all the contracts should be retrieved from the database
Then the get contracts response status code should be 200
And the get contracts response's content type should be JSON

@test_getAllContracts2
Scenario: User shall not be able to retrieve any contracts data if there are no contracts in the database
Meta: @automated
Given there are no contracts in the database
And no internal errors occur when retrieving the contracts
When the API which retrieves the contracts is invoked with an org ID
Then the get contracts response status code should be 200
And the get contracts response's content type should be JSON

@test_getAllContracts3
Scenario: User shall not be able to retrieve any contracts data if an internal error occurs
Meta: @automated
Given there are one or more contracts in the database
And an internal error occurs when retrieving the contracts
When the API which retrieves the contracts is invoked with an org ID
Then the get contracts response status code should be 500
And the get contracts response's content type should be JSON
And the response's body should contain a message saying could not get the contracts details due to an internal error
