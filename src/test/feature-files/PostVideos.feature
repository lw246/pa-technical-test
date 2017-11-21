Feature: POST Videos

  Scenario: POST a single video to the API with an empty database
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have no videos in the database
    When I make a POST to the "/video" endpoint with the song details in the body
    |Song      |Artist  |PublishDate|
    |8 (circle)|Bon Iver|2016-01-01 |
    Then The response code should be 201
    And The "artist" property in the response should be "Bon Iver"
    And The "song" property in the response should be "8 (circle)"
    And The "publishDate" property in the response should be "2016-01-01"

  Scenario: POST a single video the the API with a populated database
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have multiple videos in the database
    When I make a POST to the "/video" endpoint with the song details in the body
      |Song      |Artist  |PublishDate|
      |Message   |Boris   |2008-01-01 |
    Then The response code should be 201
    And The "artist" property in the response should be "Boris"
    And The "song" property in the response should be "Message"
    And The "publishDate" property in the response should be "2008-01-01"

  Scenario: POST multiple songs to the API]
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    And I have multiple videos in the database
    When I make a POST to the "/video" endpoint with the song details in the body
      |Song      |Artist  |PublishDate|
      |Buzz-In   |Boris   |2008-01-01 |
      |Shoot!    |Boris   |2008-01-01 |
      |Statement |Boris   |2008-01-01 |
    Then The response code should be 201
    And The returned JSON should contain a song with song property set to "Buzz-In"
    And The returned JSON should contain a song with song property set to "Shoot!"
    And The returned JSON should contain a song with song property set to "Statement"

  # Fails as this returns a response code of 500 - Discuss if 400 - bad request better
  Scenario: POST song with song property as integer
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    When I make a POST to the "/video" endpoint passing the "song" property as an "integer"
    |Song       |1234      |
    |Artist     |Deerhoof  |
    |PublishDate|2017-09-01|
    Then The response code should be 400

    # Fails as this returns a response code of 500 - Discuss if 400 - bad request better
  Scenario: POST song with song property as integer
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    When I make a POST to the "/video" endpoint passing the "artist" property as an "integer"
      |Song       |Ay That's Me|
      |Artist     |12345       |
      |PublishDate|2017-09-01  |
    Then The response code should be 400

    # Fails as this returns a response code of 500 - Discuss if 400 - bad request better
  Scenario: POST song with song property as integer
    Given I'm using the API on url "http://turing.niallbunting.com:3006/api"
    When I make a POST to the "/video" endpoint passing the "publishDate" property as an "integer"
      |Song       |Ay That's Me |
      |Artist     |Deerhoof     |
      |PublishDate|12456        |
    Then The response code should be 400

