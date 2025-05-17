# Introduction

This is a project to run an application to get new Leads from a form.

# Run the Application

The project is set up to run smoothly in Docker with docker compose, just run:

`docker compose up`

You can run the backend outside a Docker, but you must give access to a Postgres DB to the application:

`./gradlew bootRun --args="--DB_URL=jdbc:postgresql://base_url:port/database_name --PG_USER=user --PG_PASSWORD=password"`

# Backend Documentation

## Register a new Lead:

`POST /leads`

```
{
    "name": "John Doe",
    "phone": "1234567890",
    "email": "john@doe.com",
    "referralSource": "OTHERS",
    "referralComment": "Somewhere else"
}
```

## Find a Lead

`GET /leads/{leadId}`
