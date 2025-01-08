# Payment Service
The project includes two microservices: **AccountService** and **PaymentService**, designed to showcase two types of communication between services: via **REST** and **gRPC**.

---

## Description
- **AccountService** — manages user accounts and balances.
- **PaymentService** — processes payments and calls methods from **AccountService** to update balances.

The project demonstrates two communication approaches:
- **REST** 
- **gRPC**

---

## Main Objective
- The goal is to develop an API to compare the speed and performance of **REST** vs **gRPC**.

---

## Benchmarks
For performance evaluation, I used **Apache Benchmark**.

During testing with 1000 requests and 100 concurrent threads, I observed the following results:

In this benchmark, the PaymentService was tested, where the amount parameter in the request indicates the number of requests to be made between services. The higher the value of amount, the more requests occur behind the scenes, between services, without the user's direct involvement. In my test, I used a value of 5 for amount.

![](img.png)
