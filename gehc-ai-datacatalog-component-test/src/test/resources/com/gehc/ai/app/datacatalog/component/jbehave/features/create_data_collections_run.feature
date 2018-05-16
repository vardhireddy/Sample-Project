Feature: DataCatalog Component - Create one or more data collections

Narrative: As a data scientist or radiologist, I should be able to create one or more data collections.

@functional

@test
Scenario Outline: User shall be able to create one data collection from a pool of unique image sets
Meta: @automated
Given a pool of unique image sets
And the pool image of image sets is to be represented as 1 data collection
And no internal errors occur
When the API which creates a data collection is invoked
Then the image sets should saved as 1 data collection in the database
And the response's status code should be 201
And the response's content type should be JSON

@test
Scenario Outline: User shall be able to create multiple distinct data collections from a pool of unique image sets
Meta: @automated
Given a pool of unique image sets
And the pool image of image sets is to be represented as <numCollections> data collections
And no internal errors occur
When the API which creates a data collection is invoked
Then the image sets should saved as <numCollections> data collections in the database whereby each collection does not contain an image set in another collection
And the response's status code should be 201
And the response's content type should be JSON

Examples:
|numCollections|
|2|
|5|

@test
Scenario Outline: User shall not be able to create one or more data collections if a pool of non-unique image sets are provided
Meta: @automated
Given a pool of non-unique image sets
And the pool image of image sets is to be represented as <description>
When the API which creates a data collection is invoked
Then the response's status code should be 400
And the response's content type should be JSON
And the response's body should contain an error message saying the provided image sets should be unique

Examples:
|description|numCollections|
|a single data collection|1|
|multiple data collections|5|

@test
Scenario Outline: User shall not be able to create any data collections due to an internal error that causes all data collections to not be created
Meta: @automated
Given a pool of unique image sets
And the pool image of image sets is to be represented as <description>
And an internal error that causes no data collections to be created
When the API which creates a data collection is invoked
Then the response's status code should be 500
And the response's content type should be JSON
And the response's body should contain an error message saying there was an internal error and no collections were created

Examples:
|description|numCollections|
|a single data collection|1|
|multiple data collections|5|

@test
Scenario Outline: User shall not be able to create one or more data collections if the number of data collections to create is specified as less than 1
Meta: @automated
Given a pool of unique image sets
And the pool image of image sets is to be represented as <numCollections> data collections
When the API which creates a data collection is invoked
Then the response's status code should be 400
And the response's content type should be JSON
And the response's body should contain an error message saying the number of data collections to create should be greater than or equal to 1

Examples:
|numCollections|
|0|
|-1|

@test
Scenario Outline: User shall not be able to create any data collections due to an internal error that causes one or more data collections to not be created
Meta: @automated
Given a pool of unique image sets
And the pool image of image sets is to be represented as <description>
And an internal error that causes no data collections to be created
When the API which creates a data collection is invoked
Then the response's status code should be 500
And the response's content type should be JSON
And the response's body should contain an error message saying there was an internal error and no collections were created

Examples:
|description|numCollections|
|a single data collection|1|
|multiple data collections|5|

# User passes in 1 image set and wants to split