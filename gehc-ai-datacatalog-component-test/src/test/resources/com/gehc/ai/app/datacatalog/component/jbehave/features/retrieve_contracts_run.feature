Feature: DataCatalog Component - retrieve one or more contracts

Narrative: As a data scientist or radiologist, I should be able to retrieve one or more contracts

@functional

@test_getAllContracts1
Scenario: User shall be able to retrieve all contracts data if there are one or more contracts in the database
Meta: @automated
Given there are one or more contracts in the database
And no internal errors occur when retrieving the contracts
When the API which retrieves the contracts is invoked with an org ID
Then a single request to the database should be made to retrieve all contracts for the target org ID
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

@test_getAllContracts4
Scenario: User shall not be able to retrieve any contracts data if an org ID is not provided
Meta: @automated
Given there are one or more contracts in the database
And no internal errors occur when retrieving the contracts
When the API which retrieves the contracts is invoked without an org ID
Then the get contracts response status code should be 400
And the get contracts response's content type should be JSON
And the response's body should contain a message saying an org ID must be provided


@test
Scenario: For a data collection/set ID not supported by LF get the contracts associated with the image sets of that data collection
Meta: @automated
Given a data collection/set ID not supported by LF
When the api that gets contracts associated with the image sets of that data collection is hit
Then the api must return error message saying no contracts exist for the given dataSet ID

@test
Scenario: For a data collection/set ID supported by LF get the contracts associated with the image sets of that data collection
Meta: @automated
Given a data collection/set ID supported by LF
When the api that gets contracts associated with the image sets of that data collection
Then the api must return a map of active and inactive contracts associated with the data collection
