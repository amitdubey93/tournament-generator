function openModal(button) {
      const matchId = button.getAttribute("data-matchid");
      const tourId = button.getAttribute("data-tourid");
      const matchNo = button.getAttribute("data-matchno");
      const roundNo = button.getAttribute("data-roundno");
      const playerOneImagePath = button.getAttribute("data-playeroneimagepath");
      const playerTwoImagePath = button.getAttribute("data-playertwoimagepath");
      const playerOneId = button.getAttribute("data-playeroneid");
      const playerTwoId = button.getAttribute("data-playertwoid");
      const playerOneScore = button.getAttribute("data-playeronescore");
      const playerTwoScore = button.getAttribute("data-playertwoscore");

      //console.log(matchId+" "+playerOne);

      document.getElementById("matchId").value = matchId;
      document.getElementById("tourId").value = tourId;
      document.getElementById("matchNo").value = matchNo;
      document.getElementById("matchNo1").innerHTML = matchNo;
      document.getElementById("roundNo").value = roundNo;
      document.getElementById("playerOneImagePath").src = playerOneImagePath;
      document.getElementById("playerTwoImagePath").src = playerTwoImagePath;
      document.getElementById("playerOneId").value = playerOneId;
      document.getElementById("playerTwoId").value = playerTwoId;
      document.getElementById("playerOneScore").value = playerOneScore;
      document.getElementById("playerTwoScore").value = playerTwoScore;

      console.log(playerOneId);
      const updateModal = new bootstrap.Modal(document.getElementById('updateModal'));
      updateModal.show();
  }