Feature: Delete video

  Scenario: Delete a video that exists in the database
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have multiple videos in the database
    And I have acquired the Id for a song
    When I make a DELETE request to "/video"/SongId
    Then The response code should be 204

  # This will throw a 500 error in the APIs current form so test will fail.
  Scenario: Shouldn't be able to retrieve a deleted video by Id
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have multiple videos in the database
    And I have acquired the Id for a song
    And I make a DELETE request to "/video"/SongId
    When I make a GET request to "/video"/SongId
    Then The response code should be 200
    And The response body should read "null"
