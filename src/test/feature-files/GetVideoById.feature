Feature: Get a single song by ID

  Scenario: Get request passing song Id
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have multiple videos in the database
    And I have acquired the Id for a song
    When I make a GET request to "/video"/SongId
    Then The response code should be 200
    Then A single JSON object should be returned
    And The Songs ID should match that which was passed in the request

  Scenario: Getting a specific song returns all the properties expected
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have multiple videos in the database
    And I have acquired the Id for a song
    When I make a GET request to "/video"/SongId
    Then The "_id" property should be present in the JSON object returned
    And The "artist" property should be present in the JSON object returned
    And The "song" property should be present in the JSON object returned
    And The "publishDate" property should be present in the JSON object returned
    And The "__v" property should be present in the JSON object returned
    And The "date_created" property should be present in the JSON object returned


  # This test fails due to a bug in the API
  # The response in this situation would normally be opened up for debate - eg response of 404 or as below
  Scenario: Get request passing an invalid song Id
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have multiple videos in the database
    When I make a GET request to "/video/bogussongid"
    Then The response code should be 200
    And The response body should be empty