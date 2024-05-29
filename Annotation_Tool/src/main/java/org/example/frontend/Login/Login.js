function handleFormSubmission(event) {
    event.preventDefault();

    const login = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;

    var endpoint =`http://localhost:8080/users/${login}`;
    fetch(endpoint, {
        mode: 'no-cors',
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => {
            console.log(response.json)
        })
        .catch(error => console.error('Error fetching codes: ', error));
}