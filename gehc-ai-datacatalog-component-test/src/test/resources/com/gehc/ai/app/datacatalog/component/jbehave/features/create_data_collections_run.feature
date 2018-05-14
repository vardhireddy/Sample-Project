Feature: DataCatalog Component - Create one or more data collections

Narrative: As a data scientist or radiologist, I should be able to create one or more data collections.

@functional

@test
Scenario Outline: Create one data collection from a pool of unique image sets
Meta: @automated
Given a pool of unique image sets
When the API which creates a data collection is invoked to create a single data collection
Then the response's status code should be 201
Then the response's content type should be JSON
Then the response's body should contain a JSON string that defines a single data collection

@test
Scenario Outline: Create multiple distinct data collections from a pool of unique image sets
Meta: @automated
Given a pool of unique image sets
When the API which creates a data collection is invoked to create <numCollections> data collections
Then the response's status code should be 201
Then the response's content type should be JSON
Then the response's body should contain a JSON string that defines <numCollections> data collections whereby each collection does not contain an image set in another collection

Examples:
|numCollections|
|2|
|5|

@test
Scenario Outline: Fail to create one or more data collections due to non unique image sets
Meta: @automated
Given a pool of non-unique image sets
When the API which creates a data collection is invoked to create <description>
Then the response's status code should be 400
Then the response's content type should be JSON
Then the response's body should contain an error message saying the provided image sets should be unique

Examples:
|description|numCollections|
|a single data collection|1|
|multiple data collections|5|

@test
Scenario Outline: Fail to create any data collections due to internal error
Meta: @automated
Given a pool of unique image sets
Given an interal error that causes no data collections to be created
When the API which creates a data collection is invoked to create <description>
Then the response's status code should be 500
Then the response's content type should be JSON
Then the response's body should contain an error message saying there was an internal error and no collections were created

Examples:
|description|numCollections|
|a single data collection|1|
|multiple data collections|5|

@test
Scenario Outline: Fail to create all data collections due to internal error
Meta: @automated
Given a pool of unique image sets
Given an interal error that causes at least one data collection to not be created
When the API which creates a data collection is invoked to create <description>
Then the response's status code should be 206
Then the response's content type should be JSON
Then the response's body should contain a message saying that some but not all data collections were created

Examples:
|description|numCollections|
|a single data collection|1|
|multiple data collections|5|