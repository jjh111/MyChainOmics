pragma solidity ^0.4.0;
contract CareChainContract{
    address creator;
    string greeting;
    uint public uniquePatientId;

    function CareChainContract(string _greeting){
        greeting = _greeting;
        creator = msg.sender;
    }

    function greet() constant returns (string){
        return greeting;
    }

    function setGreeting(string _greeting){
        greeting = _greeting;
    }

    function getUniquePatientId() public returns (uint) {
        //Generates a unique id everytime the function is called
        return uniquePatientId++;
    }

}