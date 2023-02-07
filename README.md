
# Simple User Management project by Spring Boot

REST API to manage user's registration and authentication by JWT


## Instructions

1. Create a database. Make sure to change database properties in "/resources/application.properties" file with your database credentials.
2. Simply just run the Application for create database tables or execute "/resources/tables_schema.sql" file.
3. Make sure to execute "/resources/initial_data.sql" to insert initial data to database tables.
4. Use "APIs" Postman Collection file located in root of the project to test API's.

ps: By default new accounts registers as a "USER". For to be an "ADMIN" we should change manualy account role from "account_roles" table in database.



## Description

In this application we have two Roles : "ADMIN" and "USER".
Following table shows capabilities of these two roles :

| Action | USER | ADMIN |
| ------ | ------ | ------ |
| Get All Accounts|Access is Denied|Full Control|
| Get Account by username  |Only for itself|Full Control|
| Update account by username | Only for itself |Full Control|
| Delete account by username | Only for itself |Full Control|
| Create a new Province | Access is Denied |Full Control|
| Get All Provinces |Access is Denied |Full Control|
| Get Province by ID | Access is Denied |Full Control|
| Update Province by ID | Access is Denied |Full Control|
| Create new City for a Province  | Access is Denied |Full Control|
| Get All Cities for a Province | Access is Denied |Full Control|
| Get City by ID for a Province | Access is Denied |Full Control|
| Update City by ID | Access is Denied |Full Control|
| Delete City by ID | Access is Denied |Full Control|

ps: "Login" and "Register" pages do not require any authentication.


    
