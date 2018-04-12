(function(){
    var xhr = new XMLHttpRequest();
    var stompClient = null;
    var playerIDX;

    function updatePlayerList(playerList){
        if(playerList.length===0) return;
        var list = document.getElementById('playerList');
        for(var i=0; i<playerList.userNames.length; i++){
            var name = playerList.userNames[i];
            var strategy = playerList.strategies[i];
            if(list.children[i]){
                list.children[i].children[0].innerHTML = name;
            } else {
                var oli = document.createElement('li');
                var span = document.createElement('span');
                span.innerHTML = name;
                oli.appendChild(span);
                if(strategy!=0 && isHost){
                    var select = document.createElement('select');
                    var option1 = document.createElement('option');
                    option1.innerHTML = "Strategy 1";
                    option1.value = 1;
                    select.appendChild(option1);
                    var option2 = document.createElement('option');
                    option2.innerHTML = "Strategy 2";
                    option2.value = 2;
                    select.appendChild(option2);
                    select.value = strategy;
                    select.onchange = changeAIStrategy(i);
                    oli.appendChild(select);
                }
                list.appendChild(oli);
            }
        }
        document.getElementById('rigged').checked = playerList.rigged;
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

    function changeAIStrategy(index) {
        return function(ev){
            console.log(index, ev.target.value);
            stompClient.send('/command/changeAIStrategy', {}, JSON.stringify({
                index: index,
                newStrategy: parseInt(ev.target.value),
            }))
        }
    }

    function startGame(){
        stompClient.send('/command/startGame', {}, {});
    }

    function redirectToGamePage(){
        location.pathname = '/gamePage';
    }

    function changeRigged(ev) {
        stompClient.send('/command/changeRigged', {}, ev.target.checked);
    }
    document.getElementById('rigged').onchange = changeRigged;

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