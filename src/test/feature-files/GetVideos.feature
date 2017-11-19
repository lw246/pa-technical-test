Feature: Get List of Songs

  Scenario: List songs with nothing in the database
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have no videos in the database
    When I make a GET request to "/video"
    Then An Empty JSON array is returned
    And The response code should be 200

  Scenario: List videos with single video in the database
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have a single video in the database
    When I make a GET request to "/video"
    Then A JSON Array should be returned with a single result
    And The response code should be 200

  Scenario: List videos with multiple videos in the database
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have multiple videos in the database
    When I make a GET request to "/video"
    Then A JSON Array should be returned with multiple results
    And The response code should be 200

  Scenario: Get request passing song Id
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have multiple videos in the database
    And I have acquired the Id for a song
    When I make a GET request to "/video"/SongId
    Then A single JSON object should be returned
    And The Songs ID should match that which was passed in the request