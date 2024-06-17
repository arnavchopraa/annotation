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
        window.location.href = "../Annotation/Annotation.js";
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
    removePlaceholder();
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
    slides.forEach((slide, i) => {
        if (i === index) {
            slide.classList.add('active');
        } else {
            slide.classList.remove('active');
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
    // if (response.ok)
    displaySavedPopUp("Your message has been successfully submitted!");
});

document.getElementById('feedbackSubmit').addEventListener('click', function () {
    // do backend logic here
    // if (response.ok)
    displaySavedPopUp("Your feedback has been successfully submitted!");
});