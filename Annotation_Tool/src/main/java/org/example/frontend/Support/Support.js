const slides = document.querySelectorAll('.slide');
const options = document.querySelectorAll('.option');
const backButtons = document.querySelectorAll('.back-button');
const textFields = document.querySelectorAll('.text-field');
const arr = document.querySelectorAll('.student')
const faqQuestions = document.querySelectorAll('.faq-question');
const dashboard = document.querySelector('#settingsDashboard')
const token = localStorage.getItem('token')
var role;

dashboard.addEventListener('click', (e) => {
    e.preventDefault();
    if(role === 'student') {
        window.location.href = "../Student/Student.html";
    } else {
        window.location.href = "../Annotation/Annotation.html";
    }
});


/**
    * @returns {void} adds an event listener to each option and associates them with the corresponding slide
*/
document.addEventListener('DOMContentLoaded', function() {
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
    options.forEach((option, index) => {
        option.addEventListener('click', () => {
            showSlide(index + 1);
        });
    });

    openSlide();
    faqOpenAndClose();
});

/**
    * @returns {void} shows the first slide when the back button is clicked
*/
backButtons.forEach(backButton => {
    backButton.addEventListener('click', () => {
        showSlide(0);
    });
});

/**
    * @param {number} index - the index of the slide to show
    * @returns {void} shows the slide with the given index
*/
function showSlide(index) {
    const slider = document.getElementById('slider');
    slides.forEach((slide, i) => {
        if (i === index) {
            slide.classList.add('active');

            if (i == 3) {
                slider.classList.add('feedback');
            } else {
                slider.classList.remove('feedback');
            }
        } else {
            slide.classList.remove('active');
            slider.classList.remove('feedback');
        }

        updateURL(slideName(index));
    });
}

/**
    * @returns {void} opens the slide corresponding to the url's query parameter
*/
function openSlide() {
    const urlParams = new URLSearchParams(window.location.search);
    showSlide(indexSlide(urlParams.get('slide')));
}

/**
    * @param {string} slide - the name of the slide
    * @returns {void} updates the page's url with the given slide name
*/
function updateURL(slide) {
    const url = new URL(window.location);
    if (slide) {
        url.searchParams.set('slide', slide);
    } else {
        url.searchParams.delete('slide');
    }
    history.pushState(null, '', url);
}

/**
     * @param {number} index
     * @returns {name} name corresponding to the slide with the given index
 */
function slideName(index) {
    switch(index) {
        case 1: return 'contact';
        case 2: return 'faq';
        case 3: return 'feedback';
        default: return 'support';
    }
}

/**
    * @param {string} name
    * @returns {number} index corresponding to the slide with the given name
*/
function indexSlide(name) {
    switch(name) {
        case 'contact': return 1;
        case 'faq': return 2;
        case 'feedback': return 3;
        default: return 0;
    }
}

/**
    * @returns {void} opens and closes the answer to the faq question
*/
function faqOpenAndClose() {
    faqQuestions.forEach((question, index) => {
        question.addEventListener('click', () => {
            const answer = question.nextElementSibling;
            const plusWrap = question.querySelector('.faq-plus-wrap');
            const plusI = question.querySelector('.faq-plus-i');

            if (answer.classList.contains('expanded')) {
                answer.classList.remove('expanded');
                plusWrap.classList.remove('rotated');
                plusI.classList.remove('hidden');
                answer.style.height = '0';
            } else {
                answer.classList.add('expanded');
                plusWrap.classList.add('rotated');
                plusI.classList.add('hidden');
                answer.style.height = 'auto';
            }
        });
    });
}

document.getElementById('contactSubmit').addEventListener('click', function () {
    // do backend logic here
    const inboxAddress = 'e03601394@gmail.com';

    const cfName = document.getElementById('cFName')
    const clName = document.getElementById('cLName')
    const cEmail = document.getElementById('cEmail')
    const cPhone = document.getElementById('cPhone')
    const cMessage = document.getElementById('cPhone')
    let fd = new FormData()
    fd.append('firstName', cfName.value)
    fd.append('lastName', clName.value)
    fd.append('email', cEmail.value)
    fd.append('phone', cPhone.value)
    fd.append('message', cMessage.value)
    // if (response.ok)
    fetch(`http://localhost:8080/frontend/sendMail/${inboxAddress}`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            firstName: cfName.value,
            lastName: clName.value,
            email: cEmail.value,
            phone: cPhone.value,
            message: cMessage.value
        })
    }).then(response => {
        // Check if the response is successful (status code 200)
        if (response.ok) {
            // Parse the JSON response
            cfName.value = '';
            clName.value = '';
            cEmail.value = '';
            cPhone.value = '';
            cMessage.value = '';
            displaySavedPopUp("Your message has been successfully submitted!");
        } else {
            if(response.status === 400) {
                throw new Error("400")
            } else {
                throw new Error("500")
            }
        }
    })
        .catch(e => {
            if(e === 400)
                displayErrorPopUp("Please fill all of the inputs of the form!", false);
            else
                displayErrorPopUp("Something went wrong. Please try again!", false);
        })

});

document.getElementById('feedbackSubmit').addEventListener('click', function () {
    // do backend logic here
    // do backend logic here
    const inboxAddress = 'e03601394@gmail.com';

    const fName = document.getElementById('fName')
    const fRole = document.getElementById('fRole')
    const fMessage = document.getElementById('fMessage')
    let fd = new FormData()
    fd.append('name', fName.value)
    fd.append('role', fRole.value)
    fd.append('message', fMessage.value)
    console.log(fd)
    // if (response.ok)
    fetch(`http://localhost:8080/frontend/sendFeedback/${inboxAddress}`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            name: fName.value,
            role: fRole.value,
            message: fMessage.value
        })
    }).then(response => {
        // Check if the response is successful (status code 200)
        if (response.ok) {
            // Parse the JSON response
            fName.value = '';
            fRole.value = '';
            fMessage.value = '';
            return response.text();
        } else {
            if(response.status === 400) {
                throw new Error("400")
            } else {
                throw new Error("500")
            }
        }
    }).then(txt => displaySavedPopUp("Your feedback has been successfully submitted!"))
        .catch(e => {
            if(e === 400)
                displayErrorPopUp("Please fill all of the inputs of the form!", false);
            else
                displayErrorPopUp("Something went wrong. Please try again!", false);
        })
});