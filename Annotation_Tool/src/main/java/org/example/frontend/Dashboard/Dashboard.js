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
            const table = document.getElementById('table-content');

            let index = 0;

            submissions.forEach(sub => {
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
                three.innerText = 'No';
                line.appendChild(three);

                const four = document.createElement('p')
                four.className = 'table-cell'
                four.innerText = 'No';
                line.appendChild(four);

                line.addEventListener('click', function() {
                    localStorage.setItem('file', sub.id)
                    window.location.href = "../Annotation/Annotation.html"
                });


                table.appendChild(line)
            })

        })
        .catch(error => {
            // Handle any errors that occur during the fetch
            console.log(error)
        });
}