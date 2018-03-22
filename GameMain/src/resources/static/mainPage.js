var xhr = new XMLHttpRequest();
var stompClient = null;

function updatePlayerList(playerList){
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
			console.log(xhr.response);
            stompClient.send('/command/updatePlayers', {}, {});
		}	
	}
	xhr.open("POST", "addNewPlayer", true);
	var un = document.getElementById('userName').value;
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send(JSON.stringify({name: un}));
}

function connect(){
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/status/playerList', function (greeting) {
            updatePlayerList(JSON.parse(greeting.body));
        });
    });
}
connect();