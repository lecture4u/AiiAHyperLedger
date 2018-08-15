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
docker-compose -f docker-compose.yml up -d ca.org1.trch.com ca.org2.trch.com ca.org3.trch.com ca.org4.trch.com orderer.trch.com peer0.org1.trch.com peer0.org2.trch.com peer0.org3.trch.com peer0.org4.trch.com cli

# wait for Hyperledger Fabric to start
# incase of errors when running later commands, issue export FABRIC_START_TIMEOUT=<larger number>
export FABRIC_START_TIMEOUT=10
#echo ${FABRIC_START_TIMEOUT}
sleep ${FABRIC_START_TIMEOUT}

# Create the trpwchannelchannel
docker exec cli peer channel create -o orderer.trch.com:7050 -c trpwchannel -f ./config/trpwchannel.tx

# Create the trpwchannelchannel
docker exec -e "CORE_PEER_LOCALMSPID=Org3MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.trch.com/users/Admin@org3.trch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org3.trch.com:7051" cli peer channel create -o orderer.trch.com:7050 -c trcrchannel -f ./config/trcrchannel.tx

# Join peer0.org1 to the channel.
docker exec cli peer channel join -b trpwchannel.block

# Join peer0.org2 to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.trch.com/users/Admin@org2.trch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.trch.com:7051" cli peer channel join -b trpwchannel.block

# Join peer0.org3 to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org3MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.trch.com/users/Admin@org3.trch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org3.trch.com:7051" cli peer channel join -b trcrchannel.block

# Join peer0.org4 to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org4MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org4.trch.com/users/Admin@org4.trch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org4.trch.com:7051" cli peer channel join -b trcrchannel.block

# Anchor Peer Update
docker exec cli peer channel update -o orderer.trch.com:7050 -c trpwchannel -f ./config/Org1MSPanchors.tx
docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.trch.com/users/Admin@org2.trch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.trch.com:7051" cli peer channel update -o orderer.trch.com:7050 -c trpwchannel -f ./config/Org2MSPanchors.tx
docker exec -e "CORE_PEER_LOCALMSPID=Org3MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.trch.com/users/Admin@org3.trch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org3.trch.com:7051" cli peer channel update -o orderer.trch.com:7050 -c trcrchannel -f ./config/Org3MSPanchors.tx
docker exec -e "CORE_PEER_LOCALMSPID=Org4MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org4.trch.com/users/Admin@org4.trch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org4.trch.com:7051" cli peer channel update -o orderer.trch.com:7050 -c trcrchannel -f ./config/Org4MSPanchors.tx