#!/bin/bash
#
# Copyright IBM Corp All Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
# Exit on first error, print all commands.
set -ev

# don't rewrite paths for Windows Git Bash users
export MSYS_NO_PATHCONV=1
docker-compose -f docker-compose.yml down
docker-compose -f docker-compose.yml up -d ca.org1.filecont.com ca.org2.filecont.com orderer.filecont.com peer0.org1.filecont.com peer0.org2.filecont.com cli

# wait for Hyperledger Fabric to start
# incase of errors when running later commands, issue export FABRIC_START_TIMEOUT=<larger number>
export FABRIC_START_TIMEOUT=10
#echo ${FABRIC_START_TIMEOUT}
sleep ${FABRIC_START_TIMEOUT}

# Create the channel
docker exec cli peer channel create -o orderer.filecont.com:7050 -c filecontchannel -f ./config/channel.tx

# Join peers to the channel.
docker exec cli peer channel join -b filecontchannel.block
docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.filecont.com/users/Admin@org2.filecont.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.filecont.com:7051" cli peer channel join -b filecontchannel.block

# Anchor Peer Update
docker exec cli peer channel update -o orderer.filecont.com:7050 -c filecontchannel -f ./config/Org1MSPanchors.tx
docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.filecont.com/users/Admin@org2.filecont.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.filecont.com:7051" cli peer channel update -o orderer.filecont.com:7050 -c filecontchannel -f ./config/Org2MSPanchors.tx
