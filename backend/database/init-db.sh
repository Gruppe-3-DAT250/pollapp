# Run this by sourcing it!
docker run \
	--name my-postgres \
	--rm \
	-p 5432:5432 \
	-e POSTGRES_PASSWORD=verysecret \
	-v "$(pwd)/init-db.sql":/docker-entrypoint-initdb.d/init-db.sql \
	postgres
