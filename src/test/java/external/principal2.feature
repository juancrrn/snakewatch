Feature: remove friends
    @remove_friend
    Scenario: remove a friend
    Given driver baseUrl + '/login'
    And input('#username', 'user1')
    And input('#password', 'aa')
    * click(".form-signin button")
    * waitForUrl(baseUrl + '/user/2')
    Given driver baseUrl + '/user/4'
    * click("button[id=friendship-finish-button]")
    * click("button[id=logout]")
    * waitForUrl(baseUrl + '/login') 
    And input('#username', 'user3')
    And input('#password', 'aa')
    * click(".form-signin button")
    * waitForUrl(baseUrl + '/user/4')
    Given driver baseUrl + '/user/2'
    * click("button[id=friendship-send-button]")
