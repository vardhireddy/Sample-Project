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
Then the get contract response status code should be 200
Then verify Retrieve contract data

@test
Scenario: User shall not be able to retrieve contract data for valid and inactive contract Id
Meta: @automated
Given a valid and inactive contract ID to retrieve
When the API which retrieves a contract is invoked with a valid and inactive contract ID
Then the get contract response status code should be 200
Then the response's body should contain a message saying the contract associated with given ID is inactive

@test
Scenario: User shall not be able to retrieve contract data for invalid contract Id
Meta: @automated
Given an invalid contract ID to retrieve
When the API which retrieves a contract is invoked with an invalid contract ID
Then the get contract response status code should be 400
Then the response's body should contain a message saying no contract exists with the given ID



@test_1
Scenario: User shall be able to update contract data for valid and active contract Id, valid request
Meta: @automated
Given a valid and active contract ID, valid update request
When the API which updates a contract is invoked with a valid and active contract ID, valid update request
Then the update contract response status code should be 200
Then response and database contract data should reflect the updated details

@test_2
Scenario: User shall not be able to update contract data for valid and active contract Id, invalid/empty request
Meta: @automated
Given a valid and active contract ID, invalid/empty update request
When the API which updates a contract is invoked with a valid and active contract ID, invalid/empty update request
Then the update contract response status code should be 400
Then the response's body should contain a message saying the update request cannot be empty. Either status or uri must be provided

@test_3
Scenario: User shall not be able to update contract data for valid and inactive contract Id, valid request
Meta: @automated
Given a valid and inactive contract ID to retrieve, valid request
When the API which updates a contract is invoked with a valid and inactive contract ID, valid request
Then the update contract response status code should be 400
Then the response's body should contain a message saying the contract ID does not exist or is inactive

@test_4
Scenario: User shall not be able to update contract data for valid and inactive contract Id, invalid/empty update request
Meta: @automated
Given a valid and inactive contract ID to retrieve, invalid request (update request is validated first)
When the API which updates a contract is invoked with a valid and inactive contract ID, invalid request
Then the update contract response status code should be 400
Then the response's body should contain a message saying the update request cannot be empty. Either status or uri must be provided

@test_5
Scenario: User shall not be able to update contract data for invalid contract Id, valid update request
Meta: @automated
Given an invalid contract ID to update, valid update request
When the API which updates a contract is invoked with an invalid contract ID, valid update request
Then the update contract response status code should be 400
Then the response's body should contain a message saying the contract ID does not exist or is inactive

@test_6
Scenario: User shall not be able to update contract data for invalid contract Id, invalid/empty update request
Meta: @automated
Given an invalid contract ID to update, invalid/empty update request
When the API which updates a contract is invoked with an invalid contract ID, invalid/empty update request
Then the update contract response status code should be 400
Then the response's body should contain a message saying the update request cannot be empty. Either status or uri must be provided

@test_7
Scenario: User shall be able to update contract data for valid and active contract Id, valid status value
Meta: @automated
Given a valid and active contract ID, valid status value only
When the API which updates a contract is invoked with a valid and active contract ID, valid status value
Then the update contract response status code should be 200
Then response and database contract data should reflect the updated details with change only in status

@test_8
Scenario: User shall be able to update contract data for valid and active contract Id, valid uri value
Meta: @automated
Given a valid and active contract ID, valid uri value only
When the API which updates a contract is invoked with a valid and active contract ID, valid uri value
Then the update contract response status code should be 200
Then the contract's uri should be updated

@test_9
Scenario: User shall not be able to update contract data for valid contract Id, valid data due to exception in retrieving contract
Meta: @automated
Given a valid contract ID, valid data but exception in retrieving contract
When the API which updates a contract is invoked with a valid ID, valid data but internal exception rises in retrieving
Then the update contract response status code should be 500
Then response's body should contain a message exception retrieving the contract

@test_10
Scenario: User shall not be able to update contract data for valid and active contract Id, valid data due to exception in saving updated contract
Meta: @automated
Given a valid contract ID, valid data but exception in saving updated contract
When the API which updates a contract is invoked with a valid ID, valid data but internal exception rises in saving the contract
Then the update contract response status code should be 500
Then response's body should contain a message exception saving the updated contract