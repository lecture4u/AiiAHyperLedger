#!/bin/bash
#
# Copyright IBM Corp All Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#
# Exit on first error, print all commands.
# Chaincode Path

echo "Install, Initsiate, invoke initLedger for all TRCH chaincode"
./chaincodeTRCHProducer.sh
./chaincodeTRCHWholeSale.sh
./chaincodeTRCHRetail.sh
./chaincodeTRCHConsumer.sh

# Chaincode InitLedger Invoke
#docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.trpw.com/users/Admin@org1.trpw.com/msp" cli peer chaincode invoke -o orderer.trch.com:7050 -C trpwchannel -n trchproducer -c '{"function":"initLedger","Args":[""]}'
# Chaincode InitLedger Invoke
#docker exec -e "CORE_PEER_LOCALMSPID=Org2MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.trpw.com/users/Admin@org2.trpw.com/msp" -e "CORE_PEER_ADDRESS=peer0.org2.trpw.com:7051" cli peer chaincode invoke -o orderer.trch.com:7050 -C trpwchannel -n trchwholesale -c '{"function":"initLedger","Args":[""]}'
# Chaincode InitLedger Invoke
#docker exec -e "CORE_PEER_LOCALMSPID=Org3MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.trcr.com/users/Admin@org3.trcr.com/msp" -e "CORE_PEER_ADDRESS=peer0.org3.trcr.com:7051" cli peer chaincode invoke -o orderer.trch.com:7050 -C trcrchannel -n trchretail -c '{"function":"initLedger","Args":[""]}'
# Chaincode InitLedger Invoke
#docker exec -e "CORE_PEER_LOCALMSPID=Org4MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org4.trcr.com/users/Admin@org4.trcr.com/msp" -e "CORE_PEER_ADDRESS=peer0.org4.trcr.com:7051" cli peer chaincode invoke -o orderer.trch.com:7050 -C trcrchannel -n trchconsumer -c '{"function":"initLedger","Args":[""]}'
