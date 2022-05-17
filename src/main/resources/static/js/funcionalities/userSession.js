function updateFriendship(id, action) {
  go("/friendship/" + action + "/" + id + "/", "POST").then((d) => {
    const fBlock = document.getElementById('friends-block');
    const card = document.getElementById("friend-card-" + id);

    switch (action) {
      case "accept_req":
        fBlock.appendChild(card);
        card.querySelector('#request-accept-button').style.display = 'none';
        card.querySelector('#request-reject-button').style.display = 'none';
        card.querySelector('#friendship-delete-button').style.display = '';
        break;
      case "reject_req":
      case "finish":
        card.remove();
        break;

      default:
        break;
    }
  }).catch((e) => {
    console.log("Error: ", e);
  })
}
