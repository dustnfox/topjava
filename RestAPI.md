#Calories Management application REST controllers API
##Meal REST Controller
Every request's executed the in context of the authentificated user. If meal with requested ID was created by another 
user, request will fail.

####Get 
Returns the meal by the meal's ID.
- HTTP method type: GET
- Context related URL: /rest/meals/{ID}
- Parameters: ID of the saved meal 
- Success status: 200
- Return type: JSON
- curl command example: 
  >curl --request GET http://localhost:8080/topjava/rest/meals/100002
- response example:
  > {
      "id": 100002,
      "dateTime": "2015-05-30T10:00:00",
      "description": "Завтрак",
      "calories": 500,
      "user": null
  }

####Get all
Returns all the meals of the authenificated user.
- HTTP method type: GET
- Context related URL: /rest/meals
- Parameters: none
- Success status: 200
- Return type: JSON
- curl command example: 
  >curl --request GET http://localhost:8080/topjava/rest/meals
- response example:
  >[
     {
         "id": 100007,
         "dateTime": "2015-05-31T20:00:00",
         "description": "Ужин",
         "calories": 510,
         "exceed": true
     },
     {
         "id": 100006,
         "dateTime": "2015-05-31T13:00:00",
         "description": "Обед",
         "calories": 1000,
         "exceed": true
     },
     {
         "id": 100005,
         "dateTime": "2015-05-31T10:00:00",
         "description": "Завтрак",
         "calories": 500,
         "exceed": true
     },
     {
         "id": 100002,
         "dateTime": "2015-05-30T10:00:00",
         "description": "Завтрак",
         "calories": 500,
         "exceed": false
     }
 ]
 
 ####Delete
 Removes the meals by ID.
 - HTTP method type: DELETE
 - Context related URL: /rest/meals/{ID}
 - Parameters: ID of the meal to delete 
 - Success status: 204
 - Return type: none
 - curl command example: 
   > curl --request DELETE http://localhost:8080/topjava/rest/meals/100002
 
 ####Save
Saves given JSON meal representation. Returns URL of the saved meal as response header_location_ field. 
  - HTTP method type: POST
  - Context related URL: /rest/meals
  - Parameters: JSON entity representation in request body
  - Success status: 201
  - Return type: JSON
  - Return value: save entity with given ID
  - Curl command example: 
    > curl --request POST --data '{"id":null,"dateTime":"2019-05-30T10:00:00","description":"Завтрак","calories":500,"user":null}' http://localhost:8080/topjava/rest/meals
  - Response example:
    - header: location -> http://localhost:8080/topjava/rest/meals/100012
    - body: 
      > {
                "id": 100012,
                "dateTime": "2019-05-30T10:00:00",
                "description": "Завтрак",
                "calories": 500,
                "user": null
            }
            
####Update
Updates meal by ID. 
  - HTTP method type: PUT
  - Context related URL: /rest/meals/{ID}
  - Parameters: 
    - URL: ID of the meal to update
    - Body: meal JSON representation
  - Success status: 200
  - Return type: none
  - Curl command example: 
    >curl --request PUT --data '{
                                  "id": 100012,
                                  "dateTime": "2019-05-30T10:00:00",
                                  "description": "New Description",
                                  "calories": 500,
                                  "user": null
                              }' http://localhost:8080/topjava/rest/meals/100012

####Get all with filter by date/time
Returns all the meals having creation date and time between given parameters. Date and time in request parameters have 
to be formatted in ISO. Leading zeros are mandatory. 
  - HTTP method type: GET
  - Context related URL: /rest/meals/filter
  - Parameters: 
    - URL encoded
      - fromDate: meals whose creation date comes before the given will not be shown
      - fromTime: meals whose creation time comes before the given will not be shown
      - toDate:   meals whose creation date comes after the given will not be shown
      - toTime:   meals whose creation time comes after the given will not be shown
  - Success status: 200
  - Return type: JSON
  - Curl command example: 
    >curl --request GET --data-urlencoded='fromDate=2015-04-30&toDate=2015-05-30&fromTime=06:00:00&toTime=11:00:00' http://localhost:8080/topjava/rest/meals/filter
  - Response example:
    >[
         {
             "id": 100002,
             "dateTime": "2015-05-30T10:00:00",
             "description": "Завтрак",
             "calories": 500,
             "exceed": false
         }
     ]
 