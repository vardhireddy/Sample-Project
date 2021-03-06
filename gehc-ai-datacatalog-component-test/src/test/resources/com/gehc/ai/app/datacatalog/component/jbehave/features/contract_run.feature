Feature: DataCatalog Component - Contract

Narrative: As a data scientist, I should be able to
Store contract data
Retrieve contract data
Validate the existence of a contract Id and Org Id to allow uploading of data in COS - valid data
Validate the existence of a contract Id and Org Id to allow uploading of data in COS - invalid data
Delete a contract in active state with given contract id
Delete a contract in in-active state with given contract id
Delete a contract when contract id does not exist in repository

 @functional
@crs

@test
Scenario: Validate the existence of a contract Id and Org Id to allow uploading of data in COS - valid data
Meta: @automated
Given contract Id and Org Id
When the given parameters are existing in the repository
Then verify the api response status code is 200
Then verify the api response body contains "Contract exists"

@test
Scenario: Validate the existence of a contract Id and Org Id to allow uploading of data in COS - inactive contract
Meta: @automated
Given an inactive contract Id and an Org Id
When the API to validate contract is invoked
Then a single request to retrieve the contract should be made to the repository
Then the status code for the contract validation should be 403
Then verify the api response body contains "Contract is inactive/expired"

 @test
Scenario: Validate the existence of a contract Id and Org Id to allow uploading of data in COS - invalid data
Meta: @automated
Given invalid contract Id or Org Id
When any of the given parameters are not existing in the repository
Then verify that the api response status code is 404
Then verify the api response body contains "Contract does not exist"

@test
Scenario: Delete a contract in active state with given contract id
Meta: @automated
Given a valid contract Id
When the contract id exists in repository and contract is active/ in true state
Then verify that the api response status code is 200
Then verify the api response body contains "Contract is inactivated successfully"

@test
Scenario: Delete a contract in in-active state with given contract id
Meta: @automated
Given a valid contract Id - contract inactive
When the contract id exists in repository but the contract is inactive/ in false state
Then verify that the api response status code is 200
Then verify the api response body contains "Contract with given id is already inactive"

@test
Scenario: Delete a contract in active state with given contract id
Meta: @automated
Given an invalid contract Id
When the contract id does not exist in repository
Then verify that the api response status code is 404
Then verify the api response body contains "No contract exists with given id"