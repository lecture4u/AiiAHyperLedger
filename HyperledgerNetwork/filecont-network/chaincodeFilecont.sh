#!/bin/bash
#
# Copyright IBM Corp All Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
# Exit on first error, print all commands.
# Chaincode Path
CC_SRC_PATH=github.com/fileconf/go

# Chaincode Language
LANGUAGE=${1:-"golang"}

# Chaincode Name
CC_NAME=${1:-"filecont"}

# Chaincode Version
CC_VERSION=${1:-"1.0"}

echo "Chaincode Install Start.."

#Chaincode Install
docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.filecont.com/users/Admin@org1.filecont.com/msp" cli peer chaincode install -n "$CC_NAME" -v "$CC_VERSION" -p "$CC_SRC_PATH" -l "$LANGUAGE"

docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.filecont.com/users/Admin@org2.filecont.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.filecont.com:7051" cli peer chaincode install -n "$CC_NAME" -v "$CC_VERSION" -p "$CC_SRC_PATH" -l "$LANGUAGE"

# Chaincode Instantiate
docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.filecont.com/users/Admin@org1.filecont.com/msp" cli peer chaincode instantiate -o orderer.filecont.com:7050 -C filecontchannel -n "$CC_NAME" -l "$LANGUAGE" -v "$CC_VERSION" -c '{"Args":[""]}' -P "OR ('Org1MSP.member','Org2MSP.member')"
docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.filecont.com/users/Admin@org2.filecont.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.filecont.com:7051" cli peer chaincode instantiate -o orderer.filecont.com:7050 -C filecontchannel -n "$CC_NAME" -l "$LANGUAGE" -v "$CC_VERSION" -c '{"Args":[""]}' -P "OR ('Org1MSP.member','Org2MSP.member')"
