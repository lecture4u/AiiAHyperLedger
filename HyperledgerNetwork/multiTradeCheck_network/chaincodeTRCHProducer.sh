#!/bin/bash
#
# Copyright IBM Corp All Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
# Exit on first error, print all commands.
# Chaincode Path
CC_SRC_PATH=github.com/trchproducer/go

# Chaincode Language
LANGUAGE=${1:-"golang"}

# Chaincode Name
CC_NAME=${1:-"trchproducer"}

# Chaincode Version
CC_VERSION=${1:-"1.0"}

echo "TRCH Producer Chaincode Install Start.."

#Chaincode Install
docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.trpw.com/users/Admin@org1.trpw.com/msp" cli peer chaincode install -n trchproducer -v "$CC_VERSION" -p "$CC_SRC_PATH" -l "$LANGUAGE"

# Chaincode Instantiate
docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.trpw.com/users/Admin@org1.trpw.com/msp" cli peer chaincode instantiate -o orderer.trch.com:7050 -C trpwchannel -n trchproducer -l "$LANGUAGE" -v "$CC_VERSION" -c '{"Args":[""]}' -P "OR ('Org1MSP.member')"

