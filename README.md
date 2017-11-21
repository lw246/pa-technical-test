# pa-technical-test
Technical test for the Press Association

Cucumber test project for a small Music Player RESTful API


Current issues I have with this project:

- The API is being used to add test data. This isn't ideal as any issues with POST commands would cause tests to fail.
  I'd prefer to add the test data directly into the database instead.
- I'm not 100% sure on the project structure at the moment due to not having much experience with Java projects
- I don't believe I'm currently following Java naming conventions, need to read up on these and refactor as appropriate
- Multiple assertions in test cases (Multiple Then statements in the .feature files)
- Assertion on the PATCH video body text. The body has \n in the response, however I can't get assertEquals to account for this.
Added line below to work around this (not all that happy with this approach
    expectedBodyText += "\n";


Current issues discovered:

- GET Video/{ID} returns 500 error if the ID doesn't exist. It also returns the error message in the body
- POST Video has no data validation on the json object. Throws 500 errors instead of handling and returning a nice response