Feature: Get Playlist by Id

  Scenario: Get a playlist without videos passing playlist Id
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And I have acquired the Id for a playlist
    When I make a GET request to "/playlist"/PlaylistId
    Then The response code should be 200
    And A single playlist JSON object should be returned
    And The playlists Id should match that which was passed in the request

  Scenario: Get a playlist without videos passing playlist Id
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And I have acquired the Id for a playlist
    When I make a GET request to "/playlist"/PlaylistId
    Then The "_id" property should be present in the JSON object returned
    And The "desc" property should be present in the JSON object returned
    And The "title" property should be present in the JSON object returned
    And The "date_created" property should be present in the JSON object returned
    And The "__v" property should be present in the JSON object returned

  Scenario: Get a playlist with videos passing playlist Id has correct properties
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And I have videos in file "10Videos.json" in the database
    And The playlists have 5 videos each
    And I have acquired the Id for a playlist
    When I make a GET request to "/playlist"/PlaylistId
    Then The "_id" property should be present in the JSON object returned
    And The "desc" property should be present in the JSON object returned
    And The "title" property should be present in the JSON object returned
    And The "date_created" property should be present in the JSON object returned
    And The "__v" property should be present in the JSON object returned
    And The "videos" property should be present in the JSON object returned

  Scenario: Get a playlist with videos passing playlist Id has correct number of videos
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And I have videos in file "10Videos.json" in the database
    And The playlists have 5 videos each
    And I have acquired the Id for a playlist
    When I make a GET request to "/playlist"/PlaylistId
    Then The video object in the playlist JSON should contain 5 videos

  # Failing test due to the API returning a 500 error
  Scenario: Try to get an invalid playlist Id
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    When I make a GET request to "/playlist/invalidId"
    Then The response code should be 400
    And The response body should be empty