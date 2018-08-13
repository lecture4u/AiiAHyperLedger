#!/bin/bash
#
# Copyright IBM Corp All Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
# Exit on first error, print all commands.
# Chaincode Path

echo "Install, Initsiate, invoke initLedger for all DSCH chaincode"
./chaincodeDSCHUser.sh
./chaincodeDSCHWholeSale.sh
./chaincodeDSCHLocalSale.sh

# Chaincode InitLedger Invoke
#docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.dsch.com/users/Admin@org1.dsch.com/msp" cli peer chaincode invoke -o orderer.dsch.com:7050 -C dschchannel -n dschuser -c '{"function":"initLedger","Args":[""]}'
# Chaincode InitLedger Invoke
#docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.dsch.com/users/Admin@org2.dsch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.dsch.com:7051" cli peer chaincode invoke -o orderer.dsch.com:7050 -C dschchannel -n dschwholesale -c '{"function":"initLedger","Args":[""]}'
# Chaincode InitLedger Invoke
#docker exec -e "CORE_PEER_LOCALMSPID=Org3MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.dsch.com/users/Admin@org3.dsch.com/msp" -e "CORE_PEER_ADDRESS=peer0.org3.dsch.com:7051" cli peer chaincode invoke -o orderer.dsch.com:7050 -C dschchannel -n dschlocalesale -c '{"function":"initLedger","Args":[""]}'
