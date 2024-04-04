# IssueTracker
This is a Github issue tracker application that allows a user to track their issues in various repositories. Using GitHub graphql api, the app gathers
a list of issues of a particular user. The app consists of the following screens:

![1(1)](https://github.com/tedblair2/IssueTracker/assets/39332527/89104e83-9864-4f94-9df1-4414d3d7d0d8),  ![2](https://github.com/tedblair2/IssueTracker/assets/39332527/c1720194-08fe-430f-a0b1-bbfd6cbdfdd3),  ![3](https://github.com/tedblair2/IssueTracker/assets/39332527/90008c96-55e0-407a-9c65-c3f2a676f300),  ![4](https://github.com/tedblair2/IssueTracker/assets/39332527/ceb586a4-1ae8-4110-8532-2bb21baa00de)

To authenticate requests to the GitHub graphql API, users are authenticated using Firebase authentication with the GitHub provider which generates an access_token that is used to send requests to the GitHub graphql API.
