document.addEventListener('DOMContentLoaded', function() {
    localStorage.clear()
})
function handleFormSubmission(event) {
    event.preventDefault();

    const login = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;

    var endpoint = 'http://localhost:8080/auth/login';

    fetch(endpoint, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: login,
            password: password
        })
    }).then(response => {
        // Check if the response is successful (status code 200)
        if (response.ok) {
            // Parse the JSON response
            return {
                success: true,
                resp: response.text()
            }
        } else {
            // If the response is not successful, throw an error
            return response.text()
        }
    })
    .then(message => {
        if(message.success)
            return message.resp
        else
            throw new Error(message)
    })
        .then(x => {
            localStorage.setItem('token', x)
            localStorage.setItem('username', login)
            fetch( "http://localhost:8080/users/me", {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${x}`
                }
            }).then(response => response.json()).then(pageDetails => {
                console.log(pageDetails)
                if (pageDetails.role === 'student') {
                    window.location.href = "../Student/Student.html";
                } else if (pageDetails.role === 'admin' || pageDetails.role === 'supervisor') {
                    window.location.href = "../Dashboard/Dashboard.html"
                }
            })
        })
    .catch(error => {
        // If there is an error with the request, display an error message
        alert(error.message);
    });
}
