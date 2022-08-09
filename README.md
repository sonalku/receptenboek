# receptenboek
# Recipes Book Platform 

## Implementation

1. Application is developed in Spring Boot with Java 8 on Spring Tool Suite IDE. Database used is embedded mongodb database.

2. You can add/delete/modify/search recipes using given criteria using the application.

3. Logging is done on console and file

5. Exception Handling is done using ExceptionInterceptor. 

6. JUnit/Integration test implemented.

7. Sample Data populated


## Setting up the data

1. Access the application using swagger on `http://localhost:8080/swagger-ui/#/Recipes`

## Recipes Book

1. Use the POST /recipes/ api to add recipes.
{
    "ingredients":[
        {
            "amount": "20",
            "name": "Potato"
        },
        {
            "amount": "10",
            "name": "Panner"
        }],
        "instructions": ["Fry","Cook"],
        "time": "10",
        "title": "Panner Veg Curry",
        "category":"VEG",
        "servings":4
}


2. Search recipes using POST "/recipes/search/" api 

specify : criteria [INCLUDE/EXCLUDE]
filter : availble filetrs [TITLE, INGRDIENT, INSTRUCTION, SERVINGS, CATEGORY[VEG/NONVEG]]
value : {any given value}

{
  "searchFilters": [
    {
      "criteria": "INCLUDE",
      "filter": "TITLE",
      "value": "Curry"
    },
    {
      "criteria": "INCLUDE",
      "filter": "SERVINGS",
      "value": "4"
    },
    {
      "criteria": "EXCLUDE",
      "filter": "INGREDIENTS",
      "value": "Panner"
    },
    {
      "criteria": "EXCLUDE",
      "filter": "CATEGORY",
      "value": "NONVEG"
    }
  ]
}


## Other API Details

1. GET "/recipes/{id}" -  API to get recipe by id

2. PUT '/recipes/{id}" - API to update recipe by id

3. DELETE "/recipes/{id}" - API to delete recipe by id

4. GET "/recipes/all' - API to get all recipes

5. GET "/recipes/allByPage' - API to get all recipes

6. GET "/recipes/filters" - Api to get all allowed filters

7. GET "/recipes/search/ingredients" - Api to search recipe based on ingredients


## Future scope

1. Sorting is not yet implemented due to time constraint

2. Integration teting done for Controller but need to work more to utilized embeded mongo correctly

3. User added but kept optional as Securiy is not implemented but Oaut2 with JWT can be impletemed
