#!/bin/bash
#
# Copyright IBM Corp All Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
# Exit on first error, print all commands.
# Chaincode Path
CC_SRC_PATH=github.com/trchretail/go

# Chaincode Language
LANGUAGE=${1:-"golang"}

# Chaincode Name
CC_NAME=${1:-"trchretail"}

# Chaincode Version
CC_VERSION=${1:-"1.0"}

echo "TRCH Retail Chaincode Install Start.."

#Chaincode Install
docker exec -e "CORE_PEER_LOCALMSPID=Org3MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.trch.com/users/Admin@org3.trch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org3.trch.com:7051" cli peer chaincode install -n "$CC_NAME" -v "$CC_VERSION" -p "$CC_SRC_PATH" -l "$LANGUAGE"

# Chaincode Instantiate
docker exec -e "CORE_PEER_LOCALMSPID=Org3MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.trch.com/users/Admin@org3.trch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org3.trch.com:7051" cli peer chaincode instantiate -o orderer.trch.com:7050 -C trcrchannel -n "$CC_NAME" -l "$LANGUAGE" -v "$CC_VERSION" -c '{"Args":[""]}' -P "OR ('Org3MSP.member')"

