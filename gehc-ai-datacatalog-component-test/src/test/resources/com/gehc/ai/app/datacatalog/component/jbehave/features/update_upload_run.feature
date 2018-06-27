Feature: DataCatalog Component - Update an upload
Narrative: As a program manager, I should be able to update an upload's status and summary

@functional

@test
Scenario: User shall be able to update a upload if all required data are provided
Meta: @automated
Given all required data are provided for update upload request
When the API which updates a upload is invoked
Then a single upload should be save updated upload to the database
And the update uploads API response status code should be 200
And the update uploads API response content type should be JSON

@test
Scenario: User shall not be able to update a upload if an upload ID is not provided
Meta: @automated
Given upload ID provided  is invalid in the update upload request
When the API which updates a upload is invoked without upload ID
Then the update uploads API response status code should be 400
And the update uploads API response content type should be JSON
And the update uploads API response body should contain an error message saying the Id provided in update upload request is invalid.

@test
Scenario: User shall not be able to update a upload if summary and status both are not provided
Meta: @automated
Given summary and status both are not provided in the update upload request
When the API which updates a upload is invoked without summary and status
Then the update uploads API response status code should be 400
And the update uploads API response content type should be JSON
And the update uploads API response body should contain an error message saying either status or summary must be provided in update upload request

@test
Scenario: User shall not be able to update a upload if lastModified date is not provided
Meta: @automated
Given lastModified date is not provided in the upload request
When the API which updates a upload is invoked without lastModified date
Then the update uploads API response status code should be 400
And the update uploads API response content type should be JSON
And the update uploads API response body should contain an error message saying the Update Upload Request lastModifiedTime is missing

@test
Scenario: User shall not be able to create a upload if last modified date doesn't match the upload entity in DB
Meta: @automated
Given stale last modified date is provided in the upload request
When the API which updates a upload is invoked with old lastModified date
Then the update uploads API response status code should be 409
And the update uploads API response content type should be JSON
And the update uploads API response body should contain an error message saying Upload update request last modified time does not match with the upload entity in db. Please pull the latest instance and make an update
