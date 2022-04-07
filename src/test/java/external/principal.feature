Feature: add friends

    @add_friend
    Scenario: add a friend
    Given driver baseUrl + '/login'
    And input('#username', 'user1')
    And input('#password', 'aa')
    * click(".form-signin button")
    * waitForUrl(baseUrl + '/user/2')
    Given driver baseUrl + '/user/3'
    * click("button[id=friendship-send-button]")
    * click("button[id=logout]")
    * waitForUrl(baseUrl + '/login') 
    And input('#username', 'user2')
    And input('#password', 'aa')
    * click(".form-signin button")
    * waitForUrl(baseUrl + '/user/3')
    * click("button[id=accept_friend_req]")


