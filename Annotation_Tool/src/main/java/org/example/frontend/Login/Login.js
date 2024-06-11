function handleFormSubmission(event) {
    event.preventDefault();

    const login = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;

    console.log(password)

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
            return response.text()
        } else {
            // If the response is not successful, throw an error
            throw new Error('Invalid login credentials. Please try again.');
        }
    })
        .then(x => {
            localStorage.setItem('role', x)
            localStorage.setItem('username', login)
            if(x === 'student') {
                window.location.href = "../Student/Student.html";
            } else {
                window.location.href = "../Dashboard/Dashboard.html";
            }
        })
    .catch(error => {
        // If there is an error with the request, display an error message
        alert(error.message);
    });
}
