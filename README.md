# Function Description

Implement a bank transaction management system. The main logic is user deposit and withdrawal records

/add         Create a transaction
/delete/{id} delete a transaction
/update      modify a transaction
/            list all transaction

# Getting Started

## Installation

Follow belows steps:
1. mvn package
2. chmod 777 deploy.sh
3. ./deploy.sh
4. access to http://<ServerIP>: 8080/  in Browser

## Module partitioning

1. controller：api service
2. service：Service layer, abstract implementation for external interfaces
3. model：model layer
4. common: Auxiliary functions

Unified logging of security levels through custom annotations and slicing

## Security

Unified logging of security levels through custom annotations and slicing：

1. Unified security log recording

## High concurrency

1. Realize high performance through frameworks and multi-threaded technology

## Dependent components

springboot web Related functions, basic framework

lombok ：simplified get and set

aop: aop

spring-boot-starter-thymeleaf：Writing pages using thymeleaf templates

# Test Design

## Unit Testing

Verify basic functionality through local unit testing

## Performance Testing

Verify the response speed and throughput of the system

## System Testing

none for demo
