#!/bin/bash
set -o xtrace
./gradlew build -Pprofile=prod -x test
docker build -t techtests/shopcart .
docker tag techtests/shopcart:latest davidgfolch/shop-cart:latest
docker push davidgfolch/shop-cart
