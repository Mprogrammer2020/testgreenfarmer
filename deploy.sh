# backend deployment procedure

echo 'deploying backend java'
echo 'pulling source code'

git pull

echo 'maven creating builds'

mvn clean install -Dmaven.test.skip

echo 'creating docker image...'

docker build -t gef-be .

# start all docker containers

echo 'starting containers...'

cd /home/deploy/greenelegantfarmer

docker compose up -d

echo 'deployment done!'

