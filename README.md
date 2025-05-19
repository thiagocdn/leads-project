# Introduction

This is a project to run an application to get new Leads from a form.

# Run the Application

The project is set up to run smoothly in Docker with docker compose, just run:

`docker compose up -d`
-d to deattach the terminal; this is optional.

You can run the backend outside a Docker, but must have access to a PostgresDB and SNS Service providing the environment variables:

`cd backend`
`./gradlew bootRun --args="--DB_URL=jdbc:postgresql://localhost:5432/leads --PG_USER=postgres --PG_PASSWORD=postgres --SNS_ACCESS_KEY=access_key --SNS_SECRET_KEY=secret_key --SNS_REGION=us-east-1 --SNS_ENDPOINT=http://localhost:4566"`

To run the frontend application in dev mode just run:
`cd leads-website`
`npm i`
`npm run dev`

# Front Documentation

## Main route `/`

Just a welcome route with metatags to be indexed.

## Form route `/form`

Route to access the form to fill in and register a new lead.

## Confirmation route `/form/sent`

A simple route to inform the user that the form was sent

## Dashboard route `/dashboard`

A simple dashbord to update the leads that were contacted.

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

## Find Leads by Email:

This endpoint will return a list of Leads.
Even though a lead can only have an e-mail registered, it can change its phone or name - if the user uses the same name and phone it'll save as the same lead with multiple requests but if the user changes any data besides referral data, the system will save as a new lead.

`GET /email/{leadEmail}`

## Set Leads contacted by e-mail:

To have an easier way to set an e-mail contacted, this endpoint will set ALL Leads with the given e-mail contacted even though they have other data registered.

`POST /email-contacted/{leadEmail}`

## Set Leads Contacted by phone:

Just the same as above, but for phone

`POST /phone-contacted/{leadPhone}`

## List not Contacted Leads:

This is a paginated endpoint to list not contacted leads:

`GET /leads/not-contacted?page=0&size=10`

## Set a Lead contacted:

This endpoint will set lead contacted by e-mail

`POST /leads/{leadId}/contacted`
