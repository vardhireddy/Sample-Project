Feature: DataCatalog Component - Create a contract

Narrative: As a program manager, I should be able to create a contract

@functional

@test
Scenario: User shall be able to create a contract if all required legal meta data are provided
Meta: @automated
Given all required legal meta data are provided
And no internal errors occur when saving the contract
When the API which creates a contract is invoked with an org ID
Then a single contract should be saved to the database
And the create contract response's status code should be 201
And the create contract response's content type should be JSON

@test
Scenario: User shall not be able to create a contract if an org ID is not provided
Meta: @automated
Given all required legal meta data are provided
And no internal errors occur when saving the contract
When the API which creates a contract is invoked without an org ID
Then the create contract response's status code should be 400
And the create contract response's content type should be JSON
And the response's body should contain an error message saying an org ID must be provided

@test
Scenario: User shall not be able to create a contract if an internal error occurs
Meta: @automated
Given all required legal meta data are provided
And an internal error occur when saving the contract
When the API which creates a contract is invoked with an org ID
Then the create contract response's status code should be 500
And the create contract response's content type should be JSON
And the response's body should contain an error message saying could not save contract due to an internal error

@test
Scenario: User shall not be able to create a contract if agreement name is not provided
Meta: @automated
Given required legal meta data - agreement name is not provided
And no internal errors occur when saving the contract
When the API which creates a contract is invoked with an org ID
Then the create contract response's status code should be 400
And the create contract response's content type should be JSON
And the response's body should contain an error message saying an agreement name must be provided

@test
Scenario: User shall not be able to create a contract if primary contact email is not provided
Meta: @automated
Given required legal meta data - primary contact email is not provided
And no internal errors occur when saving the contract
When the API which creates a contract is invoked with an org ID
Then the create contract response's status code should be 400
And the create contract response's content type should be JSON
And the response's body should contain an error message saying a primary contact email must be provided

@test
Scenario: User shall not be able to create a contract if de-identified status is not provided
Meta: @automated
Given required legal meta data - de-identified status is not provided
And no internal errors occur when saving the contract
When the API which creates a contract is invoked with an org ID
Then the create contract response's status code should be 400
And the create contract response's content type should be JSON
And the response's body should contain an error message saying the de-identified status must be provided

@test
Scenario: User shall not be able to create a contract if the data usage period is not provided
Meta: @automated
Given required legal meta data - data usage period is not provided
And no internal errors occur when saving the contract
When the API which creates a contract is invoked with an org ID
Then the create contract response's status code should be 400
And the create contract response's content type should be JSON
And the response's body should contain an error message saying the data usage period must be provided

@test
Scenario: User shall not be able to create a contract if agreement begin date is not provided
Meta: @automated
Given required legal meta data - agreement begin date is not provided
And no internal errors occur when saving the contract
When the API which creates a contract is invoked with an org ID
Then the create contract response's status code should be 400
And the create contract response's content type should be JSON
And the response's body should contain an error message saying an agreement begin date must be provided

@test
Scenario: User shall not be able to create a contract if data use cases are not provided
Meta: @automated
Given required legal meta data - data use cases are not provided
And no internal errors occur when saving the contract
When the API which creates a contract is invoked with an org ID
Then the create contract response's status code should be 400
And the create contract response's content type should be JSON
And the response's body should contain an error message saying the data use cases must be provided

@test
Scenario: User shall not be able to create a contract if data origin country and state are not provided
Meta: @automated
Given required legal meta data - data origin country and state are not provided
And no internal errors occur when saving the contract
When the API which creates a contract is invoked with an org ID
Then the create contract response's status code should be 400
And the create contract response's content type should be JSON
And the response's body should contain an error message saying the data origin country and state must be provided

@test
Scenario: User shall not be able to create a contract if data allowed location is not provided
Meta: @automated
Given required legal meta data - data allowed location is not provided
And no internal errors occur when saving the contract
When the API which creates a contract is invoked with an org ID
Then the create contract response's status code should be 400
And the create contract response's content type should be JSON
And the response's body should contain an error message saying the the data allowed location must be provided

@test
Scenario: User shall be able to retrieve contract data for valid and active contract Id
Meta: @automated
Given a valid and active contract ID to retrieve
When the API which retrieves a contract is invoked with a valid and active contract ID
Then the validate contract response's status code should be 200
Then verify Retrieve contract data