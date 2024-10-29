#!/bin/bash

# This script will:
# - Wait for the postgres database to be initialized and populated
# - Fetch the necessary user IDs to populate mongo
# - populate mongo with mock data
# This is because users are stored in postgres, but polls
# depend on knowing their owner, which is defined using a user id.

echo "This script is yet to be implemented!"
exit 1
