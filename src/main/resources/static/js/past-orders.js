// Function to submit ratings
function submitRatings() {
    const orderId = parseFloat(document.getElementById('modal-order-id').value);
    const kitchenRating = document.getElementById('kitchenRating').value;
    const userNote = document.getElementById('userNote').value;
    const itemRatings = [];

    // Collect ratings for each item
    document.querySelectorAll('.order-item').forEach(item => {
      const itemName = item.querySelector('.item-name').innerText;
      const itemRating = item.querySelector('.item-rating').value;

      itemRatings.push({
        itemName: itemName,
        rating: itemRating
      });
    });

    // Prepare data to send to the server
    const data = {
        kitchenRating: kitchenRating,
        userNote: userNote,
        itemRatings: itemRatings
    };

    // Send data to the server
    fetch(`/orders/${orderId}/ratings`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'success') {
        alert('Thank you for your feedback!');
        // Close the modal
        const ratingModal = bootstrap.Modal.getInstance(document.getElementById('ratingModal'));
        ratingModal.hide();
        } else {
        alert('Failed to submit ratings. Please try again.');
        }
    })
    .catch(error => console.error('Error:', error));
}

// Function to handle star hover
function setupStarHover() {
    // Kitchen rating stars
    const kitchenStars = document.querySelectorAll('.kitchen-rating .star');
    kitchenStars.forEach(star => {
        star.addEventListener('mouseover', function() {
            const value = parseInt(this.getAttribute('data-value'));
            highlightStars(kitchenStars, value);
        });
        
        star.addEventListener('mouseout', function() {
            const currentRating = parseInt(document.getElementById('kitchenRating').value);
            highlightStars(kitchenStars, currentRating);
        });
        
        star.addEventListener('click', function() {
            const value = parseInt(this.getAttribute('data-value'));
            document.getElementById('kitchenRating').value = value;
            highlightStars(kitchenStars, value);
        });
    });

    // Item rating stars
    document.querySelectorAll('.order-item .star-rating').forEach(ratingContainer => {
        const stars = ratingContainer.querySelectorAll('.star');
        const ratingInput = ratingContainer.nextElementSibling; // Assuming the input is right after
        
        stars.forEach(star => {
            star.addEventListener('mouseover', function() {
                const value = parseInt(this.getAttribute('data-value'));
                highlightStars(stars, value);
            });
            
            star.addEventListener('mouseout', function() {
                const currentRating = parseInt(ratingInput.value);
                highlightStars(stars, currentRating);
            });
            
            star.addEventListener('click', function() {
                const value = parseInt(this.getAttribute('data-value'));
                ratingInput.value = value;
                highlightStars(stars, value);
            });
        });
    });
}

// Function to fetch and populate ratings
function fetchAndPopulateRatings(orderId) {
    let data = document.getElementById('rate-order');
    let kitchenName = data.getAttribute('data-kitchen-name');
    let kitchenElement = document.getElementById('kitchenName');
    if(kitchenElement){
        kitchenElement.textContent = `${kitchenName}`;
    }
    
    fetch(`/orders/${orderId}/get-ratings`)
    .then(response => response.json())
    .then(data => {
        if (data) {
            // Populate kitchen rating if exists
            if (data.kitchenRating > 0) {
                const ratingField = document.getElementById('kitchenRating');
                const ratingStars = ratingField.closest('.kitchen-rating').querySelectorAll('.star');
                ratingField.value = data.kitchenRating;
                highlightStars(ratingStars, data.kitchenRating);
            }

            // Populate user note if exists
            if (data.userNote) {
                document.getElementById('userNote').value = data.userNote;
            }

            // Populate item ratings if exists
            if (data.itemRatings && data.itemRatings.length > 0) {
                const container = document.getElementById('orderItemsRating');
                
                data.itemRatings.forEach(item => {
                    const itemHTML = `
                    <div class="order-item mb-3">
                        <p class="mb-1 text-dark">Item: <span class="item-name text-warning">${item.itemName}</span></p>
                        <div class="star-rating">
                        <span class="star" data-value="1">&#9733;</span>
                        <span class="star" data-value="2">&#9733;</span>
                        <span class="star" data-value="3">&#9733;</span>
                        <span class="star" data-value="4">&#9733;</span>
                        <span class="star" data-value="5">&#9733;</span>
                        </div>
                        <input type="hidden" class="item-rating" name="itemRating" value="${item.rating || 0}">
                    </div>
                    `;
                    container.insertAdjacentHTML('beforeend', itemHTML);

                    const ratingInput = container.lastElementChild.querySelector('.item-rating');
                    const ratingStars = ratingInput.closest('.order-item').querySelectorAll('.star');
                    highlightStars(ratingStars, item.rating || 0);
                });
            }
            
            // Set up star hover interactions after populating
            setupStarHover();
        }
    })
    .catch(error => console.error('Error fetching ratings:', error));
}

// Function to highlight stars based on rating
function highlightStars(stars, rating) {
stars.forEach((star, index) => {
    if (index < rating) {
    star.classList.add('active');
    } else {
    star.classList.remove('active');
    }
});
}

// Function to reset the modal
function resetRatingModal() {
    // Reset kitchen rating
    const kitchenRatingField = document.getElementById('kitchenRating');
    kitchenRatingField.value = '0';
    const kitchenStars = document.querySelectorAll('.kitchen-rating .star');
    highlightStars(kitchenStars, 0);

    // Reset user note
    document.getElementById('userNote').value = '';

    // Reset item ratings
    const orderItemsContainer = document.getElementById('orderItemsRating');
    orderItemsContainer.innerHTML = ''; // Clear all item ratings
}

document.addEventListener('DOMContentLoaded', function () {
document.getElementById('ratingModal').addEventListener('show.bs.modal', function (event) {
    console.log('Modal is being shown'); // Check if this log appears in the console
    resetRatingModal(); // Reset the modal first
    const button = event.relatedTarget; // Button that triggered the modal
    const orderId = button.getAttribute('data-order-id'); // Extract order ID from data-order-id attribute
    document.getElementById('modal-order-id').value = orderId;
    fetchAndPopulateRatings(orderId); // Fetch and populate ratings
});
});