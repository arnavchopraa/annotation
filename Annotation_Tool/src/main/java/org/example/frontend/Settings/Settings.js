const arr = document.querySelectorAll('.student')
const dashboard = document.querySelector('#settingsDashboard')
const role = localStorage.getItem('role')

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