Feature: add friends

    @add_friend
    Scenario: add a friend
    Given driver baseUrl + '/login'
    And input('#username', 'user1')
    And input('#password', 'aa')
    When submit().click(".form-signin button")
    Then waitForUrl(baseUrl + '/user/2')
    Given driver baseUrl + '/user/3'
    When submit().click("{button}send_friend_req")
    When submit().click("{button}logout")
    Then waitForUrl(baseUrl + '/login')
    And input('#username', 'user2')
    And input('#password', 'aa')
    When submit().click(".form-signin button")
    Then waitForUrl(baseUrl + '/user/3')
    When submit().click("{button}accept_friend_req")
    When submit().click("{button}logout")
    Then waitForUrl(baseUrl + '/login')

