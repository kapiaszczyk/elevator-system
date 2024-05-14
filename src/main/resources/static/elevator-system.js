var stompClient = null;
var floorHeight = 20;
var elevatorHeight = 20;
var elevatorWidth = 10;
var simulationHeight = floorHeight * numFloors + 40;
var areElevatorsCreated = false;

function connectWebSocket() {
    var socket = new SockJS('/states');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/elevator-states', function (message) {
            var states = JSON.parse(message.body);
            updateTable(states);
            updateElevatorPositions(states);
        });
    });
}

function disconnectWebSocket() {
    if (stompClient !== null) {
        stompClient.disconnect();
        console.log('Disconnected');
        stompClient = null;
    }
}

function createElevatorDivs(numElevators) {
    var elevatorContainer = document.getElementById('elevatorContainer');
    elevatorContainer.innerHTML = ''; // Clear previous contents
    elevatorContainer.style.height = simulationHeight + 'px';

    for (var i = 0; i < numElevators; i++) {
        var elevatorDiv = document.createElement('div');
        elevatorDiv.className = 'elevator';
        elevatorDiv.style.left = (i * 30) + 'px';
        elevatorContainer.appendChild(elevatorDiv);
    }

    areElevatorsCreated = true;
}

function updateElevatorPositions(states) {
    if(!areElevatorsCreated) {
        createElevatorDivs(states.length);
    }

    states.forEach(function(elevatorState, index) {
        var elevatorElement = document.getElementsByClassName('elevator')[index];
        if (elevatorElement) {
            var bottomPos = elevatorState.currentFloor * floorHeight;
            elevatorElement.style.bottom = bottomPos + 'px';
        }
    });
}

function updateTable(states) {

    var tableBody = document.getElementById('stateTableBody');
    tableBody.innerHTML = '';

    states.forEach(function(state) {
        var row = tableBody.insertRow();
        var elevatorId = row.insertCell(0);
        var currentFloor = row.insertCell(1);
        var destinationFloor = row.insertCell(2);
        elevatorId.textContent = state.elevatorId;
        currentFloor.textContent = state.currentFloor;
        destinationFloor.textContent = state.destinationFloor;
    });
}

function sendSimulationRequest(apiEndpoint) {
    fetch(apiEndpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: null
    })
    .then(response => {
        if (response.ok) {
            console.log('Request completed');
        } else {
            console.error('Failed to complete request');
        }
    })
    .catch(error => {
        console.error('Failed to complete request:', error);
    });
}

function sendGETRequest(apiEndpoint) {
    return fetch(apiEndpoint, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            console.log('Request completed');
            return response.json();
        } else {
            console.error('Failed to complete request');
            throw new Error('Failed to complete request');
        }
    })
    .catch(error => {
        console.error('Failed to complete request:', error);
        throw error;
    });
}


function updateElevatorState() {
        const elevatorId = document.getElementById('elevatorId').value;
        const currentFloor = document.getElementById('currentFloor').value;
        const destinationFloor = document.getElementById('destinationFloor').value;

        const apiUrl = `/api/v1/simulation/system/elevator/${elevatorId}/state/current-floor/${currentFloor}/destination-floor/${destinationFloor}`;

        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: null
        })
        .then(response => {
            if (response.ok) {
                console.log('Data sent successfully!');
            } else {
                console.error('Failed to send data.');
            }
        })
        .catch(error => {
            console.error('Error sending data:', error);
        });
    }

function emitFloorDispatchEvent() {
      const floorOrigin = document.getElementById('floorOrigin').value;
      const direction = document.getElementById('directionInput').value;

      const data = {
          eventType: "FLOOR_DISPATCH_EVENT",
          floorOrigin: parseInt(floorOrigin),
          direction: direction
      };

      fetch('/api/v1/simulation/system/event', {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
      })
      .then(response => {
          if (response.ok) {
              console.log('Data sent successfully!');
          } else {
              console.error('Failed to send data.');
          }
      })
      .catch(error => {
          console.error('Error sending data:', error);
      });
}

function fetchElevatorStates() {
    sendGETRequest('/api/v1/simulation/system/state')
        .then(data => {
            if (data) {
                var tableBody = document.getElementById('fetchedData');
                tableBody.innerHTML = '';

                data.forEach(function(state) {
                    var row = tableBody.insertRow();
                    var elevatorId = row.insertCell(0);
                    var currentFloor = row.insertCell(1);
                    var destinationFloor = row.insertCell(2);
                    elevatorId.textContent = state.elevatorId;
                    currentFloor.textContent = state.currentFloor;
                    destinationFloor.textContent = state.destinationFloor;
                });
            } else {
                console.log("Failed to fetch elevator states.");
            }
        })
        .catch(error => {
            console.error('Failed to complete request:', error);
        });
}