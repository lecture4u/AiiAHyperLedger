#!/bin/bash
#
# Copyright IBM Corp All Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
# Exit on first error, print all commands.
# Chaincode Path

echo "Install, Initsiate, invoke initLedger for all TRNC chaincode"
./chaincodeTRNCconsumer.sh
./chaincodeTRNCRetail.sh


# Chaincode InitLedger Invoke
#docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.trnc.com/users/Admin@org1.trnc.com/msp" cli peer chaincode invoke -o orderer.trnc.com:7050 -C trncchannel -n trncconsumer -c '{"function":"initLedger","Args":[""]}'
# Chaincode InitLedger Invoke
#docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.trnc.com/users/Admin@org2.trnc.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.trnc.com:7051" cli peer chaincode invoke -o orderer.trnc.com:7050 -C trncchannel -n trncretail -c '{"function":"initLedger","Args":[""]}'
