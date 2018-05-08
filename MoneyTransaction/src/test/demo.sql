DROP TABLE ACCOUNT;
CREATE TABLE Account (AccountId INT PRIMARY KEY NOT NULL, 
BalanceAmount DECIMAL(19,4));
INSERT INTO Account (AccountId, BalanceAmount) VALUES (123, 100.0000);

INSERT INTO Account (AccountId, BalanceAmount) VALUES (234, 200.0000);

INSERT INTO Account (AccountId, BalanceAmount) VALUES (345, 500.0000);

INSERT INTO Account (AccountId, BalanceAmount) VALUES (222, 500.0000);

INSERT INTO Account (AccountId,BalanceAmount) VALUES (101, 500.0000);

INSERT INTO Account (AccountId, BalanceAmount) VALUES (201, 500.0000);