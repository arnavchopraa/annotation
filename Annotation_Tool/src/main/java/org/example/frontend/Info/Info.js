const token = localStorage.getItem('token')
document.addEventListener('DOMContentLoaded', function() {
    fetch("http://localhost:8080/users/me", {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    }).then(response => {
        // Check if the response is successful (status code 200)

        if (response.ok) {
            // Parse the JSON response
            return response.json();
        } else {
            // If the response is not successful, throw an error

            throw new Error('Failed to fetch user');
        }
    })
        .then(obj =>{
            sessionEmail = obj.email;
            // I don't see how we display admin functionality, this is if it doesn't happen:
            role = obj.role
            if(role === 'student')
                window.location.href = '../Student/Student.html'
        })
        .catch(error => {
            // Handle any errors that occur during the fetch
            console.log(error)
        });
    const observer = new IntersectionObserver(entries => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('in-view');
            } else {
                entry.target.classList.remove('in-view');
            }
        });
    }, { threshold: 0.5 });

    const images = document.querySelectorAll('.step-image');
    images.forEach(image => {
        observer.observe(image);
    });
});

