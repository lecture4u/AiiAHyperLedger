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
docker-compose -f docker-compose.yml up -d ca.org1.dsch.com ca.org2.dsch.com ca.org3.dsch.com orderer.dsch.com peer0.org1.dsch.com peer1.org1.dsch.com peer0.org2.dsch.com peer1.org2.dsch.com peer0.org3.dsch.com peer1.org3.dsch.com cli

# wait for Hyperledger Fabric to start
# incase of errors when running later commands, issue export FABRIC_START_TIMEOUT=<larger number>
export FABRIC_START_TIMEOUT=10
#echo ${FABRIC_START_TIMEOUT}
sleep ${FABRIC_START_TIMEOUT}

# Create the channel
docker exec cli peer channel create -o orderer.dsch.com:7050 -c dschchannel -f ./config/channel.tx

# Join peer0.org1 to the channel.
docker exec cli peer channel join -b dschchannel.block

# Join peer1.org1.example.com to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.dsch.com/users/Admin@org1.dsch.com/msp" -e "CORE_PEER_ADDRESS=peer1.org1.dsch.com:7051" cli peer channel join -b dschchannel.block

# Join peer0.org2 to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.dsch.com/users/Admin@org2.dsch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.dsch.com:7051" cli peer channel join -b dschchannel.block

# Join peer1.org2 to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.dsch.com/users/Admin@org2.dsch.com/msp" -e "CORE_PEER_ADDRESS=peer1.org2.dsch.com:7051" cli peer channel join -b dschchannel.block

# Join peer0.org3 to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org3MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.dsch.com/users/Admin@org3.dsch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org3.dsch.com:7051" cli peer channel join -b dschchannel.block

# Join peer1.org3 to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org3MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.dsch.com/users/Admin@org3.dsch.com/msp" -e "CORE_PEER_ADDRESS=peer1.org3.dsch.com:7051" cli peer channel join -b dschchannel.block

# Anchor Peer Update
docker exec cli peer channel update -o orderer.dsch.com:7050 -c dschchannel -f ./config/Org1MSPanchors.tx
docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.dsch.com/users/Admin@org2.dsch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.dsch.com:7051" cli peer channel update -o orderer.dsch.com:7050 -c dschchannel -f ./config/Org2MSPanchors.tx
docker exec -e "CORE_PEER_LOCALMSPID=Org3MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.dsch.com/users/Admin@org3.dsch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org3.dsch.com:7051" cli peer channel update -o orderer.dsch.com:7050 -c dschchannel -f ./config/Org3MSPanchors.tx