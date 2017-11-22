Feature: GET Playlist

  Scenario: List Playlists with nothing in the database
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have no playlists in the database
    When I make a GET request to "/playlist"
    Then The response body should read "[]"
    And The response code should be 200

  Scenario: List playlists with single playlist in the database without videos
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "SinglePlaylist-NoVideos.json" in the database
    When I make a GET request to "/playlist"
    Then A playlist JSON Array should be returned with 1 results
    And The response code should be 200

  Scenario: List playlists with multiple playlists in the database without videos
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    When I make a GET request to "/playlist"
    Then A playlist JSON Array should be returned with 10 results
    And The response code should be 200

  Scenario: The playlist JSON object without videos contains the correct properties
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    When I make a GET request to "/playlist"
    Then The "_id" property should be present in all the playlist JSON objects returned
    And The "desc" property should be present in all the playlist JSON objects returned
    And The "title" property should be present in all the playlist JSON objects returned
    And The "date_created" property should be present in all the playlist JSON objects returned
    And The "__v" property should be present in all the playlist JSON objects returned

  Scenario: List playlists with single playlist in the database with videos
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "SinglePlaylist-NoVideos.json" in the database
    And I have videos in file "10Videos.json" in the database
    And The playlists have 5 videos each
    When I make a GET request to "/playlist"
    Then A JSON Array should be returned with a single result
    And The response code should be 200

  Scenario: List playlists with multiple playlists in the database with videos
    Given  I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And I have videos in file "10Videos.json" in the database
    And The playlists have 5 videos each
    When I make a GET request to "/playlist"
    Then A JSON Array should be returned with multiple results
    And The response code should be 200

  Scenario: The playlist JSON object with videos contains the correct properties
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have the playlists in file "10Playlists-NoVideos.json" in the database
    And I have videos in file "10Videos.json" in the database
    And The playlists have 5 videos each
    When I make a GET request to "/playlist"
    Then The "_id" property should be present in all the playlist JSON objects returned
    And The "desc" property should be present in all the playlist JSON objects returned
    And The "title" property should be present in all the playlist JSON objects returned
    And The "date_created" property should be present in all the playlist JSON objects returned
    And The "__v" property should be present in all the playlist JSON objects returned