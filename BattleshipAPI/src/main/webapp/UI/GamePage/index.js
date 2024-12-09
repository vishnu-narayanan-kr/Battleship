
const isOnline = true
let username;

let playerShips;
let enemyShips;

let socket;

const sendToAPI = (message) => {
    if (socket?.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify(message));
    } else {
        console.error("WebSocket connection is not open.");
    }
}

if (isOnline) {
    playerShips = [
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ]
    ];

    enemyShips = [
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ]
    ]

    if (location.hostname.includes("ngrok")) {
        socket = new WebSocket("wss://" + location.hostname + "/websocket");
    } else {
        socket = new WebSocket("ws://" + location.hostname + ":8080/websocket");
    }
        
    socket.onopen = function(event) {
        console.log("WebSocket connection established.");

        // register user

        const params = new URL(document.location.toString()).searchParams;
        username = params.get("username");

        const message = {
            username,
            messageType: "register",
        };

        sendToAPI(message);
    };
    
    socket.onmessage = function(event) {
        const messageData = JSON.parse(event.data);

        console.log("Received message:", messageData);

        if (messageData.startTime) {
            let playerGridString;
            let enemyGridString;
            updateCurrentPlayerInfo(messageData.currentPlayer);

            if (messageData.p1 === username) {
                playerGridString = messageData.p1Grid;
                enemyGridString = messageData.p2Grid;
            } else if (messageData.p2 === username) {
                playerGridString = messageData.p2Grid;
                enemyGridString = messageData.p1Grid;
            }

            const updateGridFromString = (str, arr) => {
                for(let i = 0; i < str.length; i++) {
                    arr[Math.floor(i / 10)][i % 10] = Number.parseInt(str.charAt(i));
                }
            }

            updateGridFromString(playerGridString, playerShips);
            updateGridFromString(enemyGridString, enemyShips);

            renderBoards();

            if (messageData.winner) {
                alert("Game over, the winner is: " + messageData.winner);
                // redirect
                window.location.replace(location.href.replace("GamePage", "Placement"));
            }
        }
    };
    
    socket.onerror = function(error) {
        console.error("WebSocket error: ", error);
    };
    
    socket.onclose = function(event) {
        console.log("WebSocket connection closed:", event);
    };

} else {
    playerShips = [
        [ 3, 4, 4, 4, 3, 3, 3, 4, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 4, 3, 4, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 4, 3, 3 ],
        [ 3, 3, 4, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 4, 4, 4, 4, 4, 3, 3, 3, 3 ],
        [ 3, 3, 4, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 4, 4, 4, 3, 3 ],
        [ 3, 3, 4, 3, 3, 4, 3, 4, 3, 3 ],
        [ 3, 3, 4, 3, 3, 4, 4, 4, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ]
    ];

    enemyShips = [
        [ 3, 3, 3, 3, 4, 3, 3, 4, 4, 4 ],
        [ 3, 3, 3, 4, 4, 4, 3, 3, 4, 3 ],
        [ 3, 3, 3, 3, 4, 3, 3, 3, 3, 3 ],
        [ 3, 3, 4, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 4, 4, 4, 3, 3, 4, 3, 3, 3 ],
        [ 3, 3, 4, 3, 3, 3, 4, 3, 3, 3 ],
        [ 3, 3, 4, 3, 3, 4, 4, 4, 3, 3 ],
        [ 3, 3, 4, 3, 3, 4, 3, 4, 3, 3 ],
        [ 3, 3, 4, 3, 3, 3, 3, 3, 3, 3 ],
        [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 ]
    ]
}

const updateCurrentPlayerInfo = (name) => {
    document.getElementById("turn-info").innerText = "Current Player: " + name;
}

const renderBoards = () => {
    for(let i = 0; i < 10; i++) {
        for(let j = 0; j < 10; j++) {
            let playerCell = document.getElementById(`p-${i}-${j}`);

            if(playerShips[i][j] === 4) {
                playerCell.classList.add("ship");
            } else if (playerShips[i][j] == 2) {
                playerCell.classList.remove("ship");
                playerCell.classList.add("hit");
            } else if (playerShips[i][j] == 1) {
                playerCell.classList.remove("ship");
                playerCell.classList.add("miss");
            }
            
            //*
            // for testing only
            playerCell = document.getElementById(`e-${i}-${j}`);

            if(enemyShips[i][j] === 4) {
                playerCell.classList.add("ship");
            } else if (enemyShips[i][j] == 2) {
                playerCell.classList.remove("ship");
                playerCell.classList.add("hit");
            } else if (enemyShips[i][j] == 1) {
                playerCell.classList.remove("ship");
                playerCell.classList.add("miss");
            }
            //*/
        }
    }
}

renderBoards();

let hitCountPlayer = 0;
let isGameOver = false;


const movePlayer = (e) => {
    const { id = "" } = e.target;

    if(id.includes("e-")) {
        const [ _, x, y ] = id.split('-');
        
        if (isOnline) {
            const message = {
                username,
                messageType: "move",
                x,
                y,
            }


            console.log(message)
            sendToAPI(message);
        } else {
            if(enemyShips[x][y] >= 3) {
                enemyShips[x][y] -= 2;
                
                if(enemyShips[x][y] == 2) {
                    hitCountPlayer++;
                }
    
                renderBoards();
                checkVictory();
    
                if(!isGameOver) {
                    moveAI();
                    checkVictory();
                }
            }
        }
    }
}


document.getElementById("enemy-board").addEventListener(
    "click", movePlayer
)

let dropCountAI = 0;
let hitCountAI = 0;


const findPlayerShip = () => {
    while(true) {
        for(let i = random(0, 9); i < 10; i++) {
            for(let j = 0; j < 10; j++) {
                if(playerShips[i][j] === 4) {
                    return [i, j];
                }
            }
        }
    }
}

const difficulty = ["easy", "medium", "hard"][0];

const moveAI = () => {
    if(dropCountAI === 100) return;

    let moved = false;

    while(!moved) {
        let row = random(0, 9);
        let col = random(0, 9);

        if(difficulty === "easy") {
            if(Math.random() < 0.025 ) {
                [row, col] = findPlayerShip();
            }
        } else if(difficulty === "medium") {
            console.log({ hitCountPlayer, hitCountAI })
            if(Math.random() < (hitCountPlayer - hitCountAI) / 10) {
                [row, col] = findPlayerShip();
            }
        } else if(difficulty === "hard") {
            if(Math.random() < 0.5 ) {
                [row, col] = findPlayerShip();
            }
        }

        if(playerShips[row][col] >= 3) {
            moved = true;
            dropCountAI++;

            playerShips[row][col] -= 2;

            if(playerShips[row][col] == 2) {
                hitCountAI++;
            }
        }

        renderBoards();
    }
}

document.getElementById("fireBtn").addEventListener('click', moveAI);

function random(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
}

const checkVictory = () => {
    let [ didPlayerWin, didAIWin ] = [ true, true ];

    for(let i = 0; i < 10; i++) {
        for(let j = 0; j < 10; j++) {
            if(playerShips[i][j] === 4) { 
                didAIWin = false;
            }
            
            if(enemyShips[i][j] === 4) {
                didPlayerWin = false;
            }
        }
    }

    if(didPlayerWin) {
        alert("you won!");
        isGameOver = true;
        location.reload();
    }

    if(didAIWin) {
        alert("AI won, better luck next time :)");
        isGameOver = true;
        location.reload();
    }

}