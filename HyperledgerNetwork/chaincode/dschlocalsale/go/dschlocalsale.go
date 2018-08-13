/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * The sample smart contract for documentation topic:
 * Writing Your First Blockchain Application
 */

package main

/* Imports
 * 4 utility libraries for formatting, handling bytes, reading and writing JSON, and string manipulation
 * 2 specific Hyperledger Fabric specific libraries for Smart Contracts
 */
import (
	"bytes"
	"encoding/json"
	"fmt"
	"os"
	"strconv"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	sc "github.com/hyperledger/fabric/protos/peer"
)

// Define the Smart Contract structure
type SmartContract struct {
}

// Define the batting structure, with 4 properties.  Structure tags are used by encoding/json library
type RetailAsset struct {
	MarketPrice string `json:"marketPrice"`
	Quantity    string `json:"quantity"`
}

/*
 * The Init method is called when the Smart Contract "horseRaingBatting" is instantiated by the blockchain network
 * Best practice is to have any Ledger initialization in separate function -- see initLedger()
 */
func (s *SmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
	return shim.Success(nil)
}

/*
 * The Invoke method is called as a result of an application request to run the Smart Contract "horseRaingBatting"
 * The calling application program has also specified the particular smart contract function to be called, with arguments
 */
func (s *SmartContract) Invoke(APIstub shim.ChaincodeStubInterface) sc.Response {

	// Retrieve the requested Smart Contract function and arguments
	function, args := APIstub.GetFunctionAndParameters()
	// Route to the appropriate handler function to interact with the ledger appropriately
	if function == "initLedger" {
		return s.initLedger(APIstub)
	} else if function == "queryAssetWithKey" {
		return s.queryAssetWithKey(APIstub, args)
	} else if function == "queryAllAssetList" {
		return s.queryAllAssetList(APIstub)
	} else if function == "increaseQuantity" {
		return s.increaseQuantity(APIstub, args)
	} else if function == "decreaseQuantity" {
		return s.decreaseQuantity(APIstub, args)
	} else if function == "increaseMarketPrice" {
		return s.increaseMarketPrice(APIstub, args)
	} else if function == "decreaseMarketPrice" {
		return s.decreaseMarketPrice(APIstub, args)
	} else if function == "assetInfoHistory" {
		return s.assetInfoHistory(APIstub, args)
	}

	return shim.Error("Invalid Smart Contract function name.")
}

func (s *SmartContract) queryAssetWithKey(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	assetAsBytes, _ := APIstub.GetState(args[0])
	return shim.Success(assetAsBytes)
}

func (s *SmartContract) queryAllAssetList(APIstub shim.ChaincodeStubInterface) sc.Response {

	startKey := ""
	endKey := ""

	resultsIterator, err := APIstub.GetStateByRange(startKey, endKey)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	fmt.Printf("- queryAllAssetList:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())
}

func (s *SmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {
	/*
	 MarketPrice string `json:"marketPrice"`
	 Quantity    string `json:"quantity"`

	 HasVegetable string `json:"hasVegetable"`
	 HasMineral   string `json:"hasMineral"`
	 HasMeat      string `json:"hasMeat"`
	 HasGrain     string `json:"hasGrain"`
	 HasFruit     string `json:"hasFruit"`
	*/
	assets := []RetailAsset{
		RetailAsset{MarketPrice: "103", Quantity: "57673"},
		RetailAsset{MarketPrice: "196", Quantity: "23342"},
		RetailAsset{MarketPrice: "124", Quantity: "44564"},
		RetailAsset{MarketPrice: "143", Quantity: "24567"},
		RetailAsset{MarketPrice: "194", Quantity: "11225"},
	}

	assetAsBytes, _ := json.Marshal(assets[0])
	APIstub.PutState("Vegetable", assetAsBytes)
	assetAsBytes, _ = json.Marshal(assets[1])
	APIstub.PutState("Mineral", assetAsBytes)
	assetAsBytes, _ = json.Marshal(assets[2])
	APIstub.PutState("Meat", assetAsBytes)
	assetAsBytes, _ = json.Marshal(assets[3])
	APIstub.PutState("Grain", assetAsBytes)
	assetAsBytes, _ = json.Marshal(assets[4])
	APIstub.PutState("Fruit", assetAsBytes)

	/*i := 0
	 for i < len(assets) {
		 fmt.Println("i is ", i)
		 assetAsBytes, _ := json.Marshal(assets[i])
		 APIstub.PutState("UserAsset"+strconv.Itoa(i), assetAsBytes)
		 fmt.Println("Added", assets[i])
		 i = i + 1
	 }*/

	return shim.Success(nil)
}

func (s *SmartContract) increaseQuantity(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	wholeAssetAsBytes, _ := APIstub.GetState(args[0])
	wholeAsset := RetailAsset{}
	json.Unmarshal(wholeAssetAsBytes, &wholeAsset)
	blockquantity, err := strconv.Atoi(wholeAsset.Quantity)
	checkError(err)
	argquantity, err := strconv.Atoi(args[1])
	checkError(err)

	blockquantity = blockquantity + argquantity

	wholeAsset.Quantity = strconv.Itoa(blockquantity)

	wholeAssetAsBytes, _ = json.Marshal(wholeAsset)
	APIstub.PutState(args[0], wholeAssetAsBytes)

	return shim.Success(nil)

}

func (s *SmartContract) decreaseQuantity(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	wholeAssetAsBytes, _ := APIstub.GetState(args[0])
	wholeAsset := RetailAsset{}
	json.Unmarshal(wholeAssetAsBytes, &wholeAsset)
	blockquantity, err := strconv.Atoi(wholeAsset.Quantity)
	checkError(err)
	argquantity, err := strconv.Atoi(args[1])
	checkError(err)

	blockquantity = blockquantity - argquantity

	wholeAsset.Quantity = strconv.Itoa(blockquantity)

	wholeAssetAsBytes, _ = json.Marshal(wholeAsset)
	APIstub.PutState(args[0], wholeAssetAsBytes)

	return shim.Success(nil)

}

func (s *SmartContract) increaseMarketPrice(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	wholeAssetAsBytes, _ := APIstub.GetState(args[0])
	wholeAsset := RetailAsset{}
	json.Unmarshal(wholeAssetAsBytes, &wholeAsset)
	blockMarketPrice, err := strconv.Atoi(wholeAsset.MarketPrice)
	checkError(err)
	argMarketPrice, err := strconv.Atoi(args[1])
	checkError(err)

	blockMarketPrice = blockMarketPrice + argMarketPrice

	wholeAsset.MarketPrice = strconv.Itoa(blockMarketPrice)

	wholeAssetAsBytes, _ = json.Marshal(wholeAsset)
	APIstub.PutState(args[0], wholeAssetAsBytes)

	return shim.Success(nil)

}

func (s *SmartContract) decreaseMarketPrice(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	wholeAssetAsBytes, _ := APIstub.GetState(args[0])
	wholeAsset := RetailAsset{}
	json.Unmarshal(wholeAssetAsBytes, &wholeAsset)
	blockMarketPrice, err := strconv.Atoi(wholeAsset.MarketPrice)
	checkError(err)
	argMarketPrice, err := strconv.Atoi(args[1])
	checkError(err)

	blockMarketPrice = blockMarketPrice + argMarketPrice

	wholeAsset.MarketPrice = strconv.Itoa(blockMarketPrice)

	wholeAssetAsBytes, _ = json.Marshal(wholeAsset)
	APIstub.PutState(args[0], wholeAssetAsBytes)

	return shim.Success(nil)

}

func (s *SmartContract) assetInfoHistory(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	key := args[0]
	keysIter, err := APIstub.GetHistoryForKey(key)

	if err != nil {
		return shim.Error(fmt.Sprintf("Query operation failed. Error accessing state: %s", err))
	}
	defer keysIter.Close()
	var keys []string
	for keysIter.HasNext() {
		response, iterErr := keysIter.Next()
		if iterErr != nil {
			return shim.Error(fmt.Sprintf("Query operation failed. Error accessing state: %s", err))
		}
		keys = append(keys, response.TxId, string(response.Value))
	}

	for key, txID := range keys {
		fmt.Printf("key %d contains %s\n", key, txID)
	}
	jsonKeys, err := json.Marshal(keys)
	if err != nil {
		return shim.Error(fmt.Sprintf("Query operation faild. Error marshaling JSON: %s", err))
	}
	return shim.Success(jsonKeys)
}

// The main function is only relevant in unit test mode. Only included here for completeness.
func main() {

	// Create a new Smart Contract
	err := shim.Start(new(SmartContract))
	if err != nil {
		fmt.Printf("Error creating new Smart Contract: %s", err)
	}
}
func checkError(err error) {
	if err != nil {
		fmt.Println("Fatal Error", err.Error())
		os.Exit(1)
	}
}

/*func (s *SmartContract) queryBatting(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

		  if len(args) != 1 {
			  return shim.Error("Incorrect number of arguments. Expecting 1")
		  }

		  battingAsBytes, _ := APIstub.GetState(args[0])
		  return shim.Success(battingAsBytes)

	  if len(args) < 1 {
		  return shim.Error("Incorrect number of arguments. Expecting 1")
	  }

	  queryString := args[0]

	  queryResults, err := getQueryResultForQueryString(APIstub, queryString)
	  if err != nil {
		  return shim.Error(err.Error())
	  }
	  return shim.Success(queryResults)

  }*/
