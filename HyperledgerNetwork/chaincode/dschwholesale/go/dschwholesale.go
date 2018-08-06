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
	"strconv"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	sc "github.com/hyperledger/fabric/protos/peer"
)

// Define the Smart Contract structure
type SmartContract struct {
}

// Define the batting structure, with 4 properties.  Structure tags are used by encoding/json library
type HorseRacingBatting struct {
	Voter      string `json:"voter"`
	RacingTime string `json:"racingTime"`
	HorseName  string `json:"horseName"`
	Cost       string `json:"cost"`
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
	} else if function == "createBatting" {
		return s.createBatting(APIstub, args)
	} else if function == "queryBattingWithKey" {
		return s.queryBattingWithKey(APIstub, args)
	} else if function == "queryBatting" {
		return s.queryBatting(APIstub, args)
	} else if function == "invokeBatting" {
		return s.invokeBatting(APIstub, args)
	}

	return shim.Error("Invalid Smart Contract function name.")
}

func (s *SmartContract) queryBattingWithKey(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	battingAsBytes, _ := APIstub.GetState(args[0])
	return shim.Success(battingAsBytes)
}

func getQueryResultForQueryString(stub shim.ChaincodeStubInterface, queryString string) ([]byte, error) {

	fmt.Printf("- getQueryResultForQueryString queryString:\n%s\n", queryString)

	resultsIterator, err := stub.GetQueryResult(queryString)
	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryRecords
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
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

	fmt.Printf("- getQueryResultForQueryString queryResult:\n%s\n", buffer.String())

	return buffer.Bytes(), nil
}

func (s *SmartContract) queryBatting(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	/*	if len(args) != 1 {
			return shim.Error("Incorrect number of arguments. Expecting 1")
		}

		battingAsBytes, _ := APIstub.GetState(args[0])
		return shim.Success(battingAsBytes)
	*/
	if len(args) < 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	queryString := args[0]

	queryResults, err := getQueryResultForQueryString(APIstub, queryString)
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(queryResults)

}

func (s *SmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {
	battings := []HorseRacingBatting{
		HorseRacingBatting{Voter: "Prius(010-1231-2222)", RacingTime: "2018-07-17 12:00", HorseName: "팔도대장", Cost: "1000"},
		HorseRacingBatting{Voter: "Mustang(010-1231-2223)", RacingTime: "2018-07-17 12:00", HorseName: "욜로", Cost: "1000"},
		HorseRacingBatting{Voter: "Tucson(010-1231-2224)", RacingTime: "2018-07-17 12:00", HorseName: "좋은나들", Cost: "1000"},
		HorseRacingBatting{Voter: "Passat(010-1231-2225)", RacingTime: "2018-07-17 12:00", HorseName: "스페셜위", Cost: "1000"},
	}

	i := 0
	for i < len(battings) {
		fmt.Println("i is ", i)
		battingAsBytes, _ := json.Marshal(battings[i])
		APIstub.PutState("HorseRacingBatting"+strconv.Itoa(i), battingAsBytes)
		fmt.Println("Added", battings[i])
		i = i + 1
	}

	return shim.Success(nil)
}

func (s *SmartContract) createBatting(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting 5")
	}

	var batting = HorseRacingBatting{Voter: args[1], RacingTime: args[2], HorseName: args[3], Cost: args[4]}

	battingAsBytes, _ := json.Marshal(batting)
	APIstub.PutState(args[0], battingAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) invokeBatting(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	battingAsBytes, _ := APIstub.GetState(args[0]) //args[0] : key
	var updateJsonBytes = []byte(args[1])

	batting := HorseRacingBatting{}

	json.Unmarshal(battingAsBytes, &batting)
	json.Unmarshal(updateJsonBytes, &batting) //변경된 내용만 반영, args[1] : updateJsonStr
	//	batting.Voter = args[1]
	//	batting.RacingTime = args[2]
	//	batting.HorseName = args[3]
	//	batting.Cost = args[4]

	battingAsBytes, _ = json.Marshal(batting)
	APIstub.PutState(args[0], battingAsBytes)

	return shim.Success(nil)
}

// The main function is only relevant in unit test mode. Only included here for completeness.
func main() {

	// Create a new Smart Contract
	err := shim.Start(new(SmartContract))
	if err != nil {
		fmt.Printf("Error creating new Smart Contract: %s", err)
	}
}
