# Deployment to Render

This project is prepared for Docker, Docker Compose, Render hosting, and GitHub Actions CI/CD.

## Local Docker Run

1. Copy `.env.example` to `.env` and set local values.
2. Start the app and PostgreSQL:

```bash
docker compose up --build
```

3. Open:

```text
http://localhost:8080
http://localhost:8080/actuator/health
```

## Environment Variables

Local and production configuration is controlled through environment variables:

```text
PORT
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
DATABASE_URL
JWT_SECRET
JWT_EXPIRATION_SECONDS
```

For local Docker Compose, `.env` can also contain:

```text
DB_NAME
DB_USERNAME
DB_PASSWORD
DB_PORT
APP_PORT
```

## Render Hosting

Deployment target: Render.

The repository contains `render.yaml`, a Render Blueprint that creates:

- a Docker web service named `videogames-shop`
- a free PostgreSQL database named `videogames-shop-db`
- a healthcheck path: `/actuator/health`
- generated production `JWT_SECRET`
- database connection environment variables
- disabled automatic deploys, so GitHub Actions controls deployment with the Render deploy hook

Steps:

1. Push the repository to GitHub.
2. In Render, create a new Blueprint from this repository.
3. Render will read `render.yaml`, build the Docker image, create PostgreSQL, and deploy the app.
4. In the Render service settings, keep the healthcheck path as `/actuator/health`.

## GitHub Actions CI/CD

Workflow file: `.github/workflows/cicd.yml`.

It performs:

- frontend build
- backend tests
- Docker image build
- deploy trigger to Render
- public healthcheck after deploy

Add these repository secrets in GitHub:

```text
RENDER_DEPLOY_HOOK_URL
RENDER_APP_URL
```

`RENDER_DEPLOY_HOOK_URL` is the deploy hook URL from the Render service settings.
`RENDER_APP_URL` is the public Render URL, for example:

```text
https://videogames-shop.onrender.com
```

Render also supports deploying after GitHub checks pass. If that mode is enabled in Render, the workflow can still be used for build, tests, Docker validation, and healthcheck.

Official Render docs used for this setup:

- https://render.com/docs/blueprint-spec
- https://render.com/docs/deploy-hooks
- https://render.com/docs/health-checks
- https://render.com/docs/web-services
