
Go here http://wiremock.org/docs/running-standalone/

Download

Run
java -jar wiremock-standalone-2.27.2.jar 

Go to http://localhost:8080/__admin/recorder/

Point to real endpoint

Start Recording

Point your app/tests to localhost:8080 (wiremock proxy)

Run

Stop Recording

Enjoy captured mappings/*.json files