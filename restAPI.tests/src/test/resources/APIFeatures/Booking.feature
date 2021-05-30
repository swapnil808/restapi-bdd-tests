Feature: API Tests for Creating, Updating, Fetching, deleting Booking details
  and validating expected response and details are correct.

  Scenario Outline: Creation of New Booking
    Given I have details <firstName>, <lastName> and <additionalNeeds> to make a booking
    When I send Booking request
    Then I should see my booking got confirmed
    And I verify that my booking details are correct

    Examples: 
      | firstName | lastName | additionalNeeds |
      | Mike      | Russell  | Breakfast       |

  Scenario Outline: Amendment of Created Booking
    Given I am a Authenticated User
    And I have Booking Id
    When I send request to update booking details with <firstName>, <lastName> and <additionalNeeds>
    Then I should see my booking details got updated
    And I verify that my updated booking details are correct

    Examples: 
      | firstName | lastName | additionalNeeds |
      | John      | Wright   | Dinner          |

  Scenario: Getting a Booking by Id
    Given I have Booking Id
    When I send request to get my booking details
    Then I should see my booking details
    And I verify that details matches with booking details

  Scenario: Delete a Booking
    Given I am a Authenticated User
    And I have Booking Id
    When I send request to cancel my Booking
    Then I should see my booking is cancelled
    And I send request to get my booking details
    And I should not see my booking details
