# Pollapp

This application was built throughout the autumn semester of 2024 in DAT250.
It mainly revolves around building a proper full-stack application, evaluating software, and getting familiar with different tools for all parts of the development process.

## Features
Pollapp is a poll application, as the name implies. It lets the users register, create polls, and vote on each others' polls.

The main features are:
- User registering and login system
- Authentication
- Poll creation
- Vote creation

All of this is available through the sveltekit frontend.

## Usage
To try the application, simply clone the repository and move to the project root.
From there, run the docker compose file: `docker-compose up -d`.
Keep in mind that the images used within this compose file were created on an M1 mac, which led to some compatability issues. The custom images only work for macos/windows on arm64.
This is due to how nginx resolves the address for the backend server, namely through a docker interface. The backend requires arm64 as it was built on arm64.

If you are using another setup, run the development version of the application as specified below, or build the images yourself using the provided dockerfile.

## Development
In order to further develop the application, it is necessary to run it to test the changes along the way.
There are three components that need to be run to test the application:
- Frontend
- Backend
- Dependencies for backend (database, etc.)

### Backend dependencies
From the `backend` directory, run `docker-compose -f compose-dev.yaml up -d`.
This should start all the necessary containers that provide services to the backend.

### Backend
Development build of the backend are run through gradle.
To use gradle for this application, run `gradle bootRun` from anywhere in the project. This needs to be re-run when changes have been applied to the backend code.

### Frontend
Navigate to the `frontend` directory. Do `npm install` to install dependencies, and `npm run dev` to run the frontend service.
The sveltekit development tools provide hot reloading, so there is no need to restart this to test changes.
