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
type UserAsset struct {
	UserName     string `json:"userName"`
	HasMoney     string `json:"hasMoney"`
	HasVegetable string `json:"hasVegetable"`
	HasMineral   string `json:"hasMineral"`
	HasMeat      string `json:"hasMeat"`
	HasGrain     string `json:"hasGrain"`
	HasFruit     string `json:"hasFruit"`
	BoxVegetable string `json:"boxVegetable"`
	BoxMineral   string `json:"boxMineral"`
	BoxMeat      string `json:"boxMeat"`
	BoxGrain     string `json:"boxGrain"`
	BoxFruit     string `json:"boxFruit"`
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
	} else if function == "registUserAsset" {
		return s.registUserAsset(APIstub, args)
	} else if function == "queryAssetWithKey" {
		return s.queryAssetWithKey(APIstub, args)
	} else if function == "queryAllUserList" {
		return s.queryAllUserList(APIstub)
	} else if function == "buyingAsset" {
		return s.buyingAsset(APIstub, args)
	} else if function == "sellingAsset" {
		return s.sellingAsset(APIstub, args)
	} else if function == "userInfoHistory" {
		return s.userInfoHistory(APIstub, args)
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

func (s *SmartContract) queryAllUserList(APIstub shim.ChaincodeStubInterface) sc.Response {

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

	fmt.Printf("- queryAllqueryAllUserList:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())
}

func (s *SmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {
	/*
			UserName     string `json:"userName"`
			HasMoney     string `json:"hasMoney"`
			HasVegetable string `json:"hasVegetable"`
			HasMineral   string `json:"hasMineral"`
			HasMeat      string `json:"hasMeat"`
			HasGrain     string `json:"hasGrain"`
			HasFruit     string `json:"hasFruit"`
			BoxVegetable string `json:"boxVegetable"`
		    BoxMineral   string `json:"boxMineral"`
		    BoxMeat      string `json:"boxMeat"`
		    BoxGrain     string `json:"boxGrain"`
		    BoxFruit     string `json:"boxFruit"
	*/
	assets := []UserAsset{
		UserAsset{UserName: "JungHyunAn", HasMoney: "781122", HasVegetable: "701", HasMineral: "468", HasMeat: "972", HasGrain: "761", HasFruit: "669", BoxVegetable: "7", BoxMineral: "46", BoxMeat: "97", BoxGrain: "76", BoxFruit: "66"},
		UserAsset{UserName: "Chathy M. kinney", HasMoney: "77234", HasVegetable: "7671", HasMineral: "52", HasMeat: "400", HasGrain: "101", HasFruit: "670", BoxVegetable: "76", BoxMineral: "5", BoxMeat: "4", BoxGrain: "10", BoxFruit: "67"},
		UserAsset{UserName: "Donald C. Lupien", HasMoney: "97012", HasVegetable: "5541", HasMineral: "592", HasMeat: "575", HasGrain: "400", HasFruit: "391", BoxVegetable: "55", BoxMineral: "59", BoxMeat: "57", BoxGrain: "40", BoxFruit: "39"},
		UserAsset{UserName: "KristaC. Bruner", HasMoney: "61442", HasVegetable: "3411", HasMineral: "120", HasMeat: "952", HasGrain: "100", HasFruit: "568", BoxVegetable: "34", BoxMineral: "12", BoxMeat: "95", BoxGrain: "10", BoxFruit: "56"},
		UserAsset{UserName: "Katrina J. Kiss", HasMoney: "54611", HasVegetable: "1711", HasMineral: "487", HasMeat: "166", HasGrain: "931", HasFruit: "830", BoxVegetable: "17", BoxMineral: "48", BoxMeat: "16", BoxGrain: "93", BoxFruit: "83"},
	}

	i := 0
	for i < len(assets) {
		fmt.Println("i is ", i)
		assetAsBytes, _ := json.Marshal(assets[i])
		APIstub.PutState("UserAsset"+strconv.Itoa(i), assetAsBytes)
		fmt.Println("Added", assets[i])
		i = i + 1
	}

	return shim.Success(nil)
}

func (s *SmartContract) registUserAsset(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 13 {
		return shim.Error("Incorrect number of arguments. Expecting 13")
	}

	var asset = UserAsset{UserName: args[1], HasMoney: args[2], HasVegetable: args[3], HasMineral: args[4], HasMeat: args[5], HasGrain: args[6], HasFruit: args[7], BoxVegetable: args[8], BoxMineral: args[9], BoxMeat: args[10], BoxGrain: args[11], BoxFruit: args[12]}

	assetAsBytes, _ := json.Marshal(asset)
	APIstub.PutState(args[0], assetAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) buyingAsset(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 4 { //[0] -> Key, [1] -> AssetName, [2]->Asset Quantity, [3]->Money
		return shim.Error("Incorrect number of arguments. Expecting 4")
	}

	assetAsBytes, _ := APIstub.GetState(args[0])
	userAsset := UserAsset{}

	json.Unmarshal(assetAsBytes, &userAsset)

	userMoney, err := strconv.Atoi(userAsset.HasMoney)
	checkError(err)
	assetName := args[1]
	switch {
	case assetName == "Vegetable":
		assetVegetable, err := strconv.Atoi(userAsset.HasVegetable)
		checkError(err)
		assetVegeArg, err := strconv.Atoi(args[2])
		checkError(err)

		assetVegetable = assetVegetable + assetVegeArg
		userAsset.HasVegetable = strconv.Itoa(assetVegetable)

	case assetName == "Mineral":
		assetMineral, err := strconv.Atoi(userAsset.HasMineral)
		checkError(err)
		assetMineralArg, err := strconv.Atoi(args[2])
		checkError(err)
		assetMineral = assetMineral + assetMineralArg

		userAsset.HasMineral = strconv.Itoa(assetMineral)

	case assetName == "Meat":
		assetMeat, err := strconv.Atoi(userAsset.HasMeat)
		checkError(err)
		assetMeatArg, err := strconv.Atoi(args[2])
		checkError(err)

		assetMeat = assetMeat + assetMeatArg
		userAsset.HasMeat = strconv.Itoa(assetMeat)

	case assetName == "Grain":
		assetGrain, err := strconv.Atoi(userAsset.HasGrain)
		checkError(err)
		assetGrainArg, err := strconv.Atoi(args[2])
		checkError(err)

		assetGrain = assetGrain + assetGrainArg
		userAsset.HasGrain = strconv.Itoa(assetGrain)

	case assetName == "Fruit":
		assetFruit, err := strconv.Atoi(userAsset.HasFruit)
		checkError(err)
		assetFruitArg, err := strconv.Atoi(args[2])
		checkError(err)

		assetFruit = assetFruit + assetFruitArg
		userAsset.HasFruit = strconv.Itoa(assetFruit)

	default:
		panic("unrecognized asset name")
	}
	userMoneyArg, err := strconv.Atoi(args[3])
	checkError(err)

	userMoney = userMoney - userMoneyArg

	userAsset.HasMoney = strconv.Itoa(userMoney)

	assetAsBytes, _ = json.Marshal(userAsset)
	APIstub.PutState(args[0], assetAsBytes)

	return shim.Success(nil)

}

func (s *SmartContract) sellingAsset(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 4 { //[0] -> Key, [1] -> AssetName, [2]->Asset Quantity, [3]->Money
		return shim.Error("Incorrect number of arguments. Expecting 4")
	}

	assetAsBytes, _ := APIstub.GetState(args[0])
	userAsset := UserAsset{}

	json.Unmarshal(assetAsBytes, &userAsset)

	userMoney, err := strconv.Atoi(userAsset.HasMoney)
	checkError(err)
	assetName := args[1]
	switch {
	case assetName == "Vegetable":
		assetVegetable, err := strconv.Atoi(userAsset.BoxVegetable)
		checkError(err)
		assetVegeArg, err := strconv.Atoi(args[2])
		checkError(err)

		assetVegetable = assetVegetable - assetVegeArg
		userAsset.BoxVegetable = strconv.Itoa(assetVegetable)

	case assetName == "Mineral":
		assetMineral, err := strconv.Atoi(userAsset.BoxMineral)
		checkError(err)
		assetMineralArg, err := strconv.Atoi(args[2])
		checkError(err)
		assetMineral = assetMineral - assetMineralArg

		userAsset.BoxMineral = strconv.Itoa(assetMineral)

	case assetName == "Meat":
		assetMeat, err := strconv.Atoi(userAsset.BoxMeat)
		checkError(err)
		assetMeatArg, err := strconv.Atoi(args[2])
		checkError(err)

		assetMeat = assetMeat - assetMeatArg
		userAsset.BoxMeat = strconv.Itoa(assetMeat)

	case assetName == "Grain":
		assetGrain, err := strconv.Atoi(userAsset.BoxGrain)
		checkError(err)
		assetGrainArg, err := strconv.Atoi(args[2])
		checkError(err)

		assetGrain = assetGrain - assetGrainArg
		userAsset.BoxGrain = strconv.Itoa(assetGrain)

	case assetName == "Fruit":
		assetFruit, err := strconv.Atoi(userAsset.BoxFruit)
		checkError(err)
		assetFruitArg, err := strconv.Atoi(args[2])
		checkError(err)

		assetFruit = assetFruit - assetFruitArg
		userAsset.BoxFruit = strconv.Itoa(assetFruit)

	default:
		panic("unrecognized asset name")
	}
	userMoneyArg, err := strconv.Atoi(args[3])
	checkError(err)
	userMoney = userMoney + userMoneyArg

	userAsset.HasMoney = strconv.Itoa(userMoney)

	assetAsBytes, _ = json.Marshal(userAsset)
	APIstub.PutState(args[0], assetAsBytes)

	return shim.Success(nil)

}
func (s *SmartContract) userInfoHistory(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
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
