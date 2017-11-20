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

  Scenario: The song JSON object contains the correct properties
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have multiple videos in the database
    When I make a GET request to "/video"
    Then The "_id" property should be present in all the JSON objects returned
    And The "song" property should be present in all the JSON objects returned
    And The "artist" property should be present in all the JSON objects returned
    And The "publishDate" property should be present in all the JSON objects returned
    And The "__v" property should be present in all the JSON objects returned
    And The "date_created" property should be present in all the JSON objects returned