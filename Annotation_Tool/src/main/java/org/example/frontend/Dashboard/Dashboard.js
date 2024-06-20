var sessionEmail;
var role;
let token = localStorage.getItem('token')
const logout = document.querySelector('#logout')

logout.addEventListener('click', () => {
    localStorage.clear();
    window.location.href = '../Login/Login.html';
});
let displayedSubmissions
let sortBy = 'lastSubmitted'
let orderSortBy = 'desc'

document.addEventListener('DOMContentLoaded', function () {
    fetch("http://localhost:8080/users/me", {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    }).then(response => {
        // Check if the response is successful (status code 200)

        if (response.ok) {
            // Parse the JSON response
            return response.json();
        } else {
            // If the response is not successful, throw an error

            throw new Error('Failed to fetch user');
        }
    })
        .then(obj =>{
            sessionEmail = obj.email;
            // I don't see how we display admin functionality, this is if it doesn't happen:
            role = obj.role
            if(role === 'student')
                window.location.href = '../Student/Student.html'
            getFiles()
            getRecentlySubmitted()
        })
        .catch(error => {
            // Handle any errors that occur during the fetch
            console.log(error)
        });
})

/**
    * Methood to get the files assigned to the supervisor
*/
function getFiles() {
    console.log(sessionEmail)
    var endpoint =`http://localhost:8080/submissions/coordinator/${sessionEmail}`;

    fetch(endpoint, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    }).then(response => {
        // Check if the response is successful (status code 200)
        if (response.ok) {
            // Parse the JSON response
            return response.json();
        } else {
            // If the response is not successful, throw an error

            throw new Error('Failed to fetch user');
        }
    })
        .then(submissions => {
            displayedSubmissions = submissions
            sortSubmitted(orderSortBy)
        })
        .catch(error => {
            // Handle any errors that occur during the fetch
            console.log(error)
        });
}


/**
    * Method to display the submissions assigned to the supervisor
*/
function displaySubmissions(submissions) {
    clearSearchResults();
    const table = document.getElementById('table-content');

    localStorage.setItem('sublength', submissions.length)
    for(let index = 0; index < submissions.length; index++) {
        let sub = submissions[index]
        let name = "submission"+index
        localStorage.setItem(name, sub.id)

        const line = document.createElement('div')
        line.className = 'table-line'

        const one = document.createElement('p')
        one.className = 'table-cell'
        one.innerText = sub.id;
        line.appendChild(one)

        const two = document.createElement('p')
        two.className = 'table-cell'
        if(sub.lastEdited == null)
            two.innerText = 'Never';
        else
            two.innerText = sub.lastEdited;
        line.appendChild(two);

        const three = document.createElement('p')
        three.className = 'table-cell'
        three.innerText = sub.groupName;
        line.appendChild(three);

        const four = document.createElement('p')
        four.className = 'table-cell'
        if(sub.submitted === true)
            four.innerText = 'Yes';
        else
            four.innerText = 'No';
        line.appendChild(four);

        line.addEventListener('click', function() {
            localStorage.setItem('whichList', 'center')
            localStorage.setItem('file', sub.id)
            localStorage.setItem('curidx', index)
            verifyLock(sub.id)
        });

        table.appendChild(line)
    }
}

/*
    Listening to keyboard typing in the searchbar
*/
document.getElementById("search").addEventListener("keyup", () => {
    getSearchResults(document.getElementById("search").value)
});

/**
    * Method to get the search results
    * @param writtenText - the text written in the search bar
*/
function getSearchResults(writtenText) {

    if(writtenText == "") {
        getFiles();
        return;
    }

    var endpoint =`http://localhost:8080/submissions/search/${writtenText}/${sessionEmail}`;
    fetch(endpoint, {
       method: "GET",
       headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
       }
    }).then(response => {
        if (response.ok) {
           return response.json();
        } else {
            throw new Error('Failed to fetch user');
        }
    })
    .then(submissions => {
        displayedSubmissions = submissions
        if(sortBy === 'name')
            sortName(orderSortBy)
        else if(sortBy === 'lastEdited')
            sortLastEdited(orderSortBy)
        else if(sortBy === 'lastSubmitted')
            sortSubmitted(orderSortBy)
        else
            displaySubmissions(submissions);
    })
    .catch(error => {
        console.error("Could not fetch submissions: " , error);
    });
}

/**
    * Method to clear the search results
*/
function clearSearchResults() {
    document.getElementById("table-content").innerHTML = "";
}

/**
    * Method to get the recently submitted files
*/
function getRecentlySubmitted() {
    var endpoint =`http://localhost:8080/submissions/${sessionEmail}/submitted`;

    fetch(endpoint, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if(response.ok)
            return response.json()
        else
            throw new Error("Couldn't retrieve files")
    })
    .then(submissions => {
        displayRecentlySubmitted(submissions)
    })
    .catch(error => console.error(error)) // TODO - better error handling
}

/**
    * Method to display the top 5 most recently submitted files
*/
function displayRecentlySubmitted(submissions) {

    submissions = Array.from(submissions).slice(0, 5)
    localStorage.setItem('rightlength', submissions.length)
    for(let index = 0; index < submissions.length; index++) {
        let cursub = submissions[index]
        let name = 'rightsub'+index
        localStorage.setItem(name, cursub.id)

        const link = document.createElement('div')
        link.className = 'top-line'

        const wrap = document.createElement('div')
        wrap.className = 'top-text'

        const p1 = document.createElement('p')
        p1.className = 'big-p'
        const p2 = document.createElement('p')
        p2.className = 'grey-p'
        const text1 = document.createTextNode(cursub.id)
        const text2 = document.createTextNode('Submitted at ' + cursub.lastSubmitted)
        p1.appendChild(text1)
        p2.appendChild(text2)

        wrap.appendChild(p1)
        wrap.appendChild(p2)
        link.appendChild(wrap)

        topSection.appendChild(link)
    }
}

/**
    * Method to redirect to the file's annotation page when it is clicked in the recently submitted section
*/
const topSection = document.getElementById('topContent')
topSection.addEventListener('click', (e) => {
    if(e.target.className === 'top-line') {
        localStorage.setItem('whichList', 'right')
        localStorage.setItem('file', e.target.firstElementChild.firstElementChild.innerText)
        localStorage.setItem('curidx', Array.from(topSection.children).indexOf(e.target))
        window.location.href = '../Annotation/Annotation.html'
    }
})


/**
    * Method to animate the arrows when clicked
    * Sorts the table by the column clicked, either ascending or descending
*/
const arrows = document.querySelectorAll('.arrow-icon');
arrows.forEach(arrow => {
    console.log(arrow.id);
    arrow.addEventListener('click', () => {
        if (arrow.classList.contains('sorted')) {
            sortOrder(arrow.id, 'asc');
            arrow.classList.remove('sorted');
        } else {
            sortOrder(arrow.id, 'desc');
            arrow.classList.add('sorted');
        }
    });
});

/**
    * Method to sort the table by the column clicked
*/
function sortOrder(id, order) {
    switch(id) {
        case 'nameArrow':
            sortBy = 'name'
            orderSortBy = order
            sortName(order);
            break;
        case 'lastEditedArrow':
            sortBy = 'lastEdited'
            orderSortBy = order
            sortLastEdited(order);
            break;
        case 'lastSubmittedArrow':
            sortBy = 'lastSubmitted'
            orderSortBy = order
            sortSubmitted(order);
            break;
        default:
            displaySubmissions(displayedSubmissions)
    }
}

/**
    * Method to sort the table by the name of the student
    * @param order - the order to sort the table by
*/
function sortName(order) {
    let sortedSubmissions = Array.from(displayedSubmissions).sort((a, b) => {
        if(a.id < b.id) {
            if(order == 'asc') {
                return 1;
            } else {
                return -1;
            }
        }
        else if(order == 'asc') {
            return -1;
        }
        return 1;
    })
    displaySubmissions(sortedSubmissions)
}


/**
    * Method to sort the table by the last edited date
    * @param order - the order to sort the table by
*/
function sortLastEdited(order) {
    let sortedSubmissions = Array.from(displayedSubmissions).sort((a, b) => {
        if(a.lastEdited === null) {
            if(order === 'asc')
                return 1
            return -1
        }
        if(b.lastEdited === null) {
            if(order === 'asc')
                return -1
            return 1
        }
        if(a.lastEdited < b.lastEdited) {
            if(order == 'asc') {
                return 1;
            } else {
                return -1;
            }
        }
        else if(order == 'asc') {
            return -1;
        }
        return 1;
    })
    displaySubmissions(sortedSubmissions)
}

/**
    * Method to sort the table by the last submitted date
    * @param order - the order to sort the table by
*/
function sortSubmitted(order) {
    let sortedSubmissions = Array.from(displayedSubmissions).sort((a, b) => {
        if(a.lastSubmitted === null) {
            if(order === 'asc')
                return 1
            return -1
        }
        if(b.lastSubmitted === null) {
            if(order === 'asc')
                return -1
            return 1
        }
        if(a.lastSubmitted < b.lastSubmitted) {
            if(order == 'asc') {
                return 1;
            } else {
                return -1;
            }
        }
        else if(order == 'asc') {
            return -1;
        }
        return 1;
    })
    displaySubmissions(sortedSubmissions)
}

function verifyLock(check) {
    const endpoint = `http://localhost:8080/submissions/${check}/getLock`

    fetch(endpoint, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
    }
    }).then(response => {
        if(response.ok) {
            return response.json()
        }
        else {
            throw new Error("Failed to retrieve current submission lock")
        }
    }).then(lock => {
        if(lock === true) {
            alert("This file is being edited by someone else")
        }
        else {
            window.location.href = "../Annotation/Annotation.html"
        }
    })
}