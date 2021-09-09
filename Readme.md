# Zendesk Challenge Search

## Constraints

1. Need to search by multiple fields
2. Can not use an off the shelf search product 
3. Cannot use a DB (2nd best option due to indexing)
4. Need better than linear searching (i.e. O(1) or O(logn)) for all provided searches

## Assumptions and scope

1. Unlimited storage available - no storage constraints (important for the Strategy section)
2. No need to add ability to search records between any two values (eg. between dates)
3. No pagination needed
4. No need to add or remove data in the already provided datasets
5. All records have an id value
6. Only searches by id need to show related entities
7. Assuming JSON is a human readable format

## Descoped (in the interest of time)

1. Not adding a logging framework
2. Not  adding heaps of javadoc (aiming to write as self-documenting code as possible)
3. Not adding search by tags or domain_names (the way this would have been implemented would be - denormalising the structure into the below mentioned 'index' structure)

## Strategy

To make search better than linear, indexing is being done on every field (except dates and lists). i.e. Worst Case O(1) lookup...comes at the cost of memory.
Using a Map<String,Map<String,Set<String>>> per entity to index as <field,<field_value, list_of_ids>>

The way this structure would accommodate searching is as following.

Eg. 

    1. Normal searching by some field value.
	when searching for all users that have an 'active' value of true, their ids would be searched in the denormalised data, and then their details by id would be fetched from a base list of data.
	 <"active",<"true",["user1","userId2","userId3",...]>>
	 
    2. Searching by tags/domains -
       when searching for all items that have a tag e.g "friendly"
       A map of similar structure but slightly different keys would have values as so
	<tag_value,<Entity_type,list_of_ids>>
       <"friendly",<"Organization",[org1,org2,org3,org4]>>
    
## Installation

- Java jdk 11
- Gradle 6.8.3

Note : Using older version of gradle and java since the latest gradle 7.1.1 and java 16 were causing IDE issues for Intellij with a 'java.lang.String org.codehaus.groovy.runtime.StringGroovyMethods.capitalize(java.lang.String) error. It has been noted as bug.

```shell script
gradle wrapper
```

## Build (inclusive of tests)

```shell script
./gradlew clean build
```

## Test

```shell script
./gradlew clean test
```

## Run Application

```shell script
./gradlew clean build run
```