Feature: Patch Video

  Scenario: Make a Patch request to a video endpoint specifying the Id
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have videos in file "10Videos.json" in the database
    And I have acquired the Id for a song
    When I make a PATCH request to the "/video"/SongId with the below data
      |Artist        |
      |The Mars Volta|
    Then The response code should be 501
    And The response body should read "Not implemented."
