import { baseURLApi } from "../../config.js";

const dialog = document.getElementById("match-dialog");
let username;

let playerShips = [
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

let enemyShips = [
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


window.renderBoards = () => {
    for(let i = 0; i < 10; i++) {
        for(let j = 0; j < 10; j++) {
            let playerCell = document.getElementById(`p-${i}-${j}`);

            playerCell.classList.remove("ship");
            playerCell.classList.remove("hit");
            playerCell.classList.remove("miss");

            if(playerShips[i][j] === 4) {
                playerCell.classList.add("ship");
            } else if (playerShips[i][j] == 2) {
                playerCell.classList.add("hit");
            } else if (playerShips[i][j] == 1) {
                playerCell.classList.add("miss");
            }
            
            playerCell = document.getElementById(`e-${i}-${j}`);

            playerCell.classList.remove("ship");
            playerCell.classList.remove("hit");
            playerCell.classList.remove("miss");

            if(enemyShips[i][j] === 4) {
                playerCell.classList.add("ship");
            } else if (enemyShips[i][j] == 2) {
                playerCell.classList.remove("ship");
                playerCell.classList.add("hit");
            } else if (enemyShips[i][j] == 1) {
                playerCell.classList.remove("ship");
                playerCell.classList.add("miss");
            }
        }
    }
}

window.openMatchDialog = (mid) => {
    dialog.showModal();
    const matchData = window.matchHistory.find(match => match.mid === mid)

    let playerGridString;
    let enemyGridString;

    if (matchData.p1.toLowerCase() === username.toLowerCase()) {
        playerGridString = matchData.p1Grid;
        enemyGridString = matchData.p2Grid;
    } else if (matchData.p2.toLowerCase() === username.toLowerCase()) {
        playerGridString = matchData.p2Grid;
        enemyGridString = matchData.p1Grid;
    }

    const updateGridFromString = (str, arr) => {
        for(let i = 0; i < str.length; i++) {
            arr[Math.floor(i / 10)][i % 10] = Number.parseInt(str.charAt(i));
        }
    }

    updateGridFromString(playerGridString, playerShips);
    updateGridFromString(enemyGridString, enemyShips);

    window.renderBoards();
}

window.onCloseDialog = () => {
    dialog.close();
}

window.retrieveMatchHistory = () => {
    username = document.getElementById("username").value;
    const url = baseURLApi + "/matches/getMatchHistoryByPlayer?username=" + username;

    try {
        fetch(url).then(response => {
            if (response.ok) {
                return response.json()
            }
        }).then(data => {
            window.matchHistory = data;

            console.log(data)

            const tbody = document.getElementById("historyRows");
            
            let tbodyHTML = "";

            data.forEach(element => {
                const { mid, p1, p2, startTime, lastMovedAt, winner } = element;

                tbodyHTML += `
                    <tr id="mid-${mid}" onclick="openMatchDialog(${mid})">
                        <td>${mid}</td>
                        <td>${p1}</td>
                        <td>${p2}</td>
                        <td>${startTime}</td>
                        <td>${lastMovedAt}</td>
                        <td>${winner}</td>
                    </tr>
                `;
            });

            tbody.innerHTML = tbodyHTML;
        })
    } catch(e) {
        console.error(e)
    }
}