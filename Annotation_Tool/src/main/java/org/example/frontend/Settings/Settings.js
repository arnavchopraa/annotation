const arr = document.querySelectorAll('.student')
const dashboard = document.querySelector('#settingsDashboard')
var role;
const token = localStorage.getItem('token')
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
    fetch( "http://localhost:8080/users/me", {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    }).then(response => response.json()).then(pageDetails => {
        console.log(pageDetails.role)
        role = pageDetails.role
        if(role === 'student') {
            arr.forEach(elem => {
                // console.log(elem)
                elem.style.display = 'none'
            })
        }
    })
});

document.getElementById('downloadSubmitted').addEventListener('click', function () {
    var endpoint = `http://localhost:8080/frontend/allsubmissions/${sessionEmail}`
    fetch(endpoint, {
        method: 'GET',
        headers: {
            "Content-Type": "application/zip",
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if(response.ok) {
            displaySavedPopUp("Your submissions have been downloaded successfully!");
            return response.blob()
        }
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

document.getElementById("savePassword").addEventListener('click', (e) => {
    e.preventDefault()

    let oldPassword = document.getElementById("oldPassword").value
    let newPassword = document.getElementById("newPassword").value
    let newPasswordConfirmation = document.getElementById("newPasswordConfirmation").value

    if(newPassword.length == 0) {
        displayErrorPopUp("New password cannot be empty.", false);
        return
    }

    if(!(newPassword === newPasswordConfirmation)) {
        displayErrorPopUp("Please make sure that the new password matches the confirmation!", false);
        return
    }

    const endpoint = `http://localhost:8080/users/updatePassword/${sessionEmail}`;

    fetch(endpoint, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            oldPassword: oldPassword,
            newPassword: newPassword
        })
    }).then(response => {
        if(response.ok) {
            document.getElementById("oldPassword").value = ""
            document.getElementById("newPassword").value = ""
            document.getElementById("newPasswordConfirmation").value = ""
            return {
                success: true,
                resp: "Your password has been updated!"
            }
        } else if(response.status == 400) {
            return "Fields cannot be empty. In case they are not empty, something went wrong while processing the request."
        } else if (response.status === 403) {
            return "Incorrect old password! Please try again"
        } else if (response.status === 404) {
            return "User not found"
        } else {
            return "An error occurred when attempting to hash the new password"
        }
    })
    .then(message => {
        if(message.success)
            displaySavedPopUp(message.resp)
        else
            throw new Error(message)
    })
    .catch(error => displayErrorPopUp(error, false))
})

document.getElementById("deleteAccount").addEventListener('click', (e) => {
    e.preventDefault();

    Swal.fire({
        title: 'Are you sure?',
        text: 'Are you sure you want to delete your account? Note that this process is irreversible.',
        icon: 'warning',
        iconColor: '#bd3233',
        color: '#a6a6a6',
        showCancelButton: true,
        confirmButtonText: 'Yes, delete it!',
        cancelButtonText: 'No, cancel!',
        customClass: {
            popup: 'popup-container',
            title: 'popup-title',
            confirmButton: 'popup-confirm-delete-button',
            cancelButton: 'popup-cancel-button'
        },
        buttonsStyling: false,
    }).then((result) => {
        if (result.isConfirmed) {
            const endpoint = `http://localhost:8080/users/${sessionEmail}`

                fetch(endpoint, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                }).then(response => {
                    if(response.ok) {
                        alert("The account was successfully deleted!")
                        window.location.href = "../Login/Login.html";
                    }
                    else {
                        alert("Failed to delete account!")
                    }
                })
        } else if(result.dismiss === Swal.DismissReason.cancel) {
            displayCancelPopUp("Your account has not been deleted.");
        }
    })
    .catch(error => console.error(error));
});
