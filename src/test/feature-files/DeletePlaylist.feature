Feature: Delete Playlist

  Scenario: Delete a playlist that exists and doesn't videos
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And I have acquired the Id for a playlist
    When I make a DELETE request to "/playlist"/PlaylistId
    Then The response code should be 204
    
  Scenario: Delete a playlist that exists and has videos
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And The playlists have 3 videos each
    And I have acquired the Id for a playlist
    When I make a DELETE request to "/playlist"/PlaylistId
    Then The response code should be 204

  # Oddly the server doesn't respond to this GET request.
  # This causes the test to fail
  Scenario: Shouldn't be able to retrieve a deleted playlist by Id
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And I have acquired the Id for a playlist
    And I make a DELETE request to "/playlist"/PlaylistId
    When I make a GET request to "/playlist"/PlaylistId
    Then The response code should be 200

  # This test has the same situation as above.
  Scenario: Delete request to playlist method with an invalid Id
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    When I make a DELETE request to "/playlist/invalidid"
    Then The response code should be 400