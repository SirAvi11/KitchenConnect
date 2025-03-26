function toggleCart(foodItemId) {
    updateCart(foodItemId, 1);
}

function decreaseCount(foodItemId) {
    let countDisplay = document.getElementById(`itemCount-${foodItemId}`);
    let count = parseInt(countDisplay.innerText);

    if (count > 1) {
        countDisplay.innerText = count - 1;
        updateCart(foodItemId, count - 1);
    } else {
        // Restore "Add to Cart" button when count reaches 0
        document.getElementById(`inner-cart-button-container-${foodItemId}`).innerHTML = `
            <button id="addToCart-${foodItemId}" class="add-to-cart" onclick="toggleCart(${foodItemId})">
                Add to Cart
            </button>
        `;
        updateCart(foodItemId, 0);
        window.location.reload();
    }
}

function increaseCount(foodItemId) {
    let countDisplay = document.getElementById(`itemCount-${foodItemId}`);
    let count = parseInt(countDisplay.innerText);
    countDisplay.innerText = count + 1;
    updateCart(foodItemId, count + 1);
}

function updateCart(foodItemId, quantity) {
    fetch("/cart/update", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ foodItemId: foodItemId, quantity: quantity })
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === "error") {
            let cartModal = new bootstrap.Modal(document.getElementById("cartModal"));            
            cartModal && cartModal.show();

            // Store food item ID and quantity for later use when confirmed
            document.getElementById("confirmAdd")?.setAttribute("onclick", `clearCartAndAddItem(${foodItemId}, ${quantity})`);
        }else{
            if(quantity != 0){
                let container = document.getElementById(`inner-cart-button-container-${foodItemId}`);

                // If item is not in the cart, add it
                container.innerHTML = `
                    <button class="cart-btn" onclick="decreaseCount(${foodItemId})">-</button>
                    <span id="itemCount-${foodItemId}" class="count">${quantity}</span>
                    <button class="cart-btn" onclick="increaseCount(${foodItemId})">+</button>
                `;
                window.location.reload();
            }
        }
    })
    .catch(error => console.error("Error updating cart:", error));
}

function clearCartAndAddItem(foodItemId, quantity) {
    fetch("/cart/clear-cart", { method: "POST" })
    .then(() => {
       hideModal();

        // Add the item to the now-empty cart
        updateCart(foodItemId, quantity);
        window.location.reload();
    })
    .catch(error => console.error("Error clearing cart:", error));
}

// Function for Cancel button to just close the modal and keep cart unchanged
function cancelAddToCart() {
// Hide modal correctly
hideModal();
}

  function hideModal() {
    let modal = document.getElementById("cartModal");
    modal.classList.remove("show");
    modal.style.display = "none";
    document.body.classList.remove("modal-open");

    // ✅ Remove Bootstrap modal backdrop
    let modalBackdrop = document.querySelector(".modal-backdrop");
    if (modalBackdrop) {
        modalBackdrop.remove();
    }
}

function buildCart(orderId) {
    fetch(`/cart/build?orderId=${orderId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === "error") {
            // Show the confirmation modal
            let cartModal = new bootstrap.Modal(document.getElementById("cartModal"));            
            cartModal && cartModal.show();

            // Store the order ID for later use when confirmed
            document.getElementById("confirmAdd")?.setAttribute("onclick", `buildNew(${orderId})`);
        } else if (data.status === "success") {
            // Redirect to the cart page or show a success message
            window.location.href = "/cart";
        }
    })
    .catch(error => console.error("Error updating cart:", error));
}

function buildNew(orderId) {
    // Clear the cart and build it with the new order items
    fetch(`/cart/build?orderId=${orderId}&clearCart=true`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === "success") {
            // Redirect to the cart page or show a success message
            window.location.href = "/cart";
        }
    })
    .catch(error => console.error("Error updating cart:", error));
}