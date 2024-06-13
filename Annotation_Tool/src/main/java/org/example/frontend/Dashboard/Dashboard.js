const sessionEmail = localStorage.getItem('username')
const topSection = document.getElementById('top-content')

document.addEventListener('DOMContentLoaded', function () {
    getFiles();
    getRecentlySubmitted();
})

function getFiles() {
    var endpoint =`http://localhost:8080/submissions/notsubmitted/${sessionEmail}`;

    fetch(endpoint, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
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
            displaySubmissions(submissions);
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
        if(sub.submitted === true)
            three.innerText = 'Yes';
        else
            three.innerText = 'No';
        line.appendChild(three);

                    line.addEventListener('click', function() {
                        localStorage.setItem('whichList', 'center')
                        localStorage.setItem('file', sub.id)
                        localStorage.setItem('curidx', index)
                        window.location.href = "../Annotation/Annotation.html"
                    });

        table.appendChild(line)
    }
}

/**
    * Method to animate the arrows when clicked
    * Should be expanded to sort by the column clicked
*/
const arrows = document.querySelectorAll('.arrow-icon');
arrows.forEach(arrow => {
    arrow.addEventListener('click', () => {
        if (arrow.classList.contains('sorted')) {
            arrow.classList.remove('sorted');
        } else {
            arrow.classList.add('sorted');
        }
    });
});

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
       }
    }).then(response => {
        if (response.ok) {
           return response.json();
        } else {
            throw new Error('Failed to fetch user');
        }
    })
    .then(submissions => {
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

function getRecentlySubmitted() {
    var endpoint = `http://localhost:8080/submissions/submitted/${sessionEmail}`

    fetch(endpoint, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
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

function displayRecentlySubmitted(submissions) {

    localStorage.setItem('rightlength', submissions.length)
    for(let index = 0; index < 5 && index < submissions.length; index++) {
        let cursub = submissions[index]
        let name = 'rightsub'+index
        localStorage.setItem(name, cursub.id)

        const link = document.createElement('a')
        link.className = 'top-line'
        link.href = '#' // TODO - pass file to the annotation page

        const wrap = document.createElement('div')
        wrap.className = 'top-text'

        const p1 = document.createElement('p')
        p1.className = 'big-p'
        const p2 = document.createElement('p')
        p2.className = 'grey-p'
        const text1 = document.createTextNode(cursub.id)
        const text2 = document.createTextNode(cursub.lastEdited)
        p1.appendChild(text1)
        p2.appendChild(text2)

        wrap.appendChild(p1)
        wrap.appendChild(p2)
        link.appendChild(wrap)

        topSection.appendChild(link)
    }
}

topSection.addEventListener('click', (e) => {
    if(e.target.className === 'top-line') {
        localStorage.setItem('whichList', 'top')
        localStorage.setItem('file', e.target.firstElementChild.innerText)
        localStorage.setItem('curidx', Array.from(topSection.children).indexOf(e.target))
        window.location.href = '../Annotation/Annotation.html'
    }
})

