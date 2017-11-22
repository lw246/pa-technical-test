Feature: Get a single video by ID

  Scenario: Get request passing video Id
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have videos in file "10Videos.json" in the database
    And I have acquired the Id for a song
    When I make a GET request to "/video"/SongId
    Then The response code should be 200
    And A single JSON object should be returned
    And The Songs ID should match that which was passed in the request

  Scenario: Getting a specific video returns all the properties expected
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have videos in file "10Videos.json" in the database
    And I have acquired the Id for a song
    When I make a GET request to "/video"/SongId
    Then The "_id" property should be present in the video JSON object returned
    And The "artist" property should be present in the video JSON object returned
    And The "song" property should be present in the video JSON object returned
    And The "publishDate" property should be present in the video JSON object returned
    And The "__v" property should be present in the video JSON object returned
    And The "date_created" property should be present in the video JSON object returned


  # This test fails due to the API returning a 500 error
  # The response in this situation would normally be opened up for debate - eg response of 404 or as below
  Scenario: Get request passing an invalid video Id
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have videos in file "10Videos.json" in the database
    When I make a GET request to "/video/bogussongid"
    Then The response code should be 400
    And The response body should be empty