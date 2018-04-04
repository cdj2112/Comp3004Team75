(function(){
	console.log(">>>>>>>>>", playerIdx);

	var xhr = new XMLHttpRequest();
	var stompClient;
    var imgMap = {};
    var draggingCard;
    var timeout;

	function updateGame(gameStatus){
		console.log(gameStatus);
		updateButtons(gameStatus.currentStatus, gameStatus.activePlayer);
        updatePlayer(gameStatus.playerStatus, gameStatus.currentStatus==='PLAYING_QUEST');
        if(gameStatus.currentQuest){
            updateQuest(gameStatus.currentQuest);
        } else {
            clearQuest();
        }
        updateDiscard(gameStatus.toDiscard);
        updatePrompt(gameStatus.currentStatus, gameStatus.activePlayer, gameStatus.toDiscard);

        if(gameStatus.storyCard){
            document.getElementById('storyCard').src = gameStatus.storyCard.url;
        } else {
             document.getElementById('storyCard').src = '';
        }

        if(gameStatus.currentStatus === 'EVAL_QUEST_STAGE' && gameStatus.activePlayer === playerIdx && !timeout){
            timeout = setTimeout(function(){
                timeout = null;
                stompClient.send("/command/playStage", {}, JSON.stringify({
                    player: playerIdx
                }));
            }, 1000);
        }
	}

    function updateButtons(status, active) {
		var drawButton = document.getElementById("drawStoryCard")
        drawButton.disabled = !(status === "IDLE" && playerIdx === active);

		var acceptButton = document.getElementById("acceptButton");
        var declineButton = document.getElementById("declineButton");

        var isVisible = (status === "SPONSORING" || status === "BUILDING_QUEST" || status === "ACCEPTING_QUEST" || status === "PLAYING_QUEST") && playerIdx === active;

        acceptButton.className = isVisible ? "" : "invisible";
		declineButton.className = (isVisible && status!=="BUILDING_QUEST" && status !== "PLAYING_QUEST") ? "" : "invisible";

        if(status === "SPONSORING"){
            acceptButton.onclick = acceptDeclineSponsor(true);
            declineButton.onclick = acceptDeclineSponsor(false);
        } else if(status==="BUILDING_QUEST") {
            acceptButton.onclick = finalizeQuest;
            declineButton.onclick = null;
        } else if(status==="ACCEPTING_QUEST"){
            acceptButton.onclick = acceptDeclineQuest(true);
            declineButton.onclick = acceptDeclineQuest(false);
        } else if (status="PLAYING_QUEST") {
            acceptButton.onclick = finalizePlay;
            declineButton.onclick = null;
        } else {
            acceptButton.onclick = declineButton.onclick = null;
        }
    }

    function getCardImg(card) {
        if(imgMap[card.id]) return imgMap[card.id];

        var img = document.createElement('img');
        img.id = 'card'+card.id;
        img.className = 'card';
        img.src = card.url;
        img.draggable = true;
        img.ondragstart = dragCardStart;
        img.ondrag = dragCard;
        img.ondragend = cardDrop;
        imgMap[card.id] = img;
        return img;
    }

    function matchCardsDom(cardArray, domElement, cl, disabledDrag){
        var className = cl || 'card';
        var inDom = [].slice.call(domElement.children);
        for(var c=0; c<cardArray.length; c++){
            var card = cardArray[c];
            var img = getCardImg(card);
            if(!inDom.includes(img)) {
                img.className = className;
                domElement.appendChild(img);
                img.draggable = !disabledDrag;
                img.ondragstart = !disabledDrag ? dragCardStart : null;
                img.ondrag = !disabledDrag ? dragCard : null;
                img.ondragend = !disabledDrag ? cardDrop : null;
            }
        }

        for(var i = 0; i<inDom.length; i++){
            var child = inDom[i];
            var match = child.id.match(/card([0-9]+)/);
            if(!match) continue;

            var id = parseInt(match[1]);
            var card = cardArray.find(function(c){
                return c.id === id;
            });
            
            if(!card) domElement.removeChild(child);
        }
    }

    function updatePlayer(players, playing){
        var mainPlayer = document.getElementsByClassName('lowerPlayer')[0];
        var mainHand = document.querySelectorAll('.lowerPlayer > .playerHand')[0];
        var mainPlay = document.querySelectorAll('.lowerPlayer > .playerPlay')[0];
        var rank = document.querySelectorAll('.lowerPlayer .rank')[0];
        var battlePoints = document.querySelectorAll('.lowerPlayer .battlePoints')[0];

        var player = players[playerIdx];
        matchCardsDom(player.hand, mainHand);
        matchCardsDom(player.play, mainPlay, null, true);

        if(playing) {
            mainPlay.className = 'playerPlay active';
            mainPlay.ondragover = function(ev){
                ev.preventDefault();
                return false;
            };
            mainPlay.ondrop = playDrop;
        } else {
            mainPlay.className = 'playerPlay';
            mainPlay.ondrop = null;
            mainPlay.ondragover = null;
        }
        rank.src = player.rank;
        battlePoints.innerHTML = "Battle Points: "+player.battlePoints;
    }

    function updateQuest(quest){
        for(var i =0; i< 5; i++){
            var stageDiv = document.getElementById('stage'+i);
            if(i<quest.stages.length){
                var stage = quest.stages[i];
                stageDiv.style.display = '';
                stageDiv.ondragover = function(ev){
                    ev.preventDefault();
                    return false;
                };
                stageDiv.ondrop = dropCardOnStage(i);
                matchCardsDom(stage.cards, stageDiv, 'stageCard', true);
            } else {
                stageDiv.style.display = 'none';
                stageDiv.ondragover = null;
                stageDiv.ondrop = null;
            }
        }
    }

    function clearQuest(){
        for(var i =0; i<5; i++){
            var stageDiv = document.getElementById('stage'+i);
            stageDiv.style.display = 'none';
            stageDiv.ondrop = null;
        }
    }

    function updateDiscard(discard){
        console.log(discard);
        var discardElm = document.getElementById('discard');
        if(discard[playerIdx]>0){
            discardElm.style.display = '';
            discardElm.ondragover = function(ev){
                ev.preventDefault();
                return false;
            }
            discardElm.ondrop = discardDrop;
        } else {
            discardElm.style.display = 'none';
            discardElm.ondrop = null;
            discardElm.ondragover = null
        }
    }

    function updatePrompt(status, active, toDiscard){
        var prompt = document.getElementById('prompt');
        if(status === 'IDLE'){
            prompt.innerHTML = active === playerIdx ? 'Draw Story Card' : 'Waiting For Story Card';
        } else if (status === 'SPONSORING') {
            prompt.innerHTML = active === playerIdx ? 'Sponsor Quest?' : 'Waiting For Other Player';
        } else if (status === 'BUILDING_QUEST') {
            prompt.innerHTML = active === playerIdx ? 'Build Quest' : 'Waiting For Sponsor';
        } else if (status === 'ACCEPTING_QUEST') {
            prompt.innerHTML = active === playerIdx ? 'Accept Quest?' : 'Waiting For Other Player';
        } else if(status === 'PRE_QUEST_DISCARD' || status === 'END_TURN_DISCARD') {
            prompt.innerHTML = toDiscard[playerIdx]>0 ? 'Discard '+toDiscard[playerIdx]+' card'+(toDiscard[playerIdx]>1?'s':'') : 'Waiting For Other Player';
        } else if(status === 'PLAYING_QUEST') {
            prompt.innerHTML = active === playerIdx ? 'Play Cards for Stage' : 'Waiting For Other Player';
        } else if (status === 'EVAL_QUEST_STAGE') {
            prompt.innerHTML = 'Player '+active+' playing stage';
        } else {
            prompt.innerHTML = 'No prompt set';
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

    function finalizeQuest(){
        stompClient.send("/command/finalizeQuest", {}, JSON.stringify({}));
    }

    function acceptDeclineQuest(accept){
        return function(){
            stompClient.send("/command/acceptQuest", {}, JSON.stringify({
                accept: accept,
                player: playerIdx,
            }))
        }
    }

    function finalizePlay(ev){
        stompClient.send("/command/finalizePlay", {}, JSON.stringify({}))
    }

    function dragCardStart(ev){
        var d = ev.target.cloneNode(true);
        d.style.opacity = 0;
        document.body.appendChild(d);
        ev.dataTransfer.setDragImage(d, 0, 0);
        //ev.target.style.setProperty('cursor', 'move', 'important');
        draggingCard = ev.target;
    }
    function dragCard(ev){
        var img = ev.target;
        var imgBox = img.getBoundingClientRect();
        img.style.position = 'absolute';

        var hand = document.querySelectorAll('.lowerPlayer > .playerHand')[0];
        var handBox = hand.getBoundingClientRect();

        var x = ev.clientX;
        var y = ev.clientY;
        img.style.top = y - handBox.top - imgBox.height/2+'px';
        img.style.left = x - handBox.left - imgBox.width/2+'px';
    }
    function cardDrop(ev){
        var img = ev.target;
        img.style.position = '';
        img.style.top ='';
        img.style.left = '';
        draggingCard= null;
    }

    function dropCardOnStage(stageNumber){
        return function(ev){
            if(!draggingCard) return;
            var match = draggingCard.id.match(/card([0-9]+)/);
            var id = match && match[1];
            if(id){
                stompClient.send('/command/playToStage', {}, JSON.stringify({
                    player: playerIdx,
                    cardId: id,
                    stageNum: stageNumber,
                }))
            }
        }
    }

    function discardDrop(ev){
        if(!draggingCard) return;
        var match = draggingCard.id.match(/card([0-9]+)/);
        var id = match && match[1];
        if(id){
            stompClient.send('/command/discardCard', {}, JSON.stringify({
                player: playerIdx,
                cardId: id,
            }))
        }
    }

    function playDrop(ev){
        if(!draggingCard) return;
        var match = draggingCard.id.match(/card([0-9]+)/);
        var id = match && match[1];
        if(id){
            stompClient.send('/command/playCards', {}, JSON.stringify({
                player: playerIdx,
                cardId: id,
            }))
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