//hello
function celebrate(winnerName) {
  const firecrackerContainer = document.getElementById("firecrackerContainer");
  const audio = document.getElementById("firecrackerAudio");

  // Play the audio
  //audio.stop();
  audio.play();

  // Create and animate firecrackers
  //   var emojiList = ["🎉", "🤩", "🍦", "😵", "🎆", "🎊", "🎈", "🏴‍☠️", "♛", "🥳"];
  //   var emojiList = ["🎉", "🤩", "😍", "😵", "😁", "🥳", "🎈", "🤣", "😎", "🧐"];
  var emojiList = [
    "😇",
    "🤩",
    "😍",
    "😵",
    winnerName,
    "😁",
    "🥳",
    "🤣",
    "😎",
    winnerName,
    "🧐",
    "🥰",
    "😘",
    "😃",
    winnerName,
    "🤡",
    "🤑",
    "😏",
    "😋",
    winnerName
  ];
  for (let i = 0; i < 1000; i++) {
    setTimeout(() => {
      const firecracker = document.createElement("div");
      firecracker.classList.add("firecracker");
      //   firecracker.innerText = "🎉";
      firecracker.innerText = emojiList[i % 16];

      // Set random position
      firecracker.style.left = `${Math.random() * 100}%`;
      firecracker.style.top = `${Math.random() * 100}%`;

      // Append to container
      firecrackerContainer.appendChild(firecracker);

      // Remove firecracker after animation
      firecracker.addEventListener("animationend", () => {
        firecracker.remove();
      });
    }, i * 100); // Stagger the firecrackers
  }
};

//function openModal2(button) {
//  const matchId = button.getAttribute("data-matchid");
//  const tourId = button.getAttribute("data-tourid");
//  const matchNo = button.getAttribute("data-matchno");
//  const roundNo = button.getAttribute("data-roundno");
//  const playerOneImagePath = button.getAttribute("data-playeroneimagepath");
//  const playerTwoImagePath = button.getAttribute("data-playertwoimagepath");
//  const playerOneId = button.getAttribute("data-playeroneid");
//  const playerTwoId = button.getAttribute("data-playertwoid");
//  const playerOneScore = button.getAttribute("data-playeronescore");
//  const playerTwoScore = button.getAttribute("data-playertwoscore");
//
//  //console.log(matchId+" "+playerOne);
//
//  document.getElementById("matchId2").value = matchId;
//  document.getElementById("tourId2").value = tourId;
//  document.getElementById("matchNo2").value = matchNo;
//  document.getElementById("matchNo3").innerHTML = matchNo;
//  document.getElementById("roundNo2").value = roundNo;
//  document.getElementById("playerOneImagePath2").src = playerOneImagePath;
//  document.getElementById("playerTwoImagePath2").src = playerTwoImagePath;
//  document.getElementById("playerOneId2").value = playerOneId;
//  document.getElementById("playerTwoId2").value = playerTwoId;
//  document.getElementById("playerOneScore2").value = playerOneScore;
//  document.getElementById("playerTwoScore2").value = playerTwoScore;
//
//  //console.log(playerOneId);
//  const updateModal = new bootstrap.Modal(document.getElementById('updateModal2'));
//  updateModal.show();
//}

