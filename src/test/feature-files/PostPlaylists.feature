Feature: POST Playlist

  Scenario: Post single playlist without any videos with an empty database
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have no playlists in the database
    When I make a POST to the "/playlist" method with playlist details in the body
    |Title          |Desc                            |
    |Single Playlist|A Single playlist to test POST playlist|
    Then The response code should be 201
    And The "title" property in the playlist JSON response should be "Single Playlist"
    And The "desc" property in the playlist JSON response should be "A Single playlist to test POST playlist"

  Scenario: Post playlist without and videos with an empty database
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have no playlists in the database
    When I make a POST to the "/playlist" method with playlist details in the body
      |Title    |Desc        |
      |Playlist1|Playlist One|
      |Playlist2|Playlist Two|
    Then The response code should be 201
    And The Playlist JSON response should contain a playlist with the "title" property set to "Playlist1"
    And The Playlist JSON response should contain a playlist with the "title" property set to "Playlist1"
    And The Playlist JSON response should contain a playlist with the "desc" property set to "Playlist One"
    And The Playlist JSON response should contain a playlist with the "desc" property set to "Playlist Two"

