set -x
mvn clean verify
open -a "Google Chrome" $(pwd)/$(ls -d target/pit-reports/*|head -n 1)/victor.testing.mutation/index.html