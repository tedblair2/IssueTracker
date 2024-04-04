# IssueTracker
This is a Github issue tracker application that allows a user to track their issues in various repositories. Using GitHub graphql API, the app gathers
a list of issues of a particular user. The app consists of the following screens:

![1(1)](https://github.com/tedblair2/IssueTracker/assets/39332527/89104e83-9864-4f94-9df1-4414d3d7d0d8),  ![2](https://github.com/tedblair2/IssueTracker/assets/39332527/c1720194-08fe-430f-a0b1-bbfd6cbdfdd3),  ![3](https://github.com/tedblair2/IssueTracker/assets/39332527/90008c96-55e0-407a-9c65-c3f2a676f300),  ![4](https://github.com/tedblair2/IssueTracker/assets/39332527/ceb586a4-1ae8-4110-8532-2bb21baa00de)

To authenticate requests to the GitHub graphql API, users are authenticated using Firebase authentication with the GitHub provider which generates an access_token that is used to send requests to the GitHub graphql API. The scope of the access_token is of "repo" to allow requests to issues for a user's repositories.

The application also allows a user to filter the issues based on the date created, labels, repository name, and the state of the issue. It allows multiple filters
allowing for better filtering of a user's issues.

The details screen shows more details for a particular issue including the date created, description, and the comments under each issue.

Finally, the profile screen shows the current login user details and provides a logout button. 

The application uses the MVVM architecture and in order to show a user's issues cursor-based pagination is also employed. 
