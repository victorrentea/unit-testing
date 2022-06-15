
Go here http://wiremock.org/docs/running-standalone/

Download the standalone jar 

Run:
java -jar wiremock-standalone-2.27.2.jar 

Go to http://localhost:8080/__admin/recorder/

Point the recorded proxy to the real endpoint you consume (http://localhost:8090)

Start Recording

Point your app/tests to localhost:8080 (wiremock proxy)

Launch SafetyApp.java (the "real" endpoint)

Run Tests

Stop Recording

Enjoy captured mappings/*.json files