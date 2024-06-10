const slides = document.querySelectorAll('.slide');
const options = document.querySelectorAll('.option');
const backButton = document.querySelector('.back-button');
const textFields = document.querySelectorAll('.text-field');

/**
    * @returns {void} adds an event listener to each option and associates them with the corresponding slide
*/
document.addEventListener('DOMContentLoaded', function() {
    options.forEach((option, index) => {
        option.addEventListener('click', () => {
            showSlide(index + 1);
        });
    });

    openSlide();
    removePlaceholder();
});

/**
    * @returns {void} shows the first slide when the back button is clicked
*/
backButton.addEventListener('click', () => {
    showSlide(0);
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

        if (index === 0) {
            backButton.style.display = 'none';
        } else {
            backButton.style.display = 'flex';
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

function removePlaceholder() {
    textFields.forEach(function(textField) {
        textField.addEventListener('input', function() {
            if (this.textContent.trim() !== '') {
                this.setAttribute('data-empty', 'false');
            } else {
                this.setAttribute('data-empty', 'true');
            }
        });
    });
}