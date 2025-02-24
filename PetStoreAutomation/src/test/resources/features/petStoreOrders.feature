#Autor: Melissa Paternina
@petStoreOrders
Feature: Pet store order management
  As a pet store owner,
  I want to manage store orders,
  To keep track of orders.

  Background:
    Given that the pet store is open and ready to process orders

    @CleanupOrders @createOrder
    Scenario Outline: Successfully place a new order
      When I place a new pet order with id "<id>" and status "<status>"
      Then the order must be confirmed and saved in the system
      Examples:
        | status    | id |
        | approved  | 10 |
        | placed    | 11 |
        | delivered | 12 |

    @CleanupOrders @verifyInventory
    Scenario Outline: Check inventory changes when placing a new order
      Given that I check the current status of the pet store inventory
      When I place a new pet order with id "<id>" and status "<status>"
      Then the inventory count for status "<status>" must be updated accordingly
      Examples:
        | status    | id |
        | approved  | 13 |
        | placed    | 14 |
        | delivered | 15 |

    @deleteOrder
    Scenario Outline: Successfully remove an existing order
      When I place a new pet order with id "<id>" and status "<status>"
      And I delete the order I just created
      Then the order must no longer exist in the system
      Examples:
        | status    | id |
        | approved  | 16 |
        | placed    | 17 |
        | delivered | 18 |

    @verifyInventory
    Scenario Outline: Check inventory changes when removing an existing order
      Given I place a new pet order with id "<id>" and status "<status>"
      When that I check the current status of the pet store inventory
      And I delete the order I just created
      Then the inventory count for status "<status>" must be updated accordingly
      Examples:
        | status    | id |
        | approved  | 19 |
        | placed    | 20 |
        | delivered | 21 |

    @CleanupOrders @createOrder @alternativeCase
    Scenario: Attempt to place a new order with invalid data
      When I attempt to place a new pet order with invalid data
      Then the order must not be created

    @deleteOrder @alternativeCase
    Scenario: Attempt to remove a non-existent order
      When I attempt to delete an non-existent order
      Then the system must return that the order was not found

