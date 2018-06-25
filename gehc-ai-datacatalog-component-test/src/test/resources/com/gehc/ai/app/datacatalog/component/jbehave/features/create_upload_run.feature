Feature: DataCatalog Component - Create an upload
Narrative: As a program manager, I should be able to create an upload

@functional

@test
Scenario: User shall be able to create a upload if all required data are provided
Meta: @automated
Given all required data are provided for create upload request
When the API which creates a upload is invoked
Then a single upload should be saved to the database
And the create upload response's status code should be 201
And the create upload response's content type should be JSON

@test
Scenario: User shall not be able to create a upload if an org ID is not provided
Meta: @automated
Given org ID is not provided in the upload request
When the API which creates a upload is invoked without an org ID
Then the create upload response's status code should be 400
And the create upload response's content type should be JSON
And the response's body should contain an error message saying the request is Missing one/more required fields data.

@test
Scenario: User shall not be able to create a upload if contractId is not provided
Meta: @automated
Given contractId is not provided in the upload request
When the API which creates a upload is invoked without contractId
Then the create upload response's status code should be 400
And the create upload response's content type should be JSON
And the response's body should contain an error message saying the request is Missing one/more required fields data.

@test
Scenario: User shall not be able to create a upload if spaceId is not provided
Meta: @automated
Given spaceId is not provided in the upload request
When the API which creates a upload is invoked without spaceId
Then the create upload response's status code should be 400
And the create upload response's content type should be JSON
And the response's body should contain an error message saying the request is Missing one/more required fields data.

@test
Scenario: User shall not be able to create a upload if uploadBy is not provided
Meta: @automated
Given uploadBy status is not provided in the upload request
When the API which creates a upload is invoked without uploadBy
Then the create upload response's status code should be 400
And the create upload response's content type should be JSON
And the response's body should contain an error message saying the request is Missing one/more required fields data.

@test
Scenario: User shall not be able to create a upload if dataType is not provided
Meta: @automated
Given dataType is not provided in the upload request
When the API which creates a upload is invoked without dataType
Then the create upload response's status code should be 400
And the create upload response's content type should be JSON
And the response's body should contain an error message saying the request is Missing one/more required fields data.

@test
Scenario: User shall not be able to create a upload if tags is not provided
Meta: @automated
Given tags is not provided in the upload request
When the API which creates a upload is invoked without tags
Then the create upload response's status code should be 400
And the create upload response's content type should be JSON
And the response's body should contain an error message saying the request is Missing one/more required fields data.

@test
Scenario: User shall not be able to create a upload if upload is not unique based on spaceId, orgId, contractId
Meta: @automated
Given spaceId, orgId, contractId is already associated with an existing upload entity
When the API which creates a upload is invoked with duplicate data in spaceId, orgId, contractId
Then the create upload response's status code should be 409
And the create upload response's content type should be JSON
And the response's body should contain an error message saying the an upload entity already exists with given spaceId, orgId and contractId.
