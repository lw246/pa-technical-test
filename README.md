# pa-technical-test
Technical test for the Press Association

Cucumber test project for a small Music Player REST API

It should be noted that a number of the tests currently fail.
I have left them like this deliberately due to the reasons for failure not being an issue with the test, but due to unexpected/undesirable responses from the APi

Current issues I have with this project:

- The API is being used to add test data. This isn't ideal as any issues with POST commands would cause tests to fail.
  I'd prefer to add the test data directly into the database instead.
- I don't believe I'm currently following Java naming conventions, need to read up on these and refactor as appropriate
- Assertion on the PATCH video body text. The body has \n in the response, however I can't get assertEquals to account for this.
Added line below to work around this (not all that happy with this approach
    expectedBodyText += "\n";


Current issues discovered:

- GET Video/{ID} returns 500 error if an invalid ID is passed. It also returns the error message in the body
- POST Video has no data validation on the json object. Throws 500 errors instead of handling and returning a nice response
- GET Video and GET Playlist behave different if try to retrieve a video/playlist that has been deleted
GET Video - Comes back with a 200 response but no content (though Postman doesn't get this...)
GET Playlist - Doesn't receive a response at all.
- PATCH playlist returns a 500 response code if an invalid video Id is passed