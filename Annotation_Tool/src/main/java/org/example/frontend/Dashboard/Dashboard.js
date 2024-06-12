const sessionEmail = localStorage.getItem('username')

document.addEventListener('DOMContentLoaded', function () {
    getFiles();
})

function getFiles() {
    var endpoint =`http://localhost:8080/submissions/coordinator/${sessionEmail}`;

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
                        localStorage.setItem('file', sub.id)
                        localStorage.setItem('curidx', index)
                        window.location.href = "../Annotation/Annotation.html"
                    });

                    table.appendChild(line)
                }
}

/*
    Listening to keyboard typing in the searchbar
*/
document.getElementById("search").addEventListener("keyup", () => {
                                        getSearchResults(document.getElementById("search").value)
                                    })

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

function clearSearchResults() {
    document.getElementById("table-content").innerHTML = "";
}