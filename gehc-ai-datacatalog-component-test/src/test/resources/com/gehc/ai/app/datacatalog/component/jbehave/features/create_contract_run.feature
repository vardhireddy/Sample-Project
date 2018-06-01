Feature: DataCatalog Component - Create a contract

Narrative: As a program manager, I should be able to create a contract

@functional

@test
Scenario: User shall be able to create a contract if all required legal meta data are provided
Meta: @automated
Given all required legal meta data are provided
And no internal errors occurs
When the API which creates a contract is invoked with an org ID
Then a single contract should be saved to the database
And the response status code should be 201
And the response content type should be JSON

@test
Scenario: User shall not be able to create a contract if not all required legal meta data are provided
Meta: @automated
Given not all required legal meta data are provided
And no internal errors occurs
When the API which creates a contract is invoked with an org ID
Then the response status code should be 400
And the response content type should be JSON
And the response's body should contain an error message saying not all required legal meta were provided

@test
Scenario: User shall not be able to create a contract if an org ID is not provided
Meta: @automated
Given all required legal meta data are provided
And no internal errors occurs
When the API which creates a contract is invoked without an org ID
Then the response status code should be 400
And the response content type should be JSON
And the response's body should contain an error message saying an org ID must be provided

@test
Scenario: User shall not be able to create a contract if an internal error occurs
Meta: @automated
Given all required legal meta data are provided
And an internal error occurs
When the API which creates a contract is invoked with an org ID
Then the response status code should be 500
And the response content type should be JSON
And the response's body should contain an error message saying an internal error occurred

#  get contract API test cases
@test
Scenario: User shall be able to retrieve contract data for valid and active contract Id
Meta: @automated
Given a valid and active contract ID to retrieve
When the API which retrieves a contract is invoked with a valid and active contract ID
Then  the response status code should be 200
Then verify Retrieve contract data

@test
Scenario: User shall not be able to retrieve contract data for valid and inactive contract Id
Meta: @automated
Given a valid and inactive contract ID to retrieve
When the API which retrieves a contract is invoked with a valid and inactive contract ID
Then the response status code should be 200
Then the response's body should contain a message saying the contract associated with given ID is inactive

@test
Scenario: User shall not be able to retrieve contract data for invalid contract Id
Meta: @automated
Given an invalid contract ID to retrieve
When the API which retrieves a contract is invoked with an invalid contract ID
Then the response status code should be 400
Then the response's body should contain a message saying no contract exists with the given ID