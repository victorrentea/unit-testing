## Recording WireMock stub from real traffic

### Download
Download the WireMock standalone jar from http://wiremock.org/docs/running-standalone/

### Start
Run: `java -jar wiremock-standalone-<version>.jar` 

### Configure Proxy
- Open browser at: http://localhost:8080/__admin/recorder/
- Point the recorder proxy to the real endpoint you consume (eg http://api-i-call.intra, in our case http://localhost:8090)
- Click "Start Recording"

### Run real traffic through the WireMock Recorder Proxy
- Point your application or your tests to localhost:8080 (wiremock proxy)
- Launch SafetyApp.java (the "real" endpoint)
- Run Tests
- Stop Recording
- ⭐️Enjoy recorded mappings/*.json files 