#!/bin/bash
#
# Copyright IBM Corp All Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
# Exit on first error, print all commands.
set -e

# Shut down the Docker containers for the system tests.
docker-compose -f docker-compose.yml down
docker rm -f $(docker ps -aq)
docker network prune

# remove chaincode docker images
docker rmi $(docker images *filecont* -q)

echo  "Your Blockchain network is now clean"
