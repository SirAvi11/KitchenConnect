let orderIdToCancel = null;

// Set the orderId to cancel when the button is clicked
function setOrderIdToCancel(button) {
    orderIdToCancel = button.getAttribute('data-order-id');
}

// Handle the actual cancellation
function cancelOrder() {
    if (!orderIdToCancel) {
    alert('No order selected for cancellation.');
    return;
    }

    // Call the existing endpoint with the CANCELLED status
    fetch(`/orders/${orderIdToCancel}/cancel`, {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    })
    .then(data => {
    window.location.href = "/dashboard?tab=my-orders";
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred while cancelling the order.');
    })
    .finally(() => {

        orderIdToCancel = null;
    });
}

function loadTrackingModal(button) {
    // Get data from the button's data-* attributes
    const orderId = button.getAttribute('data-order-id');
    const expectedDeliveryTime = button.getAttribute('data-expected-delivery-time');
    const deliveryAddress = button.getAttribute('data-delivery-address');
    const contactInfo = button.getAttribute('data-contact-info');
    const status = button.getAttribute('data-status');

    // Update modal content with the data
    document.getElementById('tracking-orderId').innerText = orderId;
    document.getElementById('tracking-expectedDeliveryTime').innerText = expectedDeliveryTime;
    document.getElementById('tracking-deliveryAddress').innerText = deliveryAddress;
    document.getElementById('tracking-contactInfo').innerText = contactInfo;

    // Update order status
    updateOrderStatus(status);
}

function updateOrderStatus(status) {
    // Reset all status circles and lines
    const statusCircles = document.querySelectorAll('.status-circle');
    const statusLines = document.querySelectorAll('.status-line');
    const statusTexts = document.querySelectorAll('.status-text');

    statusCircles.forEach(circle => {
    circle.classList.remove('bg-success', 'bg-warning', 'bg-secondary');
    circle.classList.add('bg-secondary'); // Default color for incomplete steps
    });

    statusLines.forEach(line => {
    line.classList.remove('bg-success', 'bg-warning');
    line.classList.add('status-line-gray'); // Default color for incomplete steps
    });

    statusTexts.forEach(text => {
    text.classList.remove('text-success', 'text-warning');
    text.classList.add('text-secondary'); // Default color for incomplete steps
    });

    // Highlight completed and current steps
    if (status === 'PENDING') {
    // Current step: Pending (orange)
    statusCircles[0].classList.remove('bg-secondary');
    statusCircles[0].classList.add('bg-warning');
    statusTexts[0].classList.remove('text-secondary');
    statusTexts[0].classList.add('text-warning');
    } else if (status === 'PREPARING') {
    // Completed step: Pending (green)
    statusCircles[0].classList.remove('bg-secondary');
    statusCircles[0].classList.add('bg-success');
    statusLines[0].classList.remove('status-line-gray');
    statusLines[0].classList.add('bg-success');
    statusTexts[0].classList.remove('text-secondary');
    statusTexts[0].classList.add('text-success');

    // Current step: Preparing (orange)
    statusCircles[1].classList.remove('bg-secondary');
    statusCircles[1].classList.add('bg-warning');
    statusTexts[1].classList.remove('text-secondary');
    statusTexts[1].classList.add('text-warning');
    } else if (status === 'READY') {
    // Completed steps: Pending and Preparing (green)
    statusCircles[0].classList.remove('bg-secondary');
    statusCircles[0].classList.add('bg-success');
    statusLines[0].classList.remove('status-line-gray');
    statusLines[0].classList.add('bg-success');
    statusTexts[0].classList.remove('text-secondary');
    statusTexts[0].classList.add('text-success');

    statusCircles[1].classList.remove('bg-secondary');
    statusCircles[1].classList.add('bg-success');
    statusLines[1].classList.remove('status-line-gray');
    statusLines[1].classList.add('bg-success');
    statusTexts[1].classList.remove('text-secondary');
    statusTexts[1].classList.add('text-success');

    // Current step: On The Way (orange)
    statusCircles[2].classList.remove('bg-secondary');
    statusCircles[2].classList.add('bg-warning');
    statusTexts[2].classList.remove('text-secondary');
    statusTexts[2].classList.add('text-warning');
    } else if (status === 'DELIVERED') {
    // Completed steps: Pending, Preparing, and On The Way (green)
    statusCircles[0].classList.remove('bg-secondary');
    statusCircles[0].classList.add('bg-success');
    statusLines[0].classList.remove('status-line-gray');
    statusLines[0].classList.add('bg-success');
    statusTexts[0].classList.remove('text-secondary');
    statusTexts[0].classList.add('text-success');

    statusCircles[1].classList.remove('bg-secondary');
    statusCircles[1].classList.add('bg-success');
    statusLines[1].classList.remove('status-line-gray');
    statusLines[1].classList.add('bg-success');
    statusTexts[1].classList.remove('text-secondary');
    statusTexts[1].classList.add('text-success');

    statusCircles[2].classList.remove('bg-secondary');
    statusCircles[2].classList.add('bg-success');
    statusLines[2].classList.remove('status-line-gray');
    statusLines[2].classList.add('bg-success');
    statusTexts[2].classList.remove('text-secondary');
    statusTexts[2].classList.add('text-success');

    // Current step: Delivered (orange)
    statusCircles[3].classList.remove('bg-secondary');
    statusCircles[3].classList.add('bg-warning');
    statusTexts[3].classList.remove('text-secondary');
    statusTexts[3].classList.add('text-warning');
    }
}