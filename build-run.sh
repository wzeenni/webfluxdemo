#!/bin/bash

mvn clean package -U

java -jar ./target/WebFluxDemo-0.0.1.jar