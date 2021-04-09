#!/bin/bash
mkdir coverage
gradle cloverAggregateReports
docker run --rm -v `pwd`/coverage:/coverage-out  citest scripts/test.sh
