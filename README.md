# Introduction

This is a project to run an application to get new Leads from a form.

## Ports

### Frontend

The website/frontend is set to run in port 3000.
http://localhost:3000

### Backend

The server/backend is set to run in port 8080.
http://localhost:8080

# Running the Application

The project is set up to run smoothly in Docker with docker compose, just run:

`docker compose up -d`
-d to deattach the terminal; this is optional.

- With this command, you will run both frontend and backend (with required stacks - Localstack + Postgres).

---

You can run the backend outside a Docker, but must have access to a PostgresDB and SNS Service providing the environment variables:

`cd backend`
`./gradlew bootRun --args="--DB_URL=jdbc:postgresql://localhost:5432/leads --PG_USER=postgres --PG_PASSWORD=postgres --SNS_ACCESS_KEY=access_key --SNS_SECRET_KEY=secret_key --SNS_REGION=us-east-1 --SNS_ENDPOINT=http://localhost:4566"`

These variables are ones used in docker compose, please replace as needed.

---

You can also run the frontend application outside a Docker in dev mode just running:
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

---

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

## Search Leads:

This endpoint will return a paginated list of Leads.
A Lead will increase its requestedQuantity only if it was not already contacted and all personal data (Name, Email and Phone) are the same; otherwise it'll create another entry.
This is the reason, an email can have more than one lead registered (with a different name or phone - or if it was already contacted and the person registered another contact request)

`GET /leads?page=0&size=10&contacted=false&...`

Optional Query params:

```
page - page from pagination
size - number of results per page
email - search by an email
phone - search by a phone
contacted - search for leads contacted and not contacted
```

## Set Leads contacted by e-mail:

To have an easier way to set an e-mail contacted, this endpoint will set ALL Leads with the given e-mail contacted even though they have other data registered.

`POST /leads/email-contacted/{leadEmail}`

## Set Leads contacted by phone:

Just the same as above, but for phone

`POST /phone-contacted/{leadPhone}`

## Set a Lead contacted:

This endpoint will set lead contacted by e-mail

`POST /leads/{leadId}/contacted`
