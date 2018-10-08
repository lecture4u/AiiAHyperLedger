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
docker-compose -f docker-compose.yml up -d ca.org1.trpw.com ca.org2.trpw.com ca.org3.trcr.com ca.org4.trcr.com zookeeper0 zookeeper1 zookeeper2 kafka0 kafka1 kafka2 kafka3 orderer.trch.com peer0.org1.trpw.com peer0.org2.trpw.com peer0.org3.trcr.com peer0.org4.trcr.com cli

# wait for Hyperledger Fabric to start
# incase of errors when running later commands, issue export FABRIC_START_TIMEOUT=<larger number>
export FABRIC_START_TIMEOUT=10
#echo ${FABRIC_START_TIMEOUT}
sleep ${FABRIC_START_TIMEOUT}

# Create the trpwchannelchannel
docker exec cli peer channel create -o orderer.trch.com:7050 -c trpwchannel -f ./config/trpwchannel.tx

# Create the trcrchannelchannel
docker exec -e "CORE_PEER_LOCALMSPID=Org4MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org4.trcr.com/users/Admin@org4.trcr.com/msp" -e "CORE_PEER_ADDRESS=peer0.org4.trcr.com:7051" cli peer channel create -o orderer.trch.com:7050 -c trcrchannel -f ./config/trcrchannel.tx

# Join peer0.org1 to the channel.
docker exec cli peer channel join -b trpwchannel.block

# Join peer0.org2 to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.trpw.com/users/Admin@org2.trpw.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.trpw.com:7051" cli peer channel join -b trpwchannel.block

# Join peer0.org3 to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org3MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.trcr.com/users/Admin@org3.trcr.com/msp" -e "CORE_PEER_ADDRESS=peer0.org3.trcr.com:7051" cli peer channel join -b trcrchannel.block

# Join peer0.org4 to the channel.
docker exec -e "CORE_PEER_LOCALMSPID=Org4MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org4.trcr.com/users/Admin@org4.trcr.com/msp" -e "CORE_PEER_ADDRESS=peer0.org4.trcr.com:7051" cli peer channel join -b trcrchannel.block

# Anchor Peer Update
docker exec cli peer channel update -o orderer.trch.com:7050 -c trpwchannel -f ./config/Org1MSPanchors.tx
docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.trpw.com/users/Admin@org2.trpw.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.trpw.com:7051" cli peer channel update -o orderer.trch.com:7050 -c trpwchannel -f ./config/Org2MSPanchors.tx
docker exec -e "CORE_PEER_LOCALMSPID=Org3MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.trcr.com/users/Admin@org3.trcr.com/msp" -e "CORE_PEER_ADDRESS=peer0.org3.trcr.com:7051" cli peer channel update -o orderer.trch.com:7050 -c trcrchannel -f ./config/Org3MSPanchors.tx
docker exec -e "CORE_PEER_LOCALMSPID=Org4MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org4.trcr.com/users/Admin@org4.trcr.com/msp" -e "CORE_PEER_ADDRESS=peer0.org4.trcr.com:7051" cli peer channel update -o orderer.trch.com:7050 -c trcrchannel -f ./config/Org4MSPanchors.tx