Feature: DataCatalog Component - Create one or more data collections

User shall be able to create several data collections if the base collection contains a set of unique image sets and the specified data collection size is 1
User shall be able to create several data collections if the base collection contains a set of unique image sets and the specified data collection size is greater than 1 and less than the number of image sets
User shall be able to create 1 data collection if the base collection contains a set of unique image sets and the specified data collection size is equal to the number of image sets
User shall be able to create 1 data collection if the base collection contains a set of unique image sets and no data collection size is specified
User shall be able to update a single existing data collection
User shall not be able to create one or more data collections if no data collection is provided
User shall not be able to create one or more data collections if no image sets are defined for the data collection
User shall not be able to create one or more data collections if no name is defined for the data collection
User shall not be able to create one or more data collections if no type is defined for the data collection
User shall not be able to create one or more data collections if a pool of non-unique image sets are provided
User shall not be able to create any data collections due to an internal error that causes all data collections to not be created
User shall not be able to create one or more data collections if the number of data collections to create is specified as less than 1
User shall not be able to create one or more data collections if the number of data collections to create is greater than the number of image sets

@functional

@test
Scenario: User shall be able to create several data collections if the base collection contains a set of unique image sets and the specified data collection size is 1
Meta: @automated
Given a data collection with unique image sets
And the data collection size is 1
And no internal errors occur
When the API which creates a data collection is invoked
Then the image sets should be split such that there are as many data collections saved to the database as there are image sets
And the response's status code should be 201
And the response's content type should be JSON

@test
Scenario: User shall be able to create several data collections if the base collection contains a set of unique image sets and the specified data collection size is greater than 1 and less than the number of image sets
Meta: @automated
Given a data collection with unique image sets
And the data collection size is greater than 1 and less than the number of image sets
And no internal errors occur
When the API which creates a data collection is invoked
Then the number of data collections that have the target collection size should be quotient of the number of image sets divided by the target collection size and there should be one data collection saved that contains the remainder of the quotient
And the response's status code should be 201
And the response's content type should be JSON

@test
Scenario: User shall be able to create 1 data collection if the base collection contains a set of unique image sets and the specified data collection size is equal to the number of image sets
Meta: @automated
Given a data collection with unique image sets
And the data collection size is equal to the number of image sets
And no internal errors occur
When the API which creates a data collection is invoked
Then 1 data collection should be saved to the database
And the response's status code should be 201
And the response's content type should be JSON

@test
Scenario: User shall be able to create 1 data collection if the base collection contains a set of unique image sets and no data collection size is specified
Meta: @automated
Given a data collection with unique image sets
And no data collection size is specified
And no internal errors occur
When the API which creates a data collection is invoked
Then 1 data collection should be saved to the database
And the response's status code should be 201
And the response's content type should be JSON

@test
Scenario: User shall be able to update a single existing data collection
Meta: @automated
Given an existing data collection
And no data collection size is specified
And no internal errors occur
When the API which creates a data collection is invoked
Then 1 data collection should be updated to the database
And the response's status code should be 201
And the response's content type should be JSON

@test
Scenario: User shall not be able to create one or more data collections if no data collection is provided
Meta: @automated
Given no data collection is provided
And no internal errors occur
When the API which creates a data collection is invoked
Then the response's status code should be 400
And the response's content type should be JSON
And the response's body should contain an error message saying a data collection needs to be provided

@test
Scenario: User shall not be able to create one or more data collections if no image sets are defined for the data collection
Meta: @automated
Given no image sets are defined
And no internal errors occur
When the API which creates a data collection is invoked
Then the response's status code should be 400
And the response's content type should be JSON
And the response's body should contain an error message saying image sets must defined for the data collection

@test
Scenario: User shall not be able to create one or more data collections if no name is defined for the data collection
Meta: @automated
Given a data collection that does not have a name defined
And no internal errors occur
When the API which creates a data collection is invoked
Then the response's status code should be 400
And the response's content type should be JSON
And the response's body should contain an error message saying a name must defined for the data collection

@test
Scenario: User shall not be able to create one or more data collections if no type is defined for the data collection
Meta: @automated
Given a data collection that does not have a type defined
And no internal errors occur
When the API which creates a data collection is invoked
Then the response's status code should be 400
And the response's content type should be JSON
And the response's body should contain an error message saying a type must defined for the data collection

@test
Scenario: User shall not be able to create one or more data collections if a pool of non-unique image sets are provided
Meta: @automated
Given a data collection with non-unique image sets
And the specified data collection size doesn't matter
And no internal errors occur
When the API which creates a data collection is invoked
Then the response's status code should be 400
And the response's content type should be JSON
And the response's body should contain an error message saying the provided image sets should be unique

@test
Scenario: User shall not be able to create any data collections due to an internal error that causes all data collections to not be created
Meta: @automated
Given a data collection with unique image sets
And the specified data collection size doesn't matter
And an internal error that causes no data collections to be created
When the API which creates a data collection is invoked
Then the response's status code should be 500
And the response's content type should be JSON
And the response's body should contain an error message saying there was an internal error and no collections were created

@test
Scenario: User shall not be able to create one or more data collections if the number of data collections to create is specified as less than 1
Meta: @automated
Given a data collection with unique image sets
And the data collection size is less than 1
When the API which creates a data collection is invoked
Then the response's status code should be 400
And the response's content type should be JSON
And the response's body should contain an error message saying the image sets could be batched using the provided data collection size

@test
Scenario: User shall not be able to create one or more data collections if the number of data collections to create is greater than the number of image sets
Meta: @automated
Given a data collection with unique image sets
And the data collection size is greater than the number of image sets
When the API which creates a data collection is invoked
Then the response's status code should be 400
And the response's content type should be JSON
And the response's body should contain an error message saying the image sets could be batched using the provided data collection size