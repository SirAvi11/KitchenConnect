function toggleCart(foodItemId) {
    let container = document.getElementById(`cart-button-container-${foodItemId}`);

    // If item is not in the cart, add it
    container.innerHTML = `
        <button class="cart-btn" onclick="decreaseCount(${foodItemId})">-</button>
        <span id="itemCount-${foodItemId}" class="count">1</span>
        <button class="cart-btn" onclick="increaseCount(${foodItemId})">+</button>
    `;

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
        document.getElementById(`cart-button-container-${foodItemId}`).innerHTML = `
            <button id="addToCart-${foodItemId}" class="add-to-cart" onclick="toggleCart(${foodItemId})">
                Add to Cart
            </button>
        `;
        updateCart(foodItemId, 0);
    }
}

function increaseCount(foodItemId) {
    let countDisplay = document.getElementById(`itemCount-${foodItemId}`);
    let count = parseInt(countDisplay.innerText);
    countDisplay.innerText = count + 1;
    updateCart(foodItemId, count + 1);
}

function updateCart(foodItemId, quantity) {
    fetch('/cart/update', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ foodItemId: foodItemId, quantity: quantity })
    })
    .then(response => response.text())
    .then(data => console.log("Cart Updated:", data))
    .catch(error => console.error("Error updating cart:", error));
}