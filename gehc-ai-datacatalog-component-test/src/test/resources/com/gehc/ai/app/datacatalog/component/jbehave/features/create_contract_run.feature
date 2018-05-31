Feature: DataCatalog Component - Create a contract

Narrative: As a program manager, I should be able to create a contract

@functional

@test
Scenario : User shall be able to create a contract if all required legal meta data are provided
Meta: @automated
Given all required legal meta data are provided
And no internal errors occur
When the API which creates a contract is invoked with an org ID
Then a single contract should be saved to the database
And the response status code should be 201
And the response content type should be JSON

@test
Scenario Outline: User shall not be able to create a contract if not all required legal meta data are provided
Meta: @automated
Given not all required legal meta data are provided
And no internal errors occur
When the API which creates a contract is invoked with an org ID
Then the response status code should be 400
And the response content type should be JSON
And the response's body should contain an error message saying not all required legal meta were provided

@test
Scenario Outline: User shall not be able to create a contract if an org ID is not provided
Meta: @automated
Given all required legal meta data are provided
And no internal errors occur
When the API which creates a contract is invoked without an org ID
Then the response status code should be 400
And the response content type should be JSON
And the response's body should contain an error message saying an org ID must be provided

@test
Scenario Outline: User shall not be able to create a contract if an internal error occurs
Meta: @automated
Given all required legal meta data are provided
And an internal error occurs
When the API which creates a contract is invoked with an org ID
Then the response status code should be 500
And the response content type should be JSON
And the response's body should contain an error message saying an internal error occurred

