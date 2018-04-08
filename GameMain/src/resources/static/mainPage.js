(function(){
    var xhr = new XMLHttpRequest();
    var stompClient = null;
    var playerIDX;

    function updatePlayerList(playerList){
        if(playerList.length===0) return;
        var list = document.getElementById('playerList');
        for(var i=0; i<playerList.length; i++){
            var name = playerList[i];
            if(list.children[i]){
                list.children[i].innerHTML = name;
            } else {
                var oli = document.createElement('li');
                oli.innerHTML = name;
                list.appendChild(oli);
            }
        }
    }

    function addNewPlayer(){
    	xhr.onreadystatechange = function() {//Call a function when the state changes.
    		if(xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
    			// Request finished. Do processing here.
    			playerIDX = parseInt(xhr.response);
                if(playerIDX >= 0){
                    location.search='?player='+playerIDX+'&joined=true';
                }
                stompClient.send('/command/updatePlayers', {}, {});
    		}	
    	}
    	xhr.open("POST", "addNewPlayer", true);
    	var un = document.getElementById('userName').value;
    	xhr.setRequestHeader('Content-Type', 'application/json');
    	xhr.send(JSON.stringify({name: un}));
    }

    function addAIPlayer(){
        xhr.onreadystatechange = function() {//Call a function when the state changes.
            if(xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
                // Request finished. Do processing here.
                stompClient.send('/command/updatePlayers', {}, {});
            }   
        }
        xhr.open("POST", "addAIPlayer", true);
        xhr.send();
    }

    function startGame(){
        stompClient.send('/command/startGame', {}, {});
    }

    function redirectToGamePage(){
        location.pathname = '/gamePage';
    }

    function connect(){
        var socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/status/playerList', function (greeting) {
                updatePlayerList(JSON.parse(greeting.body));
            });
            stompClient.subscribe('/status/gameStart', function (gameStart) {
                redirectToGamePage();
            });
        });
    }

    function initPlayerList(){
        xhr.onreadystatechange = function() {
            if(xhr.readyState = XMLHttpRequest.DONE && xhr.status == 200){
                updatePlayerList(JSON.parse(xhr.response));
            }
        }
        xhr.open("GET", "playerList", true);
        xhr.send();
    }

    document.getElementById("addAIButton").onclick = addAIPlayer;
    document.getElementById("startGame").onclick = startGame;
    document.getElementById("joinGame").onclick = addNewPlayer;
    connect();
    initPlayerList();
})()