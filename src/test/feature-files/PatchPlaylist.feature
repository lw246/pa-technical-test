Feature: Patch Playlist
  
  Scenario: Add a single video to a playlist returns the correct response
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have videos in file "10Videos.json" in the database
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And I have acquired the Id for a song
    And I have acquired the Id for a playlist
    When I make a PATCH "add" request to the "/playlist"/PlaylistId passing the song Id in the body
    Then The response code should be 204
    And The response should have no content

  Scenario: Add a single video to a playlist associates the video with the playlist
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have videos in file "10Videos.json" in the database
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And I have acquired the Id for a song
    And I have acquired the Id for a playlist
    And I make a PATCH "add" request to the "/playlist"/PlaylistId passing the song Id in the body
    When I make a GET request to "/playlist"/PlaylistId
    Then The video object in the playlist JSON should contain 1 videos
    And The video id should be the same as the video Id posted

  Scenario: Adding 4 videos to a playlist
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have videos in file "10Videos.json" in the database
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And I have acquired the Id for a playlist
    And I make a patch request passing in 4 video Ids
    When I make a GET request to "/playlist"/PlaylistId
    And The video object in the playlist JSON should contain 4 videos

  Scenario: Try to remove a video from a playlist
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have videos in file "10Videos.json" in the database
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And The playlists have 2 videos each
    And I have acquired the Id for a playlist
    And I have acquired the Id for a video in the playlist
    When I make a PATCH "remove" request to the "/playlist"/PlaylistId passing the song Id in the body
    Then The response code should be 501
    And The response body should read "Not implemented."

  Scenario: Try to add an invalid Video Id to a playlist
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have videos in file "10Videos.json" in the database
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And I have acquired the Id for a playlist
    When I make a PATCH "add" request to the "/playlist"/PlaylistId passing videoId of "invalidId"
    Then The response code should be 400