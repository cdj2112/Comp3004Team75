(function(){
	console.log(">>>>>>>>>", playerIdx);

	var xhr = new XMLHttpRequest();
	var stompClient;
    var imgMap = {};

	function updateGame(gameStatus){
		console.log(gameStatus);
		updateButtons(gameStatus.currentStatus, gameStatus.activePlayer);
        updatePlayer(gameStatus.playerStatus);
        if(gameStatus.storyCard){
            document.getElementById('storyCard').src = gameStatus.storyCard.url;
        } else {
             document.getElementById('storyCard').src = '';
        }
	}

    function updateButtons(status, active) {
		var drawButton = document.getElementById("drawStoryCard")
        drawButton.disabled = !(status === "IDLE" && playerIdx === active);

		var acceptButton = document.getElementById("acceptButton");
        var declineButton = document.getElementById("declineButton");
        acceptButton.className = (status === "SPONSORING" && playerIdx === active) ? "" : "invisible";
		declineButton.className = (status === "SPONSORING" && playerIdx === active) ? "" : "invisible";

        if(status === "SPONSORING"){
            acceptButton.onclick = acceptDeclineSponsor(true);
            declineButton.onclick = acceptDeclineSponsor(false);
        } else {
            acceptButton.onclick = declineButton.onclick = null;
        }
    }

    function updatePlayer(players){
        var mainPlayer = document.getElementsByClassName('lowerPlayer')[0];
        var mainHand = document.querySelectorAll('.lowerPlayer > .playerHand')[0];
        var mainPlay = document.querySelectorAll('.lowerPlayer > .playerPlay')[0];

        var player = players[playerIdx];
        for(var c = 0; c < player.hand.length; c++){
            var card = player.hand[c];
            var img = imgMap[card.id];
            if(!img){
                img = document.createElement('img');
                img.id = 'card'+card.id;
                img.className = 'card';
                img.src = card.url;
                mainHand.appendChild(img);
                imgMap[card.id] = img;
            }
        }
    }

    function drawStoryCard(){
    	stompClient.send("/command/drawStoryCard");
    }
    document.getElementById("drawStoryCard").onclick = drawStoryCard;

    function acceptDeclineSponsor(accept){
    	return function(){
    		stompClient.send("/command/sponsorQuest", {}, JSON.stringify({
    			accept: accept,
                player: playerIdx
    		}));
    	}
    }

	function connect(){
        var socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/status/gameStatus', function (greeting) {
                updateGame(JSON.parse(greeting.body));
            });
        });
    }

	function initGameStatus(){
        xhr.onreadystatechange = function() {
            if(xhr.readyState = XMLHttpRequest.DONE && xhr.status == 200 && xhr.response.length){
                updateGame(JSON.parse(xhr.response));
            }
        }
        xhr.open("GET", "gameStatus", true);
        xhr.send();
    }

    initGameStatus();
    connect();

})()