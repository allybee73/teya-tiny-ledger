# Teya tiny ledger

## Notes.

The list of test accounts is populated on application startup. Account Ids are UUIDs, the test account details are written to the console so they can be used in the REST api URLs. E.g.
```
=================== ACCOUNTS ===================
AccountId f7bbe20d-e7fe-4a78-86fb-451dd7b6f852 Account Details: Account(id=f7bbe20d-e7fe-4a78-86fb-451dd7b6f852, transactionList=[Transaction(reference=decdc857-b001-4b33-a2d2-f6cdbe27efca, type=DEPOSIT, amount=25.99, description=Transaction1), Transaction(reference=957fdd87-256f-4bda-91bb-effe12200f44, type=WITHDRAWAL, amount=10.99, description=Transaction2)])
AccountId 6e13a149-9b03-4bd6-a94e-b5167a386096 Account Details: Account(id=6e13a149-9b03-4bd6-a94e-b5167a386096, transactionList=[Transaction(reference=de1073f8-15ef-4df1-afd0-e596847a2b1a, type=DEPOSIT, amount=250.99, description=Transaction1), Transaction(reference=aa470235-af69-4076-b135-49209e62e694, type=WITHDRAWAL, amount=100.99, description=Transaction2)])
================================================
```

the application uses the Spring Boot default port - 8080.

## Endpoints

### Ability to record money movements (ie: deposits and withdrawals)
**POST** to **/api/v1/accounts/{account id}/transactions**

Example request JSON body. 
```
{
    "reference": "61666ab9-cd42-474e-a502-441eb06253c8",
    "type": "DEPOSIT",
    "currency": "GBP",
    "amount": 400.00,
    "description": "Transaction1"
}
```
reference should be a valid client provided UUID, this reference would be used for idempotency checks. 
This website will generate a valid UUID for demo purposes - https://www.uuidgenerator.net/

When the new transaction has been added the new transaction will be returned as the JSON response body. If the account id 
is not valid then a 404 response will be returned. 


### View current balance
**GET** to **/api/v1/accounts/{accouunt id}/balance**
Example response body JSON.
```
{
    "balance": 500.00
}
```

### View transaction history
**GET** to **/api/v1/accounts/{account id}/transactions**
Example response body JSON
```
{
    "transactions": [
        {
            "id": "97d91535-aa53-4a4c-a6b7-d89ae9b52ca4",
            "type": "DEPOSIT",
            "amount": 250.99,
            "description": "Transaction1"
        },
        {
            "id": "cdde3127-2f62-4e44-ae3f-e3c8c64e98b6",
            "type": "WITHDRAWAL",
            "amount": 100.99,
            "description": "Transaction2"
        }
    ]
}
```

### Assumptions. 
I have not considered currency in this demo application, for the purposes of this test I have assumed we are only supporting GBP.
