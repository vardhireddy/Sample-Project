#Feature: DataCatalog Component - Contract
#
#Narrative: As a data scientist, I should be able to
#Store contract data
#Retrieve contract data
#Validate the existence of a contract Id and Org Id to allow uploading of data in COS - valid data
#Validate the existence of a contract Id and Org Id to allow uploading of data in COS - invalid data
#
# @functional
#@crs
##
##@test
##Scenario: Store contract data
##Meta: @automated
##Given Store contract data - DataSetUp Provided
##When Store contract data
##Then verify Store contract data
##
##@test
##Scenario: Retrieve contract data
##Meta: @automated
##Given Retrieve contract data - DataSetUp Provided
##When Retrieve contract data
##Then verify Retrieve contract data
##
##@test
##Scenario: Validate the existence of a contract Id and Org Id to allow uploading of data in COS - valid data
##Meta: @automated
##Given contract Id and Org Id
##When the given parameters are existing in the repository
##Then verify the api response status code is 200
##Then verify the api response body contains "Contract exists"
##
##@test
##Scenario: Validate the existence of a contract Id and Org Id to allow uploading of data in COS - invalid data
##Meta: @automated
##Given invalid contract Id or Org Id
##When any of the given parameters are not existing in the repository
##Then verify that the api response status code is 200
##Then verify the api response body contains "Contract does not exist"