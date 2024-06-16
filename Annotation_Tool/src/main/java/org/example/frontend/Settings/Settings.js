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