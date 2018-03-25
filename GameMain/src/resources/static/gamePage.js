(function(){
	var xhr = new XMLHttpRequest();
	var stompClient;

	function updateGame(gameStatus){
		console.log(gameStatus);
		updateButtons(gameStatus.currentStatus, gameStatus.activePlayer);
	}

    function updateButtons(status, active) {
		var drawButton = document.getElementById("drawStoryCard")
        drawButton.className = (status === "IDLE" && playerIdx === active) ? "" : "invisible";

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