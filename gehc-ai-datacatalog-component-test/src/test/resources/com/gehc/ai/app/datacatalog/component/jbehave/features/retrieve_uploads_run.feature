Feature: DataCatalog Component - Retrieve uploads
Narrative: As a program manager, I should be able to retrieve uploads

@functional

@test
Scenario: User shall be able to retrieve all uploads for an organization if he is authorized to access a specific organization’s uploads
Meta: @automated
Given a request to retrieve all uploads for an organization
And the request is authorized to read uploads for a specific organization
When the API which retrieves uploads is invoked
Then a single call to get all uploads for the target organization should be made to the database
And the retrieve uploads API response status code should be 200
And the retrieve uploads API response content type should be JSON

@test
Scenario: User shall not be able to retrieve any uploads if he is not authorized to access a specific organization’s uploads
Meta: @automated
Given a request to retrieve all uploads for an organization
And the request does not authorize read access to specific organization’s uploads
When the API which retrieves uploads is invoked - unauthorized
Then the retrieve uploads API response status code should be 403
And the retrieve uploads API response content type should be JSON
And the retrieve uploads API response body should contain an error message saying he is not authorized to access a specific organization’s uploads


@test
Scenario: User shall be able to retrieve a specific upload if he is authorized to access the target upload
Meta: @automated
Given a request to retrieve a specific upload
And the target upload belongs to the organization specified in the request
When the API which retrieves a specific upload is invoked
Then a single call to get the target upload should be made to the database
And the retrieve uploads API response status code should be 200
And the retrieve uploads API response content type should be JSON

@test
Scenario: User shall not be able to retrieve a specific upload if he is not authorized to access the target upload
Meta: @automated
Given a request to retrieve a specific upload
And the target upload does not belong to the organization specified in the request
When the API which retrieves a specific upload is invoked
Then the retrieve uploads API response status code should be 403
And the retrieve uploads API response content type should be JSON
And the response body should contain an error message saying the user is unauthorized to access the target upload


@test
Scenario: User shall be able to retrieve a specific upload for given query parameters
Meta: @automated
Given a request to retrieve a specific upload for given query parameters
And the target upload exists for specified query parameters in the request
When the API which retrieves a specific upload for specified query parameters is invoked
Then a single call to get the target upload for specified query parameters should be made to the database
And the retrieve uploads API response status code should be 200
And the retrieve uploads API response content type should be JSON

@test
Scenario: User shall not be able to retrieve a specific upload for given query parameters
Meta: @automated
Given a request to retrieve a specific upload for given query parameters
And the target upload does not exist for specified query parameters in the request
When the API which retrieves a specific upload for specified query parameters is invoked
Then a single call to get the target upload for specified query parameters should be made to the database
And the retrieve uploads API response status code should be 200
