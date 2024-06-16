const arr = document.querySelectorAll('.student')
const dashboard = document.querySelector('#settingsDashboard')
const role = localStorage.getItem('role')
let sessionEmail = localStorage.getItem('username')

dashboard.addEventListener('click', (e) => {
    e.preventDefault();
    if(role === 'student') {
        window.location.href = "../Student/Student.html";
    } else {
        window.location.href = "../Annotation/Annotation.js";
    }
});

document.addEventListener('DOMContentLoaded', () => {
    if(role === 'student') {
        arr.forEach(elem => {
            console.log(elem)
            elem.style.display = 'none'
        })
    }
});

document.getElementById('downloadSubmitted').addEventListener('click', function () {
    var endpoint = `http://localhost:8080/frontend/allsubmissions/${sessionEmail}`
    fetch(endpoint, {
        method: 'GET',
        headers: {
            "Content-Type": "application/zip"
        }
    })
    .then(response => {
        if(response.ok)
            return response.blob()
        else
            throw new Error("Couldn't fetch downloads")
    })
    .then(bytes => {
        var blob = new Blob([bytes], {type: 'application/zip'})
        var a = document.createElement('a')

        a.download = 'download.zip'
        a.style = 'display: none'
        let url = window.URL.createObjectURL(blob)
        a.href = url
        a.click()
        a.remove()
        window.URL.revokeObjectURL(url)
    })
    .catch(error => console.error(error))
})

document.getElementById("save").addEventListener('click', (e) => {
    e.preventDefault()

    let oldPassword = document.getElementById("oldPassword").value
    let newPassword = document.getElementById("newPassword").value
    let newPasswordConfirmation = document.getElementById("newPasswordConfirmation").value

    if(newPassword.length == 0) {
        alert("New password cannot be empty")
        return
    }

    if(!(newPassword === newPasswordConfirmation)) {
        alert("Please make sure that the new password matches the confirmation!")
        return
    }

    const endpoint = `http://localhost:8080/users/updatePassword/${sessionEmail}`;

    fetch(endpoint, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            oldPassword: oldPassword,
            newPassword: newPassword
        })
    }).then(response => {
        if(response.ok) {
            alert("The password was successfully updated")
            console.log("Successfully changed password")
            document.getElementById("oldPassword").value = ""
            document.getElementById("newPassword").value = ""
            document.getElementById("newPasswordConfirmation").value = ""
        } else if (response.status === 403) {
            alert("Incorrect old password")
            console.log("Incorrect old password.");
        } else if (response.status === 404) {
            console.log("User not found.");
        } else {
            console.log("An error occurred with the hashing service");
        }
    })
})
