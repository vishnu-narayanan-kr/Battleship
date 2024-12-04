"use strict";

// Select elements
const shipPlacementGrid = document.getElementById("shipPlacementGrid");
const rotateButton = document.getElementById("rotateButton");
const shipsLeftContainer = document.querySelector(".ship-status");
const myFleetGrid = document.querySelector(".my-fleet");
const fleetCells = myFleetGrid.querySelectorAll(".cell");
const confirmButton = document.getElementById("confirmButton");

// Ship Queue: Lengths and counts
let shipQueue = [
  { length: 4, count: 1 },
  { length: 3, count: 2 },
  { length: 2, count: 3 },
  { length: 1, count: 4 },
];

let currentShipIndex = 0; // Track the current ship
let isVertical = true; // Track rotation state
let draggedShip; // Store the currently dragged ship





// Update "Ships Left" UI
function updateShipsLeft() {
  const shipsLeftHTML = shipQueue
    .map((ship) => {
      const className = `ship${ship.length} ${ship.count === 0 ? "ship-not-available" : ""}`;
      return `
        <div>
          <p class="ships-left">${ship.count} x</p>
          <div class="${className} ships-left"></div>
        </div>`;
    })
    .join("");
  shipsLeftContainer.innerHTML = `
    <h3>Ships Left:</h3>
    ${shipsLeftHTML}
  `;

  // Update the state of the confirmation button
  updateConfirmButtonState(); // <- Ensure this is called here
}
// Initialize the ships left 
updateShipsLeft();



// Display current ship on "shipPlacementGrid"
function updateShipPlacement() {
  const cells = shipPlacementGrid.querySelectorAll(".cell");
  cells.forEach((cell) => cell.classList.remove("ship"));

  // Check if currentShipIndex is valid
  if (
    currentShipIndex >= shipQueue.length ||
    shipQueue[currentShipIndex] === undefined ||
    shipQueue[currentShipIndex].count === 0
  ) {
    return; // No ship to place
  }

  const shipLength = shipQueue[currentShipIndex]?.length;

  if (isVertical) {
    // Place ship vertically in the center column
    for (let i = 0; i < shipLength; i++) {
      const index = 1 + i * 4; // Index for the cell
      cells[index].classList.add("ship");
    }
  } else {
    // Place ship horizontally in the center row
    for (let i = 0; i < shipLength; i++) {
      const index = 4 + i; // Index for the cell
      cells[index].classList.add("ship");
    }
  }
}
// Initialize the first ship placement
updateShipPlacement();



//ROTATION
//roration button functionality
rotateButton.addEventListener("click", () => {
  isVertical = !isVertical; // Toggle orientation
  updateShipPlacement();    // Update the placement grid
});


//VALIDATION
// Validate placement
function isPlacementValid(row, col, length, vertical) {
  for (let i = 0; i < length; i++) {
    const checkRow = vertical ? row + i : row;
    const checkCol = vertical ? col : col + i;

    // Check grid boundaries
    if (checkRow > 10 || checkCol > 10 || checkRow < 1 || checkCol < 1) {
      return false; // Invalid: Out of bounds
    }

    // Check if cell is occupied or adjacent
    if (isCellOccupied(checkRow, checkCol)) {
      return false; // Invalid: Overlaps or touches another ship
    }
  }
  return true; // Valid placement
}

// Check if a cell or its neighbors are occupied
function isCellOccupied(row, col) {
  const directions = [
    [-1, -1], [-1, 0], [-1, 1],
    [0, -1], [0, 0], [0, 1],
    [1, -1], [1, 0], [1, 1],
  ];
  
  return directions.some(([dRow, dCol]) => {
    const checkRow = row + dRow;
    const checkCol = col + dCol;

    if (checkRow >= 1 && checkRow <= 10 && checkCol >= 1 && checkCol <= 10) {
      const cell = document.querySelector(`[data-row="${checkRow}"][data-col="${checkCol}"]`);
      return cell && cell.classList.contains("ship");
    }
    return false;
  });
}

// Place the ship
function placeShip(row, col, length, vertical) {
  const surroundingCells = [];

  for (let i = 0; i < length; i++) {
    const placeRow = vertical ? row + i : row;
    const placeCol = vertical ? col : col + i;

    const cell = document.querySelector(`[data-row="${placeRow}"][data-col="${placeCol}"]`);
    if (cell) cell.classList.add("ship");

    addSurroundingCells(placeRow, placeCol, surroundingCells);
  }

  // Add dots to surrounding cells
  surroundingCells.forEach(({ row, col }) => {
    const cell = document.querySelector(`[data-row="${row}"][data-col="${col}"]`);
    if (cell && !cell.classList.contains("ship")) {
      addDotToCell(cell);
    }
  });
}

// Add surrounding cells to a list
function addSurroundingCells(row, col, surroundingCells) {
  const directions = [
    [-1, -1], [-1, 0], [-1, 1],
    [0, -1],          [0, 1],
    [1, -1], [1, 0], [1, 1],
  ];

  directions.forEach(([dRow, dCol]) => {
    const newRow = row + dRow;
    const newCol = col + dCol;

    if (newRow >= 1 && newRow <= 10 && newCol >= 1 && newCol <= 10) {
      surroundingCells.push({ row: newRow, col: newCol });
    }
  });
}

// Add a dot to a cell
function addDotToCell(cell) {
  if (!cell.classList.contains("dot")) {
    cell.classList.add("dot");
  }
}

// Remove a dot from a cell
function removeDotFromCell(cell) {
  if (cell.classList.contains("dot")) {
    cell.classList.remove("dot");
  }
}

// Refresh dots for all ships on the MY FLEET grid
function refreshDotsOnFleet() {
  // Clear all existing dots
  myFleetGrid.querySelectorAll('.dot').forEach((cell) => removeDotFromCell(cell));

  // Recalculate dots based on current ship positions
  myFleetGrid.querySelectorAll('.ship').forEach((shipCell) => {
    const row = parseInt(shipCell.dataset.row, 10);
    const col = parseInt(shipCell.dataset.col, 10);
    const surroundingCells = [];

    // Get all surrounding cells for each ship
    addSurroundingCells(row, col, surroundingCells);

    surroundingCells.forEach(({ row, col }) => {
      const cell = document.querySelector(`[data-row="${row}"][data-col="${col}"]`);
      if (cell && !cell.classList.contains("ship")) {
        addDotToCell(cell);
      }
    });
  });
}




// DRAG-N-DROP
// Allow drop on cells
function allowDrop(event) {
  event.preventDefault();
}

// Handle drag start
function dragStart(event) {
  draggedShip = event.target;
}

// Handle drop on "myFleetGrid"
function dropShip(event) {
  event.preventDefault();

  const targetCell = event.target;
  if (!targetCell.classList.contains("cell")) return;

  const targetRow = parseInt(targetCell.dataset.row, 10);
  const targetCol = parseInt(targetCell.dataset.col, 10);

  const shipLength = shipQueue[currentShipIndex]?.length;
  if (!shipLength) return;

  if (isPlacementValid(targetRow, targetCol, shipLength, isVertical)) {
    placeShip(targetRow, targetCol, shipLength, isVertical);

    // Update queue
    shipQueue[currentShipIndex].count--;

    if (shipQueue[currentShipIndex]?.count === 0) {
      currentShipIndex = shipQueue.findIndex((ship) => ship.count > 0);
    }

    // Ensure currentShipIndex stays valid
    if (currentShipIndex === -1) {
      currentShipIndex = shipQueue.length; // Set index out of bounds
    }

    // Refresh dots
    refreshDotsOnFleet();

    // Update UI
    updateShipsLeft();
    updateShipPlacement();
    updateConfirmButtonState(); // Check button activation
  }/* else {
    notifyUser("Invalid placement. Ships cannot overlap or touch.");
  }*/
}

// Attach drag-and-drop listeners
document.querySelectorAll("#shipPlacementGrid .cell").forEach((cell) => {
  cell.setAttribute("draggable", "true");
  cell.addEventListener("dragstart", dragStart);
});

// Drag over
fleetCells.forEach((cell) => {
  cell.addEventListener("dragover", allowDrop);
  cell.addEventListener("drop", dropShip);
});






// Initialize the ships left section and the first ship placement
updateShipsLeft();
updateShipPlacement();




// Function to update the ship placement grid
function updateShipPlacement() {
  const cells = shipPlacementGrid.querySelectorAll(".cell");
  cells.forEach((cell) => cell.classList.remove("ship"));

  const shipLength = shipQueue[currentShipIndex].length;

  if (isVertical) {
    // Place ship vertically in the center column
    for (let i = 0; i < shipLength; i++) {
      const index = 1 + i * 4; // Index for the cell
      cells[index].classList.add("ship");
    }
  } else {
    // Place ship horizontally in the center row
    for (let i = 0; i < shipLength; i++) {
      const index = 4 + i; // Index for the cell
      cells[index].classList.add("ship");
    }
  }
}





// removing ship from my fleet back to placement grid
// Handle double-click on ships in MY FLEET grid
myFleetGrid.addEventListener("dblclick", (event) => {
  const targetCell = event.target;

  if (!targetCell.classList.contains("ship")) return;

  const targetRow = parseInt(targetCell.dataset.row, 10);
  const targetCol = parseInt(targetCell.dataset.col, 10);

  const shipInfo = findShipCells(targetRow, targetCol);
  if (!shipInfo) return;

  const { length, vertical, cells } = shipInfo;

  cells.forEach(({ row, col }) => {
    const cell = document.querySelector(`[data-row="${row}"][data-col="${col}"]`);
    if (cell) cell.classList.remove("ship");
  });

  shipQueue.find((ship) => ship.length === length).count++;

  currentShipIndex = shipQueue.findIndex((ship) => ship.count > 0);

  refreshDotsOnFleet();
  updateShipsLeft();
  updateShipPlacement();
  updateConfirmButtonState(); // Check button activation
});




// Function to find all cells occupied by a ship
function findShipCells(row, col) {
  const cells = [];
  let length = 0;
  let vertical = false;

  // Check vertically first
  if (
    document.querySelector(`[data-row="${row + 1}"][data-col="${col}"]`)?.classList.contains("ship") ||
    document.querySelector(`[data-row="${row - 1}"][data-col="${col}"]`)?.classList.contains("ship")
  ) {
    vertical = true;

    // Traverse up
    let currentRow = row;
    while (
      document.querySelector(`[data-row="${currentRow}"][data-col="${col}"]`)?.classList.contains(
        "ship"
      )
    ) {
      cells.push({ row: currentRow, col });
      length++;
      currentRow--;
    }

    // Traverse down
    currentRow = row + 1;
    while (
      document.querySelector(`[data-row="${currentRow}"][data-col="${col}"]`)?.classList.contains(
        "ship"
      )
    ) {
      cells.push({ row: currentRow, col });
      length++;
      currentRow++;
    }
  } else {
    // Traverse left and right (horizontal)
    let currentCol = col;

    // Traverse left
    while (
      document.querySelector(`[data-row="${row}"][data-col="${currentCol}"]`)?.classList.contains(
        "ship"
      )
    ) {
      cells.push({ row, col: currentCol });
      length++;
      currentCol--;
    }

    // Traverse right
    currentCol = col + 1;
    while (
      document.querySelector(`[data-row="${row}"][data-col="${currentCol}"]`)?.classList.contains(
        "ship"
      )
    ) {
      cells.push({ row, col: currentCol });
      length++;
      currentCol++;
    }
  }

  return { length, vertical, cells };
}

// Remove dots when a ship is removed
function removeShipFromFleet(row, col, length, vertical) {
  const surroundingCells = [];

  for (let i = 0; i < length; i++) {
    const targetRow = vertical ? row + i : row;
    const targetCol = vertical ? col : col + i;

    const cell = document.querySelector(`[data-row="${targetRow}"][data-col="${targetCol}"]`);
    if (cell) cell.classList.remove("ship");

    addSurroundingCells(targetRow, targetCol, surroundingCells);
  }

  // Refresh dots after removing the ship
  refreshDotsOnFleet();
}

//CONFIRMATION
function updateConfirmButtonState() { // Enable/Disable the button based on "Ships Left"
  const allShipsPlaced = shipQueue.every((ship) => ship.count === 0);
  confirmButton.disabled = !allShipsPlaced; // Enable only if all ships are placed
}

// Add event listener to confirmation button
confirmButton.addEventListener("click", () => {
  const grid = generateGridArray().toString().replaceAll(',','');
  //console.log("Ship Placement Array:", gridArray); // Print to console or pass to backend
  const ip = true ? location.hostname : "172.20.10.12";  // false for developing
  const url = "http://" + ip + ":8080/queue/enterQueue";

  const params = new URL(document.location.toString()).searchParams;
  const  username = params.get("username");
  
  fetch(url, {
    method: 'POST', // HTTP method
    headers: {
        'Content-Type': 'application/json', // Specifies the content type (e.g., JSON)
    },
    body: JSON.stringify({
        username,
        grid
      }), // The data to send
  })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json(); // Convert the response to JSON
    })
    .then(data => {
        console.log('Success:', data); // Handle the response data
        window.location.replace("http://" + ip + ":5500/UI/GamePage/index.html?username=" + username);  //redirecting to gamePage

    })
    .catch(error => {
        console.error('Error:', error); // Handle any errors
    });

  
});

// Generate the 10x10 grid array
function generateGridArray() {
  const gridArray = Array.from({ length: 10 }, () => Array(10).fill(3)); // Initialize 10x10 grid with 0s

  myFleetGrid.querySelectorAll(".ship").forEach((cell) => {
    const row = parseInt(cell.dataset.row, 10) - 1; // Convert to 0-indexed
    const col = parseInt(cell.dataset.col, 10) - 1; // Convert to 0-indexed
    gridArray[row][col] = 4; // Mark cell as occupied
  });
  


  //console.log("Generated Grid Array:", gridArray); // Log once after the array is fully constructed
  return gridArray;
  
}

