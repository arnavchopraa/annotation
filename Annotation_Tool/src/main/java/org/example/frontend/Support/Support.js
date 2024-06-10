document.addEventListener('DOMContentLoaded', function() {
    const slides = document.querySelectorAll('.slide');
    const options = document.querySelectorAll('.option');
    const backButton = document.querySelector('.back-button');

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
        });
    }

    options.forEach((option, index) => {
        option.addEventListener('click', () => {
            showSlide(index + 1);
        });
    });

    backButton.addEventListener('click', () => {
        showSlide(0);
    });

    // Initially show the first slide
    showSlide(0);
});
